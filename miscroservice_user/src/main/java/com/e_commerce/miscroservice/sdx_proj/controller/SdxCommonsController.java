package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.sdx_proj.service.SdxCommonsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;

/**
 * 用户评论的操作接口
 */
@RestController
@Log
@RequestMapping("SdxCommons")
public class SdxCommonsController {
	@Autowired
	private SdxCommonsService sdxCommonsService;

	/***
	 * 查询好友动态以及动态下面的评论
	 * @param userId
	 * @author why
	 * @date 2019-12-9
	 */
	@RequestMapping("findFriendTrends")
	public Response findFriendTrends(long userId) {

		return Response.success(sdxCommonsService.findFriendTrends(userId));
	}


	/**
	 * 查询跟你看过同样书的人发的动态(发的动态是同样的书//有同样的书籍信息编号)
	 *
	 * @param userId 用户ID
	 * @author why
	 * @date 2019-12-10
	 */
	@RequestMapping("findFriendTrendsByUserId")
	public Response findFriendTrendsByUserId(Long userId) {
		return Response.success(sdxCommonsService.findFriendTrendsByUserId(userId));
	}
}
