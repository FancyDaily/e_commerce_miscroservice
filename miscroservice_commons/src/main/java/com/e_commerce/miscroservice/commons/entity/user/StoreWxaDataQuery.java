package com.e_commerce.miscroservice.commons.entity.user;

import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/17 18:21
 * @Copyright 玖远网络
 */
@Data
public class StoreWxaDataQuery{


    private Integer pageSize;
    private Integer pageNumber;
    private String businessName;
    private String phone;
    private String shopName;
    private Long wxaOpenTimeCeil;
    private Long wxaOpenTimeFloor;
    private Integer businessType;


    public void setWxaOpenTimeCeilStr(String wxaOpenTimeCeil) {
        this.wxaOpenTimeCeil = TimeUtils.str2Long (wxaOpenTimeCeil);
    }

    public void setWxaOpenTimeFloorStr(String wxaOpenTimeFloor) {
        this.wxaOpenTimeFloor = TimeUtils.str2Long (wxaOpenTimeFloor);
    }
}
