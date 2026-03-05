package cn.lili.common.event;

import cn.hutool.json.JSONUtil;
import cn.lili.rocketmq.tags.GoodsTagsEnum;
import cn.lili.rocketmq.tags.OrderTagsEnum;
import lombok.Data;

@Data
public class OrderEvent {

    private OrderTagsEnum tag;
    private String body;

    public OrderEvent(OrderTagsEnum tag, Object body) {
        this.tag = tag;
        this.body = JSONUtil.toJsonStr(body);
    }
}
