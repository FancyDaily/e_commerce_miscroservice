package com.e_commerce.miscroservice.commons.entity.application.user;

import java.math.BigDecimal;
import java.util.Date;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 商家会员表
 * </p>
 *
 * @author Charlie
 * @since 2018-09-29
 */
@Table("yjj_member")
@Data
public class Member implements Serializable{

    /**
     * id
     */
	@Id
	private Long id;
    /**
     * 平台类型
     */
	private Integer platformType;
    /**
     * 用户id
     */
	private Long userId;
    /**
     * 会员等级 默认0：普通用户，一级会员：1 等
     */
	private Integer memberLevel;
    /**
     * 历史充值总额
     */
	private BigDecimal moneyTotal;
    /**
     * 默认0 正常  1 删除 2 冻结
     */
	private Integer delState;
    /**
     * 创建时间
     */
	private Date createTime;
    /**
     * 修改时间
     */
	private Date updateTime;
    /**
     * 会员类型 1:基础版,2:会员版,3:至尊版
     */
	private Integer type;
    /**
     * 有效时间队列和会员等级,时间升序排序 [1515640271000:3][1519630271000:2]
     */
	private String validTimeQueue;
    /**
     * 过期时间 0也过期
     */
	private Long endTime;
    /**
     * 开通渠道
     */
	private String canal;
    /**
     * 市
     */
	private String city;
    /**
     * 省
     */
	private String province;
    /**
     * 区
     */
	private String district;
    /**
     * 用户姓名
     */
	private String name;


}
