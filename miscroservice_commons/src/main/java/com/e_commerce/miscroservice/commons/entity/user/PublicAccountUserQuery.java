package com.e_commerce.miscroservice.commons.entity.user;

import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/21 18:04
 * @Copyright 玖远网络
 */
@Data
public class PublicAccountUserQuery extends PublicAccountUser{

    private String userName;
    private String createTimeBefore;
    private String createTimeAfter;



}
