package com.e_commerce.miscroservice.commons.enums.task;

/**
 * @Author hyf
 * @Date 2019/1/10 14:23
 */
public enum TaskTypeEnums {
    /**
     * 未知
     */
    UNKNOWN(- 1),

    //=============================模块前缀标识规则=============================//
    //直播间模块
    MODEL_TYPE_LIVE(1),
    //店中店模块
    MODEL_TYPE_SHOP(2),

    //=============================属性标识=============================//
    LIVE_COMMON_SHOP(1001), //店家直播
    LIVE_SPECIAL_PLATFORM(1002), //平台直播
    LIVE_SPECIAL_DISTRIBUTOR(1003),//经销商直播


    //=============================操作类型选择=============================//
    LIVE_SHOP_READ(10011),
    LIVE_SHOP_CREATE(10012),
    LIVE_SHOP_UPDATE(10013),
    LIVE_SHOP_DELETE(10014),
    LIVE_READ_ALL_SHOP(10015),


//   用户进入直播间
    LIVE_ROOM_MEMBER_INTO(10001),
//    销毁直播间
    LIVE_ROOM_DESTROY(10002),
//    用户推出
    LIVE_ROOM_MEMBER_OUT(10003),
//    当前直播间在线 用户
    LIVE_ROOM_MEMBER_LIST(10004),
    /**
     * 直播间发言
     */
    LIVE_TALK_SEND(10040),
    /**
     * 直播间接收别人发言
     */
    LIVE_TALK_RECEIVE(10041),

    //店中店类型  CRUD
    TYPE_SHOP_READ(1),
    TYPE_SHOP_CREATE(2),
    TYPE_SHOP_UPDATE(3),
    TYPE_SHOP_DELETE(4),
    ;

    private Integer key;

    public static TaskTypeEnums create(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (TaskTypeEnums e : values()) {
            if (e.key.equals(code)) {
                return e;
            }
        }
        return UNKNOWN;
    }

    public Integer getKey() {
        return key;
    }

    TaskTypeEnums(Integer key) {
        this.key = key;
    }

    public boolean isMe(Integer key) {
        if (key == null) {
            return Boolean.FALSE;
        }
        return key.intValue() == this.key.intValue();
    }

    public static boolean inLiveType(Integer key){
        if (key == null) {
            return Boolean.FALSE;
        }
        return key==LIVE_COMMON_SHOP.getKey()||key==LIVE_SPECIAL_PLATFORM.getKey()||key==LIVE_SPECIAL_DISTRIBUTOR.getKey();
    }


}
