package com.e_commerce.miscroservice.commons.enums.wx;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/12 14:38
 * @Copyright 玖远网络
 */
public enum TemplateEnum{

    /**
     * 收益到账通知
     */
    DISTRIBUTION_EARNINGS_ADVICE(
            "AT0035",
            "收益到账通知",
            new TemplateKeyEnum[]{
                    TemplateKeyEnum.PRODUCT_NAME,
                    TemplateKeyEnum.ORDER_NO,
                    TemplateKeyEnum.MONEY,
                    TemplateKeyEnum.EARNINGS_SOURCE,
                    TemplateKeyEnum.EARNINGS_MONEY,
                    TemplateKeyEnum.IN_ACCOUNT_TIME,
            })
    ;

    /**
     * id
     */
    private String id;
    private String title;
    /**
     * 关键词
     */
    private TemplateKeyEnum[] keys;

    TemplateEnum(String id, String title, TemplateKeyEnum[] keys) {
        this.id = id;
        this.title = title;
        this.keys = keys;
    }


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public TemplateKeyEnum[] getKeys() {
        return keys;
    }

    public Integer[] getKeysId() {
        Integer[] ids = new Integer[keys.length];
        for (int i = 0; i < keys.length; i++) {
            ids[i] = keys[i].getId ();
        }
        return ids;
    }
}
