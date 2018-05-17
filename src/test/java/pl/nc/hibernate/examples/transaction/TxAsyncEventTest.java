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

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static pl.nc.hibernate.examples.common.AsyncTxOperationEvent.async;
import static pl.nc.hibernate.examples.common.CommonAssertions.assertPlanetChanged;
import static pl.nc.hibernate.examples.common.CommonAssertions.assertPlanetUnchanged;
import static pl.nc.hibernate.examples.common.CommonOps.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class TxAsyncEventTest {
  @Autowired
  private TxTemplate tx;
  @Autowired
  private EntityManager em;
  @Autowired
  private ApplicationEventPublisher publisher;

  @Test
  public void willNotWaitForTransactionCommitAfterPropagationToAsync() {
    //when
    tx.execute(() -> {
      updatePlanet(em, VENUS_ID);
      publisher.publishEvent(async(() ->
          tx.executeMandatory(() -> {
            updatePlanet(em, MARS_ID);
            threadSleep(100);
          })));
    });

    //then
    assertPlanetChanged(em, VENUS_ID);
    assertPlanetUnchanged(em, MARS_ID);

    threadSleep(500);

    assertPlanetChanged(em, VENUS_ID);
    assertPlanetChanged(em, MARS_ID);
  }

  @Test
  public void willRollbackOnlyAsyncPartOfTransactionAfterRuntimeException() {
    //when
    tx.execute(() -> {
      updatePlanet(em, VENUS_ID);
      publisher.publishEvent(async(() ->
          tx.executeMandatory(() -> {
            updatePlanet(em, MARS_ID);
            threadSleep(100);
            throwRuntimeException();
          })));
    });

    //then
    assertPlanetChanged(em, VENUS_ID);
    assertPlanetUnchanged(em, MARS_ID);

    threadSleep(500);

    assertPlanetChanged(em, VENUS_ID);
    assertPlanetUnchanged(em, MARS_ID);
  }
}
