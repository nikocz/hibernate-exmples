package pl.nc.hibernate.examples.transaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import pl.nc.hibernate.examples.common.SomeRuntimeException;
import pl.nc.hibernate.examples.common.TxTemplate;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static pl.nc.hibernate.examples.common.CommonAssertions.*;
import static pl.nc.hibernate.examples.common.CommonOps.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class TxRequiresNewTest {

  @Autowired
  private TxTemplate tx;
  @Autowired
  private EntityManager em;

  @Test
  public void willCommitBothTransactionsWhenNestedRequiresNew() {
    //when
    tx.execute(() -> {
      updatePlanet(em, VENUS_ID);
      tx.executeRequiresNew(() -> updatePlanet(em, MARS_ID));
    });

    //then
    assertPlanetChanged(em, VENUS_ID);
    assertPlanetChanged(em, MARS_ID);
  }

  @Test
  public void willRollBackBothTransactionsWhenExceptionPropagatesFromNestedRequiresNew() {
    //when
    final Throwable thrown = catchThrowable(() ->
        tx.execute(() -> {
          updatePlanet(em, VENUS_ID);
          tx.executeRequiresNew(() -> {
            updatePlanet(em, MARS_ID);
            throwRuntimeException();
          });
        }));

    //then
    assertSomeRuntimeException(thrown);

    assertPlanetUnchanged(em, VENUS_ID);
    assertPlanetUnchanged(em, MARS_ID);
  }

  @Test
  public void willRollBackOnlyNestedRequiresNewWhenSurroundingTransactionHandlesException() {
    //when
    tx.execute(() -> {
      updatePlanet(em, VENUS_ID);
      try {
        tx.executeRequiresNew(() -> {
          updatePlanet(em, MARS_ID);
          throwRuntimeException();
        });
      } catch (SomeRuntimeException ignore) {}
    });

    //then
    assertPlanetChanged(em, VENUS_ID);
    assertPlanetUnchanged(em, MARS_ID);
  }

  @Test
  public void willCommitNestedRequiresNewWhenSurroundingTransactionIsRolledBack() {
    //when
    final Throwable thrown = catchThrowable(() ->
        tx.execute(() -> {
          updatePlanet(em, VENUS_ID);
          tx.executeRequiresNew(() -> updatePlanet(em, MARS_ID));
          throwRuntimeException();
        }));

    //then
    assertSomeRuntimeException(thrown);

    assertPlanetUnchanged(em, VENUS_ID);
    assertPlanetChanged(em, MARS_ID);
  }
}
