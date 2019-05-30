package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao;

import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzVideo;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-30 12:02
 */
public interface GZVideoDao {

	List<TGzVideo> selectBySubjectId(Long subjectId);

	int insert(TGzVideo gzVideo);

	List<TGzVideo> selectBySubjectIdAndLessonIdAndFileName(Long subject, Long lessonId, String fileName);

	TGzVideo selectByPrimaryKey(Long videoId);

	int update(TGzVideo gzVideo);

	List<TGzVideo> selectByLessonId(Long lessonId);
}
