package cn.lili.consumer.listener;

import cn.lili.common.event.MessageEvent;
import cn.lili.modules.message.entity.dto.NoticeMessageDTO;
import cn.lili.modules.message.service.NoticeMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/**
 * 站内信通知
 *
 * @author paulG
 * @since 2020/12/9
 */
@Component
public class NoticeMessageListener  {



    /**
     * 站内信
     */
    @Autowired
    private NoticeMessageService noticeMessageService;

  @EventListener(MessageEvent.class)
    public void onMessage(MessageEvent e) {
        NoticeMessageDTO noticeMessageDTO = e.getBody() ;
        noticeMessageService.noticeMessage(noticeMessageDTO);
    }
}
