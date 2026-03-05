package cn.lili.common.event;

import cn.hutool.json.JSONUtil;
import cn.lili.rocketmq.tags.OtherTagsEnum;
import lombok.Data;

@Data
public class OtherEvent {

    OtherTagsEnum tag;
    String body;

    public OtherEvent(OtherTagsEnum tag, Object body) {
        this.tag = tag;
        this.body = JSONUtil.toJsonStr(body);
    }
}
