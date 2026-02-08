package cn.lili.common.message.queue.template;

import cn.lili.common.message.Topic;
import cn.lili.common.message.queue.service.MessageQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class MessageQueueTemplate {


    private final MessageQueueService messageQueueService;

    private final cn.lili.common.message.queue.processor.MessageQueueProcessor messageQueueProcessor;


    public void send(Topic topic, String tag, Object message) {
        try {
            messageQueueService.send(topic, tag, message);
            messageQueueProcessor.triggerExecute();
            log.info("Message sent synchronously: {} {}", topic, tag);
        } catch (Exception e) {
            log.error("Failed to send message synchronously: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }




}
