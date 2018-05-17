package pl.nc.hibernate.examples.common;

@FunctionalInterface
public interface TxOperationVoid extends TxOperation<Void> {

  default Void execute() {
    executeVoid();
    return null;
  }

  void executeVoid();
}
