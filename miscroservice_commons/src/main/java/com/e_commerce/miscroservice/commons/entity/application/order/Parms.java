package com.e_commerce.miscroservice.commons.entity.application.order;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Parms {
    //订单号
    private Long orderNo;
    //物流信息
    private  String expressCompamyName;
    //物流号
    private String expressNo;

    //发货备注
    private String remark;
    //选择的skuId和byCount
    private StoreSend[] storeSendList;

    //发货类型(是否全部发货)
    private int type;

}
