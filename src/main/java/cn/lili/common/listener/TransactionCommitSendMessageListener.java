package cn.lili.common.listener;

import cn.lili.common.event.TransactionCommitSendMessageEvent;
import cn.lili.common.message.queue.template.MessageQueueTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Transaction commit send message listener
 * <p>
 * Listens for transaction commit events and sends messages
 *
 * @author Trae
 * @since 2026-02-07
 */
@Slf4j
@Component
public class TransactionCommitSendMessageListener {

    /**
     * Message queue template
     */
    @Autowired
    private MessageQueueTemplate messageQueueTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void send(TransactionCommitSendMessageEvent event) {
        log.info("Transaction committed, sending message: topic={}, tag={}", event.getTopic(), event.getTag());
        messageQueueTemplate.send(event.getTopic(), event.getTag(), event.getMessage());
    }

}
