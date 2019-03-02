package com.e_commerce.miscroservice.commons.enums.user;


/**
 * 会员审核状态枚举

 * @author czy
 */
public enum StoreAuditStatusEnum{
	fail(-1),
	
    submit(0),
    
    pass(1),
    
    forbidden(-2);

    private StoreAuditStatusEnum(int intValue) {
        this.intValue = intValue;
    }

    private int intValue;

    public int getIntValue() {
        return intValue;
    }
}
