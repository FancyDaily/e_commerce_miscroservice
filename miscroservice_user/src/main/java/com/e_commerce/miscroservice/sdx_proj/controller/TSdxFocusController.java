package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.JavaDocReader;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxFocusEnum;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxFocusService;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookInfoVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxFocusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 关注书友的操作接口
 */
@RestController
@RequestMapping("tSdxFocus")
public class TSdxFocusController {
	@Autowired
	private TSdxFocusService tSdxFocusService;

	/**
	 * 添加或者修改关注书友
	 *
	 * @param id             关注书友的Id,修改或者查询的需要
	 * @param type           关注类型
	 * @param userId         用户ID
	 * @param typeName       关注类型名称
	 * @param bookFriendId   书友ID
	 * @param bookFriendPic  书友头像
	 * @param bookFriendName 书友名称
	 *                       <p>
	 *                       code==503,代表服务器出错,请先检测参数类型是否正确
	 *                       code==500,代表参数不正确
	 *                       code==200,代表请求成功
	 *                       data==0,代表操作不成功
	 *                       data!=0,新增时候data返回的是自增id,修改的时候代表影响的数量
	 * @return
	 */
	@PostMapping("mod")
	@Consume(TSdxFocusVo.class)
	@UrlAuth
	public Response modTSdxFocus(@RequestParam(required = false) Long id,
								 @RequestParam(required = false) Integer type,
								 @RequestParam(required = false) Long userId,
								 @RequestParam(required = false) String typeName,
								 @RequestParam(required = false) Long bookFriendId,
								 @RequestParam(required = false) String bookFriendPic,
								 @RequestParam(required = false) String bookFriendName) {
		TSdxFocusVo tSdxFocusVo = (TSdxFocusVo) ConsumeHelper.getObj();
		if (tSdxFocusVo == null) {
			return Response.fail();
		}
		return Response.success(tSdxFocusService.modTSdxFocus(tSdxFocusVo.copyTSdxFocusPo()));
	}

	/**
	 * 删除关注书友
	 *
	 * @param ids 关注书友的Id的集合,例如1,2,3多个用英文,隔开
	 *            <p>
	 *            code==503,代表服务器出错,请先检测参数类型是否正确
	 *            code==500,代表参数不正确
	 *            code==200,代表请求成功
	 *            data==0,代表操作不成功
	 *            data!=0,代表影响的数量
	 * @return
	 */
	@RequestMapping("del")
	public Response delTSdxFocus(Long[] ids) {
		if (ids == null || ids.length == 0) {
			return Response.fail();
		}
		return Response.success(tSdxFocusService.delTSdxFocusByIds(ids));
	}

	/**
	 * 查找关注书友
	 *
	 * @param page                    页数默认第一页
	 * @param size                    每页返回的数量，默认10个
	 * @param openResponseExplainFlag 如果想查看返回类型的字段说明,请填写任意内容即可查看返回说明
	 * @param id                      关注书友的Id,修改或者查询的需要
	 * @param type                    关注类型
	 * @param userId                  用户ID
	 * @param typeName                关注类型名称
	 * @param bookFriendId            书友ID
	 * @param bookFriendPic           书友头像
	 * @param bookFriendName          书友名称
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
	@Consume(TSdxFocusVo.class)
	public Response findTSdxFocus(@RequestParam(required = false) Integer page,
								  @RequestParam(required = false) Integer size,
								  @RequestParam(required = false) String openResponseExplainFlag,
								  @RequestParam(required = false) Long id,
								  @RequestParam(required = false) Integer type,
								  @RequestParam(required = false) Long userId,
								  @RequestParam(required = false) String typeName,
								  @RequestParam(required = false) Long bookFriendId,
								  @RequestParam(required = false) String bookFriendPic,
								  @RequestParam(required = false) String bookFriendName) {

		TSdxFocusVo tSdxFocusVo = (TSdxFocusVo) ConsumeHelper.getObj();
		if (tSdxFocusVo == null) {
			return Response.fail();
		}
		if (openResponseExplainFlag != null && !openResponseExplainFlag.isEmpty()) {
			return Response.success(JavaDocReader.read(TSdxFocusVo.class));
		}
		if (tSdxFocusVo.getId() != null) {
			return Response.success(tSdxFocusService.findTSdxFocusById(tSdxFocusVo.getId()));
		}
		//查找关注书友表
		return Response.success(tSdxFocusService.findTSdxFocusByAll(tSdxFocusVo.copyTSdxFocusPo(), page, size), IdUtil.getTotal());
	}

	/***
	 *查询书友列表（我关注的/关注我的）
	 * @param userId
	 * @param page
	 * @param type
	 * @param size
	 * @return
	 */
	@RequestMapping("findTSdxFocusByUserId")
	public Response FindTSdxFocusByUserId(Long userId, Integer page, Integer type, Integer size) {
		if (type == null) {
			return Response.error(SdxFocusEnum.DATA_NULL.getMsg());//参数错误或参数为空；
		}
//		参数：用户Id：IdUtil.getId()
		Long userIds = IdUtil.getId();
		userIds = userId;
		return Response.success(tSdxFocusService.FindTSdxFocusByUserId(userIds, type, page, size));
	}

	/***
	 * 互相关注--修改关注书友表；
	 *
	 * id 关注书友表id
	 * @return
	 * @author why
	 * @date 2019-12-7
	 */
	@RequestMapping("mutualBookFriend")
	public Response mutualBookFriend(Long userId, Long id) {
		return Response.success(tSdxFocusService.mutualBookFriend(userId, id));
	}

	/***
	 * 取消关注
	 * @param id 关注书友表id
	 * @return
	 * @author why
	 * @date 2019-12-8
	 */
	//1：从相互关注---》取消关注
	//2：我关注的----》取消关注
	@RequestMapping("cancelMutualBookFriend")
	public Response cancelMutualBookFriend(Long userId, Long id) {
		return Response.success(tSdxFocusService.cancelMutualBookFriend(userId, id));
	}
	//build.eq(TSdxBookAfterReadingNoteUserPo::getUserId, userId);
	// .orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TUserTask::getCreateTime)));//按照开始时间倒序排列；
}
