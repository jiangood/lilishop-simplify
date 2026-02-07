package cn.lili.common.message.queue.service;

import cn.lili.common.message.queue.entity.MessageQueue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

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