package pl.nc.hibernate.examples.common;

import lombok.experimental.UtilityClass;
import pl.nc.hibernate.examples.model.Planet;

import javax.persistence.EntityManager;

import static org.apache.commons.lang3.StringUtils.reverse;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.nc.hibernate.examples.common.CommonOps.PLANET_NAMES;
import static pl.nc.hibernate.examples.common.CommonOps.findPlanet;

@UtilityClass
public class CommonAssertions {

  public static void assertSomeRuntimeException(final Throwable thrown) {
    assertThat(thrown).isInstanceOf(SomeRuntimeException.class);
  }

  public static void assertPlanetUnchanged(final EntityManager em, final long id) {
    final Planet planet = findPlanet(em, id);
    assertThat(planet.getName()).isEqualTo(PLANET_NAMES.get(id));
  }

  public static void assertPlanetChanged(final EntityManager em, final long id) {
    final Planet planet = findPlanet(em, id);
    assertThat(planet.getName()).isEqualTo(reverse(PLANET_NAMES.get(id)));
  }
}
