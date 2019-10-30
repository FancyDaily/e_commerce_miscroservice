package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxBookOrderDao;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxUserDao;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookOrderEnum;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxBookOrderService;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxBookOrderUserInfoVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookDonateOrderVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookOrderVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
* 订单的service层
*
*/

@Service
@Log
public class TSdxBookOrderServiceImpl implements TSdxBookOrderService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private TSdxBookOrderDao sdxBookOrderDao;
	@Autowired
    private TSdxUserDao sdxUserDao;
    @Override
    public long modTSdxBookOrder(TSdxBookOrderPo tSdxBookOrderPo) {
        if (tSdxBookOrderPo == null) {
            log.warn("操作订单参数为空");
            return ERROR_LONG;
        }
        if (tSdxBookOrderPo.getId() == null) {
            log.info("start添加订单={}", tSdxBookOrderPo);
            int result = sdxBookOrderDao.saveTSdxBookOrderIfNotExist(tSdxBookOrderPo);
            return result != 0 ? tSdxBookOrderPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改订单={}", tSdxBookOrderPo.getId());
            return sdxBookOrderDao.modTSdxBookOrder(tSdxBookOrderPo);
        }
    }
    @Override
    public int delTSdxBookOrderByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除订单,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},订单", Arrays.asList(ids));
        return sdxBookOrderDao.delTSdxBookOrderByIds(ids);
    }
    @Override
    public TSdxBookOrderVo findTSdxBookOrderById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找订单,所传Id不符合规范");
            return new TSdxBookOrderVo();
        }
        log.info("start根据Id={}查找订单", id);
        TSdxBookOrderPo tSdxBookOrderPo = sdxBookOrderDao.findTSdxBookOrderById(id);
        return tSdxBookOrderPo==null?new TSdxBookOrderVo():tSdxBookOrderPo.copyTSdxBookOrderVo() ;
    }
    @Override
    public List<TSdxBookOrderVo> findTSdxBookOrderByAll(TSdxBookOrderPo tSdxBookOrderPo,Integer page, Integer size) {
        List    <TSdxBookOrderVo> tSdxBookOrderVos = new ArrayList<>();
        if (tSdxBookOrderPo == null) {
            log.warn("根据条件查找订单,参数不对");
            return tSdxBookOrderVos;
        }
        log.info("start根据条件查找订单={}", tSdxBookOrderPo);
        List        <TSdxBookOrderPo> tSdxBookOrderPos = sdxBookOrderDao.findTSdxBookOrderByAll(            tSdxBookOrderPo,page,size);
        for (TSdxBookOrderPo po : tSdxBookOrderPos) {
            tSdxBookOrderVos.add(po.copyTSdxBookOrderVo());
        }
        return tSdxBookOrderVos;
    }

	/**
	 * 捐书订单列表
	 * @return
	 */
	@Override
    public List<TSdxBookDonateOrderVo> donateOrdersList() {
//		sdxBookOrderDao.selectByType();
		return null;
	}

	@Override
	public List<SdxBookOrderUserInfoVo> getEverDonateList(Long id) {
		//筛选已完成的 捐献 订单
		List<TSdxBookOrderPo> donateOrders = sdxBookOrderDao.selectByBookInfoIdAndTypeAndStatus(id, SdxBookOrderEnum.TYPE_DONATE.getCode(), SdxBookOrderEnum.STATUS_DONE.getCode());
		if(donateOrders.isEmpty()) return new ArrayList<>();
		List<Long> userIds = donateOrders.stream()
			.map(TSdxBookOrderPo::getUserId).collect(Collectors.toList());
		Map<Long, List<TCsqUser>> idUserMap = sdxUserDao.groupingByIdInIds(userIds);

		List<SdxBookOrderUserInfoVo> todoList = donateOrders.stream()
			.map(a -> {
				//提取捐助人编号，获取捐助人信息
				Long userId = a.getUserId();
				String name = "";
				String headPic = "";
				List<TCsqUser> users = idUserMap.get(userId);
				if (users != null) {
					TCsqUser csqUser = users.get(0);
					name = csqUser.getName();
					headPic = csqUser.getUserHeadPortraitPath();
				}
				long timeStamp = a.getCreateTime().getTime();
				String desc = buildDoneTimeDesc(timeStamp);
				return SdxBookOrderUserInfoVo.builder()
					.doneTimeDesc(desc)
					.name(name)
					.timeStamp(timeStamp)
					.headPic(headPic).build();
			}).collect(Collectors.toList());

		//倒序
		return todoList.stream()
			.sorted(Comparator.comparing(SdxBookOrderUserInfoVo::getTimeStamp).reversed()).collect(Collectors.toList());
	}

	@Override
	public String buildDoneTimeDesc(long timeStamp) {
		long currentInterval = System.currentTimeMillis() - timeStamp;
		boolean isToday = currentInterval < DateUtil.interval;
		boolean isYesterDay = !isToday && currentInterval < 2 * DateUtil.interval;
		//组装售出时间信息 分钟前、日期(如果为昨天，显示昨天）
		String desc;
		desc = isYesterDay ? "昨天" : isToday ? DateUtil.getMinsStrWithinOneDay(currentInterval) : DateUtil.timeStamp2Date(timeStamp);
		return desc;
	}
}
