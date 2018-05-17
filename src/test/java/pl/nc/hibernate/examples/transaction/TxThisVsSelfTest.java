package pl.nc.hibernate.examples.transaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import pl.nc.hibernate.examples.App;

import javax.persistence.EntityManager;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static pl.nc.hibernate.examples.common.CommonAssertions.assertPlanetChanged;
import static pl.nc.hibernate.examples.common.CommonAssertions.assertPlanetUnchanged;
import static pl.nc.hibernate.examples.common.CommonOps.VENUS_ID;
import static pl.nc.hibernate.examples.common.CommonOps.updatePlanet;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App.class, TxThisVsSelfTestConfig.class})
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class TxThisVsSelfTest {
  @Autowired
  private EntityManager em;
  @Autowired
  private TxThisVsSelfTestHelper thisVsSelfHelper;

  @Test
  public void willExecuteWithoutTransactionWhenMethodCalledOnThis() {
    //when
    thisVsSelfHelper.callDoInTransactionOnThis(() -> updatePlanet(em, VENUS_ID));

    //then
    assertPlanetUnchanged(em, VENUS_ID);
  }

  @Test
  public void willExecuteInTransactionWhenMethodCalledOnSelf() {
    //when
    thisVsSelfHelper.callDoInTransactionOnSelf(() -> updatePlanet(em, VENUS_ID));

    //then
    assertPlanetChanged(em, VENUS_ID);
  }
}
