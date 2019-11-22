package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.CsqUserEnum;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqServiceDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookInfoDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxShoppingTrolleysDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShoppingTrolleysPo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxShoppingTrolleysService;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxShoppingTrolleysServiceGroupVo;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxShoppingTrolleysVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxShoppingTrolleysVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
* 购物车的service层
*
*/

@Service
@Log
public class SdxShoppingTrolleysServiceImpl implements SdxShoppingTrolleysService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private SdxShoppingTrolleysDao sdxShoppingTrolleysDao;
	@Autowired
    private SdxBookInfoDao sdxBookInfoDao;

	private CsqServiceDao csqServiceDao;
    @Override
    public long modTSdxShoppingTrolleys(TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo) {
        if (tSdxShoppingTrolleysPo == null) {
            log.warn("操作购物车参数为空");
            return ERROR_LONG;
        }
        if (tSdxShoppingTrolleysPo.getId() == null) {
            log.info("start添加购物车={}", tSdxShoppingTrolleysPo);
            //判重
			TSdxShoppingTrolleysPo trolleysPos = sdxShoppingTrolleysDao.selectByPo(tSdxShoppingTrolleysPo);
			int result = 0;
			if(trolleysPos == null) {
				Long bookInfoId = tSdxShoppingTrolleysPo.getBookInfoId();
				//找到所属项目编号
				TSdxBookInfoPo sdxBookInfoPo = sdxBookInfoDao.selectByPrimaryKey(bookInfoId);
				tSdxShoppingTrolleysPo.setServiceId(sdxBookInfoPo.getServiceId());
				result = sdxShoppingTrolleysDao.saveTSdxShoppingTrolleysIfNotExist(tSdxShoppingTrolleysPo);
			}
            return result != 0 ? tSdxShoppingTrolleysPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改购物车={}", tSdxShoppingTrolleysPo.getId());
            return sdxShoppingTrolleysDao.modTSdxShoppingTrolleys(tSdxShoppingTrolleysPo);
        }
    }
    @Override
    public int delTSdxShoppingTrolleysByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除购物车,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},购物车", Arrays.asList(ids));
        return sdxShoppingTrolleysDao.delTSdxShoppingTrolleysByIds(ids);
    }
    @Override
    public TSdxShoppingTrolleysVo findTSdxShoppingTrolleysById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找购物车,所传Id不符合规范");
            return new TSdxShoppingTrolleysVo();
        }
        log.info("start根据Id={}查找购物车", id);
        TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo = sdxShoppingTrolleysDao.findTSdxShoppingTrolleysById(id);
        return tSdxShoppingTrolleysPo==null?new TSdxShoppingTrolleysVo():tSdxShoppingTrolleysPo.copyTSdxShoppingTrolleysVo() ;
    }
    @Override
    public List<TSdxShoppingTrolleysVo> findTSdxShoppingTrolleysByAll(TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo,Integer page, Integer size) {
        List    <TSdxShoppingTrolleysVo> tSdxShoppingTrolleysVos = new ArrayList<>();
        if (tSdxShoppingTrolleysPo == null) {
            log.warn("根据条件查找购物车,参数不对");
            return tSdxShoppingTrolleysVos;
        }
        log.info("start根据条件查找购物车={}", tSdxShoppingTrolleysPo);
        List        <TSdxShoppingTrolleysPo> tSdxShoppingTrolleysPos = sdxShoppingTrolleysDao.findTSdxShoppingTrolleysByAll(            tSdxShoppingTrolleysPo,page,size);
        for (TSdxShoppingTrolleysPo po : tSdxShoppingTrolleysPos) {
            tSdxShoppingTrolleysVos.add(po.copyTSdxShoppingTrolleysVo());
        }
        return tSdxShoppingTrolleysVos;
    }

	@Override
	public QueryResult list(Long id, Integer pageNum, Integer pageSize) {
		List<TSdxShoppingTrolleysPo> trolleysPos = sdxShoppingTrolleysDao.selectByUserIdPage(id, pageNum, pageSize);
		//分组、排序 -> 按关联项目分组，并组内排序, 要注意关联项目为空的情况(这些不进行分组, 组外排序，将组内最晚的元素提取，代表组参与排序
		Map<Boolean, List<TSdxShoppingTrolleysPo>> collect = trolleysPos.stream()
			.collect(Collectors.partitioningBy(a -> a.getServiceId() == null));
		List<SdxShoppingTrolleysServiceGroupVo> finalList = new ArrayList<>();
		//分组
		List<TSdxShoppingTrolleysPo> noneServiceIdTrolleyPoList = collect.get(Boolean.TRUE);
		List<TSdxShoppingTrolleysPo> serviceIdTrolleyPoList = collect.get(Boolean.FALSE);
		//"未分类" 组内部排序
		noneServiceIdTrolleyPoList = noneServiceIdTrolleyPoList.stream()
			.sorted(Comparator.comparing(TSdxShoppingTrolleysPo::getUpdateTime).reversed()).collect(Collectors.toList());

		//构建无分类元素 备用
		SdxShoppingTrolleysServiceGroupVo groupVo = new SdxShoppingTrolleysServiceGroupVo();
		groupVo.setServiceName("未分类");	//未归属任何组
		groupVo.setCoverPic(CsqUserEnum.DEFAULT_ANONYMOUS_HEADPORTRAITUREPATH);	//默认封面图
		groupVo.setDescription("这些书没有属于任何公益项目。");
		groupVo.setTrolleysBookInfos(noneServiceIdTrolleyPoList.stream().map(TSdxShoppingTrolleysPo::copySdxShoppingTrolleysVo).collect(Collectors.toList()));
		groupVo.setTimeStamp(noneServiceIdTrolleyPoList.isEmpty()? -1L : noneServiceIdTrolleyPoList.get(0).getUpdateTime().getTime());
		finalList.add(groupVo);

		Map<Long, List<TSdxShoppingTrolleysPo>> serviceIdTrolleysMap = serviceIdTrolleyPoList.stream()
			.collect(Collectors.groupingBy(TSdxShoppingTrolleysPo::getServiceId));
		//有分类的组 -> 内部排序
		List<SdxShoppingTrolleysServiceGroupVo> objects = new ArrayList<>();
		serviceIdTrolleysMap.forEach((k,v) -> {
			//获取项目信息
			TCsqService csqService = csqServiceDao.selectByPrimaryKey(k);
			//排序
			List<SdxShoppingTrolleysVo> vos = v.stream()
				.sorted(Comparator.comparing(TSdxShoppingTrolleysPo::getUpdateTime).reversed())
				.map(TSdxShoppingTrolleysPo::copySdxShoppingTrolleysVo)
				.collect(Collectors.toList());
			SdxShoppingTrolleysServiceGroupVo build = SdxShoppingTrolleysServiceGroupVo.builder()
				.userId(id)
				.serviceId(k)
				.serviceName(csqService.getName())
				.coverPic(csqService.getCoverPic())
				.description(csqService.getPurpose())    //简介
				.trolleysBookInfos(vos)
				.timeStamp(vos.get(0).getUpdateTime().getTime())
				.build();
			objects.add(build);
		});
		finalList.addAll(objects);

		//外层排序
		finalList = finalList.stream()
			.sorted(Comparator.comparing(SdxShoppingTrolleysServiceGroupVo::getTimeStamp).reversed()).collect(Collectors.toList());

		return PageUtil.buildQueryResult(finalList);
	}

}
