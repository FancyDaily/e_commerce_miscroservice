package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.JavaDocReader;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.SdxScoreRecordService;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxScoreRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * T积分流水的操作接口
 */
@RestController
@RequestMapping("tSdxScoreRecord")
public class TSdxScoreRecordController {
	@Autowired
	private SdxScoreRecordService sdxScoreRecordService;

	/**
	 * 添加或者修改积分流水
	 *
	 * @param id                     积分流水的Id,修改或者查询的需要
	 * @param userId				 用户编号
	 * @param bookInfoIds			 书本信息编号
	 * @param bookIds				 书本编号
	 * @param bookAfterReadingNoteId 读后感编号
	 * @param orderId 				 订单编号
	 * @param description			 描述
	 * @param inOut					 输入支出类型
	 * @param amount				 积分
	 * @param money					 金额
	 * @param type					 类型
	 * 								<p>
	 *                               code==503,代表服务器出错,请先检测参数类型是否正确
	 *                               code==500,代表参数不正确
	 *                               code==200,代表请求成功
	 *                               data==0,代表操作不成功
	 *                               data!=0,新增时候data返回的是自增id,修改的时候代表影响的数量
	 * @return
	 */
	@PostMapping("mod")
	@Consume(TSdxScoreRecordVo.class)
	public Response modTSdxScoreRecord(@RequestParam(required = false) Long id,
									   Long userId,
									   String bookInfoIds,
									   String bookIds,
									   Long bookAfterReadingNoteId,
									   Long orderId,
									   String description,
									   @RequestParam Integer inOut,
									   @RequestParam Integer amount,
									   Double money,
									   @RequestParam Integer type) {
		TSdxScoreRecordVo tSdxScoreRecordVo = (TSdxScoreRecordVo) ConsumeHelper.getObj();
		if (tSdxScoreRecordVo == null) {
			return Response.fail();
		}
		return Response.success(sdxScoreRecordService.modTSdxScoreRecord(tSdxScoreRecordVo.copyTSdxScoreRecordPo()));
	}

	/**
	 * 删除积分流水
	 *
	 * @param ids 积分流水的Id的集合,例如1,2,3多个用英文,隔开
	 *            <p>
	 *            code==503,代表服务器出错,请先检测参数类型是否正确
	 *            code==500,代表参数不正确
	 *            code==200,代表请求成功
	 *            data==0,代表操作不成功
	 *            data!=0,代表影响的数量
	 * @return
	 */
	@RequestMapping("del")
	public Response delTSdxScoreRecord(Long[] ids) {
		if (ids == null || ids.length == 0) {
			return Response.fail();
		}
		return Response.success(sdxScoreRecordService.delTSdxScoreRecordByIds(ids));
	}

	/**
	 * 查找积分流水
	 *
	 * @param page                    页数默认第一页
	 * @param size                    每页返回的数量，默认10个
	 * @param openResponseExplainFlag 如果想查看返回类型的字段说明,请填写任意内容即可查看返回说明
	 * @param id                      积分流水的Id,修改或者查询的需要
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
	@Consume(TSdxScoreRecordVo.class)
	public Response findTSdxScoreRecord(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) String openResponseExplainFlag, @RequestParam(required = false) Long id) {

		TSdxScoreRecordVo tSdxScoreRecordVo = (TSdxScoreRecordVo) ConsumeHelper.getObj();
		if (tSdxScoreRecordVo == null) {
			return Response.fail();
		}
		if (openResponseExplainFlag != null && !openResponseExplainFlag.isEmpty()) {
			return Response.success(JavaDocReader.read(TSdxScoreRecordVo.class));
		}
		if (tSdxScoreRecordVo.getId() != null) {
			return Response.success(sdxScoreRecordService.findTSdxScoreRecordById(tSdxScoreRecordVo.getId()));
		}
		return Response.success(sdxScoreRecordService.findTSdxScoreRecordByAll(tSdxScoreRecordVo.copyTSdxScoreRecordPo(), page, size), IdUtil.getTotal());
	}
}
