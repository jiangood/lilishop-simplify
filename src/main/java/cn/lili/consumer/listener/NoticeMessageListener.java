package cn.lili.consumer.listener;

import cn.hutool.json.JSONUtil;
import cn.lili.common.message.queue.entity.MessageQueue;
import cn.lili.common.message.queue.listener.MessageQueueListener;
import cn.lili.modules.message.entity.dto.NoticeMessageDTO;
import cn.lili.modules.message.service.NoticeMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 站内信通知
 *
 * @author paulG
 * @since 2020/12/9
 */
@Component
public class NoticeMessageListener implements MessageQueueListener {

    @Override
    public String getTopic() {
        return "notice-topic";
    }

    /**
     * 站内信
     */
    @Autowired
    private NoticeMessageService noticeMessageService;

    @Override
    public void onMessage(MessageQueue messageExt) {
        NoticeMessageDTO noticeMessageDTO = JSONUtil.toBean(new String(messageExt.getBody()), NoticeMessageDTO.class);
        noticeMessageService.noticeMessage(noticeMessageDTO);
    }
}
