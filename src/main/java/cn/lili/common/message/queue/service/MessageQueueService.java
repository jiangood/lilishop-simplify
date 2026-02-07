package cn.lili.common.message.queue.service;

import cn.hutool.json.JSONUtil;
import cn.lili.common.message.queue.entity.MessageQueue;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Message queue service implementation
 * <p>
 * Handles message operations using JPA
 *
 * @author Trae
 * @since 2026-02-07
 */
@Slf4j
@Service
public class MessageQueueService  {

    @Resource
    private MessageQueueRepository messageQueueRepository;

    @Transactional
    public MessageQueue send(String topic, String tag, Object message) {
        MessageQueue messageQueue = new MessageQueue();
        messageQueue.setTopic(topic);
        messageQueue.setTag(tag);
        messageQueue.setMessage(JSONUtil.toJsonStr(message));
        messageQueue.setStatus(0);
        return messageQueueRepository.save(messageQueue);
    }

    
    @Async
    public void sendAsync(String topic, String tag, Object message) {
        try {
            send(topic, tag, message);
        } catch (Exception e) {
            log.error("Failed to send message asynchronously: {}", e.getMessage(), e);
        }
    }

    
    public Page<MessageQueue> findByTopic(String topic, Pageable pageable) {
        return messageQueueRepository.findByTopic(topic, pageable);
    }

    
    public List<MessageQueue> findPendingByTopic(String topic, int limit) {
        return messageQueueRepository.findByTopicAndStatus(topic, 0, Pageable.ofSize(limit)).getContent();
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

    
    public MessageQueue findById(Long id) {
        return messageQueueRepository.findById(id).orElse(null);
    }

    
    public MessageQueue save(MessageQueue messageQueue) {
        return messageQueueRepository.save(messageQueue);
    }



}