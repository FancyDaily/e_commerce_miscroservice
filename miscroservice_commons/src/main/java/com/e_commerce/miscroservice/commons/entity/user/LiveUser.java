package com.e_commerce.miscroservice.commons.entity.user;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author hyf
 * @Date 2019/1/15 14:22
 */
@Data
@Table(value = "live_user",commit = "主播表")
public class LiveUser {

    @Id
    private Long  id;

    /**
     * 姓名
     */
    @Column(value = "name",length = 256,commit = "姓名")
    private String name;
    /**
     * 昵称
     */
    @Column(value = "nick_name",length = 256,commit = "昵称")
    private String nickName;
     /**
     * 直播标题
     */
    @Column(value = "title",length = 256,commit = "直播标题")
    private String title;


    @Column(value = "icon",length = 512,commit = "头像")
    private String icon;
    /**
     * 年龄
     */
    @Column(value = "age",length = 11,commit = "年龄")
    private Integer age;

    /**
     * 身份证
     */
    @Column(value = "id_card",length = 256,commit = "身份证")
    private String idCard;

    /**
     * 手机号
     */
    @Column(value = "phone",length = 256,commit = "手机号")
    private String phone;

    @Column(value = "open_official",length = 2,commit = "是否开启官方直播 默认0：关闭  1：开启",defaultVal = "0")
    private Integer openOfficial;

    /**
     * 性别
     */
    @Column(value = "sex",length = 2,commit = "性别  0未知 1 男，2 女" , defaultVal = "2")
    private Integer sex;

    @Column(value = "store_id",length = 20,commit = "所属店铺")
    private Long storeId;
    @Column(value = "member_id",length = 20,commit = "小程序id")
    private Long memberId;

    @Column(value = "room_num",length = 20,commit = "房间号")
    private Long roomNum;

    @Column(value = "live_type",length = 11,defaultVal = "0",commit = "主播类型: 0 店家主播，1 平台主播  ，2 供应商主播 ，3 普通主播")
    private Integer liveType;

    @Column(value = "live_room_type",length = 11,defaultVal = "1001",commit = "直播间类型  1001 店家 1002 平台 1003 经销商")
    private Integer liveRoomType;

    @Column(value = "status",length = 2,commit = "主播状态：默认 0 正常 ，1 冻结",defaultVal = "0")
    private Integer status;


    /**
     * 是否删除 0 正常  1 删除
     */
    @Column(value = "del_status",length = 4,defaultVal = "0",commit = "是否删除 0 正常  1 删除")
    private Integer delStatus;

    /**
     * 创建时间
     */
    @Column(value = "create_time",dateGeneStrategy= DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    /**
     * 更新时间
     */
    @Column(value = "update_time",dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE,commit = "更新时间")
    private Timestamp updateTime;

}
