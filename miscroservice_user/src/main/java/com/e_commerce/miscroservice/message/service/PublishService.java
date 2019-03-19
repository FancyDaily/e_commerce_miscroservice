package com.e_commerce.miscroservice.message.service;

import com.e_commerce.miscroservice.commons.entity.application.TMessageNotice;
import com.e_commerce.miscroservice.commons.entity.application.TPublish;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.view.RemarkLablesView;
import com.e_commerce.miscroservice.message.vo.BroadcastView;
import com.e_commerce.miscroservice.message.vo.MessageDetailView;
import com.e_commerce.miscroservice.message.vo.MessageShowLIstView;
import com.e_commerce.miscroservice.message.vo.NoticesFirstView;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * 功能描述:订单关系service层
 * 模块:订单关系
 * 项目:时间银行
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:姜修弘
 * 邮箱:414368243@qq.com
 * 创建时间:2019年3月2日 下午4:21:43
 */
public interface PublishService {

    /**
     * 插入key-value
     * @param id
     * @param key
     * @param value
     * @param extend
     */
    void pulishIn( Long id , String key , String value , String extend );
    /**
     *
     * 功能描述:获取key—value
     * 作者:姜修弘
     * 创建时间:2018年11月12日 下午3:36:43
     * @param key
     * @return
     */
    TPublish publishGet(String key);
    /**
     * 通过key和标签id 获取值
     * @param labelsId
     * @param key
     * @return
     */
    String getValue(long labelsId , String key);

    String getValue(String key);

    /**
     * 根据分辨率返回不同类型封面图
     * @param length
     * @param width
     * @return
     */
    List<BroadcastView> getBroadcast(String length , String width);

    /**
     * 解析remark的lables
     * @param key
     * @return
     */
    RemarkLablesView getAllRemarkLables(String key);
}
