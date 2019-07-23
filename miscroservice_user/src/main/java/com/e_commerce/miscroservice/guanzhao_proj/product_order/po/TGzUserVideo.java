package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-31 15:32
 */
@Table(commit = "观照-用户视频关联表")
@Data
@Builder
@NoArgsConstructor
public class TGzUserVideo {

	@Id
	private Long id;

	private Long userId;

	private Long subjectId;

	private Long lessonId;

	private Long videoId;

	@Column(commit = "视频进度", length = 11, defaultVal = "0")
	private Integer videoCompletion;

	@Column(commit = "视频完成状态", length = 11, defaultVal = "0")
	private Integer videoCompletionStatus;

	@Column(commit = "扩展字段")
	private String extend;

	@Column(commit = "创建者编号", isNUll = false)
	private Long createUser;

	@Column(commit = "创建时间戳", dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE)
	private Timestamp createTime;

	@Column(commit = "更新者编号", isNUll = false)
	private Long updateUser;

	@Column(commit = "更新时间戳", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE)
	private Timestamp updateTime;

	@Column(commit = "有效性", defaultVal = "1")
	private String isValid;
}
