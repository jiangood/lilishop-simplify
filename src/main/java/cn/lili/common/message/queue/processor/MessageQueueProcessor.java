package cn.lili.common.message.queue.processor;

import cn.lili.common.message.queue.entity.MessageQueue;
import cn.lili.common.message.queue.listener.MessageQueueListener;
import cn.lili.common.message.queue.service.MessageQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class MessageQueueProcessor {
    public static final int BATCH_PROCESS_SIZE = 1000;
    public static final int TINY_MESSAGE_COUNT = 100;


    @Autowired
    private MessageQueueService messageQueueService;


    private final Map<String, MessageQueueListener> listenerMap = new HashMap<>();

    public MessageQueueProcessor(MessageQueueService messageQueueService, List<MessageQueueListener> messageQueueListeners) {
        this.messageQueueService = messageQueueService;

        for (MessageQueueListener messageQueueListener : messageQueueListeners) {
            if (listenerMap.containsKey(messageQueueListener.getTopic())) {
                throw new IllegalArgumentException("Duplicate listener for topic: " + messageQueueListener.getTopic());
            }
            listenerMap.put(messageQueueListener.getTopic(), messageQueueListener);
        }

    }

    private MessageQueueListener findByTopic(String topic) {
        MessageQueueListener listener = listenerMap.get(topic);
        Assert.notNull(listener, "No listener found for topic: " + topic);
        return listener;
    }


    @Scheduled(fixedDelay = 5000)
    public void processMessages() {
        List<MessageQueue> pendingMessages = messageQueueService.findPending(BATCH_PROCESS_SIZE);
        if (pendingMessages.isEmpty()) {
            return;
        }

        // 量小，串行
        if (pendingMessages.size() < TINY_MESSAGE_COUNT) {
            pendingMessages.forEach(this::processOne);
            return;
        }

        // 量大，并行
        MultiValueMap<String, MessageQueue> topicMessageList = new LinkedMultiValueMap<>();
        for (MessageQueue pendingMessage : pendingMessages) {
            topicMessageList.add(pendingMessage.getTopic(), pendingMessage);
        }

        topicMessageList.entrySet().parallelStream().forEachOrdered(entry -> {
            List<MessageQueue> messages = entry.getValue();
            messages.forEach(this::processOne);
        });

    }

    private void processOne(MessageQueue message) {
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
