package cn.lili.consumer.listener;

import cn.hutool.json.JSONUtil;
import cn.lili.modules.message.entity.dto.NoticeMessageDTO;
import cn.lili.modules.message.service.NoticeMessageService;
import io.github.jiangood.openadmin.framework.middleware.mq.annotation.MQMessageListener;
import io.github.jiangood.openadmin.framework.middleware.mq.core.MQListener;
import io.github.jiangood.openadmin.framework.middleware.mq.core.Message;
import io.github.jiangood.openadmin.framework.middleware.mq.core.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cn.lili.common.message.Topic.NOTICE;

/**
 * 站内信通知
 *
 * @author paulG
 * @since 2020/12/9
 */
@Component
@MQMessageListener(topic = NOTICE)
public class NoticeMessageListener implements MQListener {



    /**
     * 站内信
     */
    @Autowired
    private NoticeMessageService noticeMessageService;

    @Override
    public Result onMessage(Message messageExt) {
        NoticeMessageDTO noticeMessageDTO = JSONUtil.toBean(messageExt.getBody(), NoticeMessageDTO.class);
        noticeMessageService.noticeMessage(noticeMessageDTO);
        return Result.SUCCESS;
    }
}
