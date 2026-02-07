package cn.lili.common.message.queue.service;

import cn.lili.common.message.queue.entity.MessageQueue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Message queue service
 * <p>
 * Handles message operations for the message queue system
 *
 * @author Trae
 * @since 2026-02-07
 */
public interface MessageQueueService {

    /**
     * Send a message
     *
     * @param topic   Message topic
     * @param tag     Message tag
     * @param message Message content
     * @return MessageQueue entity
     */
    MessageQueue send(String topic, String tag, Object message);

    /**
     * Send a message asynchronously
     *
     * @param topic   Message topic
     * @param tag     Message tag
     * @param message Message content
     */
    void sendAsync(String topic, String tag, Object message);

    /**
     * Find messages by topic
     *
     * @param topic    Message topic
     * @param pageable Pageable
     * @return Page of MessageQueue entities
     */
    Page<MessageQueue> findByTopic(String topic, Pageable pageable);

    /**
     * Find pending messages by topic
     *
     * @param topic Message topic
     * @param limit Maximum number of messages to return
     * @return List of pending MessageQueue entities
     */
    List<MessageQueue> findPendingByTopic(String topic, int limit);

    /**
     * Mark message as processed
     *
     * @param id Message id
     * @return boolean
     */
    boolean markAsProcessed(Long id);

    /**
     * Mark message as failed
     *
     * @param id          Message id
     * @param errorMessage Error message
     * @return boolean
     */
    boolean markAsFailed(Long id, String errorMessage);

    /**
     * Delete old processed messages
     *
     * @param days Number of days to keep messages
     * @return Number of deleted messages
     */
    int deleteOldMessages(int days);

    /**
     * Find message by id
     *
     * @param id Message id
     * @return MessageQueue entity
     */
    MessageQueue findById(Long id);

    /**
     * Save message
     *
     * @param messageQueue MessageQueue entity
     * @return MessageQueue entity
     */
    MessageQueue save(MessageQueue messageQueue);

}