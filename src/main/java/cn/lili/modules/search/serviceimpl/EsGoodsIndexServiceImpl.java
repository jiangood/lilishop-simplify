package cn.lili.modules.search.serviceimpl;

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
public class EsGoodsIndexServiceImpl implements EsGoodsIndexService {
    @Override
    public Boolean deleteGoodsDown() {
        return null;
    }

    @Override
    public Boolean delSkuIndex() {
        return null;
    }

    @Override
    public Boolean goodsCache() {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    public Map<String, Long> getProgress() {
        return null;
    }

    @Override
    public <T> T findIndexById(String id, Class<T> clazz) {
        return null;
    }

    @Override
    public EsGoodsIndex findEsGoodsIndexById(String id) {
        return null;
    }

    @Override
    public EsGoodsIndex addIndex(EsGoodsIndex goods) {
        return null;
    }

    @Override
    public void addIndex(List<EsGoodsIndex> goods) {

    }

    @Override
    public void updateIndex(EsGoodsIndex goods) {

    }

    @Override
    public void updateIndex(String id, EsGoodsIndex goods) {

    }

    @Override
    public void updateIndex(Map<String, Object> queryFields, Map<String, Object> updateFields) {

    }

    @Override
    public void deleteIndexById(String id) {

    }

    @Override
    public void deleteIndexByIds(List<String> ids) {

    }

    @Override
    public void initIndex(List<EsGoodsIndex> goodsIndexList, boolean regeneratorIndex) {

    }

    @Override
    public void updateEsGoodsIndexPromotions(List<String> ids, BasePromotions promotion, String key) {

    }

    @Override
    public void updateEsGoodsIndexByList(List<PromotionGoods> promotionGoodsList, BasePromotions promotion, String key) {

    }

    @Override
    public void updateEsGoodsIndexAllByList(BasePromotions promotion, String key) {

    }

    @Override
    public void deleteEsGoodsPromotionByPromotionKey(List<String> skuIds, String promotionsKey) {

    }

    @Override
    public void deleteEsGoodsPromotionByPromotionKey(String promotionsKey) {

    }

    @Override
    public void cleanInvalidPromotion() {

    }

    @Override
    public EsGoodsIndex findById(String id) {
        return null;
    }

    @Override
    public Map<String, Object> getPromotionMap(String id) {
        return null;
    }

    @Override
    public List<String> getPromotionIdByPromotionType(String id, PromotionTypeEnum promotionTypeEnum) {
        return null;
    }

    @Override
    public EsGoodsIndex getResetEsGoodsIndex(GoodsSku goodsSku) {
        return null;
    }
}
