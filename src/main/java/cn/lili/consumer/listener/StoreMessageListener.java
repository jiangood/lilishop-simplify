package cn.lili.consumer.listener;

import cn.hutool.json.JSONUtil;
import cn.lili.common.message.Topic;
import cn.lili.common.message.queue.entity.MessageQueue;
import cn.lili.common.message.queue.listener.MessageQueueListener;
import cn.lili.consumer.event.StoreSettingChangeEvent;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.rocketmq.tags.StoreTagsEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 店铺消息
 * @author chc
 * @since 2022/6/2114:46
 */
@Component
@Slf4j
public class StoreMessageListener implements MessageQueueListener {

    @Override
    public Topic getTopic() {
        return Topic.STORE;
    }

    @Autowired
    private List<StoreSettingChangeEvent> storeSettingChangeEventList;

    @Override
    public void onMessage(MessageQueue messageExt) {
        switch (StoreTagsEnum.valueOf(messageExt.getTag())){
            //修改店铺
            case EDIT_STORE_SETTING:
                for (StoreSettingChangeEvent storeSettingChangeEvent : storeSettingChangeEventList) {
                    try {
                        Store store = JSONUtil.toBean(new String(messageExt.getBody()), Store.class);
                        storeSettingChangeEvent.storeSettingChange(store);
                    } catch (Exception e) {
                        log.error("会员{},在{}业务中，状态修改事件执行异常",
                                new String(messageExt.getBody()),
                                storeSettingChangeEvent.getClass().getName(),
                                e);
                    }
                }
                break;
            default:
                break;
        }
    }
}
