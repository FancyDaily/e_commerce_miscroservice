package com.e_commerce.miscroservice.commons.enums.user;


public enum UserType  {
    EMAIL(0),

    PHONE(1),

    WEIXIN(2);
    
    private UserType(int intValue) {
        this.intValue = intValue;
    }

    private int intValue;

    public int getIntValue() {
        return intValue;
    }

    public static UserType parse(int intValue) {
        for (UserType type : values()) {
            if (type.getIntValue() == intValue) {
                return type;
            }
        }
        return null;
    }

}
