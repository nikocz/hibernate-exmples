package pl.nc.hibernate.examples.transaction;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import pl.nc.hibernate.examples.common.TxTemplate;
import pl.nc.hibernate.examples.model.Planet;

import javax.persistence.EntityManager;

import static org.apache.commons.lang3.StringUtils.reverse;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static pl.nc.hibernate.examples.common.CommonAssertions.assertPlanetChanged;
import static pl.nc.hibernate.examples.common.CommonAssertions.assertPlanetUnchanged;
import static pl.nc.hibernate.examples.common.CommonOps.*;
import static pl.nc.hibernate.examples.model.QPlanet.planet;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class TxReadOnlyTest {
  @Autowired
  private EntityManager em;
  @Autowired
  private TxTemplate tx;

  @Test
  public void willNotUpdateDirtyEntity() {
    //when
    tx.executeReadOnly(() -> updatePlanet(em, VENUS_ID));

    //then
    assertPlanetUnchanged(em, VENUS_ID);
  }

  @Test
  public void willNotUpdateDirtyEntityInJoinedTransaction() {
    //when
    tx.executeReadOnly(() ->
        tx.execute(() ->
            updatePlanet(em, VENUS_ID)));

    //then
    assertPlanetUnchanged(em, VENUS_ID);
  }

  @Test
  public void willRunUpdateQueryNormally() {
    //when
    tx.executeReadOnly(() -> {
      final Planet venus = findPlanet(em, VENUS_ID);
      new JPAQueryFactory(em)
          .update(planet)
          .set(planet.name, reverse(venus.getName()))
          .where(planet.id.eq(VENUS_ID))
          .execute();
    });

    //then
    assertPlanetChanged(em, VENUS_ID);
  }

  @Test
  public void willUpdateDirtyEntityInNewTransaction() {
    //when
    tx.executeReadOnly(() ->
        tx.executeRequiresNew(() ->
            updatePlanet(em, VENUS_ID)));

    //then
    assertPlanetChanged(em, VENUS_ID);
  }
}
