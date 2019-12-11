package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.TSdxTrendsVo;



import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.AutoGenerateCode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(commit = "发布动态")
@Data
@Builder
@NoArgsConstructor
public class TSdxTrendsPo extends BaseEntity {
	@Column(commit = "用户ID")
	private Long userId;

	@Column(commit = "用户名")
	private String userName;

	@Column(commit = "用户头像")
	private String userPic;

	@Column(commit = "动态图片")//一般是书籍图片；
	private String bookPic;

	@Column(commit = "动态内容")
	private String trentInfo;

	@Column(commit = "书籍信息编号")
	private Long bookInfoId;

	@Column(commit = "书籍名")
	private String bookName;

	@Column(commit = "书籍图片")
	private String boookPic;

	@Column(commit = "豆瓣评分", precision = 1)
	private Double scoreDouban;

	@Column(commit = "价格", precision = 2)
	private Double price;

	@Column(commit = "用户此时地址")
	private String address;

	@Column(commit = "经度", precision = 2)
	private Double longitude;

	@Column(commit = "纬度", precision = 2)
	private Double latitude;

	@Column(commit = "好友id")
	private Long friendId;

	@Column(commit = "好友名称")//提醒谁看
	private String friendName;

//	public static void main(String[] args) {
//		AutoGenerateCode.generate(com.e_commerce.miscroservice.sdx_proj.po.TSdxTrendsPo.class);
//	}

    public TSdxTrendsVo  copyTSdxTrendsVo() {
        return null;
     }

}
