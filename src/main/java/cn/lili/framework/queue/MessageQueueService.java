package cn.lili.framework.queue;

import cn.hutool.json.JSONUtil;
import cn.lili.common.message.Topic;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
public class MessageQueueService  {

    @Resource
    private MessageQueueRepository messageQueueRepository;

    @Async
    @Transactional
    public void send(Topic topic, String tag, Object message) {
        MessageQueue messageQueue = new MessageQueue();
        messageQueue.setTopic(topic.name());
        messageQueue.setTag(tag);
        messageQueue.setBody(JSONUtil.toJsonStr(message));
        messageQueue.setStatus(0);
        messageQueueRepository.saveAndFlush(messageQueue);
    }





    public List<MessageQueue> findPending( int limit) {
        PageRequest pageable = PageRequest.of(0, limit, Sort.by("id"));
        return messageQueueRepository.findByStatus( 0, pageable).getContent();
    }
    
    @Transactional
    public boolean markAsProcessed(Long id) {
        MessageQueue messageQueue = messageQueueRepository.findById(id).orElse(null);
        if (messageQueue != null) {
            messageQueue.setStatus(2);
            messageQueue.setUpdateTime(LocalDateTime.now());
            messageQueueRepository.save(messageQueue);
            return true;
        }
        return false;
    }

    
    @Transactional
    public boolean markAsFailed(Long id, String errorMessage) {
        MessageQueue messageQueue = messageQueueRepository.findById(id).orElse(null);
        if (messageQueue != null) {
            messageQueue.setStatus(3);
            messageQueue.setErrorMessage(errorMessage);
            messageQueue.setRetryCount(messageQueue.getRetryCount() + 1);
            messageQueue.setUpdateTime(LocalDateTime.now());
            messageQueueRepository.save(messageQueue);
            return true;
        }
        return false;
    }

    
    @Transactional
    public int deleteOldMessages(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return messageQueueRepository.deleteByStatusAndCreateTimeBefore(2, cutoffDate);
    }




}