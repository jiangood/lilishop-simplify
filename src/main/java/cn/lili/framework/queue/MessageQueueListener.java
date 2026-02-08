package cn.lili.framework.queue;

import cn.lili.common.message.Topic;


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
    Topic getTopic();

}
