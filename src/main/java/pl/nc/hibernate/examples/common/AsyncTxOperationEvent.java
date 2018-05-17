package pl.nc.hibernate.examples.common;

import lombok.Value;

@Value
public class AsyncTxOperationEvent {
  private final TxOperation operation;

  public static AsyncTxOperationEvent async(final TxOperation<?> operation) {
    return new AsyncTxOperationEvent(operation);
  }

  public static AsyncTxOperationEvent async(final TxOperationVoid operation) {
    return new AsyncTxOperationEvent(operation);
  }
}
