package cn.lili.consumer.listener;

import cn.hutool.json.JSONUtil;
import cn.lili.common.event.AfterSaleEvent;
import cn.lili.consumer.event.AfterSaleStatusChangeEvent;
import cn.lili.modules.order.aftersale.entity.dos.AfterSale;
import cn.lili.rocketmq.tags.AfterSaleTagsEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 售后通知
 *
 * @author paulG
 */
@Slf4j
@Component
public class AfterSaleMessageListener {


    /**
     * 售后订单状态
     */
    @Autowired
    private List<AfterSaleStatusChangeEvent> afterSaleStatusChangeEvents;


    @EventListener(AfterSaleEvent.class)
    public void onMessage(AfterSaleEvent messageExt) {
        AfterSale afterSale = messageExt.getBody();
        if (messageExt.getTag() == AfterSaleTagsEnum.AFTER_SALE_STATUS_CHANGE) {
            for (AfterSaleStatusChangeEvent afterSaleStatusChangeEvent : afterSaleStatusChangeEvents) {
                try {
                    afterSaleStatusChangeEvent.afterSaleStatusChange(afterSale);
                } catch (Exception e) {
                    log.error("售后{},在{}业务中，状态修改事件执行异常", afterSale, afterSaleStatusChangeEvent.getClass().getName(),    e);
                }
            }
        }

    }
}
