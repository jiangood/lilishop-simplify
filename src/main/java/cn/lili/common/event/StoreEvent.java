package cn.lili.common.event;

import cn.lili.modules.store.entity.dos.Store;
import cn.lili.rocketmq.tags.StoreTagsEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreEvent {

    private StoreTagsEnum tag;
    private Store body;

}
