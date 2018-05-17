package pl.nc.hibernate.examples.dto;

import lombok.Value;

@Value
public class PlanetDto {
  private final String name;
  private final Integer position;
}
