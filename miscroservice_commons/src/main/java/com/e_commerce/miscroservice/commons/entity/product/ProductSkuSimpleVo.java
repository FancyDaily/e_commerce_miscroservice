package com.e_commerce.miscroservice.commons.entity.product;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.e_commerce.miscroservice.commons.enums.EmptyEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/26 19:27
 * @Copyright 玖远网络
 */
@Data
public class ProductSkuSimpleVo{

    private static Log logger = Log.getInstance(ProductSkuSimpleVo.class);

    /**
     * skuId
     */
    private Long id;

    /**
     * 库存
     */
    private Integer remainCount;

    /**
     * 状态:-3废弃，-2停用，-1下架，0正常，1定时上架
     */
    private Integer status;

    /**
     * 颜色ID
     */
    private Long colorId;

    /**
     * 尺码ID
     */
    private Long sizeId;

    /**
     * 颜色名称
     */
    private String colorName;

    /**
     * 尺码名称
     */
    private String sizeName;


    public static List<ProductSkuSimpleVo> create(String skuJsonArray) {
        if (StringUtils.isBlank (skuJsonArray)) {
            return EmptyEnum.list ();
        }

        List<ProductSkuSimpleVo> skuList = null;
        try {
            skuList = JSONObject.parseObject (skuJsonArray, new TypeReference<List<ProductSkuSimpleVo>> (){});
        } catch (Exception e) {
            logger.error ("类型转换失败, 未知的sku格式");
            ErrorHelper.declare (false, "类型转换失败, 未知的sku格式");
        }
        return skuList;
    }
}
