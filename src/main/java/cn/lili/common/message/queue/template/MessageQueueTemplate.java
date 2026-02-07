package cn.lili.common.message.queue.template;

import cn.lili.common.message.queue.service.MessageQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Message queue template
 * <p>
 * Mimics RocketMQTemplate interface for easy replacement
 *
 * @author Trae
 * @since 2026-02-07
 */
@Slf4j
@Component
public class MessageQueueTemplate {

    @Autowired
    private MessageQueueService messageQueueService;

    /**
     * Send message synchronously
     *
     * @param destination Destination in format "topic:tag"
     * @param message     Message content
     */
    public void send(String destination, Object message) {
        try {
            String[] parts = destination.split(":");
            String topic = parts[0];
            String tag = parts.length > 1 ? parts[1] : "";
            messageQueueService.send(topic, tag, message);
            log.info("Message sent synchronously: {}", destination);
        } catch (Exception e) {
            log.error("Failed to send message synchronously: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Send message asynchronously
     *
     * @param destination Destination in format "topic:tag"
     * @param message     Message content
     */
    public void asyncSend(String destination, Object message) {
        try {
            String[] parts = destination.split(":");
            String topic = parts[0];
            String tag = parts.length > 1 ? parts[1] : "";
            messageQueueService.sendAsync(topic, tag, message);
            log.info("Message sent asynchronously: {}", destination);
        } catch (Exception e) {
            log.error("Failed to send message asynchronously: {}", e.getMessage(), e);
        }
    }

    /**
     * Send message with tags
     *
     * @param topic   Topic
     * @param tag     Tag
     * @param message Message content
     */
    public void sendWithTags(String topic, String tag, Object message) {
        try {
            messageQueueService.send(topic, tag, message);
            log.info("Message sent with tags: topic={}, tag={}", topic, tag);
        } catch (Exception e) {
            log.error("Failed to send message with tags: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Send message asynchronously with callback
     *
     * @param destination Destination in format "topic:tag"
     * @param message     Message content
     */
    public void asyncSend(String destination, Object message, MessageSendCallback callback) {
        try {
            String[] parts = destination.split(":");
            String topic = parts[0];
            String tag = parts.length > 1 ? parts[1] : "";
            messageQueueService.sendAsync(topic, tag, message);
            if (callback != null) {
                callback.onSuccess();
            }
            log.info("Message sent asynchronously with callback: {}", destination);
        } catch (Exception e) {
            log.error("Failed to send message asynchronously with callback: {}", e.getMessage(), e);
            if (callback != null) {
                callback.onException(e);
            }
        }
    }

    /**
     * Message send callback
     */
    public interface MessageSendCallback {

        /**
         * On success
         */
        void onSuccess();

        /**
         * On exception
         *
         * @param e Exception
         */
        void onException(Throwable e);

    }

}
