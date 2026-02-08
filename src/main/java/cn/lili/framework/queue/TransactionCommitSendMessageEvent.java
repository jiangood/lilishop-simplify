package cn.lili.framework.queue;

import cn.lili.common.message.Topic;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class TransactionCommitSendMessageEvent  {


    private final Topic topic;

    private final String tag;

    private final Object message;

    public TransactionCommitSendMessageEvent(Topic topic, String tag, Object message) {
        this.topic = topic;
        this.tag = tag;
        this.message = message;
    }

}
