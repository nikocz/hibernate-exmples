package pl.nc.hibernate.examples.transaction;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.nc.hibernate.examples.common.TxOperationVoid;

@AllArgsConstructor
class TxThisVsSelfTestHelper {
  private final TxThisVsSelfTestHelper self;

  public void callDoInTransactionOnThis(final TxOperationVoid operation) {
    doInTransaction(operation);
  }

  public void callDoInTransactionOnSelf(final TxOperationVoid operation) {
    self.doInTransaction(operation);
  }

  @Transactional
  public void doInTransaction(final TxOperationVoid operation) {
    operation.execute();
  }
}
