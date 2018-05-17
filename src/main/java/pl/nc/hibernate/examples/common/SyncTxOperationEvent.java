package pl.nc.hibernate.examples.common;

import lombok.Value;

@Value
public class SyncTxOperationEvent {
  private final TxOperation operation;

  public static SyncTxOperationEvent sync(final TxOperation<?> operation) {
    return new SyncTxOperationEvent(operation);
  }

  public static SyncTxOperationEvent sync(final TxOperationVoid operation) {
    return new SyncTxOperationEvent(operation);
  }
}
