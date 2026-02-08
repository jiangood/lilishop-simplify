package cn.lili.common.event;

import cn.lili.common.message.Topic;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Transaction commit send message event
 * <p>
 * Used to send messages after transaction commit
 *
 * @author Trae
 * @since 2026-02-07
 */
public class TransactionCommitSendMessageEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    @Getter
    private final Topic topic;

    @Getter
    private final String tag;

    @Getter
    private final Object message;

    public TransactionCommitSendMessageEvent(Object source, Topic topic, String tag, Object message) {
        super(source);
        this.topic = topic;
        this.tag = tag;
        this.message = message;
    }

}
