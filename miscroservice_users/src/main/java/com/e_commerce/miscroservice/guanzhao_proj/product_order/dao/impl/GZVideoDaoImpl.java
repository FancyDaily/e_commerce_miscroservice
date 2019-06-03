package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZVideoDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzVideo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-30 12:03
 */
@Component
public class GZVideoDaoImpl implements GZVideoDao {
	@Override
	public List<TGzVideo> selectBySubjectId(Long subjectId) {
		return MybatisPlus.getInstance().finAll(new TGzVideo(), new MybatisPlusBuild(TGzVideo.class)
		.eq(TGzVideo::getSubjectId, subjectId)
		.eq(TGzVideo::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public int insert(TGzVideo gzVideo) {
		return MybatisPlus.getInstance().save(gzVideo);
	}

	@Override
	public List<TGzVideo> selectBySubjectIdAndLessonIdAndFileName(Long subject, Long lessonId, String fileName) {
		return MybatisPlus.getInstance().finAll(new TGzVideo(), new MybatisPlusBuild(TGzVideo.class)
		.eq(TGzVideo::getSubjectId, subject)
		.eq(TGzVideo::getLessonId, lessonId)
		.eq(TGzVideo::getFileName, fileName)
		.eq(TGzVideo::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public TGzVideo selectByPrimaryKey(Long videoId) {
		return MybatisPlus.getInstance().findOne(new TGzVideo(), new MybatisPlusBuild(TGzVideo.class)
		.eq(TGzVideo::getId, videoId)
		.eq(TGzVideo::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public int update(TGzVideo gzVideo) {
		return MybatisPlus.getInstance().update(gzVideo, new MybatisPlusBuild(TGzVideo.class)
		.eq(TGzVideo::getId, gzVideo.getId()));
	}

	@Override
	public List<TGzVideo> selectByLessonId(Long lessonId) {
		return MybatisPlus.getInstance().finAll(new TGzVideo(), new MybatisPlusBuild(TGzVideo.class)
		.eq(TGzVideo::getLessonId, lessonId)
		.eq(TGzVideo::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public TGzVideo selectOneBySubjectIdAndLessonIdIndexDesc(Long subjectId, Long lessonId) {
		return MybatisPlus.getInstance().findOne(new TGzVideo(), new MybatisPlusBuild(TGzVideo.class)
		.eq(TGzVideo::getSubjectId, subjectId)
		.eq(TGzVideo::getLessonId, lessonId)
		.eq(TGzVideo::getIsValid, AppConstant.IS_VALID_YES)
		.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TGzVideo::getVideoIndex)));
	}

	@Override
	public List<TGzVideo> selectInLessonIds(List<Long> lessonIds) {
		return MybatisPlus.getInstance().finAll(new TGzVideo(), new MybatisPlusBuild(TGzVideo.class)
		.in(TGzVideo::getLessonId, lessonIds)
		.eq(TGzVideo::getIsValid, AppConstant.IS_VALID_YES));
	}
}
