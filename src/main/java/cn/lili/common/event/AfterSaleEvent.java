package cn.lili.common.event;

import cn.lili.modules.order.aftersale.entity.dos.AfterSale;
import cn.lili.rocketmq.tags.AfterSaleTagsEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AfterSaleEvent {

    private AfterSaleTagsEnum tag;
    private AfterSale body;
}
