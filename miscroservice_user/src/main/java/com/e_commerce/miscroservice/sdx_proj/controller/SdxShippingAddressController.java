package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.JavaDocReader;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.SdxShippingAddressService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxShippingAddressVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 书袋熊地址
 * @Author: FangyiXu
 * @Date: 2019-10-25 11:57
 */
@RestController
@RequestMapping("sdx/address")
@Log
public class SdxShippingAddressController {

	@Autowired
	private SdxShippingAddressService sdxShippingAddressService;

	/**
	 * 添加或者修改配送地址
	 *
	 * @param id        配送地址的Id,修改或者查询的需要
	 * @param city        市
	 * @param name        姓名
	 * @param county        区/县
	 * @param street        街道
	 * @param userId        用户编号
	 * @param userTel        手机号
	 * @param province        省
	 * @param detailAddress        详细地址
	 *
	 *                 code==503,代表服务器出错,请先检测参数类型是否正确
	 *                 code==500,代表参数不正确
	 *                 code==200,代表请求成功
	 *                 data==0,代表操作不成功
	 *                 data!=0,新增时候data返回的是自增id,修改的时候代表影响的数量
	 *
	 * @return
	 */
	@PostMapping("mod")
	@Consume(TSdxShippingAddressVo.class)
	@UrlAuth
	public Response modTSdxShippingAddress(@RequestParam(required = false) Long id,@RequestParam(required = false) String city,@RequestParam(required = false) String name,@RequestParam(required = false) String county,@RequestParam(required = false) String street,@RequestParam(required = false) Long userId,@RequestParam(required = false) String userTel,@RequestParam(required = false) String province,@RequestParam(required = false) String detailAddress) {
		TSdxShippingAddressVo tSdxShippingAddressVo = (TSdxShippingAddressVo) ConsumeHelper.getObj();
		if (tSdxShippingAddressVo == null) {
			return Response.fail();
		}
//		tSdxShippingAddressVo.setUserId(IdUtil.getId());
		return Response.success(sdxShippingAddressService.modTSdxShippingAddress(tSdxShippingAddressVo.copyTSdxShippingAddressPo()));
	}

	/**
	 * 删除配送地址
	 *
	 * @param ids 配送地址的Id的集合,例如1,2,3多个用英文,隔开
	 *
	 *                 code==503,代表服务器出错,请先检测参数类型是否正确
	 *                 code==500,代表参数不正确
	 *                 code==200,代表请求成功
	 *                 data==0,代表操作不成功
	 *                 data!=0,代表影响的数量
	 *
	 * @return
	 */
	@RequestMapping("del")
	@UrlAuth
	public Response delTSdxShippingAddress(Long[] ids) {
		if (ids == null || ids.length == 0) {
			return Response.fail();
		}
		return Response.success(sdxShippingAddressService.delTSdxShippingAddressByIds(ids));
	}

	/**
	 * 查找配送地址
	 *
	 * @param page 页数默认第一页
	 * @param size 每页返回的数量，默认10个
	 * @param openResponseExplainFlag 如果想查看返回类型的字段说明,请填写任意内容即可查看返回说明
	 * @param id        配送地址的Id,修改或者查询的需要
	 * @param city        市
	 * @param name        姓名
	 * @param county        区/县
	 * @param street        街道
	 * @param userId        用户编号
	 * @param userTel        手机号
	 * @param province        省
	 * @param detailAddress        详细地址
	 *
	 *                 code==503,代表服务器出错,请先检测参数类型是否正确
	 *                 code==500,代表参数不正确
	 *                 code==200,代表请求成功
	 *                 count!=0,代表当前查询条件总的数量
	 *                 data==0,代表操作不成功
	 *                 data!=0,代表影响的数量
	 *
	 * @return
	 */
	@RequestMapping("find")
	@Consume(TSdxShippingAddressVo.class)
	@UrlAuth
	public Response findTSdxShippingAddress(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size,@RequestParam(required = false) String openResponseExplainFlag,@RequestParam(required = false) Long id,@RequestParam(required = false) String city,@RequestParam(required = false) String name,@RequestParam(required = false) String county,@RequestParam(required = false) String street,@RequestParam(required = false) Long userId,@RequestParam(required = false) String userTel,@RequestParam(required = false) String province,@RequestParam(required = false) String detailAddress) {

		TSdxShippingAddressVo tSdxShippingAddressVo = (TSdxShippingAddressVo) ConsumeHelper.getObj();
		if (tSdxShippingAddressVo == null) {
			return Response.fail();
		}
		if (openResponseExplainFlag != null && !openResponseExplainFlag.isEmpty()) {
			return Response.success(JavaDocReader.read(TSdxShippingAddressVo.class));
		}
		if(tSdxShippingAddressVo.getId()!=null){
			return Response.success(sdxShippingAddressService.findTSdxShippingAddressById(tSdxShippingAddressVo.getId()));
		}
		return Response.success(sdxShippingAddressService.findTSdxShippingAddressByAll(tSdxShippingAddressVo.copyTSdxShippingAddressPo (),page,size), IdUtil.getTotal());
	}

	/**
	 * 配送地址列表
	 * @param pageNum 页码
	 * @param pageSize 大小
	 * @return
	 */
	@RequestMapping("list")
	@UrlAuth
	public Response list(Integer pageNum, Integer pageSize) {
		return Response.success(sdxShippingAddressService.list(IdUtil.getId(), pageNum, pageSize));
	}

}
