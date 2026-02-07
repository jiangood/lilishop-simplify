package cn.lili.common.message.queue.template;

import cn.lili.common.message.queue.service.MessageQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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




}
