package pl.nc.hibernate.examples.common;

public class SomeRuntimeException extends RuntimeException {
  public SomeRuntimeException() {
    super("boom");
  }
}
