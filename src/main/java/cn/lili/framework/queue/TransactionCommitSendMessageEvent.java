package cn.lili.framework.queue;

import cn.lili.common.message.Topic;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class TransactionCommitSendMessageEvent  {


    private final String topic;

    private final String tag;

    private final String message;

    public TransactionCommitSendMessageEvent(String topic, String tag, String message) {
        this.topic = topic;
        this.tag = tag;
        this.message = message;
    }

}
