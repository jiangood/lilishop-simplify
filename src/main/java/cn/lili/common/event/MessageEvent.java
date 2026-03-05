package cn.lili.common.event;

import cn.hutool.json.JSONUtil;
import cn.lili.modules.message.entity.dto.NoticeMessageDTO;
import cn.lili.rocketmq.tags.GoodsTagsEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageEvent {

    private NoticeMessageDTO body;


}
