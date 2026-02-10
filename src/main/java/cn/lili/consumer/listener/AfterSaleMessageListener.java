package cn.lili.consumer.listener;

import cn.hutool.json.JSONUtil;
import cn.lili.common.message.Topic;
import cn.lili.consumer.event.AfterSaleStatusChangeEvent;
import cn.lili.modules.order.aftersale.entity.dos.AfterSale;
import cn.lili.rocketmq.tags.AfterSaleTagsEnum;
import io.github.jiangood.openadmin.framework.middleware.mq.annotation.MQMessageListener;
import io.github.jiangood.openadmin.framework.middleware.mq.core.MQListener;
import io.github.jiangood.openadmin.framework.middleware.mq.core.Message;
import io.github.jiangood.openadmin.framework.middleware.mq.core.Result;
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
@MQMessageListener(topic = Topic.AFTER_SALE)
public class AfterSaleMessageListener implements MQListener {


    /**
     * 售后订单状态
     */
    @Autowired
    private List<AfterSaleStatusChangeEvent> afterSaleStatusChangeEvents;



    @Override
    public Result onMessage(Message msg) {
        if (AfterSaleTagsEnum.valueOf(msg.getTag()) == AfterSaleTagsEnum.AFTER_SALE_STATUS_CHANGE) {
            for (AfterSaleStatusChangeEvent afterSaleStatusChangeEvent : afterSaleStatusChangeEvents) {
                try {
                    AfterSale afterSale = JSONUtil.toBean(msg.getBody(), AfterSale.class);
                    afterSaleStatusChangeEvent.afterSaleStatusChange(afterSale);
                } catch (Exception e) {
                    log.error("售后{},在{}业务中，状态修改事件执行异常",
                            msg.getBody(),
                            afterSaleStatusChangeEvent.getClass().getName(),
                            e);
                }
            }
        }
        return Result.SUCCESS;
    }
}
