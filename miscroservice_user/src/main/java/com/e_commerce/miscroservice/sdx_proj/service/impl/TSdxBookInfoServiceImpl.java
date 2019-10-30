package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxBookInfoDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookPo;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxBookInfoService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookInfoVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 书籍信息的service层
*
*/

@Service
@Log
public class TSdxBookInfoServiceImpl implements TSdxBookInfoService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private TSdxBookInfoDao tSdxBookInfoDao;
    @Override
    public long modTSdxBookInfo(TSdxBookInfoPo tSdxBookInfoPo) {
        if (tSdxBookInfoPo == null) {
            log.warn("操作书籍信息参数为空");
            return ERROR_LONG;
        }
        if (tSdxBookInfoPo.getId() == null) {
            log.info("start添加书籍信息={}", tSdxBookInfoPo);
            int result = tSdxBookInfoDao.saveTSdxBookInfoIfNotExist(tSdxBookInfoPo);
            return result != 0 ? tSdxBookInfoPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改书籍信息={}", tSdxBookInfoPo.getId());
            return tSdxBookInfoDao.modTSdxBookInfo(tSdxBookInfoPo);
        }
    }
    @Override
    public int delTSdxBookInfoByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除书籍信息,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},书籍信息", Arrays.asList(ids));
        return tSdxBookInfoDao.delTSdxBookInfoByIds(ids);
    }
    @Override
    public TSdxBookInfoVo findTSdxBookInfoById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找书籍信息,所传Id不符合规范");
            return new TSdxBookInfoVo();
        }
        log.info("start根据Id={}查找书籍信息", id);
        TSdxBookInfoPo tSdxBookInfoPo = tSdxBookInfoDao.findTSdxBookInfoById(id);
        return tSdxBookInfoPo==null?new TSdxBookInfoVo():tSdxBookInfoPo.copyTSdxBookInfoVo() ;
    }
    @Override
    public List<TSdxBookInfoVo> findTSdxBookInfoByAll(TSdxBookInfoPo tSdxBookInfoPo, Integer page, Integer size, Integer sortType) {
        List    <TSdxBookInfoVo> tSdxBookInfoVos = new ArrayList<>();
        if (tSdxBookInfoPo == null) {
            log.warn("根据条件查找书籍信息,参数不对");
            return tSdxBookInfoVos;
        }
        log.info("start根据条件查找书籍信息={}", tSdxBookInfoPo);
        List        <TSdxBookInfoPo> tSdxBookInfoPos = tSdxBookInfoDao.findTSdxBookInfoByAll(            tSdxBookInfoPo,page,size, sortType);
        //TODO 对结果的继续处理
        for (TSdxBookInfoPo po : tSdxBookInfoPos) {
            tSdxBookInfoVos.add(po.copyTSdxBookInfoVo());
        }
        return tSdxBookInfoVos;
    }

	@Override
	public TSdxBookPo getBookInfo(String isbnCode) {
    	if(StringUtil.isEmpty(isbnCode)) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "ISBN码不能为空!");
		//向https://www.douban.com/ISBN/ + isbnCode 发送http请求
		String url = "https://www.douban.com/ISBN/" + isbnCode;
    	//发送请求，获取基本信息，需要一个豆瓣信息vo
		//与现有的bookInfo进行匹配，若不存在，插入一条相应bookInfo
		return null;
	}
}
