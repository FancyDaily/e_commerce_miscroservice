package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.JavaDocReader;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.SdxWishListService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxWishListVo;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 心愿单的操作接口
 */
@RestController
@RequestMapping("sdxWishList")
public class SdxWishListController {
	@Autowired
	private SdxWishListService sdxWishListService;

	/**
	 * 添加或者修改心愿单
	 *
	 * @param id         心愿单的Id,修改或者查询的需要
	 * @param bookInfoId 书本信息编号
	 *                   <p>
	 *                   code==503,代表服务器出错,请先检测参数类型是否正确
	 *                   code==500,代表参数不正确
	 *                   code==200,代表请求成功
	 *                   data==0,代表操作不成功
	 *                   data!=0,新增时候data返回的是自增id,修改的时候代表影响的数量
	 * @return
	 */
	@PostMapping("mod")
	@Consume(TSdxWishListVo.class)
	@UrlAuth
	public Response modTSdxWishList(@RequestParam(required = false) Long id, @RequestParam(required = false) Long bookInfoId) {
		TSdxWishListVo tSdxWishListVo = (TSdxWishListVo) ConsumeHelper.getObj();
		if (tSdxWishListVo == null) {
			return Response.fail();
		}
		return Response.success(sdxWishListService.modTSdxWishList(tSdxWishListVo.copyTSdxWishListPo()));
	}

	/**
	 * 删除心愿单
	 *
	 * @param ids 心愿单的Id的集合,例如1,2,3多个用英文,隔开
	 *            <p>
	 *            code==503,代表服务器出错,请先检测参数类型是否正确
	 *            code==500,代表参数不正确
	 *            code==200,代表请求成功
	 *            data==0,代表操作不成功
	 *            data!=0,代表影响的数量
	 * @return
	 */
	@RequestMapping("del")
	@UrlAuth
	public Response delTSdxWishList(Long[] ids) {
		if (ids == null || ids.length == 0) {
			return Response.fail();
		}
		return Response.success(sdxWishListService.delTSdxWishListByIds(ids));
	}

	/**
	 * 查找心愿单
	 *
	 * @param page                    页数默认第一页
	 * @param size                    每页返回的数量，默认10个
	 * @param openResponseExplainFlag 如果想查看返回类型的字段说明,请填写任意内容即可查看返回说明
	 * @param id                      心愿单的Id,修改或者查询的需要
	 * @param bookInfoId              书本信息编号
	 *                                <p>
	 *                                code==503,代表服务器出错,请先检测参数类型是否正确
	 *                                code==500,代表参数不正确
	 *                                code==200,代表请求成功
	 *                                count!=0,代表当前查询条件总的数量
	 *                                data==0,代表操作不成功
	 *                                data!=0,代表影响的数量
	 * @return
	 */
	@RequestMapping("find")
	@Consume(TSdxWishListVo.class)
	@UrlAuth
	public Response findTSdxWishList(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) String openResponseExplainFlag, @RequestParam(required = false) Long id, @RequestParam(required = false) Long bookInfoId, @RequestParam(required = false) Long userIds) {

		TSdxWishListVo tSdxWishListVo = (TSdxWishListVo) ConsumeHelper.getObj();
		if (tSdxWishListVo == null) {
			return Response.fail();
		}
		tSdxWishListVo.setUserId(userIds);
		if (openResponseExplainFlag != null && !openResponseExplainFlag.isEmpty()) {
			return Response.success(JavaDocReader.read(TSdxWishListVo.class));
		}
		if (tSdxWishListVo.getId() != null) {
			return Response.success(sdxWishListService.findTSdxWishListById(tSdxWishListVo.getId()));
		}
		return Response.success(sdxWishListService.findTSdxWishListByAll(tSdxWishListVo.copyTSdxWishListPo(), page, size), IdUtil.getTotal());
	}

	/**
	 * 心愿单列表
	 *
	 * @return
	 */
	@RequestMapping("list")
	@UrlAuth
	public Object list(Integer pageNum, Integer pageSize) {
		return Response.success(sdxWishListService.list(IdUtil.getId(), pageNum, pageSize));
	}
}
