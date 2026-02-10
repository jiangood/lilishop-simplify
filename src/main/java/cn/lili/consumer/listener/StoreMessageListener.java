package cn.lili.consumer.listener;

import cn.hutool.json.JSONUtil;
import cn.lili.common.message.Topic;
import cn.lili.consumer.event.StoreSettingChangeEvent;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.rocketmq.tags.StoreTagsEnum;
import io.github.jiangood.openadmin.framework.middleware.mq.annotation.MQMessageListener;
import io.github.jiangood.openadmin.framework.middleware.mq.core.MQListener;
import io.github.jiangood.openadmin.framework.middleware.mq.core.Message;
import io.github.jiangood.openadmin.framework.middleware.mq.core.Result;
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
@MQMessageListener(topic = Topic.STORE)
public class StoreMessageListener implements MQListener {


    @Autowired
    private List<StoreSettingChangeEvent> storeSettingChangeEventList;

    @Override
    public Result onMessage(Message messageExt) {
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
        return Result.SUCCESS;
    }
}
