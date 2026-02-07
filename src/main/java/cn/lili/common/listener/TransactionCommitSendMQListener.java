package cn.lili.common.listener;

import cn.lili.common.event.TransactionCommitSendMQEvent;
import cn.lili.common.message.queue.template.MessageQueueTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 事务提交监听器
 *
 * @author paulG
 * @since 2022/1/19
 **/
@Component
@Slf4j
public class TransactionCommitSendMQListener {

    /**
     * message queue
     */
    @Autowired
    private MessageQueueTemplate messageQueueTemplate;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void send(TransactionCommitSendMQEvent event) {
        log.info("事务提交，发送mq信息!{}", event);
        String destination = event.getTopic() + ":" + event.getTag();
        //发送订单变更mq消息
        messageQueueTemplate.asyncSend(destination, event.getMessage());
    }


}
