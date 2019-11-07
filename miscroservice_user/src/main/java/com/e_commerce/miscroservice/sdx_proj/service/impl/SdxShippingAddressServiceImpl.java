package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxShippingAddressDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShippingAddressPo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxShippingAddressService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxShippingAddressVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 配送地址的service层
*
*/

@Service
@Log
public class SdxShippingAddressServiceImpl implements SdxShippingAddressService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private SdxShippingAddressDao sdxShippingAddressDao;
    @Override
    public long modTSdxShippingAddress(TSdxShippingAddressPo tSdxShippingAddressPo) {
        if (tSdxShippingAddressPo == null) {
            log.warn("操作配送地址参数为空");
            return ERROR_LONG;
        }
        if (tSdxShippingAddressPo.getId() == null) {
            log.info("start添加配送地址={}", tSdxShippingAddressPo);
            int result = sdxShippingAddressDao.saveTSdxShippingAddressIfNotExist(tSdxShippingAddressPo);
            return result != 0 ? tSdxShippingAddressPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改配送地址={}", tSdxShippingAddressPo.getId());
            return sdxShippingAddressDao.modTSdxShippingAddress(tSdxShippingAddressPo);
        }
    }
    @Override
    public int delTSdxShippingAddressByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除配送地址,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},配送地址", Arrays.asList(ids));
        return sdxShippingAddressDao.delTSdxShippingAddressByIds(ids);
    }
    @Override
    public TSdxShippingAddressVo findTSdxShippingAddressById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找配送地址,所传Id不符合规范");
            return new TSdxShippingAddressVo();
        }
        log.info("start根据Id={}查找配送地址", id);
        TSdxShippingAddressPo tSdxShippingAddressPo = sdxShippingAddressDao.findTSdxShippingAddressById(id);
        return tSdxShippingAddressPo==null?new TSdxShippingAddressVo():tSdxShippingAddressPo.copyTSdxShippingAddressVo() ;
    }
    @Override
    public List<TSdxShippingAddressVo> findTSdxShippingAddressByAll(TSdxShippingAddressPo tSdxShippingAddressPo,Integer page, Integer size) {
        List    <TSdxShippingAddressVo> tSdxShippingAddressVos = new ArrayList<>();
        if (tSdxShippingAddressPo == null) {
            log.warn("根据条件查找配送地址,参数不对");
            return tSdxShippingAddressVos;
        }
        log.info("start根据条件查找配送地址={}", tSdxShippingAddressPo);
        List        <TSdxShippingAddressPo> tSdxShippingAddressPos = sdxShippingAddressDao.findTSdxShippingAddressByAll(            tSdxShippingAddressPo,page,size);
        for (TSdxShippingAddressPo po : tSdxShippingAddressPos) {
            tSdxShippingAddressVos.add(po.copyTSdxShippingAddressVo());
        }
        return tSdxShippingAddressVos;
    }

	@Override
	public QueryResult list(Long id, Integer pageNum, Integer pageSize) {
    	//查找我的收货地址
		List<TSdxShippingAddressPo> sdxShippingAddressPos = sdxShippingAddressDao.selectByUserId(id, true, pageNum, pageSize);

		return PageUtil.buildQueryResult(sdxShippingAddressPos);
	}
}
