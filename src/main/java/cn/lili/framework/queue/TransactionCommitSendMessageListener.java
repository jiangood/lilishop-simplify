package cn.lili.framework.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Slf4j
@Component
public class TransactionCommitSendMessageListener {


    @Autowired
    private MessageQueueTemplate messageQueueTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void send(TransactionCommitSendMessageEvent event) {
        log.info("Transaction committed, sending message: topic={}, tag={}", event.getTopic(), event.getTag());
        messageQueueTemplate.send(event.getTopic(), event.getTag(), event.getMessage());
    }

}
