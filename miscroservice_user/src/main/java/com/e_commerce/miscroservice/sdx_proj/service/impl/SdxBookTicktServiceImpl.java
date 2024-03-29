package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookOrderDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookTicketDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxUserDao;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookOrderEnum;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookTicketPo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookOrderService;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookTicktService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookTicktVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 预定书券的service层
*
*/

@Service
@Log
public class SdxBookTicktServiceImpl implements SdxBookTicktService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private SdxBookTicketDao sdxBookTicketDao;
	@Autowired
    private SdxUserDao sdxUserDao;
	@Autowired
	private SdxBookOrderDao sdxBookOrderDao;
	@Autowired
	private SdxBookOrderService sdxBookOrderService;
    @Override
    public long modTSdxBookTickt(TSdxBookTicketPo tSdxBookTicketPo) {
        if (tSdxBookTicketPo == null) {
            log.warn("操作预定书券参数为空");
            return ERROR_LONG;
        }
        if (tSdxBookTicketPo.getId() == null) {
			Long expire = tSdxBookTicketPo.getExpire();
			if(expire == null) expire = DateUtil.interval * 15 + System.currentTimeMillis();
			tSdxBookTicketPo.setExpire(expire);
			log.info("start添加预定书券={}", tSdxBookTicketPo);
            int result = sdxBookTicketDao.saveTSdxBookTicktIfNotExist(tSdxBookTicketPo);
            return result != 0 ? tSdxBookTicketPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改预定书券={}", tSdxBookTicketPo.getId());
            return sdxBookTicketDao.modTSdxBookTickt(tSdxBookTicketPo);
        }
    }
    @Override
    public int delTSdxBookTicktByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除预定书券,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},预定书券", Arrays.asList(ids));
        return sdxBookTicketDao.delTSdxBookTicktByIds(ids);
    }
    @Override
    public TSdxBookTicktVo findTSdxBookTicktById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找预定书券,所传Id不符合规范");
            return new TSdxBookTicktVo();
        }
        log.info("start根据Id={}查找预定书券", id);
        TSdxBookTicketPo tSdxBookTicketPo = sdxBookTicketDao.findTSdxBookTicktById(id);
        return tSdxBookTicketPo ==null?new TSdxBookTicktVo(): tSdxBookTicketPo.copyTSdxBookTicktVo() ;
    }
    @Override
    public List<TSdxBookTicktVo> findTSdxBookTicktByAll(TSdxBookTicketPo tSdxBookTicketPo, Integer page, Integer size) {
        List    <TSdxBookTicktVo> tSdxBookTicktVos = new ArrayList<>();
        if (tSdxBookTicketPo == null) {
            log.warn("根据条件查找预定书券,参数不对");
            return tSdxBookTicktVos;
        }
        log.info("start根据条件查找预定书券={}", tSdxBookTicketPo);
        List        <TSdxBookTicketPo> tSdxBookTicketPos = sdxBookTicketDao.findTSdxBookTicktByAll(tSdxBookTicketPo,page,size);
        for (TSdxBookTicketPo po : tSdxBookTicketPos) {
            tSdxBookTicktVos.add(po.copyTSdxBookTicktVo());
        }
        return tSdxBookTicktVos;
    }

	@Override
	public void earnTickt(List<Long> bookInfos, Long userId) {
		//check
		if(!checkBeforeEarnTickt(bookInfos.size(), userId)) return;
		//earn
		TSdxBookTicketPo build = TSdxBookTicketPo.builder()
			.userId(userId)
			.expire(System.currentTimeMillis() + DateUtil.interval * 15)    //默认15天
			.build();
		sdxBookTicketDao.saveTSdxBookTicktIfNotExist(build);
	}

	private Boolean checkBeforeEarnTickt(int numThisTime, Long userId) {
		//如果捐赠书本数量 < 20 && 近期(上一次的数量和本次差20)未获得书券
		List<TSdxBookOrderPo> sdxBookOrderPos = sdxBookOrderDao.selectByUserIdAndType(userId, SdxBookOrderEnum.TYPE_DONATE.getCode());
		Integer bookNums = sdxBookOrderService.getOrdersBookNums(sdxBookOrderPos);
		bookNums += numThisTime;
		//获取上一次的数量
		List<TSdxBookTicketPo> sdxBookTicketPos = sdxBookTicketDao.selectByUserIdDesc(userId);
		Integer gainNum = sdxBookTicketPos.size();

		return bookNums / 20 > gainNum;
	}
}
