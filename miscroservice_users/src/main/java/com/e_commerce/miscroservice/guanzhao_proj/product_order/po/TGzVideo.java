package com.e_commerce.miscroservice.guanzhao_proj.product_order.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-30 11:31
 */
@Table(commit = "观照-视频表")
@Data
@Builder
@NoArgsConstructor
public class TGzVideo {

	@Id
	private Long id;

	private Long subjectId;

	private Long lessonId;

	@Column(commit = "序号", length = 11, defaultVal = "0")
	private Integer videoIndex;

	@Transient
	private Integer videoCompletion;

	@Transient
	private Integer videoCompletionStatus;

	@Transient
	private String sign;

	@Column(commit = "展示名字")
	private String name;

	@Column(commit = "文件名")
	private String fileName;

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
