package com.e_commerce.miscroservice.sdx_proj.service.impl;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookInfoDao;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxFocusDao;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxTrendsDao;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxFocusEnum;
import com.e_commerce.miscroservice.sdx_proj.po.*;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxTrendsService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookInfoVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxTrendsVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 发布动态的service层
 */

@Service
@Log
public class TSdxTrendsServiceImpl implements TSdxTrendsService {
	private final long ERROR_LONG = 0L;
	private final int ERROR_INT = 0;
	@Autowired
	private TSdxTrendsDao tSdxTrendsDao;//动态
	@Autowired
	private TSdxFocusDao tSdxFocusDao;//关注书友
	@Autowired
	private SdxBookInfoDao sdxBookInfoDao;//书籍信息

	@Override
	public long modTSdxTrends(TSdxTrendsPo tSdxTrendsPo) {
		if (tSdxTrendsPo == null) {
			log.warn("操作发布动态参数为空");
			return ERROR_LONG;
		}
		if (tSdxTrendsPo.getId() == null) {
			log.info("start添加发布动态={}", tSdxTrendsPo);
			int result = tSdxTrendsDao.saveTSdxTrendsIfNotExist(tSdxTrendsPo);
			return result != 0 ? tSdxTrendsPo.getId() : ERROR_LONG;
		} else {
			log.info("start修改发布动态={}", tSdxTrendsPo.getId());
			return tSdxTrendsDao.modTSdxTrends(tSdxTrendsPo);
		}
	}

	@Override
	public int delTSdxTrendsByIds(Long... ids) {
		if (ids == null || ids.length == 0) {
			log.warn("删除发布动态,ids集合为空");
			return ERROR_INT;
		}
		log.warn("start删除Id集合={},发布动态", Arrays.asList(ids));
		return tSdxTrendsDao.delTSdxTrendsByIds(ids);
	}

	@Override
	public TSdxTrendsVo findTSdxTrendsById(Long id) {
		if (id == null || id <= 0L) {
			log.warn("根据Id查找发布动态,所传Id不符合规范");
			return new TSdxTrendsVo();
		}
		log.info("start根据Id={}查找发布动态", id);
		TSdxTrendsPo tSdxTrendsPo = tSdxTrendsDao.findTSdxTrendsById(id);
		return tSdxTrendsPo == null ? new TSdxTrendsVo() : tSdxTrendsPo.copyTSdxTrendsVo();
	}

	@Override
	public List<TSdxTrendsVo> findTSdxTrendsByAll(TSdxTrendsPo tSdxTrendsPo, Integer page, Integer size) {
		List<TSdxTrendsVo> tSdxTrendsVos = new ArrayList<>();
		if (tSdxTrendsPo == null) {
			log.warn("根据条件查找发布动态,参数不对");
			return tSdxTrendsVos;
		}
		log.info("start根据条件查找发布动态={}", tSdxTrendsPo);
		List<TSdxTrendsPo> tSdxTrendsPos = tSdxTrendsDao.findTSdxTrendsByAll(tSdxTrendsPo, page, size);
		for (TSdxTrendsPo po : tSdxTrendsPos) {
			tSdxTrendsVos.add(po.copyTSdxTrendsVo());
		}
		return tSdxTrendsVos;
	}


	//查询跟我有相同书籍的人
	@Override
	public List<Map<String, Object>> findTSdxTrendsFriend(Long userId, String bookName) {
		/***
		 * 1：根据书名查询书籍编号；根据ID查询好友id
		 * 2：根据书籍编号和好友ID查询 书袋熊书籍 表
		 * 3：根据书袋熊书籍表的拥有者ID查询用户ID和姓名
		 *
		 */
		List<Map<String, Object>> resultList = new ArrayList<>();//将查询得到的值全部放入map中；
		Map<String, Object> map = new HashMap<String, Object>();
//1：根据用户ID查询好友ID list表 循环好友ID
		List<TSdxFocusPo> mfocus1 = tSdxFocusDao.findTSdxmFocus(userId, SdxFocusEnum.MUTUAL_FOCUS.getCode());//关注我的互相关注；
		//根据书名查询书籍信息编号
		List<TSdxBookInfoPo> list1 = tSdxFocusDao.findTSdxBookInfoPo(bookName);
		if (mfocus1 != null && list1 != null && !mfocus1.isEmpty() && !list1.isEmpty()) {
			for (TSdxFocusPo fo : mfocus1) {
				for (TSdxBookInfoPo bo : list1) {
					//2：根据书籍编号和好友ID查询 书袋熊书籍 表
					TSdxBookPo tSdxBookPo = tSdxFocusDao.findTSdxBookPo(bo.getId(), fo.getUserId());
					//根据userid查询用户姓名
					if (tSdxBookPo != null) {
						TCsqUser tCsqUser = tSdxFocusDao.findtSdxFocusDao(tSdxBookPo.getCurrentOwnerId());
						if (tCsqUser != null) {
							map.put("friendId", tCsqUser.getId());
							map.put("friendName", tCsqUser.getName());
							resultList.add(map);//将map放入结果集；
						}
					}
				}
			}
		}
		List<TSdxFocusPo> mfocus2 = tSdxFocusDao.findTSdxmFocus2(userId, SdxFocusEnum.MUTUAL_FOCUS.getCode());//我关注的互相关注；
		if (mfocus2 != null && !mfocus2.isEmpty() && list1 != null && !list1.isEmpty()) {
			for (TSdxFocusPo fo2 : mfocus1) {
				for (TSdxBookInfoPo bo2 : list1) {
					//2：根据书籍编号和好友ID查询 书袋熊书籍 表
					TSdxBookPo tSdxBookPo = tSdxFocusDao.findTSdxBookPo(bo2.getId(), fo2.getBookFriendId());
					//根据userid查询用户姓名
					if (tSdxBookPo != null) {
						TCsqUser tCsqUser = tSdxFocusDao.findtSdxFocusDao(tSdxBookPo.getCurrentOwnerId());
						if (tCsqUser != null) {
							map.put("friendId", tCsqUser.getId());
							map.put("friendName", tCsqUser.getName());
							resultList.add(map);//将map放入结果集；
						}
					}
				}
			}

		}


		return resultList;
	}

	//查询最近看的书籍

	/**
	 * 1：根据用户ID查询读后感用户相关 表
	 * 2：从读后感得到书籍信息编号；
	 * 3：根据书籍信息编号查询书籍详情
	 */
	@Override
	public List<TSdxBookInfoVo> findBookInfos(Long userId) {
		List<TSdxBookInfoVo> tSdxBookInfoVos2 = new ArrayList<TSdxBookInfoVo>();
		List<TSdxBookInfoVo> tSdxBookInfoVos = new ArrayList<TSdxBookInfoVo>();
		//1:根据用户ID查询书籍读后感 表
		List<TSdxBookAfterReadingNotePo> tSdxBookAfterReadingNotePo = tSdxFocusDao.findTSdxBookAfterReadingNotePo(userId);
		if (tSdxBookAfterReadingNotePo != null && !tSdxBookAfterReadingNotePo.isEmpty()) {
			for (TSdxBookAfterReadingNotePo po : tSdxBookAfterReadingNotePo) {
				//3：根据书籍信息编号查询书籍详情
				TSdxBookInfoPo tSdxBookInfoPo = sdxBookInfoDao.findTSdxBookInfoById(po.getBookInfoId());
				tSdxBookInfoVos2.add(tSdxBookInfoPo.copyTSdxBookInfoVo());
			}
		}
		tSdxBookInfoVos = tSdxBookInfoVos2.stream().distinct().collect(Collectors.toList());//去除重复
		return tSdxBookInfoVos;
	}


}
