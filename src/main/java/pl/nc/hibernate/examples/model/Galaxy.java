package pl.nc.hibernate.examples.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Cacheable
@Getter
@Setter
public class Galaxy {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  private String name;

  @OneToMany(mappedBy = "galaxy", fetch = LAZY)
  private Set<Star> stars = new LinkedHashSet<>();

  @Override
  public boolean equals(final Object that) {
    if (this == that) { return true; }
    if (!(that instanceof Galaxy)) { return false; }
    final Galaxy galaxy = (Galaxy) that;
    return id != null && id.equals(galaxy.id);
  }

  @Override
  public int hashCode() {
    return 31;
  }
}
