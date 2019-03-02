package com.e_commerce.miscroservice.commons.entity.user;

import com.e_commerce.miscroservice.commons.helper.log.Log;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/17 14:17
 * @Copyright 玖远网络
 */
public class StoreShopVo{

    private Log logger = Log.getInstance(StoreShopVo.class);

    private Long storeId;
    /**
     * 0无,1共享版,2专享版
     */
    private Integer isOpenWxa;
    private Long wxaCloseTime;
    private Long wxaOpenTime;
    private Integer wxaBusinessType;

    public static StoreShopVo create(Long storeId, Integer isOpenWxa, Long wxaCloseTime, Long wxaOpenTime, Integer wxaBusinessType) {
        return new StoreShopVo(storeId, isOpenWxa, wxaCloseTime, wxaOpenTime, wxaBusinessType);
    }

    private StoreShopVo(Long storeId, Integer isOpenWxa, Long wxaCloseTime, Long wxaOpenTime, Integer wxaBusinessType) {
        this.storeId = storeId;
        this.isOpenWxa = isOpenWxa;
        this.wxaCloseTime = wxaCloseTime;
        this.wxaOpenTime = wxaOpenTime;
        this.wxaBusinessType = wxaBusinessType;
    }

    /**
     * 店铺是否可用
     *
     * @return boolean
     * @author Charlie
     * @date 2018/12/17 14:59
     */
    private boolean isShopCanUse() {

        if (isOpenWxa.equals (2)) {
            logger.info ("店铺冻结");
            return Boolean.FALSE;
        }

        //过期先不管
//        if (System.currentTimeMillis () > wxaOpenTime) {
//            logger.info ("店铺已过期");
//            return Boolean.FALSE;
//        }
        return Boolean.TRUE;
    }


    /**
     * 判断是否是共享店铺
     *
     * @return true:共享版, false:专项版
     * @author Charlie
     * @throws Exception
     * @date 2018/12/17 15:13
     */
    public boolean isShareShopOrSelfShopSafe() throws Exception {
        if (isShopCanUse ()) {
            //共享版都是1
            if (wxaBusinessType.equals (1)) {
                return Boolean.TRUE;
            }
            else {
                return Boolean.FALSE;
            }
            /*else if (isOpenWxa.equals (2)) {
                return Boolean.FALSE;
            }
            throw new Exception ("未知的店铺状态");*/
        }
        throw new Exception ("店铺未开通,或已过期");
    }

}
