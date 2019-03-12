package com.e_commerce.miscroservice.order.dao;


import com.e_commerce.miscroservice.commons.entity.application.TOrderRecord;

/**
 * 功能描述:
 * 模块:
 * 项目:时间银行
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:姜修弘
 * 邮箱:414368243@qq.com
 * 创建时间:2019/3/4 下午4:33
 * ************************************
 * ************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public interface OrderRecordDao {

	/**
	 * 插入订单关系表
	 *
	 * @param orderRecord
	 * @return
	 */
	int insert(TOrderRecord orderRecord);

}
