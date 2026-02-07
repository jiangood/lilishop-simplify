package cn.lili.common.message.queue.listener;

import cn.lili.common.message.queue.entity.MessageQueue;


public interface MessageQueueListener {

    /**
     * Process a message
     *
     * @param messageQueue MessageQueue entity
     */
    void onMessage(MessageQueue messageQueue);

    /**
     * Get the topic this listener is interested in
     *
     * @return Topic name
     */
    String getTopic();

}
