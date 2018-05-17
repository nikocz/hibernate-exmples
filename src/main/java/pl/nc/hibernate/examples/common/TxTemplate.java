package pl.nc.hibernate.examples.common;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Component
public class TxTemplate {

  @Transactional
  public <T> T execute(final TxOperation<T> operation) {
    return operation.execute();
  }

  @Transactional
  public void execute(final TxOperationVoid operation) {
    operation.execute();
  }

  @Transactional(propagation = REQUIRES_NEW)
  public <T> T executeRequiresNew(final TxOperation<T> operation) {
    return operation.execute();
  }

  @Transactional(propagation = REQUIRES_NEW)
  public void executeRequiresNew(final TxOperationVoid operation) {
    operation.execute();
  }

  @Transactional(propagation = MANDATORY)
  public <T> T executeMandatory(final TxOperation<T> operation) {
    return operation.execute();
  }

  @Transactional(propagation = MANDATORY)
  public void executeMandatory(final TxOperationVoid operation) {
    operation.execute();
  }

  @Transactional(isolation = SERIALIZABLE)
  public <T> T executeSerializable(final TxOperation<T> operation) {
    return operation.execute();
  }

  @Transactional(isolation = SERIALIZABLE)
  public void executeSerializable(final TxOperationVoid operation) {
    operation.execute();
  }

  @Transactional(isolation = READ_COMMITTED)
  public <T> T executeReadCommited(final TxOperation<T> operation) {
    return operation.execute();
  }

  @Transactional(isolation = READ_COMMITTED)
  public void executeReadCommited(final TxOperationVoid operation) {
    operation.execute();
  }

  @Transactional(readOnly = true)
  public <T> T executeReadOnly(final TxOperation<T> operation) {
    return operation.execute();
  }

  @Transactional(readOnly = true)
  public void executeReadOnly(final TxOperationVoid operation) {
    operation.execute();
  }
}
