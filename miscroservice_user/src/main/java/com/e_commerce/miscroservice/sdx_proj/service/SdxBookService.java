package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookVo;
import java.util.List;
public interface SdxBookService {
    long modTSdxBook(TSdxBookPo tSdxBookPo);
    int delTSdxBookByIds(Long... ids);
    TSdxBookVo findTSdxBookById(Long id);
    List<TSdxBookVo> findTSdxBookByAll(TSdxBookPo tSdxBookPo, Integer page, Integer size, Integer sortType);

	List<TSdxBookVo> findTSdxBookByAll(TSdxBookPo tSdxBookPo, Integer page, Integer size);

	String detail(Long id, Long userId);

	QueryResult soldOrPurchaseUserList(Long id, Integer pageNum, Integer pageSize, Boolean isSold);

	void preOrder(Long id, Long userId);

	List<TSdxBookPo> getAvailableBooks(Long bookInfoId);
}
