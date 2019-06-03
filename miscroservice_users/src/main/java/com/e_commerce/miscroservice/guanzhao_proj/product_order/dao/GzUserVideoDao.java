package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao;

import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzUserVideo;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-31 15:35
 */
public interface GzUserVideoDao {

	List<TGzUserVideo> selectByVideoId(Long videoId);

	int update(TGzUserVideo gzUserVideo);

	List<TGzUserVideo> selectByUserIdAndLessonId(Long userId, Long lessonId);

	List<TGzUserVideo> selectByUserIdAndSubjectId(Long userId, Long subjectId);

	int insert(TGzUserVideo... userVideo);

	int multiInsert(List<TGzUserVideo> userVideos);

	TGzUserVideo selectByVideoIdAndUserId(Long videoId, Long userId);
}
