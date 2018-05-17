package pl.nc.hibernate.examples.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.experimental.UtilityClass;
import pl.nc.hibernate.examples.model.Planet;
import pl.nc.hibernate.examples.model.Star;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.reverse;
import static pl.nc.hibernate.examples.model.QPlanet.planet;

@UtilityClass
public class CommonOps {
  public static final long SUN_ID = 1L;
  public static final long VENUS_ID = 2L;
  public static final long MARS_ID = 4L;
  public static final Map<Long, String> PLANET_NAMES = createPlanetNamesMap();

  private static Map<Long, String> createPlanetNamesMap() {
    final Map<Long, String> planetNames = new HashMap<>();
    planetNames.put(VENUS_ID, "Venus");
    planetNames.put(MARS_ID, "Mars");
    return planetNames;
  }

  public static void insertPlanet(final EntityManager em, final long sunId, final String name, final int position) {
    final Planet planet = new Planet();
    planet.setName(name);
    planet.setPosition(position);
    planet.setStar(em.getReference(Star.class, sunId));
    em.persist(planet);
  }

  public static void updatePlanet(final EntityManager em, final long id) {
    final Planet planet = findPlanet(em, id);
    planet.setName(reverse(planet.getName()));
  }

  public static Planet findPlanet(final EntityManager em, final long id) {
    return em.find(Planet.class, id);
  }

  public static List<Planet> findStarPlanets(final EntityManager em, final long starId) {
    return new JPAQueryFactory(em)
        .selectFrom(planet)
        .where(planet.star.id.eq(starId))
        .fetch();
  }

  public static void throwRuntimeException() throws RuntimeException {
    throw new SomeRuntimeException();
  }

  public static void threadSleep(final long millis) {
    try {
      Thread.sleep(millis);
    } catch (final InterruptedException ignore) { }
  }
}
