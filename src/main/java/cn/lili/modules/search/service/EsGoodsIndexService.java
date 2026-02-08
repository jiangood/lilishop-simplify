package cn.lili.modules.search.service;

import cn.lili.common.enums.PromotionTypeEnum;
import cn.lili.modules.goods.entity.dos.GoodsSku;
import cn.lili.modules.promotion.entity.dos.BasePromotions;
import cn.lili.modules.promotion.entity.dos.PromotionGoods;
import cn.lili.modules.search.entity.dos.EsGoodsIndex;
import cn.lili.modules.search.service.EsGoodsIndexService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

// TODO
@Component
public class EsGoodsIndexService  {
    
    public Boolean deleteGoodsDown() {
        return null;
    }

    
    public Boolean delSkuIndex() {
        return null;
    }

    
    public Boolean goodsCache() {
        return null;
    }

    
    public void init() {

    }

    
    public Map<String, Long> getProgress() {
        return null;
    }

    
    public <T> T findIndexById(String id, Class<T> clazz) {
        return null;
    }

    
    public EsGoodsIndex findEsGoodsIndexById(String id) {
        return null;
    }

    
    public EsGoodsIndex addIndex(EsGoodsIndex goods) {
        return null;
    }

    
    public void addIndex(List<EsGoodsIndex> goods) {

    }

    
    public void updateIndex(EsGoodsIndex goods) {

    }

    
    public void updateIndex(String id, EsGoodsIndex goods) {

    }

    
    public void updateIndex(Map<String, Object> queryFields, Map<String, Object> updateFields) {

    }

    
    public void deleteIndexById(String id) {

    }

    
    public void deleteIndexByIds(List<String> ids) {

    }

    
    public void initIndex(List<EsGoodsIndex> goodsIndexList, boolean regeneratorIndex) {

    }

    
    public void updateEsGoodsIndexPromotions(List<String> ids, BasePromotions promotion, String key) {

    }

    
    public void updateEsGoodsIndexByList(List<PromotionGoods> promotionGoodsList, BasePromotions promotion, String key) {

    }

    
    public void updateEsGoodsIndexAllByList(BasePromotions promotion, String key) {

    }

    
    public void deleteEsGoodsPromotionByPromotionKey(List<String> skuIds, String promotionsKey) {

    }

    
    public void deleteEsGoodsPromotionByPromotionKey(String promotionsKey) {

    }

    
    public void cleanInvalidPromotion() {

    }

    
    public EsGoodsIndex findById(String id) {
        return null;
    }

    
    public Map<String, Object> getPromotionMap(String id) {
        return null;
    }

    
    public List<String> getPromotionIdByPromotionType(String id, PromotionTypeEnum promotionTypeEnum) {
        return null;
    }

    
    public EsGoodsIndex getResetEsGoodsIndex(GoodsSku goodsSku) {
        return null;
    }
}
