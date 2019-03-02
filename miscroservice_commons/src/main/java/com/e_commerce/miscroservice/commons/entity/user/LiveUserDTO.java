package com.e_commerce.miscroservice.commons.entity.user;

import lombok.Data;
import org.springframework.context.annotation.Bean;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/17 12:59
 * @Copyright 玖远网络
 */
@Data
public class LiveUserDTO {

    private Long  id;

    private String name;
    private String nickName;
    private String icon;
    private Integer age;
    private Integer openOfficial;
    private String idCard;

    private String phone;

    private Integer sex;

    private Long storeId;
    private Long memberId;

    private Long roomNum;

    /**
     * 主播类型: 0 店家主播，1 平台主播  ，2 供应商主播 ，3 普通主播
     */
    private Integer liveType;

    /**
     * 主播状态：默认 0 正常 ，1 冻结
     */
    private Integer status;


    /**
     * 是否删除 0 正常  1 删除
     */
    private Integer delStatus;
}
