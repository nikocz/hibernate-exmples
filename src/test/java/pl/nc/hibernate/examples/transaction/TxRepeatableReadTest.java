package pl.nc.hibernate.examples.transaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import pl.nc.hibernate.examples.common.TxTemplate;
import pl.nc.hibernate.examples.model.Planet;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static pl.nc.hibernate.examples.common.CommonOps.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class TxRepeatableReadTest {

  @Autowired
  private TxTemplate tx;
  @Autowired
  private EntityManager em;

  @Test
  public void willIgnorePlanetUpdateCommittedInAnotherTransaction() {
    tx.execute(() -> {
      //when
      final Planet oldVenus = findPlanet(em, VENUS_ID);
      tx.executeRequiresNew(() -> updatePlanet(em, VENUS_ID));
      final Planet newVenus = findPlanet(em, VENUS_ID);

      //then
      assertThat(newVenus.getName()).isEqualTo(oldVenus.getName());
    });
  }

  @Test
  public void willUseSelectSnapshotAndIgnoreNewlyInsertedEntity() {
    tx.execute(() -> {
      //when
      final List<Planet> oldPlanets = findStarPlanets(em, SUN_ID);
      insertPlanet(em, SUN_ID, "Pluto", 9);
      final List<Planet> newPlanets = findStarPlanets(em, SUN_ID);

      //then
      assertThat(newPlanets).isEqualTo(oldPlanets);
    });
  }

  @Test
  public void willSufferFromPhantomReadAnomaly() {
    tx.execute(() -> {
      //when
      final List<Planet> oldPlanets = findStarPlanets(em, SUN_ID);
      tx.executeRequiresNew(() -> insertPlanet(em, SUN_ID, "Pluto", 9));
      final List<Planet> newPlanets = findStarPlanets(em, SUN_ID);

      //then
      assertThat(newPlanets).isNotEqualTo(oldPlanets);
    });
  }
}
