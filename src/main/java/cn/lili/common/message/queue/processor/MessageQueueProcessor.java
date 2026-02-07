package cn.lili.common.message.queue.processor;

import cn.lili.common.message.queue.entity.MessageQueue;
import cn.lili.common.message.queue.listener.MessageQueueListener;
import cn.lili.common.message.queue.service.MessageQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;


@Slf4j
@Component
public class MessageQueueProcessor {

    @Autowired
    private MessageQueueService messageQueueService;

    @Autowired
    private List<MessageQueueListener> messageQueueListeners;


    private MessageQueueListener findByTopic(String topic) {
        List<MessageQueueListener> list = messageQueueListeners.stream()
                .filter(listener -> listener.getTopic().equals(topic)).toList();
        Assert.state(list.size() == 1, "There should be only one listener for each topic");
        return list.get(0);
    }


    @Scheduled(fixedDelay = 3000)
    public void processMessages() {
        List<MessageQueue> pendingMessages = messageQueueService.findPending(10);
        if (pendingMessages.isEmpty()) {
           return;
        }

        for (MessageQueue message : pendingMessages) {
            try {
                log.info("Processing message: id={}, topic={}, tag={}", message.getId(), message.getTopic(), message.getTag());

                MessageQueueListener listener = this.findByTopic(message.getTopic());
                listener.onMessage(message);
                messageQueueService.markAsProcessed(message.getId());
                log.info("Message processed successfully: id={}", message.getId());
            } catch (Exception e) {
                log.error("Failed to process message: id={}, error={}", message.getId(), e.getMessage(), e);
                messageQueueService.markAsFailed(message.getId(), e.getMessage());
            }
        }

    }

    /**
     * Clean up old processed messages every day at midnight
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanUpOldMessages() {
        try {
            int deletedCount = messageQueueService.deleteOldMessages(7); // Keep messages for 7 days
            log.info("Cleaned up {} old processed messages", deletedCount);
        } catch (Exception e) {
            log.error("Error cleaning up old messages: {}", e.getMessage(), e);
        }
    }

}
