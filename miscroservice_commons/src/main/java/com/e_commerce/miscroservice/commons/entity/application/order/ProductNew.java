package com.e_commerce.miscroservice.commons.entity.application.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 商品表
 * 
 * CREATE TABLE `yjj_Product` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `Name` varchar(200) NOT NULL COMMENT '商品名',
  `CategoryId` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属分类id',
  `DetailImages` varchar(4096) NOT NULL DEFAULT '[]' COMMENT '橱窗图片:JSON 格式数组',
  `SummaryImages` varchar(4096) NOT NULL DEFAULT '[]' COMMENT '详情图 json数组',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
  `SaleStartTime` bigint(20) NOT NULL COMMENT '上架时间',
  `SaleEndTime` bigint(20) NOT NULL COMMENT '下架时间',
  `SaleCurrencyType` tinyint(4) NOT NULL DEFAULT '0' COMMENT '销售类型：0-人民币 1-玖币',
  `SaleTotalCount` int(11) DEFAULT '0' COMMENT '总销量',
  `SaleMonthlyMaxCount` int(11) DEFAULT '0' COMMENT '月销量最大值，每月初重新计算',
  `Price` int(11) NOT NULL DEFAULT '0' COMMENT '价格冗余字段，用于列表显示，添加SKU的时候更新一个最小的价格到这个字段',
  `Favorite` bigint(20) DEFAULT '0' COMMENT '收藏数',
  `AssessmentCount` bigint(20) DEFAULT '0' COMMENT '评论数',
  `ExpressFree` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否免邮 0-免邮 1-不免邮',
  `ExpressDetails` varchar(1024) DEFAULT NULL COMMENT 'JSON 格式的邮费说明',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  `ProductSeq` varchar(50) DEFAULT NULL COMMENT '款号',
  `remark` text COMMENT '文本描述的产品信息   商品规格',
  `MarketPrice` decimal(10,2) DEFAULT NULL COMMENT '市场价，不是玖币价格',
  `SizeTableImage` varchar(1024) DEFAULT NULL,
  `ClothesNumber` varchar(45) DEFAULT NULL COMMENT '商品款号 和 ProductSeq 重复了.',
  `PromotionImage` varchar(256) DEFAULT NULL COMMENT '推广图片存储路径',
  `Weight` int(11) DEFAULT '0' COMMENT '商品权重',
  `BrandId` bigint(20) unsigned DEFAULT '0' COMMENT '商品的品牌id',
  `ShowStatus` int(4) NOT NULL DEFAULT '0' COMMENT '0:默认全局显示，其他:自定义显示',
  `BottomPrice` decimal(10,2) DEFAULT '0.00',
  `MarketPriceMin` int(10) DEFAULT '0',
  `MarketPriceMax` int(10) DEFAULT '0',
  `Type` tinyint(4) DEFAULT '1' COMMENT '0: 零售 1:零售/批发 2:批发',
  `WholeSaleCash` decimal(10,2) DEFAULT '0.00' COMMENT '批发价',
  `Cash` decimal(10,2) DEFAULT NULL COMMENT '消费人民币\n',
  `JiuCoin` int(11) DEFAULT NULL COMMENT '消费玖币',
  `RestrictHistoryBuy` int(11) NOT NULL DEFAULT '-1' COMMENT '历史限购 -1:不限 ',
  `RestrictDayBuy` int(11) NOT NULL DEFAULT '-1' COMMENT '单日限购 -1:不限 ',
  `PromotionSetting` tinyint(4) NOT NULL DEFAULT '0' COMMENT '价格促销设置 0-无 1-定义\n',
  `PromotionCash` decimal(10,2) DEFAULT NULL COMMENT '促销“消费人民币”价',
  `PromotionJiuCoin` int(11) DEFAULT NULL COMMENT '促销“消费玖币”价',
  `PromotionStartTime` bigint(20) DEFAULT NULL COMMENT '价格促销开始时间',
  `PromotionEndTime` bigint(20) DEFAULT NULL COMMENT '价格促销结束时间',
  `RestrictCycle` int(11) NOT NULL DEFAULT '0' COMMENT '限购范围（天） 默认为0，代表无限购周期，',
  `RestrictHistoryBuyTime` bigint(20) NOT NULL DEFAULT '0',
  `RestrictDayBuyTime` bigint(20) NOT NULL DEFAULT '0',
  `LOWarehouseId` bigint(20) DEFAULT NULL COMMENT '主仓库',
  `Description` text,
  `RestrictId` bigint(20) DEFAULT NULL COMMENT '组合限购id',
  `VCategoryId` bigint(20) DEFAULT NULL COMMENT '虚拟分类Id',
  `Together` varchar(200) DEFAULT NULL COMMENT '搭配推荐',
  `CartSttstcs` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '购物车推荐统计',
  `HotSttstcs` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '热卖推荐统计',
  `SubscriptId` bigint(20) DEFAULT NULL COMMENT '角标Id',
  `BuyType` tinyint(4) DEFAULT '0' COMMENT '购买方式，0：原价或者折扣价，1：原价，2：折扣',
  `DisplayType` tinyint(4) DEFAULT '0' COMMENT '橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣',
  `PromotionBuyType` tinyint(4) DEFAULT '0' COMMENT '促销购买方式，0：原价或者折扣价，1：原价，2：折扣',
  `PromotionDisplayType` tinyint(4) DEFAULT '0' COMMENT '促销橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣\n',
  `PromotionSaleCount` int(11) DEFAULT '0' COMMENT '推广销量',
  `PromotionVisitCount` int(11) DEFAULT '0' COMMENT '推广访问量',
  `LOWarehouseId2` bigint(20) NOT NULL DEFAULT '0' COMMENT '副仓库',
  `SetLOWarehouseId2` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:不设置副仓 1:设置副仓',
  `DeductPercent` decimal(10,2) DEFAULT '0.00' COMMENT '玖币抵扣字段',
  `videoUrl` varchar(256) DEFAULT NULL COMMENT '商品视频url',
  `videoName` varchar(256) DEFAULT '' COMMENT '视频名称',
  `vip` tinyint(4) DEFAULT '0' COMMENT 'VIP商品：0不是Vip商品，1是Vip商品',
  `videoFileId` bigint(20) DEFAULT '0' COMMENT '商品视频fileId',
  `state` tinyint(4) DEFAULT NULL COMMENT '商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）',
  `delState` tinyint(4) DEFAULT '0' COMMENT '删除状态： 0（未删除）、 1（已删除）',
  `submitAuditTime` bigint(20) DEFAULT '0' COMMENT '提交审核时间',
  `auditTime` bigint(20) DEFAULT '0' COMMENT '最终审核时间',
  `auditNoPassReason` varchar(200) DEFAULT NULL COMMENT '审核拒绝理由',
  `upSoldTime` bigint(20) DEFAULT '0' COMMENT '上架时间',
  `downSoldTime` bigint(20) DEFAULT '0' COMMENT '下架时间',
  `newTime` bigint(20) DEFAULT '0' COMMENT '新建时间',
  `skuCount` int(11) DEFAULT '0' COMMENT 'SKU数量',
  `mainImg` varchar(200) DEFAULT NULL COMMENT '商品主图',
  `oneCategoryId` bigint(20) DEFAULT '0' COMMENT '所属一级分类ID',
  `oneCategoryName` varchar(30) DEFAULT NULL COMMENT '所属一级分类名称',
  `twoCategoryId` bigint(20) DEFAULT '0' COMMENT '所属二级分类ID',
  `twoCategoryName` varchar(30) DEFAULT NULL COMMENT '所属二级分类名称',
  `threeCategoryId` bigint(20) DEFAULT '0' COMMENT '所属三级分类ID',
  `threeCategoryName` varchar(30) DEFAULT NULL COMMENT '所属三级分类名称',
  `brandName` varchar(50) DEFAULT NULL COMMENT '品牌名称',
  `brandLogo` varchar(500) DEFAULT NULL COMMENT '品牌Logo',
  `needAudit` tinyint(4) DEFAULT '0' COMMENT '是否需要审核： 0（不需要审核）、 1（需要审核）',
  `ladderPriceJson` varchar(200) DEFAULT NULL COMMENT '阶梯价格JSON',
  `maxLadderPrice` decimal(10,2) DEFAULT '0.00' COMMENT '最大阶梯价格',
  `minLadderPrice` decimal(10,2) DEFAULT '0.00' COMMENT '最小阶梯价格',
  `supplierId` bigint(20) DEFAULT '0' COMMENT '供应商ID',
  `categoryIds` varchar(30) DEFAULT NULL COMMENT '分类ID集合,逗号分隔，如：一级分类ID,二级分类ID,三级分类ID',
  `badge_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '角标id：0：无角标  id:有角标',
  `badge_name` varchar(200) DEFAULT NULL COMMENT '角标名称',
  `badge_image` varchar(4096) DEFAULT NULL COMMENT '角标图片地址',
  `rank` int(11) DEFAULT '9999' COMMENT '排名',
  `grade_type` int(11) DEFAULT '0' COMMENT '品牌档次类型：0未知、1高档、2低档',
  `brand_type` int(11) DEFAULT '0' COMMENT '品牌类型：1(高档)，2(中档)',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Id_UNIQUE` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=7120 DEFAULT CHARSET=utf8;


 * </p>
 * @author 赵兴林
 * @since 2017-10-12
 */
//@TableName("yjj_product")
@Data
public class ProductNew {

    private static final long serialVersionUID = 1L;
//    删除状态： 0（未删除）、 1（已删除）
    public static final int delState_true = 1;//已删除
    public static final int delState_false = 0;//未删除
////设置商品需要提交审核，用来判断是否出现提审按钮    
//    是否需要审核： 0（不需要审核）、 1（需要审核）
    public static final int needAudit_true = 1;//1（需要审核）
    public static final int needAudit_false = 0;//0（不需要审核）
    
    /**
     * 商品id
     */
	//@TableId(value="Id", type= IdType.AUTO)
	private Long id;
    /**
     * 商品名
     */
	private String name;
    /**
     * 所属分类id
     */
	private Long categoryId;
    /**
     * 橱窗图片:JSON 格式数组
     */
	private String detailImages;
    /**
     * 详情图 json数组
     */
	private String summaryImages;
    /**
     * 状态:-1删除，0正常
     */
	private Integer status;
    /**
     * 上架时间
     */
	private Long saleStartTime;
    /**
     * 下架时间
     */
	private Long saleEndTime;
    /**
     * 销售类型：0-人民币 1-玖币
     */
	private Integer saleCurrencyType;
    /**
     * 总销量
     */
	private Integer saleTotalCount;
    /**
     * 月销量最大值，每月初重新计算
     */
	private Integer saleMonthlyMaxCount;
    /**
     * 价格冗余字段，用于列表显示，添加SKU的时候更新一个最小的价格到这个字段
     */
	private Integer price;
    /**
     * 收藏数
     */
	private Long favorite;
    /**
     * 评论数
     */
	private Long assessmentCount;
    /**
     * 是否免邮 0-免邮 1-不免邮
     */
	private Integer expressFree;
    /**
     * JSON 格式的邮费说明
     */
	private String expressDetails;
    /**
     * 创建时间
     */
	private Long createTime;
    /**
     * 更新时间
     */
	private Long updateTime;
	private Integer vip;
    /**
     * 款号(已废弃)
     */
	private String productSeq;
    /**
     * 文本描述的产品信息   商品规格
     */
	private String remark;
    /**
     * 市场价，不是玖币价格
     */
	private BigDecimal marketPrice;
	private String sizeTableImage;
    /**
     * 商品款号 （正在使用）
     */
	private String clothesNumber;
    /**
     * 推广图片存储路径
     */
	private String promotionImage;
    /**
     * 商品权重
     */
	private Integer weight;
    /**
     * 商品的品牌id
     */
	private Long brandId;
    /**
     * 0:默认全局显示，其他:自定义显示
     */
	private Integer showStatus;
	private Double bottomPrice;
	private Integer marketPriceMin;
	private Integer marketPriceMax;
    /**
     * 0: 零售 1:零售/批发 2:批发
     */
	private Integer type;
    /**
     * 批发价（该字段已经废弃，已经被阶梯价代替）
     */
	private Double wholeSaleCash;
    /**
     * 消费人民币

     */
	private Double cash;
    /**
     * 消费玖币
     */
	private Integer jiuCoin;
    /**
     * 历史限购 -1:不限 
     */
	private Integer restrictHistoryBuy;
    /**
     * 单日限购 -1:不限 
     */
	private Integer restrictDayBuy;
    /**
     * 价格促销设置 0-无 1-定义

     */
	private Integer promotionSetting;
    /**
     * 促销“消费人民币”价
     */
	private Double promotionCash;
    /**
     * 促销“消费玖币”价
     */
	private Integer promotionJiuCoin;
    /**
     * 价格促销开始时间
     */
	private Long promotionStartTime;
    /**
     * 价格促销结束时间
     */
	private Long promotionEndTime;
    /**
     * 限购范围（天） 默认为0，代表无限购周期，
     */
	private Integer restrictCycle;
	private Long restrictHistoryBuyTime;
	private Long restrictDayBuyTime;
    /**
     * 主仓库
     */
	private Long lOWarehouseId;
	private String description;
    /**
     * 组合限购id
     */
	private Long restrictId;
    /**
     * 虚拟分类Id
     */
	private Long vCategoryId;
    /**
     * 搭配推荐
     */
	private String together;
    /**
     * 购物车推荐统计
     */
	private BigDecimal cartSttstcs;
    /**
     * 热卖推荐统计
     */
	private BigDecimal hotSttstcs;
    /**
     * 
     */
	private Long subscriptId;
    /**
     * 购买方式，0：原价或者折扣价，1：原价，2：折扣
     */
	private Integer buyType;
    /**
     * 橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣
     */
	private Integer displayType;
    /**
     * 促销购买方式，0：原价或者折扣价，1：原价，2：折扣
     */
	private Integer PromotionBuyType;
    /**
     * 促销橱窗价格展示，0：原价或者折扣价，1：原价，2：折扣

     */
	private Integer promotionDisplayType;
    /**
     * 推广销量
     */
	private Integer promotionSaleCount;
    /**
     * 推广访问量
     */
	private Integer promotionVisitCount;
    /**
     * 副仓库
     */
	private Long lOWarehouseId2;
    /**
     * 0:不设置副仓 1:设置副仓
     */
	private Integer setLOWarehouseId2;
	
	
	private Double deductPercent;
    /**
     * 商品视频url
     */
	private String videoUrl;
    /**
     * 视频名称
     */
	private String videoName;
    /**
     * 商品视频fileId
     */
	private Long videoFileId;
    /**
     * 阶梯价格JSON
     */
	private String ladderPriceJson;
    /**
     * 最大阶梯价格
     */
	private Double maxLadderPrice;
    /**
     * 供应商ID
     */
	private Long supplierId;
    /**
     * 最小阶梯价格
     */
	private Double minLadderPrice;
	
	 /**
     * 商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
     */
	private Integer state;
	 /**
//     * 是否需要审核： 0（不需要审核）、 1（需要审核）
//     */
//	private Integer needAudit;
	 /**
     * 删除状态： 0（未删除）、 1（已删除）
     */
	private Integer delState;
	/**
     * 提交审核时间
     */
	private Long submitAuditTime;
	/**
     * 审核时间
     */
	private Long auditTime;
	/**
     * 审核拒绝理由
     */
	private String auditNoPassReason;
	/**
     * 上架时间
     */
	private Long upSoldTime;
	/**
     * 下架时间
     */
	private Long downSoldTime;
	/**
     * 新建时间
     */
	private Long newTime;
	/**
     * SKU数量
     */
	private Integer skuCount;
	/**
     * 商品主图
     */
	private String mainImg;
	
	/**
     * 分类ID集合,逗号分隔，如：一级分类ID,二级分类ID,三级分类ID
     */
	private String categoryIds;
	/**
     * 所属一级分类ID
     */
	private Long oneCategoryId;
	/**
     * 所属一级分类名称
     */
	private  String oneCategoryName;
	/**
     * 所属二级分类ID
     */
	private Long twoCategoryId ;
	/**
     * 所属二级分类名称
     */
	private String twoCategoryName ;
	/**
     * 所属三级分类ID
     */
	private Long threeCategoryId;
	/**
     * 所属三级分类名称
     */
	private String threeCategoryName ;
	/**
     * 品牌名称
     */
	private String brandName ;
	/**
     * 品牌Logo
     */
	private String brandLogo;

	/**
	 * 角标id
	 */
	//@TableField("badge_id")
	private Long badgeId;
	/**
	 * 角标名称
	 */
	//@TableField("badge_name")
	private String badgeName;
	/**
	 * 角标URL
	 */
	//@TableField("badge_image")
	private String badgeImage;
	/**
	 * 排名
	 */
	//@TableField("rank")
	private Integer rank;
	
	/**
	 * 品牌类型：1(高档)，2(中档)
	 */
	//@TableField("brand_type")
	private Integer brandType;

	/**
	 * @see: 商品的橱窗视频地址
	 */
	//@TableField("vedio_main")
	private String vedioMain;

	/**
	 * @see: 最后上架时间
	 */
	//@TableField("last_puton_time")
	private Long lastPutonTime;


	/**
	 * 会员等级 默认（普通）：0,一级会员商品：1 ...类推
	 */
	//@TableField("memberLevel")
	private Integer memberLevel;


	/**
	 * 会员阶梯价格JSON
	 */
	//@TableField("member_ladder_price_json")
	private String memberLadderPriceJson;


	/**
	 * 会员最大阶梯价格
	 */
	//@TableField("member_ladder_price_max")
	private BigDecimal memberLadderPriceMax;


	/**
	 * 会员最小阶梯价格
	 */
	//@TableField("member_ladder_price_min")
	private BigDecimal memberLadderPriceMin;

	/**
	 * 转换商品款号编辑值
	 * 将款号中去除看好前缀
	 * @param clothesNumberPrefix
	 * @return
	 */
	public  String buildClothesNumberEditValue(String clothesNumberPrefix){
		String clothesNumberEditValue = "";
		//    String a = this.ClothesNumber.toLowerCase();
		//    String pre = clothesNumberPrefix.toLowerCase();
		if(this.clothesNumber.startsWith(clothesNumberPrefix)){////已款号前缀开头，则截取款号前缀
			clothesNumberEditValue = this.clothesNumber.substring(clothesNumberPrefix.length());
		}else{
			clothesNumberEditValue = this.clothesNumber;
		}
		return clothesNumberEditValue;
	}
	/**
	 * 商品品类名称，只用于显示，格式例：裙装 -> 连衣裙
	 * @return
	 */
	public String getCategoryNames() {
		String categoryNames = "";
		if(StringUtils.isEmpty(oneCategoryName)){
			categoryNames = "无";
		}else if(StringUtils.isEmpty(twoCategoryName)){
			categoryNames = oneCategoryName;
			return categoryNames;
		}else if(StringUtils.isEmpty(threeCategoryName)){
			categoryNames = oneCategoryName + ">" + twoCategoryName;
			return categoryNames;
		}else{
			categoryNames = oneCategoryName + ">" + twoCategoryName + ">" +threeCategoryName;
		}
		return categoryNames;
	}
	/**
	 * 获得主图
	 *
	 * @return
	 */
	@JsonIgnore
	public String getFirstImage() {
		if (!StringUtils.equals("", getPromotionImage()) && !StringUtils.equals(null, getPromotionImage())) {
			return getPromotionImage();
		}
		String[] array = getDetailImageArray();
		if (array.length > 0) {
			return array[0];
		}
		String[] array2 = getSummaryImageArray();
		if (array2.length > 0) {
			return array2[0];
		}
		return null;
	}
	/**
	 * 得到概要图的第一张
	 *
	 * @return
	 */
	public String getFirstDetailImage() {
		String image = "";
		String[] detailImageArray = getDetailImageArray();
		if (detailImageArray.length > 0) {
			image = detailImageArray[0];
		}
		return image;
	}
	@JsonIgnore
	public String[] getDetailImageArray() {
		try {

			JSONArray array = JSON.parseArray(getDetailImages());
			if (array == null) {
				return new String[] {};
			}
			return array.toArray(new String[] {});
		} catch (Exception e) {
			return null;
		}
	}
	@JsonIgnore
	public String getDisplayPicture() {
		JSONArray retArray = JSON.parseArray(getDetailImages());
		if (retArray == null) {
			return "";
		}
		String[] pics = retArray.toArray(new String[] {});

		if (pics.length < 1) {
			return "";
		}
		return pics[0];
	}
	@JsonIgnore
	public String[] getSizeTableImageArray() {
		JSONArray array = JSON.parseArray(getSizeTableImage());
		if (array == null) {
			return new String[] {};
		}
		return array.toArray(new String[] {});
	}
	@JsonIgnore
	public String[] getSummaryImageArray() {
		JSONArray array = JSON.parseArray(getSummaryImages());
		if (array == null) {
			return new String[] {};
		}
		return array.toArray(new String[] {});
	}


//	public Integer getNeedAudit() {
//		return needAudit;
//	}
//
//	public void setNeedAudit(Integer needAudit) {
//		this.needAudit = needAudit;
//	}
	/**
	 * 根据阶梯价JSON解析出最大价格
	 * @param
	 * @return
	 */
	public static Double buildMaxLadderPrice(String ladderPriceJson) {
		double maxLadderPrice = 0;
		if(StringUtils.isNotEmpty(ladderPriceJson)){
			JSONArray ladderPriceArr = JSONArray.parseArray(ladderPriceJson);
			if(ladderPriceArr.size() > 0 ){
				JSONObject ladderPrice = ladderPriceArr.getJSONObject(0);
				String maxLadderPriceStr =ladderPrice.getString("price");
				maxLadderPrice = Double.parseDouble(maxLadderPriceStr);
			}
		}
		return maxLadderPrice;
	}
	
	/**
	 * 根据阶梯价JSON解析出最小价格
	 * @param
	 * @return
	 */
	public static Double buildMinLadderPrice(String ladderPriceJson) {
		double minLadderPrice = 0;
		if(StringUtils.isNotEmpty(ladderPriceJson)){
			JSONArray ladderPriceArr = JSONArray.parseArray(ladderPriceJson);
			if(ladderPriceArr.size() > 0 ){
				JSONObject ladderPrice = ladderPriceArr.getJSONObject(ladderPriceArr.size()-1);
				String minLadderPriceStr =ladderPrice.getString("price");
				minLadderPrice = Double.parseDouble(minLadderPriceStr);
			}
		}
		return minLadderPrice;
	}
//	/**
//	 * 根据购买数量获取对应阶梯价中的订单单价
//	 * @param buyCount
//	 * @return
//	 */
//	public double getUnitPriceByBuyCount(int buyCount) {
//		double unitPrice = buildCurrentLadderPriceByBuyCount(ladderPriceJson ,buyCount);//订单商品单价
//		return unitPrice;
//	}


	/**
	 * 获取阶梯价格中, 最小起批量
	 * @param ladderPriceJson
	 * @return int 最小值为1, 默认1
	 * @auther Charlie(唐静)
	 * @date 2018/5/28 16:13
	 */
	public static int getMinLadderCount(String ladderPriceJson) {
	    /* 默认1 */
		int defaultMin = 1;
		Map<Integer, Double> ladderPriceMap = buildLadderPriceMap(ladderPriceJson);
		if (null == ladderPriceMap || ladderPriceMap.isEmpty()) {
			return defaultMin;
		}


		/* 有阶梯价时, 返回最小起批值 */
		Set<Integer> ladderSet = ladderPriceMap.keySet();
		int i = 0;
		int temp = 1;
		for (int ladder : ladderSet) {
			if (i++ == 0) {
				temp = ladder;
			} else {
				if (temp > ladder) {
					temp = ladder;
				}
			}
		}
		return temp < defaultMin ? defaultMin : temp;
	}



	/**
	 * 将阶梯价JSON解析成Map,key(购买数量)、value(价格)
	 * @param
	 * @return
	 */
	public static Map<Integer, Double> buildLadderPriceMap(String ladderPriceJson) {
		Map<Integer, Double> ladderPriceMap = new HashMap<Integer, Double>();
		JSONArray ladderPriceJsonJSONArray = JSON.parseArray(ladderPriceJson);
		for(Object ladderPrice : ladderPriceJsonJSONArray){
			JSONObject ladderPriceJSONObject = (JSONObject)ladderPrice;
			int count = Integer.parseInt(ladderPriceJSONObject.get("count").toString());
			double price = ladderPriceJSONObject.getDoubleValue("price");
			ladderPriceMap.put(count, price);
		}
		return ladderPriceMap;
	}

	/**
	 * 根据购买数量获取对应阶梯价格
	 * @param buyCount
	 * @return 阶梯价格单价
	 */
	public static double buildCurrentLadderPriceByBuyCount(String ladderPriceJson,int buyCount){
		JSONArray ladderPriceJsonJSONArray = JSON.parseArray(ladderPriceJson);
		
//		for(Object ladderPrice : ladderPriceJsonJSONArray){
//			JSONObject ladderPriceJSONObject = (JSONObject)ladderPrice;
//			System.out.println(ladderPriceJSONObject);
//			System.out.println(ladderPriceJSONObject.get("count"));
//			System.out.println(ladderPriceJSONObject.get("price"));
//		}
		
		//阶梯价数量为1    数量之前已经做了校验
		if(ladderPriceJsonJSONArray.size() == 1){
			Object ladderPrice1 = ladderPriceJsonJSONArray.get(0);
			JSONObject ladderPriceJSONObject1 = (JSONObject)ladderPrice1;
			double price = ladderPriceJSONObject1.getDoubleValue("price");
//			logger.info("计算阶梯价格：price:"+price+",buyCount:"+buyCount+",ladderPriceJson"+ladderPriceJson);
			return price;
		}
		//阶梯价数量为2
		if(ladderPriceJsonJSONArray.size() == 2){
			Object ladderPrice2 = ladderPriceJsonJSONArray.get(1);
			JSONObject ladderPriceJSONObject2 = (JSONObject)ladderPrice2;
			int count2 = Integer.parseInt(ladderPriceJSONObject2.get("count").toString()) ;
			if(buyCount >= count2){
				double price = ladderPriceJSONObject2.getDoubleValue("price");
//				logger.info("计算阶梯价格：price:"+price+",buyCount:"+buyCount+",ladderPriceJson"+ladderPriceJson);
				return  price;
			}else{
				Object ladderPrice1 = ladderPriceJsonJSONArray.get(0);
				JSONObject ladderPriceJSONObject1 = (JSONObject)ladderPrice1;
				double price = ladderPriceJSONObject1.getDoubleValue("price");
//				logger.info("计算阶梯价格：price:"+price+",buyCount:"+buyCount+",ladderPriceJson"+ladderPriceJson);
				return  price;
			}
		}
		//阶梯价数量为3
		if(ladderPriceJsonJSONArray.size() == 3){
			Object ladderPrice3 = ladderPriceJsonJSONArray.get(2);
			JSONObject ladderPriceJSONObject3 = (JSONObject)ladderPrice3;
			int count3 = Integer.parseInt(ladderPriceJSONObject3.get("count").toString());

			if(buyCount >= count3){
				return ladderPriceJSONObject3.getDoubleValue("price");
			}else {
				Object ladderPrice2 = ladderPriceJsonJSONArray.get(1);
				JSONObject ladderPriceJSONObject2 = (JSONObject)ladderPrice2;
				int count2 = Integer.parseInt(ladderPriceJSONObject2.get("count").toString());
				if(buyCount >= count2){
					double price = ladderPriceJSONObject2.getDoubleValue("price");
//					logger.info("计算阶梯价格：price:"+price+",buyCount:"+buyCount+",ladderPriceJson"+ladderPriceJson);
					return  price;
				}else{
					Object ladderPrice1 = ladderPriceJsonJSONArray.get(0);
					JSONObject ladderPriceJSONObject1 = (JSONObject)ladderPrice1;
					double price = ladderPriceJSONObject1.getDoubleValue("price");
//					logger.info("计算阶梯价格：price:"+price+",buyCount:"+buyCount+",ladderPriceJson"+ladderPriceJson);
					return  price;
				}
			}
		}
//		logger.info("获取商品阶梯价格出现错误，请尽快排查问题！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
		return 0;
	}

	public static void main(String[] args) {
		String ladderPriceJson = "[{count:1,price:14.49}]";
		int buyCount = 1;
		JSONArray ladderPriceJsonJSONArray = JSON.parseArray(ladderPriceJson);
		
		for(Object ladderPrice : ladderPriceJsonJSONArray){
			JSONObject ladderPriceJSONObject = (JSONObject)ladderPrice;
			System.out.println(ladderPriceJSONObject);
			System.out.println(ladderPriceJSONObject.get("count"));
			System.out.println(ladderPriceJSONObject.get("price"));
		}
	}

	
}
