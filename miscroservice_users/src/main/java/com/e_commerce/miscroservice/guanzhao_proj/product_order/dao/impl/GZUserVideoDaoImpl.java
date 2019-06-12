package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GzUserVideoDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzUserVideo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-31 15:35
 */
@Component
public class GZUserVideoDaoImpl implements GzUserVideoDao {
	@Override
	public List<TGzUserVideo> selectByVideoId(Long videoId) {
		return MybatisPlus.getInstance().finAll(new TGzUserVideo(), new MybatisPlusBuild(TGzUserVideo.class)
		.eq(TGzUserVideo::getId, videoId)
		.eq(TGzUserVideo::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public int update(TGzUserVideo gzUserVideo) {
		return MybatisPlus.getInstance().update(gzUserVideo, new MybatisPlusBuild(TGzUserVideo.class)
		.eq(TGzUserVideo::getId, gzUserVideo.getId()));
	}

	@Override
	public List<TGzUserVideo> selectByUserIdAndLessonId(Long userId, Long lessonId) {
		return MybatisPlus.getInstance().finAll(new TGzUserVideo(), new MybatisPlusBuild(TGzUserVideo.class)
		.eq(TGzUserVideo::getUserId, userId)
		.eq(TGzUserVideo::getLessonId, lessonId)
		.eq(TGzUserVideo::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TGzUserVideo> selectByUserIdAndSubjectId(Long userId, Long subjectId) {
		return MybatisPlus.getInstance().finAll(new TGzUserVideo(), new MybatisPlusBuild(TGzUserVideo.class)
		.eq(TGzUserVideo::getUserId, userId)
		.eq(TGzUserVideo::getSubjectId, subjectId)
		.eq(TGzUserVideo::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public int insert(TGzUserVideo... userVideo) {
		return MybatisPlus.getInstance().save(userVideo);
	}

	@Override
	public int multiInsert(List<TGzUserVideo> userVideos) {
		if(userVideos.isEmpty()) {
			return 0;
		}
		return MybatisPlus.getInstance().save(userVideos);
	}

	@Override
	public TGzUserVideo selectByVideoIdAndUserId(Long videoId, Long userId) {
		return MybatisPlus.getInstance().findOne(new TGzUserVideo(), new MybatisPlusBuild(TGzUserVideo.class)
		.eq(TGzUserVideo::getVideoId, videoId)
		.eq(TGzUserVideo::getUserId, userId)
		.eq(TGzUserVideo::getIsValid, AppConstant.IS_VALID_YES));
	}
}
