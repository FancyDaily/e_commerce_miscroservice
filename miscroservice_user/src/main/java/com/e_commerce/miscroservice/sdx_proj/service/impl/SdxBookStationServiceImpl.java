package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookStationDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookStationPo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookStationService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookStationVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
* 书籍回收驿站的service层
*
*/

@Service
@Log
public class SdxBookStationServiceImpl implements SdxBookStationService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private SdxBookStationDao sdxBookStationDao;
    @Override
    public long modTSdxBookStation(TSdxBookStationPo tSdxBookStationPo) {
        if (tSdxBookStationPo == null) {
            log.warn("操作书籍回收驿站参数为空");
            return ERROR_LONG;
        }
        if (tSdxBookStationPo.getId() == null) {
            log.info("start添加书籍回收驿站={}", tSdxBookStationPo);
            int result = sdxBookStationDao.saveTSdxBookStationIfNotExist(tSdxBookStationPo);
            return result != 0 ? tSdxBookStationPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改书籍回收驿站={}", tSdxBookStationPo.getId());
            return sdxBookStationDao.modTSdxBookStation(tSdxBookStationPo);
        }
    }
    @Override
    public int delTSdxBookStationByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除书籍回收驿站,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},书籍回收驿站", Arrays.asList(ids));
        return sdxBookStationDao.delTSdxBookStationByIds(ids);
    }
    @Override
    public TSdxBookStationVo findTSdxBookStationById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找书籍回收驿站,所传Id不符合规范");
            return new TSdxBookStationVo();
        }
        log.info("start根据Id={}查找书籍回收驿站", id);
        TSdxBookStationPo tSdxBookStationPo = sdxBookStationDao.findTSdxBookStationById(id);
        return tSdxBookStationPo==null?new TSdxBookStationVo():tSdxBookStationPo.copyTSdxBookStationVo() ;
    }
    @Override
    public List<TSdxBookStationVo> findTSdxBookStationByAll(TSdxBookStationPo tSdxBookStationPo,Integer page, Integer size) {
        List    <TSdxBookStationVo> tSdxBookStationVos = new ArrayList<>();
        if (tSdxBookStationPo == null) {
            log.warn("根据条件查找书籍回收驿站,参数不对");
            return tSdxBookStationVos;
        }
        log.info("start根据条件查找书籍回收驿站={}", tSdxBookStationPo);
        List        <TSdxBookStationPo> tSdxBookStationPos = sdxBookStationDao.findTSdxBookStationByAll(            tSdxBookStationPo,page,size);
        for (TSdxBookStationPo po : tSdxBookStationPos) {
            tSdxBookStationVos.add(po.copyTSdxBookStationVo());
        }
        return tSdxBookStationVos;
    }

	@Override
	public QueryResult list(String city, Double longitude, Double latitude, Integer pageNum, Integer pageSize) {
		List<TSdxBookStationPo> pos = sdxBookStationDao.selectByCity(city, true, pageNum, pageSize);
		List<TSdxBookStationVo> vos = pos.stream().map(a -> a.copyTSdxBookStationVo()).collect(Collectors.toList());
		return PageUtil.buildQueryResult(vos);
	}
}
