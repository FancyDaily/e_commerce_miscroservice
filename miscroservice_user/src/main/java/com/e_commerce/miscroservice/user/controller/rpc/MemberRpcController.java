package com.e_commerce.miscroservice.user.controller.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.entity.user.MemberOperatorRequest;
import com.e_commerce.miscroservice.commons.entity.user.StoreWxaShopAuditDataQuery;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.user.service.store.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/30 17:37
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping( "user/rpc/memberRpcController" )
public class MemberRpcController{

    private Log logger = Log.getInstance(MemberRpcController.class);


    @Autowired
    private MemberService memberService;


    /**
     * 购买会员(作废)
     *
     * @param canal 渠道描述, 比如:公账号,app购买页,线下
     * @param city 市
     * @param district 区
     * @param memberLevel memberLevel
     * @param name 用户姓名
     * @param orderNo 订单号
     * @param totalMoney totalMoney  必填!!!
     * @param phone 用户APP手机号 必填!!!
     * @param province 省
     * @param type 会员类型 1:基础版,2:会员版,3:至尊版 等待... 必填!!!
     * @return com.e_commerce.miscroservice.commons.entity.application.user.Member
     * @author Charlie
     * @date 2018/9/30 17:44
     */
    @RequestMapping("buyMember")
    @Deprecated
    public String buyMember(
              @RequestParam(value = "canal", required = false) String canal,
              @RequestParam (value = "city", required = false) String city,
              @RequestParam (value = "district", required = false) String district,
              @RequestParam (value = "memberLevel", required = false, defaultValue = "1") Integer memberLevel,
              @RequestParam (value = "name", required = false) String name,
              @RequestParam (value = "orderNo", required = false) String orderNo,
              @RequestParam (value = "totalMoney") Double totalMoney,
              @RequestParam (value = "phone") String phone,
              @RequestParam (value = "province", required = false) String province,
              @RequestParam (value = "type") Integer type
    ) {
//        MemberOperatorRequest request = new MemberOperatorRequest ();
//        request.setCanal (canal);
//        request.setCity (city);
//        request.setDistrict (district);
//        request.setMemberLevel (memberLevel);
//        request.setName (name);
//        request.setOrderNo (orderNo);
//        request.setTotalMoney (totalMoney);
//        request.setPhone (phone);
//        request.setProvince (province);
//        request.setType (type);
//        try {
////            Member member = memberService.buyMember(request);
//            Member member = null;
//            return JSON.toJSONString(Response.success (member));
//        } catch (ErrorHelper e) {
//            return JSON.toJSONString(ResponseHelper.errorHandler (e));
//        }
        return JSON.toJSONString(Response.errorMsg ("接口暂停"));
    }





    /**
     * 购买会员成功
     *
     * @param request request
     * @return boolean
     * @author Charlie
     * @date 2018/12/13 9:11
     */
    @RequestMapping( "buyMemberSuccess" )
    public boolean buyMemberSuccess(@RequestBody MemberOperatorRequest request){
        try {
            return memberService.buyMemberSuccess (request);
        } catch (ErrorHelper e) {
            logger.error ("购买会员成 msg={}", e.getMsg ());
            return false;
        }
    }



    /**
     * 线下开通共享店铺
     *
     * @param bossName 老板名称
     * @param shopName 店铺名称
     * @param industry 行业
     * @param mainBusiness 主营业务
     * @param phone 手机号
     * @param memberType 会员类型
     * @param address 地址
     * @return java.lang.String
     * @author Charlie
     * @date 2018/12/19 10:57
     */
    @RequestMapping("openInShopMemberOffline")
    public String openInShopMemberOffline(
            @RequestParam( "bossName" ) String bossName,
            @RequestParam( "shopName" ) String shopName,
            @RequestParam( "industry" ) String industry,
            @RequestParam( "mainBusiness" ) String mainBusiness,
            @RequestParam( "phone" ) String phone,
            @RequestParam( "memberType" ) Integer memberType,
            @RequestParam( "address" ) String address
    ){

        MemberOperatorRequest memberReq = new MemberOperatorRequest ();
        StoreWxaShopAuditDataQuery auditReq = new StoreWxaShopAuditDataQuery ();
        memberReq.setPhone (phone);
        memberReq.setType (memberType);
        auditReq.setBossName (bossName);
        auditReq.setShopName (shopName);
        auditReq.setIndustry (industry);
        auditReq.setMainBusiness (mainBusiness);
        auditReq.setAddress (address);
        try {
            return JSONObject.toJSONString (Response.success (memberService.openInShopMemberOffline (memberReq, auditReq)));
        } catch (ErrorHelper e) {
            return JSONObject.toJSONString (ResponseHelper.errorHandler (e));
        }
    }



    /**
     * 更新用户会员状态
     *
     * @param memberId memberId
     * @param delStatus 0 正常  1 删除
     * @return java.lang.String
     * @author Charlie
     * @date 2018/12/19 11:32
     */
    @RequestMapping( "updMember/delStatus" )
    public String updMemberDelStatus(@RequestParam("memberId") Long memberId, @RequestParam("delStatus") Integer delStatus){
        try {
            return JSONObject.toJSONString (Response.success (memberService.updMemberDelStatus (memberId, delStatus)));
        } catch (ErrorHelper e) {
            return JSONObject.toJSONString (ResponseHelper.errorHandler (e));
        }
    }


}
