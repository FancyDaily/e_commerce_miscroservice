package com.e_commerce.miscroservice.user.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import lombok.Data;

@Data
public class CollectionView {
    @Id
    private Long id;

    private Long serviceId;

    private Long mainId;

    private String nameAudioUrl;

    private String serviceName;

    private Integer servicePersonnel;

    private Integer servicePlace;

    private String labels;

    private Integer type;

    private Integer status;

    private Integer source;

    private Long serviceTypeId;

    private String addressName;

    private Double longitude;

    private Double latitude;

    private Integer totalEvaluate;

    private Integer enrollNum;

    private Integer confirmNum;

    private Long startTime;

    private Long endTime;

    private Integer serviceStatus;

    private Integer openAuth;

    private Integer timeType;

    private Long collectTime;

    private Integer collectType;

    private Long createUser;

    private String createUserName;

    private Long createTime;

    private Long updateUser;

    private String updateUserName;

    private Long updateTime;

    private Long companyId;

    private String isValid;

    private String collectionTime;

    private String coverPic;
    /**
     * 可见状态 1、可见 2、不可见
     */
    private String visiableStatus;

    private static final long serialVersionUID = 1L;
}
