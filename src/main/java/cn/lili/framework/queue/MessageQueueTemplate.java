package cn.lili.framework.queue;

import cn.lili.common.message.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class MessageQueueTemplate {


    private final MessageQueueService messageQueueService;

    private final ApplicationEventPublisher applicationEventPublisher;


    public void send(Topic topic, String tag, Object message) {
        try {
            messageQueueService.send(topic, tag, message);

            applicationEventPublisher.publishEvent(new MessageQueueAddEvent());
            log.info("Message sent synchronously: {} {}", topic, tag);
        } catch (Exception e) {
            log.error("Failed to send message synchronously: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }




}
