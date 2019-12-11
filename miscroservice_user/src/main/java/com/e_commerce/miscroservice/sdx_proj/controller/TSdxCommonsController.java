package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.JavaDocReader;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxCommonsService;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxCommonsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户评论的操作接口
 */
@RestController
@RequestMapping("tSdxCommons")
public class TSdxCommonsController {
	@Autowired
	private TSdxCommonsService tSdxCommonsService;

	/**
	 * 添加或者修改用户评论
	 *
	 * @param id          用户评论的Id,修改或者查询的需要
	 * @param userId      用户ID
	 * @param friendId    被评论的好友Id
	 * @param trendsId    动态id
	 * @param contentInfo 评论内容
	 *                    <p>
	 *                    code==503,代表服务器出错,请先检测参数类型是否正确
	 *                    code==500,代表参数不正确
	 *                    code==200,代表请求成功
	 *                    data==0,代表操作不成功
	 *                    data!=0,新增时候data返回的是自增id,修改的时候代表影响的数量
	 * @return
	 */
	@PostMapping("mod")
	@Consume(TSdxCommonsVo.class)
	public Response modTSdxCommons(@RequestParam(required = false) Long id, @RequestParam(required = false) Long userId, @RequestParam(required = false) Long friendId, @RequestParam(required = false) Integer trendsId, @RequestParam(required = false) String contentInfo) {
		TSdxCommonsVo tSdxCommonsVo = (TSdxCommonsVo) ConsumeHelper.getObj();
		if (tSdxCommonsVo == null) {
			return Response.fail();
		}
		return Response.success(tSdxCommonsService.modTSdxCommons(tSdxCommonsVo.copyTSdxCommonsPo()));
	}

	/**
	 * 删除用户评论
	 *
	 * @param ids 用户评论的Id的集合,例如1,2,3多个用英文,隔开
	 *            <p>
	 *            code==503,代表服务器出错,请先检测参数类型是否正确
	 *            code==500,代表参数不正确
	 *            code==200,代表请求成功
	 *            data==0,代表操作不成功
	 *            data!=0,代表影响的数量
	 * @return
	 */
	@RequestMapping("del")
	public Response delTSdxCommons(Long[] ids) {
		if (ids == null || ids.length == 0) {
			return Response.fail();
		}
		return Response.success(tSdxCommonsService.delTSdxCommonsByIds(ids));
	}

	/**
	 * 查找用户评论
	 *
	 * @param page                    页数默认第一页
	 * @param size                    每页返回的数量，默认10个
	 * @param openResponseExplainFlag 如果想查看返回类型的字段说明,请填写任意内容即可查看返回说明
	 * @param id                      用户评论的Id,修改或者查询的需要
	 * @param userId                  用户ID
	 * @param friendId                被评论的好友Id
	 * @param trendsId                动态id
	 * @param contentInfo             评论内容
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
	@Consume(TSdxCommonsVo.class)
	public Response findTSdxCommons(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) String openResponseExplainFlag, @RequestParam(required = false) Long id, @RequestParam(required = false) Long userId, @RequestParam(required = false) Long friendId, @RequestParam(required = false) Integer trendsId, @RequestParam(required = false) String contentInfo) {

		TSdxCommonsVo tSdxCommonsVo = (TSdxCommonsVo) ConsumeHelper.getObj();
		if (tSdxCommonsVo == null) {
			return Response.fail();
		}
		if (openResponseExplainFlag != null && !openResponseExplainFlag.isEmpty()) {
			return Response.success(JavaDocReader.read(TSdxCommonsVo.class));
		}
		if (tSdxCommonsVo.getId() != null) {
			return Response.success(tSdxCommonsService.findTSdxCommonsById(tSdxCommonsVo.getId()));
		}
		return Response.success(tSdxCommonsService.findTSdxCommonsByAll(tSdxCommonsVo.copyTSdxCommonsPo(), page, size), IdUtil.getTotal());
	}
}
