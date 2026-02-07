package cn.lili.consumer.event.impl;

import cn.lili.cache.Cache;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

//翻译的 quantity.lua脚本
@RequiredArgsConstructor
public class QuantityScript {

    private final Cache cache;

//    -- 可能回滚的列表，一个记录要回滚的skuid一个记录库存

    List<String> id_list= new ArrayList<>();
    List<Integer>  quantity_list= new ArrayList<>();
    public boolean deduction(String key,int num){
        Integer value = (Integer) cache.get(key);
        if(value == null){
            value = 0;
        }
        value = value + num;

        //  -- 变更后库存数量小于
        if(value < 0){
            //  -- 发生超卖
            return false;
        }
        cache.put(key,value);
        return true;
    }

    public void rollback(){
        for (int i = 0; i < id_list.size(); i++) {
            String k = id_list.get(i);
            cache.incrBy( k, -quantity_list.get(i));
        }
    }

    public boolean execute(List<String> keys, List<String> values){
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            int num = Integer.parseInt( values.get(i)); // TODO 按道理应该传Integer,这里先翻译，后续优化
            boolean result = deduction(key, num);
            if(!result){
                rollback();
                return false;
            }else {
                id_list.add(key);
                quantity_list.add(num);
            }
        }

        return true;
    }

}
