package com.e_commerce.miscroservice.commons.enums.distributionSystem;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/18 19:03
 * @Copyright 玖远网络
 */
public enum  CashOutInStatusDetailEnum{


    /**1;待结算 */
    WAIT(1),
    /**2:成功 */
    SUCCESS(2),
    /**3:已冻结 */
    FROZEN(3),
    /**4:待发放/提现中 */
    WAIT_GRANT(4),
    /**-2:结算失败 */
    FAILED(-2),
    /**-4:待结算到失败的中间状态 */
    WAIT_FAILED(-4)
            ;

    private int code;

    CashOutInStatusDetailEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public boolean isThis(Integer code) {
        if (code == null) {
            return Boolean.FALSE;
        }
        return this.code == code;
    }
}
