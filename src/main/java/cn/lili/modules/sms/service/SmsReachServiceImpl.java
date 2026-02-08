package cn.lili.modules.sms.service;

import cn.lili.common.message.Topic;
import cn.lili.common.utils.BeanUtil;
import cn.lili.modules.sms.entity.dos.SmsReach;
import cn.lili.modules.sms.entity.dto.SmsReachDTO;
import cn.lili.modules.sms.mapper.SmsReachMapper;
import cn.lili.modules.sms.service.SmsReachService;
import cn.lili.rocketmq.tags.OtherTagsEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lili.framework.queue.MessageQueueTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 短信任务业务层实现
 *
 * @author Bulbasaur
 * @since 2021/1/30 3:19 下午
 */
@Service
public class SmsReachServiceImpl extends ServiceImpl<SmsReachMapper, SmsReach> implements SmsReachService {
    @Autowired
    private MessageQueueTemplate messageQueueTemplate;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSmsReach(SmsReach smsReach,List<String> mobile) {
        SmsReachDTO smsReachDTO = new SmsReachDTO();
        BeanUtil.copyProperties(smsReach,smsReachDTO);
        smsReachDTO.setMobile(mobile);
        this.save(smsReach);
        //发送短信批量发送mq消息
        messageQueueTemplate.send(Topic.NOTICE_SEND, OtherTagsEnum.SMS.name(),smsReachDTO);

    }
}
