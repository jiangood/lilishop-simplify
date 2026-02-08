package cn.lili.consumer.listener;

import cn.hutool.json.JSONUtil;
import cn.lili.common.message.Topic;
import cn.lili.common.message.queue.entity.MessageQueue;
import cn.lili.common.message.queue.listener.MessageQueueListener;
import cn.lili.consumer.event.AfterSaleStatusChangeEvent;
import cn.lili.modules.order.aftersale.entity.dos.AfterSale;
import cn.lili.rocketmq.tags.AfterSaleTagsEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 售后通知
 *
 * @author paulG
 */
@Slf4j
@Component
public class AfterSaleMessageListener implements MessageQueueListener {
    @Override
    public Topic getTopic() {
        return Topic.AFTER_SALE;
    }

    /**
     * 售后订单状态
     */
    @Autowired
    private List<AfterSaleStatusChangeEvent> afterSaleStatusChangeEvents;

    @Override
    public void onMessage(MessageQueue messageExt) {
        if (AfterSaleTagsEnum.valueOf(messageExt.getTag()) == AfterSaleTagsEnum.AFTER_SALE_STATUS_CHANGE) {
            for (AfterSaleStatusChangeEvent afterSaleStatusChangeEvent : afterSaleStatusChangeEvents) {
                try {
                    AfterSale afterSale = JSONUtil.toBean(new String(messageExt.getBody()), AfterSale.class);
                    afterSaleStatusChangeEvent.afterSaleStatusChange(afterSale);
                } catch (Exception e) {
                    log.error("售后{},在{}业务中，状态修改事件执行异常",
                            new String(messageExt.getBody()),
                            afterSaleStatusChangeEvent.getClass().getName(),
                            e);
                }
            }
        }

    }
}
