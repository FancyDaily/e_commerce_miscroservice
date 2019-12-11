package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.JavaDocReader;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxTrendsService;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxTrendsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发布动态的操作接口
 */
@RestController
@RequestMapping("tSdxTrends")
public class TSdxTrendsController {
	@Autowired
	private TSdxTrendsService tSdxTrendsService;

	/**
	 * 添加或者修改发布动态
	 *
	 * @param id          发布动态的Id,修改或者查询的需要
	 * @param price       价格
	 * @param userId      用户ID
	 * @param address     用户此时地址
	 * @param bookPic     动态图片
	 * @param userPic     用户头像
	 * @param bookName    书籍名
	 * @param boookPic    书籍图片
	 * @param friendId    好友id
	 * @param latitude    纬度
	 * @param userName    用户名
	 * @param longitude   经度
	 * @param trentInfo   动态内容
	 * @param bookInfoId  书籍信息编号
	 * @param friendName  好友名称
	 * @param scoreDouban 豆瓣评分
	 *                    <p>
	 *                    code==503,代表服务器出错,请先检测参数类型是否正确
	 *                    code==500,代表参数不正确
	 *                    code==200,代表请求成功
	 *                    data==0,代表操作不成功
	 *                    data!=0,新增时候data返回的是自增id,修改的时候代表影响的数量
	 * @return
	 */
	@PostMapping("mod")
	@Consume(TSdxTrendsVo.class)
	public Response modTSdxTrends(@RequestParam(required = false) Long id,
								  @RequestParam(required = false) Double price,
								  @RequestParam(required = false) Long userId,
								  @RequestParam(required = false) String address,
								  @RequestParam(required = false) String bookPic,
								  @RequestParam(required = false) String userPic,
								  @RequestParam(required = false) String bookName,
								  @RequestParam(required = false) String boookPic,
								  @RequestParam(required = false) Long friendId,
								  @RequestParam(required = false) Double latitude,
								  @RequestParam(required = false) String userName,
								  @RequestParam(required = false) Double longitude,
								  @RequestParam(required = false) String trentInfo,
								  @RequestParam(required = false) Long bookInfoId,
								  @RequestParam(required = false) String friendName,
								  @RequestParam(required = false) Double scoreDouban) {
		TSdxTrendsVo tSdxTrendsVo = (TSdxTrendsVo) ConsumeHelper.getObj();
		if (tSdxTrendsVo == null) {
			return Response.fail();
		}
		return Response.success(tSdxTrendsService.modTSdxTrends(tSdxTrendsVo.copyTSdxTrendsPo()));
	}

	/**
	 * 删除发布动态
	 *
	 * @param ids 发布动态的Id的集合,例如1,2,3多个用英文,隔开
	 *            <p>
	 *            code==503,代表服务器出错,请先检测参数类型是否正确
	 *            code==500,代表参数不正确
	 *            code==200,代表请求成功
	 *            data==0,代表操作不成功
	 *            data!=0,代表影响的数量
	 * @return
	 */
	@RequestMapping("del")
	public Response delTSdxTrends(Long[] ids) {
		if (ids == null || ids.length == 0) {
			return Response.fail();
		}
		return Response.success(tSdxTrendsService.delTSdxTrendsByIds(ids));
	}

	/**
	 * 查找发布动态
	 *
	 * @param page                    页数默认第一页
	 * @param size                    每页返回的数量，默认10个
	 * @param openResponseExplainFlag 如果想查看返回类型的字段说明,请填写任意内容即可查看返回说明
	 * @param id                      发布动态的Id,修改或者查询的需要
	 * @param price                   价格
	 * @param userId                  用户ID
	 * @param address                 用户此时地址
	 * @param bookPic                 动态图片
	 * @param userPic                 用户头像
	 * @param bookName                书籍名
	 * @param boookPic                书籍图片
	 * @param friendId                好友id
	 * @param latitude                纬度
	 * @param userName                用户名
	 * @param longitude               经度
	 * @param trentInfo               动态内容
	 * @param bookInfoId              书籍信息编号
	 * @param friendName              好友名称
	 * @param scoreDouban             豆瓣评分
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
	@Consume(TSdxTrendsVo.class)
	public Response findTSdxTrends(@RequestParam(required = false) Integer page,
								   @RequestParam(required = false) Integer size,
								   @RequestParam(required = false) String openResponseExplainFlag,
								   @RequestParam(required = false) Long id,
								   @RequestParam(required = false) Double price,
								   @RequestParam(required = false) Long userId,
								   @RequestParam(required = false) String address,
								   @RequestParam(required = false) String bookPic,
								   @RequestParam(required = false) String userPic,
								   @RequestParam(required = false) String bookName,
								   @RequestParam(required = false) String boookPic,
								   @RequestParam(required = false) Long friendId,
								   @RequestParam(required = false) Double latitude,
								   @RequestParam(required = false) String userName,
								   @RequestParam(required = false) Double longitude,
								   @RequestParam(required = false) String trentInfo,
								   @RequestParam(required = false) Long bookInfoId,
								   @RequestParam(required = false) String friendName,
								   @RequestParam(required = false) Double scoreDouban) {

		TSdxTrendsVo tSdxTrendsVo = (TSdxTrendsVo) ConsumeHelper.getObj();
		if (tSdxTrendsVo == null) {
			return Response.fail();
		}
		if (openResponseExplainFlag != null && !openResponseExplainFlag.isEmpty()) {
			return Response.success(JavaDocReader.read(TSdxTrendsVo.class));
		}
		if (tSdxTrendsVo.getId() != null) {
			return Response.success(tSdxTrendsService.findTSdxTrendsById(tSdxTrendsVo.getId()));
		}
		return Response.success(tSdxTrendsService.findTSdxTrendsByAll(tSdxTrendsVo.copyTSdxTrendsPo(), page, size), IdUtil.getTotal());
	}

	//查询最近看的书籍
	//1：根据用户ID查询读后感信息，根据书本编号查询书籍名称等详细信息
	@RequestMapping("findBookInfos")
	public Response findBookInfos(Long userId) {
		return Response.success(tSdxTrendsService.findBookInfos(userId));
	}


	//查询跟我有相同书籍的人
	@RequestMapping("cancelMutualBookFriend")
	public Response findTSdxTrendsFriend(Long userId, String bookName) {
		return Response.success(tSdxTrendsService.findTSdxTrendsFriend(userId, bookName));
	}

}
