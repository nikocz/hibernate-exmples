package pl.nc.hibernate.examples.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Cacheable
@Getter
@Setter
public class Planet {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  private String name;
  private Integer position;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "star_id")
  private Star star;

  @Override
  public boolean equals(final Object that) {
    if (this == that) { return true; }
    if (!(that instanceof Planet)) { return false; }
    final Planet planet = (Planet) that;
    return id != null && id.equals(planet.id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Planet{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", position=" + position +
        '}';
  }
}
