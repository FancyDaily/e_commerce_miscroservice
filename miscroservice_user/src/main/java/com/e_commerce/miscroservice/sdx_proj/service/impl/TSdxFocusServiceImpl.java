package com.e_commerce.miscroservice.sdx_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxFocusDao;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxFocusEnum;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxFocusPo;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxFocusService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxFocusVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * 关注书友的service层
 */

@Service
@Log
public class TSdxFocusServiceImpl implements TSdxFocusService {
	private final long ERROR_LONG = 0L;
	private final int ERROR_INT = 0;
	@Autowired
	private TSdxFocusDao tSdxFocusDao;

	/***
	 * 添加或者修改关注书友
	 * @param tSdxFocusPo
	 * @return
	 * @author why
	 * @date 2019-12-7
	 */
	@Override
	public long modTSdxFocus(TSdxFocusPo tSdxFocusPo) {
		if (tSdxFocusPo == null) {
			log.warn("操作关注书友参数为空");
			return ERROR_LONG;
		}
		if (tSdxFocusPo.getId() == null) {
			log.info("start添加关注书友={}", tSdxFocusPo);
			//获取用户ID
			tSdxFocusPo.setUserId(IdUtil.getId());//将用户ID插入参数列表；
			int result = tSdxFocusDao.saveTSdxFocusIfNotExist(tSdxFocusPo);
			return result != 0 ? tSdxFocusPo.getId() : ERROR_LONG;
		} else {
			log.info("start修改关注书友={}", tSdxFocusPo.getId());
			return tSdxFocusDao.modTSdxFocus(tSdxFocusPo);
		}
	}

	@Override
	public int delTSdxFocusByIds(Long... ids) {
		if (ids == null || ids.length == 0) {
			log.warn("删除关注书友,ids集合为空");
			return ERROR_INT;
		}
		log.warn("start删除Id集合={},关注书友", Arrays.asList(ids));
		return tSdxFocusDao.delTSdxFocusByIds(ids);
	}

	@Override
	public TSdxFocusVo findTSdxFocusById(Long id) {
		if (id == null || id <= 0L) {
			log.warn("根据Id查找关注书友,所传Id不符合规范");
			return new TSdxFocusVo();
		}
		log.info("start根据Id={}查找关注书友", id);
		TSdxFocusPo tSdxFocusPo = tSdxFocusDao.findTSdxFocusById(id);
		return tSdxFocusPo == null ? new TSdxFocusVo() : tSdxFocusPo.copyTSdxFocusVo();
	}

	//查找关注书友表
	@Override
	public List<TSdxFocusVo> findTSdxFocusByAll(TSdxFocusPo tSdxFocusPo, Integer page, Integer size) {
		List<TSdxFocusVo> tSdxFocusVos = new ArrayList<>();
		if (tSdxFocusPo == null) {
			log.warn("根据条件查找关注书友,参数不对");
			return tSdxFocusVos;
		}
		log.info("start根据条件查找关注书友={}", tSdxFocusPo);
		List<TSdxFocusPo> tSdxFocusPos = tSdxFocusDao.findTSdxFocusByAll(tSdxFocusPo, page, size);
		for (TSdxFocusPo po : tSdxFocusPos) {
			tSdxFocusVos.add(po.copyTSdxFocusVo());
		}
		return tSdxFocusVos;
	}

	//查询书友列表（我关注的/关注我的）
	@Override
	public List<TSdxFocusVo> FindTSdxFocusByUserId(Long userId, Integer type, Integer page, Integer size) {
		List<TSdxFocusVo> tSdxFocusVos = new ArrayList<>();
		if (type == SdxFocusEnum.I_FOCUS.getCode()) {//我关注的
			//查询关注我的（互相关注）
			List<TSdxFocusPo> mfocus = tSdxFocusDao.findTSdxmFocus(userId, SdxFocusEnum.MUTUAL_FOCUS.getCode());
			if (!mfocus.isEmpty() && mfocus != null) {
				for (TSdxFocusPo fos : mfocus) {
					if (fos.getType() == SdxFocusEnum.MUTUAL_FOCUS.getCode()) {
						tSdxFocusVos.add(fos.copyTSdxFocusVo());
					}

				}
			}
			//查询我关注的书友列表（包括互相关注的）
			List<TSdxFocusPo> focus = tSdxFocusDao.FindTSdxFocusByUserIdIfocus(userId, page, size);
			if (!focus.isEmpty() && focus != null) {
				for (TSdxFocusPo fo : focus) {//我关注的表不为空，循环列表，将值插入结果列表；
					tSdxFocusVos.add(fo.copyTSdxFocusVo());
				}
			}
		}
		if (type == SdxFocusEnum.FOCUS_ME.getCode()) {//关注我的
			//查询我互相关注的
			List<TSdxFocusPo> mfocus = tSdxFocusDao.findTSdxmFocus2(userId, SdxFocusEnum.MUTUAL_FOCUS.getCode());
			if (!mfocus.isEmpty() && mfocus != null) {
				for (TSdxFocusPo fos : mfocus) {
					tSdxFocusVos.add(fos.copyTSdxFocusVo());
				}
			}
			//查询关注我的书友列表（包括互相关注的）
			List<TSdxFocusPo> focus2 = tSdxFocusDao.FindTSdxFocusByUserIdFocusMe(userId, page, size);
			if (!focus2.isEmpty()) {
				for (TSdxFocusPo fo2 : focus2) {//关注我的表不为空，循环列表，将值插入结果列表；
					tSdxFocusVos.add(fo2.copyTSdxFocusVo());
				}

			}
		}

		return tSdxFocusVos;
	}

	/***
	 * 点击互相关注（1：将关注我的表个修改为互相关注   2：添加一条我和书友的互相关注表);
	 * id 关注书友表id
	 * @return
	 * @author why
	 * @date 2019-12-7
	 */
	@Override
	public int mutualBookFriend(Long userId, Long id) {
		//根据条件查询书友表数据；
		TSdxFocusPo tSdxFocusPo = tSdxFocusDao.findTSdxFocus(id);
		int result = 0;
		if (tSdxFocusPo != null) {
			tSdxFocusPo.setId(id);
			tSdxFocusPo.setType(SdxFocusEnum.MUTUAL_FOCUS.getCode());//type=2
			tSdxFocusPo.setTypeName(SdxFocusEnum.MUTUAL_FOCUS.getMsg());//typeName="互相关注"
			result = tSdxFocusDao.updateFocus(tSdxFocusPo);//修改书友表；
		}
		return result;
	}

	/***
	 * 取消关注
	 * @param id 关注书友表id
	 * @return
	 * @author why
	 * @date 2019-12-7
	 */
	@Override
	public int cancelMutualBookFriend(Long userId, Long id) {
		//根据id书友表数据；
		TSdxFocusPo tSdxFocusPo = tSdxFocusDao.findTSdxFocusById(id);
		Long userIds = tSdxFocusPo.getUserId();
		Long bookFriendId = tSdxFocusPo.getBookFriendId();
		Integer type = tSdxFocusPo.getType();
		String typeName = tSdxFocusPo.getTypeName();

		Integer result = 0;
		if (tSdxFocusPo != null) {
			if (type == SdxFocusEnum.I_FOCUS.getCode()) {//我关注的
				//删除关注书友表；
				result = tSdxFocusDao.delTSdxFocusByIds(id);
			}
			if (tSdxFocusPo.getType() == SdxFocusEnum.MUTUAL_FOCUS.getCode()) {//互相关注；
				//互相关注有两种情况、///1：我关注的互相关注  2：关注我的互相关注
//获取用户ID（userids），当userids=userId时///我关注的互相关注
				if (userId == userId) {
					tSdxFocusPo.setUserId(bookFriendId);
					tSdxFocusPo.setBookFriendId(userIds);
					tSdxFocusPo.setType(SdxFocusEnum.I_FOCUS.getCode());
					tSdxFocusPo.setTypeName(SdxFocusEnum.I_FOCUS.getMsg());
					result = tSdxFocusDao.updateFocus(tSdxFocusPo);//修改书友表；
				}
//获取用户ID（userids），当userids=bookFriendId///关注我的互相关注
				if (userId == bookFriendId) {
					tSdxFocusPo.setUserId(userIds);
					tSdxFocusPo.setBookFriendId(bookFriendId);
					tSdxFocusPo.setType(SdxFocusEnum.I_FOCUS.getCode());
					tSdxFocusPo.setTypeName(SdxFocusEnum.I_FOCUS.getMsg());
					result = tSdxFocusDao.updateFocus(tSdxFocusPo);//修改书友表；
				}

			}
		}
		return result;
	}

}
