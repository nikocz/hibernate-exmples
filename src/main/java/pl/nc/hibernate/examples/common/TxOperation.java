package pl.nc.hibernate.examples.common;

@FunctionalInterface
public interface TxOperation<T> {

  T execute();

}
