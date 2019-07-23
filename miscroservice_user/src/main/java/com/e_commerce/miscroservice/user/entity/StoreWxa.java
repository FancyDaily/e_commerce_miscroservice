/*
package com.e_commerce.miscroservice.user.entity;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;


*/
/**
 * <p>
 * 门店小程序信息记录表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-07-05
 *//*

@Table("jiuy_store_wxa")
@Data
public class StoreWxa {

	*/
/*
	这个表有点问题, 微信信息, 和支付信息都放在一起, 最好拆成两块, 一个用户基础信息,
	放到用户模块, 支付信息放在订单模块

	 *//*


	@Id
	private Long id;
    */
/**
     * 商家ID(专项版才有)
     *//*

	private Long storeId;


	*/
/**
	 * 店中店商户ID(共享版才有)
	 * <p>老系统查询兼容</p>
	 *//*

	@Column(theValue = "in_shop_store_id", commit = "店中店的storeId", defaultVal = "0", length = 20)
	private Long inShopStoreId;


    */
/**
     * 授权方APPID
     *//*

	private String appId;
    */
/**
     * 授权方昵称(小程序名称)
     *//*

	private String nickName;
    */
/**
     * 授权方头像
     *//*

	private String headImg;
    */
/**
     * 授权方公众号的原始IDgh_eb5e3a772040
     *//*

	private String userName;
    */
/**
     * 授权方所设置的微信号，可能为空
     *//*

	private String alias;
    */
/**
     * 小程序二维码图片的URL
     *//*

	private String qrcodeUrl;
    */
/**
     * 公众号的主体名称，例：腾讯计算机系统有限公司
     *//*

	private String principalName;
    */
/**
     * 签名描述
     *//*

	private String signature;
	*/
/**
     * idc
     *//*

	private String idc;
	
	*/
/**
     * 刷新token
     *//*

	private String refreshToken;
	
	*/
/**
     * 商户号
     *//*

	private String mchId;
	
	*/
/**
  	商户秘钥
     *//*

	private String payKey;
	
	
    */
/**
     * 小程序二维码图片的URL
     *//*

	private String authorizerInfoJson;
    */
/**
     * 创建时间
     *//*

	private Long createTime;


	@Column(theValue = "update_time",dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE,commit = "更新时间")
	private Timestamp updateTime;

}
*/
