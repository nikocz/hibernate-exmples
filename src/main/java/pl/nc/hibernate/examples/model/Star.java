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
public class Star {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  private String name;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "galaxy_id")
  private Galaxy galaxy;

  @OneToMany(mappedBy = "star", fetch = LAZY)
  private Set<Planet> planets = new LinkedHashSet<>();

  @Override
  public boolean equals(final Object that) {
    if (this == that) { return true; }
    if (!(that instanceof Star)) { return false; }
    final Star star = (Star) that;
    return id != null && id.equals(star.id);
  }

  @Override
  public int hashCode() {
    return 31;
  }
}
