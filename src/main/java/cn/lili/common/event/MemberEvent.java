package cn.lili.common.event;

import cn.hutool.json.JSONUtil;
import cn.lili.rocketmq.tags.GoodsTagsEnum;
import cn.lili.rocketmq.tags.MemberTagsEnum;
import lombok.Data;

@Data
public class MemberEvent {

    private MemberTagsEnum tag;
    private String body;

    public MemberEvent(MemberTagsEnum tag, Object body) {
        this.tag = tag;
        this.body = JSONUtil.toJsonStr(body);
    }
}
