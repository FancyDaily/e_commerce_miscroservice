package com.e_commerce.miscroservice.sdx_proj.dao.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxFocusDao;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxFocusEnum;
import com.e_commerce.miscroservice.sdx_proj.po.*;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 关注书友的dao层
 */

@Repository
public class TSdxFocusDaoImpl implements TSdxFocusDao {
	private final int PAGE_SIZE = 10;

	@Override
	public int saveTSdxFocusIfNotExist(TSdxFocusPo tSdxFocusPo) {
		return tSdxFocusPo.save();
	}

	@Override
	public int modTSdxFocus(TSdxFocusPo tSdxFocusPo) {
		return tSdxFocusPo.update(tSdxFocusPo.build().eq(TSdxFocusPo::getId, tSdxFocusPo.getId()));
	}

	@Override
	public int delTSdxFocusByIds(Long... ids) {
		TSdxFocusPo tSdxFocusPo = TSdxFocusPo.builder().build();
		tSdxFocusPo.setDeletedFlag(Boolean.TRUE);
		return tSdxFocusPo.update(tSdxFocusPo.build().in(TSdxFocusPo::getId, ids));
	}

	@Override
	public TSdxFocusPo findTSdxFocusById(Long id) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxFocusPo.class);
		build.eq(TSdxFocusPo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxFocusPo::getId, id);
		return MybatisPlus.getInstance().findOne(TSdxFocusPo.builder().build(), build);
	}

	//查找关注书友表
	@Override
	public List<TSdxFocusPo> findTSdxFocusByAll(TSdxFocusPo tSdxFocusPo, Integer page, Integer size) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxFocusPo.class);
		build.eq(TSdxFocusPo::getDeletedFlag, Boolean.FALSE);
		if (tSdxFocusPo.getId() == null) {
			if (tSdxFocusPo.getType() != null) {
				build.eq(TSdxFocusPo::getType, tSdxFocusPo.getType());
			}
			if (tSdxFocusPo.getUserId() != null) {
				build.eq(TSdxFocusPo::getUserId, tSdxFocusPo.getUserId());
			}
			if (StringUtils.isNotEmpty(tSdxFocusPo.getTypeName())) {
				build.like(TSdxFocusPo::getTypeName, tSdxFocusPo.getTypeName());
			}
			if (tSdxFocusPo.getBookFriendId() != null) {
				build.eq(TSdxFocusPo::getBookFriendId, tSdxFocusPo.getBookFriendId());
			}
			if (StringUtils.isNotEmpty(tSdxFocusPo.getBookFriendPic())) {
				build.like(TSdxFocusPo::getBookFriendPic, tSdxFocusPo.getBookFriendPic());
			}
			if (StringUtils.isNotEmpty(tSdxFocusPo.getBookFriendName())) {
				build.like(TSdxFocusPo::getBookFriendName, tSdxFocusPo.getBookFriendName());
			}
			if (page == null) {
				page = 1;
			}
			IdUtil.setTotal(build);
			build.page(page, size == null ? PAGE_SIZE : size);
		} else {
			build.eq(TSdxFocusPo::getId, tSdxFocusPo.getId());
		}
		return MybatisPlus.getInstance().findAll(TSdxFocusPo.builder().build(), build);
	}

	/***
	 * 查询关注书友表（我关注的）
	 * @param userId 用户ID
	 * @param page
	 * @param size
	 * @return 关注书友列表
	 * @author why
	 */
	@Override
	public List<TSdxFocusPo> FindTSdxFocusByUserIdIfocus(Long userId, Integer page, Integer size) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxFocusPo.class);
		build.eq(TSdxFocusPo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxFocusPo::getUserId, userId);
		if (page == null) {
			page = 1;
		}
		IdUtil.setTotal(build);
		build.page(page, size == null ? PAGE_SIZE : size);
		return MybatisPlus.getInstance().findAll(TSdxFocusPo.builder().build(), build);
	}


	/***
	 * 查询关注书友表（关注我的）
	 * @param bookFriendId 用户ID
	 * @param page
	 * @param size
	 * @return 关注书友列表
	 * @author why
	 */
	@Override
	public List<TSdxFocusPo> FindTSdxFocusByUserIdFocusMe(Long bookFriendId, Integer page, Integer size) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxFocusPo.class);
		build.eq(TSdxFocusPo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxFocusPo::getBookFriendId, bookFriendId);
		if (page == null) {
			page = 1;
		}
		IdUtil.setTotal(build);
		build.page(page, size == null ? PAGE_SIZE : size);
		return MybatisPlus.getInstance().findAll(TSdxFocusPo.builder().build(), build);
	}

	@Override
	public Integer updateFocus(TSdxFocusPo tSdxFocusPo) {
		return MybatisPlus.getInstance().update(tSdxFocusPo, baseBuild()
			.eq(TSdxBookAfterReadingNoteUserPo::getId, tSdxFocusPo.getId())
		);
	}


	//互相关注-查询书友表 id,type,typeName
	@Override
	public TSdxFocusPo findTSdxFocus(Long id) {
		return MybatisPlus.getInstance().findOne(new TSdxFocusPo(), baseBuild()
			.eq(TSdxFocusPo::getId, id)
		);
	}

	//互相关注（关注我的）
	@Override
	public List<TSdxFocusPo> findTSdxmFocus(Long bookFriendId, int type) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxFocusPo.class);
		build.eq(TSdxFocusPo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxFocusPo::getBookFriendId, bookFriendId);
		build.eq(TSdxFocusPo::getType, type);
		IdUtil.setTotal(build);
		return MybatisPlus.getInstance().findAll(TSdxFocusPo.builder().build(), build);
	}

	//互相关注（我关注的）
	@Override
	public List<TSdxFocusPo> findTSdxmFocus2(Long userId, int type) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxFocusPo.class);
		build.eq(TSdxFocusPo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxFocusPo::getUserId, userId);
		build.eq(TSdxFocusPo::getType, type);
		return MybatisPlus.getInstance().findAll(TSdxFocusPo.builder().build(), build);
	}

	//根据书名查询书籍编号
	@Override
	public List<TSdxBookInfoPo> findTSdxBookInfoPo(String bookName) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookInfoPo.class);
		build.eq(TSdxFocusPo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxBookInfoPo::getName, bookName);
		return MybatisPlus.getInstance().findAll(TSdxBookInfoPo.builder().build(), build);
	}

	//2：根据书籍编号和好友ID查询 书袋熊书籍 表
	@Override
	public TSdxBookPo findTSdxBookPo(Long bookInfoId, Long currentOwnerId) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookPo.class);
		build.eq(TSdxBookPo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxBookPo::getBookInfoId, bookInfoId);
		build.eq(TSdxBookPo::getCurrentOwnerId, currentOwnerId);
		return MybatisPlus.getInstance().findOne(TSdxBookPo.builder().build(), build);
	}

	//根据userid查询用户姓名
	@Override
	public TCsqUser findtSdxFocusDao(Long currentOwnerId) {
		MybatisPlusBuild build = new MybatisPlusBuild(TCsqUser.class);
		build.eq(TCsqUser::getDeletedFlag, Boolean.FALSE);
		build.eq(TCsqUser::getId, currentOwnerId);
		return MybatisPlus.getInstance().findOne(TCsqUser.builder().build(), build);
	}

	//根据用户ID查询书籍读后感 表
	@Override
	public List<TSdxBookAfterReadingNotePo> findTSdxBookAfterReadingNotePo(Long userId) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookAfterReadingNoteUserPo.class);
		build.eq(TSdxBookAfterReadingNotePo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxBookAfterReadingNoteUserPo::getUserId, userId);
		return MybatisPlus.getInstance().findAll(TSdxBookAfterReadingNotePo.builder().build(), build);
	}

	//根据书籍信息编号查询书籍详情
	@Override
	public TSdxBookInfoPo findTSdxBookInfoPoById(Long bookInfoId) {

		return null;
	}


	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TSdxFocusPo.class)
			.eq(TSdxFocusPo::getDeletedFlag, Boolean.FALSE);
	}

}
