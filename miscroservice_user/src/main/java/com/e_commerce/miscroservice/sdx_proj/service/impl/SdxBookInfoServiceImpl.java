package com.e_commerce.miscroservice.sdx_proj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookInfoDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxTagDao;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookEnum;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookInfoEnum;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxTagPo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookInfoService;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookService;
import com.e_commerce.miscroservice.sdx_proj.utils.DoubanBookInfo;
import com.e_commerce.miscroservice.sdx_proj.utils.IsbnHelper;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookInfoVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 书籍信息的service层
 */

@Service
@Log
public class SdxBookInfoServiceImpl implements SdxBookInfoService {
	private final long ERROR_LONG = 0L;
	private final int ERROR_INT = 0;
	@Autowired
	private SdxBookService sdxBookService;
	@Autowired
	private SdxBookInfoDao sdxBookInfoDao;
	@Autowired
	private SdxTagDao sdxTagDao;

	@Override
	public long modTSdxBookInfo(TSdxBookInfoPo tSdxBookInfoPo) {
		if (tSdxBookInfoPo == null) {
			log.warn("操作书籍信息参数为空");
			return ERROR_LONG;
		}
		if (tSdxBookInfoPo.getId() == null) {
			log.info("start添加书籍信息={}", tSdxBookInfoPo);
			int result = sdxBookInfoDao.saveTSdxBookInfoIfNotExist(tSdxBookInfoPo);
			return result != 0 ? tSdxBookInfoPo.getId() : ERROR_LONG;
		} else {
			log.info("start修改书籍信息={}", tSdxBookInfoPo.getId());
			return sdxBookInfoDao.modTSdxBookInfo(tSdxBookInfoPo);
		}
	}

	@Override
	public int delTSdxBookInfoByIds(Long... ids) {
		if (ids == null || ids.length == 0) {
			log.warn("删除书籍信息,ids集合为空");
			return ERROR_INT;
		}
		log.warn("start删除Id集合={},书籍信息", Arrays.asList(ids));
		return sdxBookInfoDao.delTSdxBookInfoByIds(ids);
	}

	@Override
	public TSdxBookInfoVo findTSdxBookInfoById(Long id) {
		if (id == null || id <= 0L) {
			log.warn("根据Id查找书籍信息,所传Id不符合规范");
			return new TSdxBookInfoVo();
		}
		log.info("start根据Id={}查找书籍信息", id);
		TSdxBookInfoPo tSdxBookInfoPo = sdxBookInfoDao.findTSdxBookInfoById(id);
		return tSdxBookInfoPo == null ? new TSdxBookInfoVo() : tSdxBookInfoPo.copyTSdxBookInfoVo();
	}

	@Override
	public List<TSdxBookInfoVo> findTSdxBookInfoByAll(TSdxBookInfoPo tSdxBookInfoPo, Integer page, Integer size, Integer sortType) {
		List<TSdxBookInfoVo> tSdxBookInfoVos = new ArrayList<>();
		if (tSdxBookInfoPo == null) {
			log.warn("根据条件查找书籍信息,参数不对");
			return tSdxBookInfoVos;
		}
		log.info("start根据条件查找书籍信息={}", tSdxBookInfoPo);
		List<TSdxBookInfoPo> tSdxBookInfoPos = sdxBookInfoDao.findTSdxBookInfoByAll(tSdxBookInfoPo, page, size, sortType);
		//TODO 对结果的继续处理 -> 根据积分抵扣规则(可能加上个人剩余积分)处理
		for (TSdxBookInfoPo po : tSdxBookInfoPos) {
			tSdxBookInfoVos.add(po.copyTSdxBookInfoVo());
		}

		tSdxBookInfoVos.stream()
			.map(a -> {
				Long id = a.getId();
				List<TSdxBookPo> availableBooks = sdxBookService.getAvailableBooks(id);
				a.setAvailableNum(availableBooks.size());
				return a;
			}).collect(Collectors.toList());

		return tSdxBookInfoVos;
	}

	@Override
	public TSdxBookInfoPo getBookInfo(String isbnCode) {
		if (StringUtil.isEmpty(isbnCode)) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "ISBN码不能为空!");
		DoubanBookInfo infos = null;
		TSdxBookInfoPo tSdxBookInfoPo = null;
		try {
			infos = IsbnHelper.infos(isbnCode);
			if(infos == null) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "错误的ISBN码!");

			//拷贝成bookInfo， 其中 检查是否需要插入新的分类 -> 并需要补全分类编号
			tSdxBookInfoPo = infos.copyTSdxBookInfoPo();
			String rating = infos.getRating();
			tSdxBookInfoPo.setScoreDouban(Double.valueOf(rating));
			tSdxBookInfoPo.setBindingStyle(infos.getBindingLayout());
			tSdxBookInfoPo.setMaximumReserve(SdxBookInfoEnum.DEFAULT_MAXIMUMRESERVE);
			//检查分类信息
			String tag = tSdxBookInfoPo.getTag();
			String tagId = dealWithNewTag(tag);
			tSdxBookInfoPo.setTagId(tagId);
			//与现有的bookInfo进行匹配，若不存在，插入一条相应bookInfo
			TSdxBookInfoPo exist = sdxBookInfoDao.selectByName(tSdxBookInfoPo.getName());
			if(exist == null) {
				sdxBookInfoDao.saveTSdxBookInfoIfNotExist(tSdxBookInfoPo);
			} else {
				tSdxBookInfoPo = exist;
			}
		} catch (MessageException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tSdxBookInfoPo;
	}

	private String dealWithNewTag(String tag) {
		//拆分
		if(!tag.contains(",")) return null;
		String[] tagNames = tag.split(",");
		List<TSdxTagPo> sdxTagPos = sdxTagDao.selectInNames(tagNames);
		Map<String, List<TSdxTagPo>> namePoMap = sdxTagPos.stream()
			.collect(Collectors.groupingBy(TSdxTagPo::getName));
		StringBuilder idBuilder = new StringBuilder();
		for(String tagName:tagNames) {
			List<TSdxTagPo> pos = namePoMap.get(tagName);
			TSdxTagPo sdxTagPo;
			if(pos == null) {	//本tag需要insert
				sdxTagPo = new TSdxTagPo();
				sdxTagPo.setName(tagName);
				sdxTagDao.saveTSdxTagIfNotExist(sdxTagPo);
				//加入到map
				namePoMap.put(tagName, Arrays.asList(sdxTagPo));
			} else {
				sdxTagPo = pos.get(0);
			}
			idBuilder.append(sdxTagPo.getId());
			idBuilder.append(",");
		}
		String ids = idBuilder.toString();
		ids = StringUtil.dealWithEndWith(ids, ",");

		return ids;
	}
}
