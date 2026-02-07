package cn.lili.modules.search.entity.dos;

import cn.lili.common.enums.PromotionTypeEnum;
import cn.lili.modules.goods.entity.dos.GoodsSku;
import cn.lili.modules.goods.entity.dto.GoodsParamsItemDTO;
import cn.lili.modules.promotion.tools.PromotionTools;
import com.alibaba.fastjson2.JSON;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品索引
 *
 * @author paulG
 **/
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "li_good_index")
public class EsGoodsIndex implements Serializable {

    private static final long serialVersionUID = -6856471777036048874L;

    @Id
    @Schema(description = "商品skuId")
    private String id;

    /**
     * 商品id
     */
    @Schema(description = "商品Id")
    private String goodsId;

    /**
     * 商品名称
     */
    @Schema(description = "商品名称")
    private String goodsName;

    /**
     * 商品编号
     */
    @Schema(description = "商品编号")
    private String sn;

    /**
     * 卖家id
     */
    @Schema(description = "卖家id")
    private String storeId;

    /**
     * 卖家名称
     */
    @Schema(description = "卖家名称")
    private String storeName;

    /**
     * 销量
     */
    @Schema(description = "销量")
    private Integer buyCount;

    /**
     * 小图
     */
    @Schema(description = "小图")
    private String small;

    /**
     * 缩略图
     */
    @Schema(description = "缩略图")
    private String thumbnail;

    /**
     * 品牌id
     */
    @Schema(description = "品牌id")
    private String brandId;

    /**
     * 品牌名称
     */
    @Schema(title = "品牌名称")
    private String brandName;

    /**
     * 品牌图片地址
     */
    @Schema(title = "品牌图片地址")
    private String brandUrl;

    /**
     * 分类path
     */
    @Schema(description = "分类path")
    private String categoryPath;

    /**
     * 分类名称path
     */
    @Schema(title = "分类名称path")
    private String categoryNamePath;

    /**
     * 店铺分类id
     */
    @Schema(description = "店铺分类id")
    private String storeCategoryPath;

    /**
     * 店铺分类名称
     */
    @Schema(title = "店铺分类名称")
    private String storeCategoryNamePath;

    /**
     * 商品价格
     */
    @Schema(description = "商品价格")
    private Double price;

    /**
     * 促销价
     */
    @Schema(description = "促销价")
    private Double promotionPrice;

    /**
     * 如果是积分商品需要使用的积分
     */
    @Schema(description = "积分商品需要使用的积分")
    private Integer point;

    /**
     * 评价数量
     */
    @Schema(description = "评价数量")
    private Integer commentNum;

    /**
     * 好评数量
     */
    @Schema(description = "好评数量")
    private Integer highPraiseNum;

    /**
     * 好评率
     */
    @Schema(description = "好评率")
    private Double grade;

    /**
     * 详情
     */
    @Schema(description = "详情")
    private String intro;

    /**
     * 商品移动端详情
     */
    @Schema(description = "商品移动端详情")
    private String mobileIntro;

    /**
     * 是否自营
     */
    @Schema(description = "是否自营")
    private Boolean selfOperated;

    /**
     * 是否为推荐商品
     */
    @Schema(description = "是否为推荐商品")
    private Boolean recommend;

    /**
     * 销售模式
     *
     * @see cn.lili.modules.goods.entity.enums.GoodsSalesModeEnum
     */
    @Schema(description = "销售模式")
    private String salesModel;

    /**
     * 审核状态
     */
    @Schema(description = "审核状态")
    private String authFlag;

    /**
     * 卖点
     */
    @Schema(description = "卖点")
    private String sellingPoint;

    /**
     * 上架状态
     */
    @Schema(description = "上架状态")
    private String marketEnable;

    /**
     * 商品视频
     */
    @Schema(description = "商品视频")
    private String goodsVideo;

    @Schema(description = "商品发布时间")
    private Long releaseTime;

    /**
     * @see cn.lili.modules.goods.entity.enums.GoodsTypeEnum
     */
    @Schema(description = "商品类型", required = true)
    private String goodsType;

    @Schema(description = "商品sku基础分数", required = true)
    private Integer skuSource;

    /**
     * 商品属性（参数和规格）
     */
    private List<EsGoodsAttribute> attrList;

    /**
     * 商品促销活动集合
     * key 为 促销活动类型
     *
     * @see PromotionTypeEnum
     * value 为 促销活动实体信息
     */
    @Schema(description = "商品促销活动集合JSON，key 为 促销活动类型，value 为 促销活动实体信息 ")
    private String promotionMapJson;


    public EsGoodsIndex(GoodsSku sku) {
        if (sku != null) {
            this.id = sku.getId();
            this.goodsId = sku.getGoodsId();
            this.goodsName = sku.getGoodsName();
            this.price = sku.getPrice();
            this.storeName = sku.getStoreName();
            this.storeId = sku.getStoreId();
            this.thumbnail = sku.getThumbnail();
            this.categoryPath = sku.getCategoryPath();
            this.goodsVideo = sku.getGoodsVideo();
            this.mobileIntro = sku.getMobileIntro();
            this.buyCount = sku.getBuyCount() != null ? sku.getBuyCount() : 0;
            this.commentNum = sku.getCommentNum();
            this.small = sku.getSmall();
            this.brandId = sku.getBrandId();
            this.sn = sku.getSn();
            this.storeCategoryPath = sku.getStoreCategoryPath();
            this.sellingPoint = sku.getSellingPoint();
            this.selfOperated = sku.getSelfOperated();
            this.salesModel = sku.getSalesModel();
            this.marketEnable = sku.getMarketEnable();
            this.authFlag = sku.getAuthFlag();
            this.intro = sku.getIntro();
            this.grade = sku.getGrade();
            this.recommend = sku.getRecommend();
            this.goodsType = sku.getGoodsType();
            this.releaseTime = new Date().getTime();
        }
    }

    /**
     * 参数索引增加
     *
     * @param sku            商品sku信息
     * @param goodsParamDTOS 商品参数信息
     */
    public EsGoodsIndex(GoodsSku sku, List<GoodsParamsItemDTO> goodsParamDTOS) {
        this(sku);
        //如果参数不为空
        if (goodsParamDTOS != null && !goodsParamDTOS.isEmpty()) {
            //接受不了参数索引
            List<EsGoodsAttribute> attributes = new ArrayList<>();
            //循环分组的内容
            goodsParamDTOS.forEach(goodsParam -> {
                        //如果字段需要索引，则增加索引字段
                        if (goodsParam.getIsIndex() != null && goodsParam.getIsIndex() == 1) {
                            EsGoodsAttribute attribute = new EsGoodsAttribute();
                            attribute.setType(1);
                            attribute.setName(goodsParam.getParamName());
                            attribute.setValue(goodsParam.getParamValue());
                            attribute.setSort(goodsParam.getSort());
                            attributes.add(attribute);
                        }
                    }
            );
            this.attrList = attributes;
        }
    }

    public EsGoodsIndex(GoodsSku sku, Date createDate) {
        this(sku);
        this.releaseTime = createDate.getTime();
    }

    public void setGoodsSku(GoodsSku sku) {
        if (sku != null) {
            this.id = sku.getId();
            this.goodsId = sku.getGoodsId();
            this.goodsName = sku.getGoodsName();
            this.price = sku.getPrice();
            this.storeName = sku.getStoreName();
            this.storeId = sku.getStoreId();
            this.thumbnail = sku.getThumbnail();
            this.categoryPath = sku.getCategoryPath();
            this.goodsVideo = sku.getGoodsVideo();
            this.mobileIntro = sku.getMobileIntro();
            this.buyCount = sku.getBuyCount();
            this.commentNum = sku.getCommentNum();
            this.small = sku.getSmall();
            this.brandId = sku.getBrandId();
            this.sn = sku.getSn();
            this.storeCategoryPath = sku.getStoreCategoryPath();
            this.sellingPoint = sku.getSellingPoint();
            this.selfOperated = sku.getSelfOperated();
            this.salesModel = sku.getSalesModel();
            this.marketEnable = sku.getMarketEnable();
            this.authFlag = sku.getAuthFlag();
            this.intro = sku.getIntro();
            this.grade = sku.getGrade();
            this.releaseTime = new Date().getTime();
        }
    }

    public Map<String, Object> getOriginPromotionMap() {
        return JSON.parseObject(this.promotionMapJson);
    }

    public Map<String, Object> getPromotionMap() {
        return PromotionTools.filterInvalidPromotionsMap(JSON.parseObject(this.promotionMapJson));
    }
}
