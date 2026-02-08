package cn.lili.modules.message.service;


import cn.lili.common.message.Topic;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.message.entity.dos.Message;
import cn.lili.modules.message.entity.vos.MessageVO;
import cn.lili.modules.message.mapper.MessageMapper;
import cn.lili.modules.message.service.MessageService;
import cn.lili.mybatis.util.PageUtil;


import cn.lili.rocketmq.tags.OtherTagsEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lili.framework.queue.MessageQueueTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理端发送消息内容业务层实现
 *
 * @author Chopper
 * @since 2020/11/17 3:48 下午
 */
@Service
public class MessageService extends ServiceImpl<MessageMapper, Message>  {

    @Autowired
    private MessageQueueTemplate messageQueueTemplate;


    
    public IPage<Message> getPage(MessageVO messageVO, PageVO pageVO) {
        return this.page(PageUtil.initPage(pageVO), messageVO.lambdaQueryWrapper());
    }


    
    @Transactional(rollbackFor = Exception.class)
    public Boolean sendMessage(Message message) {
        //保存站内信信息
        this.save(message);
        //发送站内信消息提醒
        messageQueueTemplate.send(Topic.NOTICE_SEND, OtherTagsEnum.MESSAGE.name(), message);
        return true;
    }

    
    public Boolean deleteMessage(String id) {
        //只有查询到此记录才真实删除，未找到记录则直接返回true即可
        Message message = this.getById(id);
        if (message != null) {
            return this.removeById(id);
        }
        return true;
    }
}