package cn.lili.consumer.listener;

import cn.hutool.json.JSONUtil;
import cn.lili.cache.Cache;
import cn.lili.common.message.Topic;
import cn.lili.consumer.event.OrderStatusChangeEvent;
import cn.lili.consumer.event.TradeEvent;
import cn.lili.modules.order.cart.entity.dto.TradeDTO;
import cn.lili.modules.order.order.entity.dto.OrderMessage;
import cn.lili.rocketmq.tags.OrderTagsEnum;
import io.github.jiangood.openadmin.framework.middleware.mq.annotation.MQMessageListener;
import io.github.jiangood.openadmin.framework.middleware.mq.core.MQListener;
import io.github.jiangood.openadmin.framework.middleware.mq.core.Message;
import io.github.jiangood.openadmin.framework.middleware.mq.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单消息
 *
 * @author paulG
 * @since 2020/12/9
 **/
@Component
@Slf4j
@MQMessageListener(topic = Topic.ORDER)
public class OrderMessageListener implements MQListener {


    /**
     * 交易
     */
    @Autowired
    private List<TradeEvent> tradeEvent;
    /**
     * 订单状态
     */
    @Autowired
    private List<OrderStatusChangeEvent> orderStatusChangeEvents;
    /**
     * 缓存
     */
    @Autowired
    private Cache<Object> cache;

    @Override
    public Result onMessage(Message messageExt) {
        try {
            this.orderStatusEvent(messageExt);
        } catch (Exception e) {
            log.error("订单状态变更事件调用异常", e);
        }
        return Result.SUCCESS;
    }

    /**
     * 订单状态变更
     * @param messageExt
     */
    public void orderStatusEvent(Message messageExt) {

        switch (OrderTagsEnum.valueOf(messageExt.getTag())) {
            //订单创建
            case ORDER_CREATE:
                String key = new String(messageExt.getBody());
                TradeDTO tradeDTO = JSONUtil.toBean(cache.getString(key), TradeDTO.class);
                boolean result = true;
                for (TradeEvent event : tradeEvent) {
                    try {
                        event.orderCreate(tradeDTO);
                    } catch (Exception e) {
                        log.error("交易{}入库,在{}业务中，状态修改事件执行异常",
                                tradeDTO.getSn(),
                                event.getClass().getName(),
                                e);
                        result = false;
                    }
                }
                //如所有步骤顺利完成
                if (Boolean.TRUE.equals(result)) {
                    //清除记录信息的trade cache key
                    cache.remove(key);
                }
                break;
            //订单状态变更
            case STATUS_CHANGE:
                for (OrderStatusChangeEvent orderStatusChangeEvent : orderStatusChangeEvents) {
                    try {
                        OrderMessage orderMessage = JSONUtil.toBean(new String(messageExt.getBody()), OrderMessage.class);
                        orderStatusChangeEvent.orderChange(orderMessage);
                    } catch (Exception e) {
                        log.error("订单{},在{}业务中，状态修改事件执行异常",
                                new String(messageExt.getBody()),
                                orderStatusChangeEvent.getClass().getName(),
                                e);
                    }
                }
                break;
            default:
                break;
        }
    }
}
