package com.e_commerce.miscroservice.sdx_proj.dao.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookAfterReadingNoteUserDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNotePo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNoteUserPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 书籍读后感用户相关的dao层
 */

@Repository
public class SdxBookAfterReadingNoteUserDaoImpl implements SdxBookAfterReadingNoteUserDao {
	private final int PAGE_SIZE = 10;

	@Override
	public int saveTSdxBookAfterReadingNoteUserIfNotExist(TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo) {
		return tSdxBookAfterReadingNoteUserPo.save();
	}

	@Override
	public int modTSdxBookAfterReadingNoteUser(TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo) {
		return tSdxBookAfterReadingNoteUserPo.update(tSdxBookAfterReadingNoteUserPo.build().eq(TSdxBookAfterReadingNoteUserPo::getId, tSdxBookAfterReadingNoteUserPo.getId()));
	}

	@Override
	public int delTSdxBookAfterReadingNoteUserByIds(Long... ids) {
		TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo = TSdxBookAfterReadingNoteUserPo.builder().build();
		tSdxBookAfterReadingNoteUserPo.setDeletedFlag(Boolean.TRUE);
		return tSdxBookAfterReadingNoteUserPo.update(tSdxBookAfterReadingNoteUserPo.build().in(TSdxBookAfterReadingNoteUserPo::getId, ids));
	}

	@Override
	public TSdxBookAfterReadingNoteUserPo findTSdxBookAfterReadingNoteUserById(Long id) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookAfterReadingNoteUserPo.class);
		build.eq(TSdxBookAfterReadingNoteUserPo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxBookAfterReadingNoteUserPo::getId, id);
		return MybatisPlus.getInstance().findOne(TSdxBookAfterReadingNoteUserPo.builder().build(), build);
	}

	@Override
	public List<TSdxBookAfterReadingNoteUserPo> findTSdxBookAfterReadingNoteUserByAll(TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo, Integer page, Integer size) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookAfterReadingNoteUserPo.class);
		build.eq(TSdxBookAfterReadingNoteUserPo::getDeletedFlag, Boolean.FALSE);
		if (tSdxBookAfterReadingNoteUserPo.getId() == null) {
			if (tSdxBookAfterReadingNoteUserPo.getType() != null) {
				build.eq(TSdxBookAfterReadingNoteUserPo::getType, tSdxBookAfterReadingNoteUserPo.getType());
			}
			if (tSdxBookAfterReadingNoteUserPo.getBookId() != null) {
				build.eq(TSdxBookAfterReadingNoteUserPo::getBookId, tSdxBookAfterReadingNoteUserPo.getBookId());
			}
			if (tSdxBookAfterReadingNoteUserPo.getUserId() != null) {
				build.eq(TSdxBookAfterReadingNoteUserPo::getUserId, tSdxBookAfterReadingNoteUserPo.getUserId());
			}
			if (tSdxBookAfterReadingNoteUserPo.getIsThumb() != null) {
				build.eq(TSdxBookAfterReadingNoteUserPo::getIsThumb, tSdxBookAfterReadingNoteUserPo.getIsThumb());
			}
			if (tSdxBookAfterReadingNoteUserPo.getBookInfoId() != null) {
				build.eq(TSdxBookAfterReadingNoteUserPo::getBookInfoId, tSdxBookAfterReadingNoteUserPo.getBookInfoId());
			}
			if (tSdxBookAfterReadingNoteUserPo.getIsPurchase() != null) {
				build.eq(TSdxBookAfterReadingNoteUserPo::getIsPurchase, tSdxBookAfterReadingNoteUserPo.getIsPurchase());
			}
			if (tSdxBookAfterReadingNoteUserPo.getBookAfterReadingNoteId() != null) {
				build.eq(TSdxBookAfterReadingNoteUserPo::getBookAfterReadingNoteId, tSdxBookAfterReadingNoteUserPo.getBookAfterReadingNoteId());
			}
			if (page == null) {
				page = 1;
			}
			IdUtil.setTotal(build);
			build.page(page, size == null ? PAGE_SIZE : size);
		} else {
			build.eq(TSdxBookAfterReadingNoteUserPo::getId, tSdxBookAfterReadingNoteUserPo.getId());
		}
		return MybatisPlus.getInstance().findAll(TSdxBookAfterReadingNoteUserPo.builder().build(), build);
	}

	@Override
	public List<TSdxBookAfterReadingNoteUserPo> selectInAfrdnIdsAndUserIdAndIsPurchase(List<Long> afrdnIds, Integer isPurchase, Long userId) {
		return MybatisPlus.getInstance().findAll(new TSdxBookAfterReadingNoteUserPo(), baseBuild()
			.in(TSdxBookAfterReadingNoteUserPo::getBookAfterReadingNoteId, afrdnIds)
			.eq(TSdxBookAfterReadingNoteUserPo::getIsPurchase, isPurchase)
			.eq(TSdxBookAfterReadingNoteUserPo::getUserId, userId)
		);
	}

	@Override
	public TSdxBookAfterReadingNoteUserPo selectByBookAfrnIdAndUserIdAndType(Long bookArnId, Long userId, int type) {
		return MybatisPlus.getInstance().findOne(new TSdxBookAfterReadingNoteUserPo(), baseBuild()
			.eq(TSdxBookAfterReadingNoteUserPo::getBookAfterReadingNoteId, bookArnId)
			.eq(TSdxBookAfterReadingNoteUserPo::getUserId, userId)
			.eq(TSdxBookAfterReadingNoteUserPo::getType, type)
		);
	}

	@Override
	public int insert(TSdxBookAfterReadingNoteUserPo... afterReadingNoteUserPo) {
		return MybatisPlus.getInstance().save(afterReadingNoteUserPo);
	}

	@Override
	public int insert(List<TSdxBookAfterReadingNoteUserPo> afterReadingNoteUserPo) {
		return MybatisPlus.getInstance().save(afterReadingNoteUserPo);
	}

	@Override
	public int update(TSdxBookAfterReadingNoteUserPo afterReadingNoteUserPo) {
		return MybatisPlus.getInstance().update(afterReadingNoteUserPo, baseBuild()
			.eq(TSdxBookAfterReadingNoteUserPo::getId, afterReadingNoteUserPo.getId())
		);
	}

	@Override
	public List<TSdxBookAfterReadingNoteUserPo> selectByBookAfrnIdAndUserIdAndIsThumb(Long afrId, Long userId, int code) {
		return MybatisPlus.getInstance().findAll(new TSdxBookAfterReadingNoteUserPo(), byBookAfrnIdAndUserIdAndIsThumbBuild(afrId, userId, code)
		);
	}

	private MybatisPlusBuild byBookAfrnIdAndUserIdAndIsThumbBuild(Long afrId, Long userId, int code) {
		return baseBuild()
			.eq(TSdxBookAfterReadingNoteUserPo::getBookAfterReadingNoteId, afrId)
			.eq(TSdxBookAfterReadingNoteUserPo::getUserId, userId)
			.eq(TSdxBookAfterReadingNoteUserPo::getIsThumb, code);
	}

	@Override
	public TSdxBookAfterReadingNoteUserPo selectByBookAfrnIdAndUserIdAndIsThumbAndType(Long afrId, Long userId, int code, int code1) {
		return MybatisPlus.getInstance().findOne(new TSdxBookAfterReadingNoteUserPo(), byBookAfrnIdAndUserIdAndIsThumbBuild(afrId, userId, code)
			.eq(TSdxBookAfterReadingNoteUserPo::getType, code1));
	}

	@Override
	public List<TSdxBookAfterReadingNoteUserPo> selectByBookAfrnIdAndUserIdAndTypeAndIsPurchase(Long id, Long currentUserId, int code, int code1) {
		return MybatisPlus.getInstance().findAll(new TSdxBookAfterReadingNoteUserPo(), baseBuild()
			.eq(TSdxBookAfterReadingNoteUserPo::getBookAfterReadingNoteId, id)
			.eq(TSdxBookAfterReadingNoteUserPo::getUserId, currentUserId)
			.eq(TSdxBookAfterReadingNoteUserPo::getType, code)
			.eq(TSdxBookAfterReadingNoteUserPo::getIsPurchase, code1)
		);
	}

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TSdxBookAfterReadingNoteUserPo.class)
			.eq(TSdxBookAfterReadingNoteUserPo::getDeletedFlag, Boolean.FALSE);
	}
}
