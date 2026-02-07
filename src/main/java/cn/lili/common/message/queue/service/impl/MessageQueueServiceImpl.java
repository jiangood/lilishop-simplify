package cn.lili.common.message.queue.service.impl;

import cn.hutool.json.JSONUtil;
import cn.lili.common.message.queue.entity.MessageQueue;
import cn.lili.common.message.queue.service.MessageQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
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
public class MessageQueueServiceImpl implements MessageQueueService {

    @Resource
    private MessageQueueRepository messageQueueRepository;

    @Override
    @Transactional
    public MessageQueue send(String topic, String tag, Object message) {
        MessageQueue messageQueue = new MessageQueue();
        messageQueue.setTopic(topic);
        messageQueue.setTag(tag);
        messageQueue.setMessage(JSONUtil.toJsonStr(message));
        messageQueue.setStatus(0);
        return messageQueueRepository.save(messageQueue);
    }

    @Override
    @Async
    public void sendAsync(String topic, String tag, Object message) {
        try {
            send(topic, tag, message);
        } catch (Exception e) {
            log.error("Failed to send message asynchronously: {}", e.getMessage(), e);
        }
    }

    @Override
    public Page<MessageQueue> findByTopic(String topic, Pageable pageable) {
        return messageQueueRepository.findByTopic(topic, pageable);
    }

    @Override
    public List<MessageQueue> findPendingByTopic(String topic, int limit) {
        return messageQueueRepository.findByTopicAndStatus(topic, 0, Pageable.ofSize(limit)).getContent();
    }

    @Override
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

    @Override
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

    @Override
    @Transactional
    public int deleteOldMessages(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return messageQueueRepository.deleteByStatusAndCreateTimeBefore(2, cutoffDate);
    }

    @Override
    public MessageQueue findById(Long id) {
        return messageQueueRepository.findById(id).orElse(null);
    }

    @Override
    public MessageQueue save(MessageQueue messageQueue) {
        return messageQueueRepository.save(messageQueue);
    }

    /**
     * Message queue repository
     */
    public interface MessageQueueRepository extends JpaRepository<MessageQueue, Long> {

        /**
         * Find messages by topic
         *
         * @param topic    Message topic
         * @param pageable Pageable
         * @return Page of MessageQueue entities
         */
        Page<MessageQueue> findByTopic(String topic, Pageable pageable);

        /**
         * Find messages by topic and status
         *
         * @param topic    Message topic
         * @param status   Message status
         * @param pageable Pageable
         * @return Page of MessageQueue entities
         */
        Page<MessageQueue> findByTopicAndStatus(String topic, Integer status, Pageable pageable);

        /**
         * Delete messages by status and create time before
         *
         * @param status      Message status
         * @param createTime Create time
         * @return Number of deleted messages
         */
        int deleteByStatusAndCreateTimeBefore(Integer status, LocalDateTime createTime);

    }

}