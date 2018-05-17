package pl.nc.hibernate.examples.common;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class TxOperationEventListener {

  @EventListener
  @Transactional
  public void onSyncTxOperationEvent(final SyncTxOperationEvent event) {
    event.getOperation().execute();
  }

  @EventListener
  @Async
  @Transactional
  public void onAsyncTxOperationEvent(final AsyncTxOperationEvent event) {
    event.getOperation().execute();
  }
}
