package cn.lili.common.event;

import cn.hutool.json.JSONUtil;
import cn.lili.rocketmq.tags.GoodsTagsEnum;
import lombok.Data;

@Data
public class GoodsEvent {

    private GoodsTagsEnum tag;
    private String body;

    public GoodsEvent(GoodsTagsEnum tag, Object body) {
        this.tag = tag;
        this.body = JSONUtil.toJsonStr(body);
    }
}
