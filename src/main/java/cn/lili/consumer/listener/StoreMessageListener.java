package cn.lili.consumer.listener;

import cn.hutool.json.JSONUtil;
import cn.lili.common.event.StoreEvent;
import cn.lili.common.message.Topic;
import cn.lili.consumer.event.StoreSettingChangeEvent;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.rocketmq.tags.StoreTagsEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 店铺消息
 * @author chc
 * @since 2022/6/2114:46
 */
@Component
@Slf4j
public class StoreMessageListener  {


    @Autowired
    private List<StoreSettingChangeEvent> storeSettingChangeEventList;

    @EventListener(StoreEvent.class)
    public void onMessage(StoreEvent evt) {
        StoreTagsEnum tag = evt.getTag();
        Store store = evt.getBody();
        switch (tag){
            //修改店铺
            case EDIT_STORE_SETTING:
                for (StoreSettingChangeEvent storeSettingChangeEvent : storeSettingChangeEventList) {
                    try {
                        storeSettingChangeEvent.storeSettingChange(store);
                    } catch (Exception e) {
                        log.error("会员{},在{}业务中，状态修改事件执行异常", store,  storeSettingChangeEvent.getClass().getName(), e);
                    }
                }
                break;
            default:
                break;
        }
    }
}
