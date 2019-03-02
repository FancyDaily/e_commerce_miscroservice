package com.e_commerce.miscroservice.commons.enums.live;


import com.e_commerce.miscroservice.commons.enums.EmptyEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/10 13:43
 * @Copyright 玖远网络
 */
public enum LiveTalkTypeEnum {

    /**
     * 未知
     */
    UNKNOWN(-1),
    /**
     * 1用户发言
     */
    MEMBER_SPEAK(1),
    /**
     * 2播主发言
     */
    ANCHOR_SPEAK(2),
    /**
     * 11进房间
     */
    MEMBER_IN_ROOM(11),
    /**
     * 12加入购物车(暂没用)
     */
    ADD_CART(12),
    /**
     * 13下单
     */
    ORDER(13),
    /**
     * 21平台通知:法律法规(暂没用)
     */
    SYS_POLICY(21),;

    LiveTalkTypeEnum(Integer code) {
        this.code = code;
    }

    private Integer code;

    private static final String SHIELD = "*";


    public static LiveTalkTypeEnum me(Integer type) {
        if (type == null) {
            return UNKNOWN;
        }
        for (LiveTalkTypeEnum typeEnum : values()) {
            if (typeEnum.code.intValue() == type.intValue()) {
                return typeEnum;
            }
        }
        return UNKNOWN;
    }


    public String formatName(String userName) {

        switch (this) {
            case MEMBER_SPEAK:
            case MEMBER_IN_ROOM:
                return userName;
            case ADD_CART:
            case ORDER:
                //''==>n**l
                //hello==> h***o
                //h==> h
                //he==>h*
                if (StringUtils.isBlank(userName)) {
                    return "n**l";
                } else {
                    int len = userName.length();
                    if (len > 2) {
                        char offset = userName.charAt(0);
                        char end = userName.charAt(len);
                        StringBuilder newName = new StringBuilder(offset);
                        for (int i = 0; i < len - 2; i++) {
                            newName.append(SHIELD);
                        }
                        return newName.append(end).toString();
                    } else if (len == 2) {
                        return userName.charAt(0)+SHIELD;
                    } else {
                        return userName;
                    }
                }
            case SYS_POLICY:
                return "系统通知";
            case ANCHOR_SPEAK:
                return "主播回复";
            default:
                return EmptyEnum.string;
        }

    }

    public boolean isThis(Integer type) {
        return this.code.equals(type);
    }
}
