package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxBookAfterReadingNoteDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNotePo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
* 书籍读后感的dao层
*
*/

@Repository
public class TSdxBookAfterReadingNoteDaoImpl implements TSdxBookAfterReadingNoteDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxBookAfterReadingNoteIfNotExist(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo) {
        return tSdxBookAfterReadingNotePo.save();
    }
    @Override
    public int modTSdxBookAfterReadingNote(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo) {
        return tSdxBookAfterReadingNotePo.update(tSdxBookAfterReadingNotePo.build().eq(TSdxBookAfterReadingNotePo::getId, tSdxBookAfterReadingNotePo.getId()));
    }
    @Override
    public int delTSdxBookAfterReadingNoteByIds(Long... ids) {
        TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo = TSdxBookAfterReadingNotePo.builder().build();
        tSdxBookAfterReadingNotePo.setDeletedFlag(Boolean.TRUE);
        return tSdxBookAfterReadingNotePo.update(tSdxBookAfterReadingNotePo.build().in(TSdxBookAfterReadingNotePo::getId, ids));
    }
    @Override
    public TSdxBookAfterReadingNotePo findTSdxBookAfterReadingNoteById(Long id) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookAfterReadingNotePo.class);
        build.eq(TSdxBookAfterReadingNotePo::getDeletedFlag, Boolean.FALSE);
        build.eq(TSdxBookAfterReadingNotePo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxBookAfterReadingNotePo.builder().build(), build);
    }
    @Override
    public List<TSdxBookAfterReadingNotePo> findTSdxBookAfterReadingNoteByAll(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo, Integer page, Integer size) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookAfterReadingNotePo.class);
        build.eq(TSdxBookAfterReadingNotePo::getDeletedFlag, Boolean.FALSE);
        if (tSdxBookAfterReadingNotePo.getId() == null) {
            if (tSdxBookAfterReadingNotePo.getBookId()!=null ) {
                build.eq(TSdxBookAfterReadingNotePo::getBookId,tSdxBookAfterReadingNotePo.getBookId());
            }
            if (tSdxBookAfterReadingNotePo.getUserId()!=null ) {
                build.eq(TSdxBookAfterReadingNotePo::getUserId,tSdxBookAfterReadingNotePo.getUserId());
            }
            if (tSdxBookAfterReadingNotePo.getBookInfoId()!=null ) {
                build.eq(TSdxBookAfterReadingNotePo::getBookInfoId,tSdxBookAfterReadingNotePo.getBookInfoId());
            }
            if (tSdxBookAfterReadingNotePo.getThumbUpNum()!=null ) {
                build.eq(TSdxBookAfterReadingNotePo::getThumbUpNum,tSdxBookAfterReadingNotePo.getThumbUpNum());
            }
            if (tSdxBookAfterReadingNotePo.getThumbDownNum()!=null ) {
                build.eq(TSdxBookAfterReadingNotePo::getThumbDownNum,tSdxBookAfterReadingNotePo.getThumbDownNum());
            }
            if (page == null) {
                page = 1;
            }
            IdUtil.setTotal(build);
            build.page(page, size==null?PAGE_SIZE:size);
        }
        else {
            build.eq(TSdxBookAfterReadingNotePo::getId, tSdxBookAfterReadingNotePo.getId());
        }
        return MybatisPlus.getInstance().findAll( TSdxBookAfterReadingNotePo.builder().build()    , build);
    }

	@Override
	public List<TSdxBookAfterReadingNotePo> selectByBookInfoId(Long id) {
		return MybatisPlus.getInstance().findAll(new TSdxBookAfterReadingNotePo(), byBookInfoIdBuild(id)
		);
	}

	private MybatisPlusBuild byBookInfoIdBuild(Long id) {
		return baseBuild()
			.eq(TSdxBookAfterReadingNotePo::getBookInfoId, id);
	}

	@Override
	public List<TSdxBookAfterReadingNotePo> selectByBookInfoIdPage(Long id, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild build = byBookInfoIdBuild(id);
		IdUtil.setTotal(build);

		return MybatisPlus.getInstance().findAll(new TSdxBookAfterReadingNotePo(), build.page(pageNum, pageSize));
	}

	@Override
	public List<TSdxBookAfterReadingNotePo> selectByBookInfoIdPageDesc(Long id, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild build = byBookInfoIdBuild(id);
		build.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TSdxBookAfterReadingNotePo::getCreateTime));
		IdUtil.setTotal(build);

		return MybatisPlus.getInstance().findAll(new TSdxBookAfterReadingNotePo(), build.page(pageNum, pageSize));
	}

	@Override
	public TSdxBookAfterReadingNotePo selectByPrimaryKey(Long bookArnId) {
		return MybatisPlus.getInstance().findOne(new TSdxBookAfterReadingNotePo(), baseBuild()
			.eq(TSdxBookAfterReadingNotePo::getId, bookArnId)
		);
	}

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TSdxBookAfterReadingNotePo.class)
			.eq(TSdxBookAfterReadingNotePo::getDeletedFlag, Boolean.FALSE);
	}
}
