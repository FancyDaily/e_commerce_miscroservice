package com.e_commerce.miscroservice.commons.entity.distribution;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DistributionSystem;
import lombok.Data;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/18 15:38
 * @Copyright 玖远网络
 */
@Data
public class UserInformationResponse extends DistributionSystem {

    private String nickName;
    private String storeName;
    private Integer higherRole;
    private Integer leaderRole;
    private Long higherId;
    private Long leaderId;
    private String higherNickName;
    private String leaderNickName;
    private Double countMoney;
}
