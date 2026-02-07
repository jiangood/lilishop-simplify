package cn.lili.common.message.queue.service;

import cn.hutool.json.JSONUtil;
import cn.lili.common.message.queue.entity.MessageQueue;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public void send(String topic, String tag, Object message) {
        MessageQueue messageQueue = new MessageQueue();
        messageQueue.setTopic(topic);
        messageQueue.setTag(tag);
        messageQueue.setMessage(JSONUtil.toJsonStr(message));
        messageQueue.setStatus(0);
        messageQueueRepository.save(messageQueue);
    }

    
    @Async
    public void sendAsync(String topic, String tag, Object message) {
        try {
            send(topic, tag, message);
        } catch (Exception e) {
            log.error("Failed to send message asynchronously: {}", e.getMessage(), e);
        }
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