package com.e_commerce.miscroservice.commons.enums.colligate;

import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import org.apache.commons.lang.StringUtils;

/**
 * 加解密公共的枚举
 */
public enum EncryptPubEnum {

    DEVICE_SECRET_KEY(TokenUtil.UID, "yiTongJin2017");
//    DEVICE_SECRET_KEY(JwtToken.UID, "yjj2018");

    private String field;
    private String key;

    EncryptPubEnum(String field, String key) {
        this.field = field;
        this.key = key;
    }

    public static String getKeyByFiled(String field) {
        String result = "";

        if (StringUtils.isEmpty(field)) {
            return result;
        }

        EncryptPubEnum[] values = EncryptPubEnum.values();


        for (EncryptPubEnum encryptPubEnum : values) {
            if (encryptPubEnum.field.equals(field)) {
                result = encryptPubEnum.key;
                break;
            }
        }

        return result;


    }

    public String toKey() {
        return key;
    }
}
