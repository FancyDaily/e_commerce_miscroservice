package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.JavaDocReader;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookInfoService;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookInfoVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 书袋熊书籍
 * @Author: FangyiXu
 * @Date: 2019-10-25 11:57
 */
@RestController
@RequestMapping("sdx/book")
@Log
public class SdxBookController {

	@Autowired
	private SdxBookService sdxBookService;

	@Autowired
	private SdxBookInfoService sdxBookInfoService;

	/**
	 * 查找书籍信息(书籍列表)
	 *
	 * @param page 页数默认第一页
	 * @param size 每页返回的数量，默认10个
	 * @param openResponseExplainFlag 如果想查看返回类型的字段说明,请填写任意内容即可查看返回说明
	 * @param id 书籍信息的Id,修改或者查询的需要
	 * @param serviceId
	 * @param name 书名
	 * @param tag 类型名
	 * @param press 出版社
	 * @param price 价格
	 * @param author 作者
	 * @param scoreDouban 豆瓣评分
	 * @param bindingStyle 装帧风格
	 * @param tagId 类型编号
	 * @param introduction 简介
	 * @param maximumReserve 最大预定接收数
	 * @param publisher 出版社
	 * @param catalog 目录
	 * @param maximumDiscount 最高可抵扣价格
	 * @param sortType 排序类型[0~3]
	 * @param isFuzzySearch 是否启用模糊查询(对书名或作者)
	 *
	 * @return
	 */
	@RequestMapping("bookinfo/find")
	@Consume(TSdxBookInfoVo.class)
	public Response findTSdxBookInfo(
		@RequestParam(required = false) Integer page,
		@RequestParam(required = false) Integer size,
		@RequestParam(required = false) String openResponseExplainFlag,
		@RequestParam(required = false) Long id, @RequestParam(required = false) Long serviceId,
		@RequestParam(required = false) String name, @RequestParam(required = false) String tag,
		@RequestParam(required = false) String press, @RequestParam(required = false) Double price,
		@RequestParam(required = false) String author, @RequestParam(required = false) Double scoreDouban,
		@RequestParam(required = false) String bindingStyle, @RequestParam(required = false) String tagId,
		@RequestParam(required = false) String introduction, @RequestParam(required = false) Integer maximumReserve,
		@RequestParam(required = false) String publisher, @RequestParam(required = false) String catalog,
		@RequestParam(required = false) Double maximumDiscount, Integer sortType, @RequestParam boolean isFuzzySearch) {

		TSdxBookInfoVo tSdxBookInfoVo = (TSdxBookInfoVo) ConsumeHelper.getObj();
		if (tSdxBookInfoVo == null) {
			return Response.fail();
		}
		if (openResponseExplainFlag != null && !openResponseExplainFlag.isEmpty()) {
			return Response.success(JavaDocReader.read(TSdxBookInfoVo.class));
		}
		if(tSdxBookInfoVo.getId()!=null){
			return Response.success(sdxBookInfoService.findTSdxBookInfoById(tSdxBookInfoVo.getId()));
		}
		return Response.success(sdxBookInfoService.findTSdxBookInfoByAll(tSdxBookInfoVo.copyTSdxBookInfoPo (),page,size, sortType), IdUtil.getTotal());
	}

	/**
	 * 书籍详情
	 * @param id 书籍信息编号
	 * @return
	 */
	@RequestMapping("bookinfo/detail")
	@Consume(TSdxBookInfoVo.class)
	public Response detail(Long id) {
		TSdxBookVo tSdxBookVo = (TSdxBookVo) ConsumeHelper.getObj();
		if (tSdxBookVo == null || tSdxBookVo.getId()==null) {
			return Response.fail();
		}
		return Response.success(sdxBookService.detail(tSdxBookVo.getId(), IdUtil.getId()));
	}

	/**
	 * 书籍详情-捐助/购买人列表
	 * @param id 书籍信息编号
	 * @return
	 */
	@RequestMapping("bookinfo/user/sold/list")
	@Consume(TSdxBookInfoVo.class)
	public Response bookSoldUserList(Long id, Integer pageNum, Integer pageSize, Boolean isSold) {
		TSdxBookVo tSdxBookVo = (TSdxBookVo) ConsumeHelper.getObj();
		if (tSdxBookVo == null || tSdxBookVo.getId()==null) {
			return Response.fail();
		}
		return Response.success(sdxBookService.soldOrPurchaseUserList(tSdxBookVo.getId(), pageNum, pageSize, isSold));
	}

	/**
	 * 申请预定
	 * @param id 书籍信息编号
	 * @return
	 */
	@RequestMapping("preOrder/add")
	public Response preOrder(Long id) {
		Long userId = IdUtil.getId();
		if(id == null) {
			return Response.fail();
		}
		sdxBookService.preOrder(id, userId);
		return Response.success();
	}

	/**
	 * 关注最高书籍列表
	 * @return
	 */
	@RequestMapping("list/most/follow")
	public Object mostFollowList() {
		return Response.success(sdxBookService.mostFollowList());
	}

	@RequestMapping("list/suggest")
	public Object suggestList() {
		return Response.success(sdxBookService.suggestList());
	}

	/**
	 * 定制日推
	 * @return
	 */
	@RequestMapping("suggest/set")
	public Object setSuggestList(Integer dayNo, List<Long> bookInfoIds) {
		sdxBookService.setSuggestList(dayNo, bookInfoIds);
		return Response.success();
	}

	/**
	 * 获取每日推荐默认Json
	 * @return
	 */
	@RequestMapping("suggest/initail/get")
	public Object getSuggestInitail() {
		return Response.success(sdxBookService.getSuggestInitail());
	}


	@RequestMapping("service/list/goto")
	public Object bookGotoServiceList() {
		return Response.success(sdxBookService.gotoServiceList());
	}
}
