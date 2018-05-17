package pl.nc.hibernate.examples.transaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import pl.nc.hibernate.examples.common.TxTemplate;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static pl.nc.hibernate.examples.common.CommonAssertions.*;
import static pl.nc.hibernate.examples.common.CommonOps.*;
import static pl.nc.hibernate.examples.common.SyncTxOperationEvent.sync;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class TxSyncEventTest {

  @Autowired
  private TxTemplate tx;
  @Autowired
  private EntityManager em;
  @Autowired
  private ApplicationEventPublisher publisher;

  @Test
  public void willPropagateTransactionToSyncEventListenerAndCommitChanges() {
    //when
    tx.execute(() -> {
      updatePlanet(em, VENUS_ID);
      publisher.publishEvent(sync(() ->
          tx.executeMandatory(() ->
              updatePlanet(em, MARS_ID)
          )));
    });

    //then
    assertPlanetChanged(em, VENUS_ID);
    assertPlanetChanged(em, MARS_ID);
  }

  @Test
  public void willRollBackAllChangesWhenExceptionPropagatesFromSyncEventListener() {
    //when
    final Throwable thrown = catchThrowable(() ->
        tx.execute(() -> {
          updatePlanet(em, VENUS_ID);
          publisher.publishEvent(sync(() ->
              tx.executeMandatory(() -> {
                updatePlanet(em, MARS_ID);
                throwRuntimeException();
              })));
        }));

    //then
    assertSomeRuntimeException(thrown);

    assertPlanetUnchanged(em, VENUS_ID);
    assertPlanetUnchanged(em, MARS_ID);
  }
}
