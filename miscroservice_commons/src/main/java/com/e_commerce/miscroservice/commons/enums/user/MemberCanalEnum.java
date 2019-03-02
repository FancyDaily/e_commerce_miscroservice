package com.e_commerce.miscroservice.commons.enums.user;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.tools.ant.taskdefs.optional.Cab;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/11 18:02
 * @Copyright 玖远网络
 */
public enum MemberCanalEnum{

    /**
     * 购买渠道1app购买,2线下,3公众号购买,4店中店购买
     */
    APP(1,"app购买页"),
    OFFLINE(2,"线下"),
    PUBLIC_ACCOUNT(3,"公账号购买"),
    IN_SHOP(4,"店中店购买")
    ;


    private int code;
    private String description;

    MemberCanalEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MemberCanalEnum instance(Integer canal) {
        if (canal == null) {
            return null;
        }
        for (MemberCanalEnum canalEnum : values ()) {
            if (canalEnum.getCode () == canal) {
                return canalEnum;
            }
        }
        return null;
    }


    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean isThis(Integer canal) {
        if (canal == null) {
            return Boolean.FALSE;
        }

        return code == canal;

    }
}
