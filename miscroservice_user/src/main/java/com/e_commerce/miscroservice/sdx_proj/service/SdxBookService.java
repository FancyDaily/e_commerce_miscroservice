package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookPo;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxBookDetailVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookVo;
import java.util.List;
import java.util.Map;

public interface SdxBookService {
    long modTSdxBook(TSdxBookPo tSdxBookPo);
    int delTSdxBookByIds(Long... ids);
    TSdxBookVo findTSdxBookById(Long id);

	void setSuggestList(Integer dayNo, List<Long> bookInfoIds);

	List<TSdxBookVo> findTSdxBookByAll(TSdxBookPo tSdxBookPo, Integer page, Integer size, Integer sortType);

	List<TSdxBookVo> findTSdxBookByAll(TSdxBookPo tSdxBookPo, Integer page, Integer size);

	SdxBookDetailVo detail(Long id, Long userId);

	Double dealWithScoreMoney(Long userId, Double price);

	QueryResult soldOrPurchaseUserList(Long id, Integer pageNum, Integer pageSize, Boolean isSold);

	void preOrder(Long id, Long userId);

	List<TSdxBookPo> getAvailableBooks(Long bookInfoId);

	Map<Long, Integer> getIdExpectedScoresMap(Long... bookInfoIds);

	void putOnShelf(String bookIds);

	List<TSdxBookInfoPo> mostFollowList();

	List<TSdxBookInfoPo> suggestList();

	String getSuggestInitail();

	List<TCsqService> gotoServiceList();

	boolean preOrderStatus(Long id, Long bookInfoId);
}
