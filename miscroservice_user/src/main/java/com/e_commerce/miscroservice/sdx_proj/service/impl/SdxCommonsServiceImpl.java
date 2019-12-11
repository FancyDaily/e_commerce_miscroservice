package com.e_commerce.miscroservice.sdx_proj.service.impl;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxCommonsDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxCommonsPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxFocusPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxTrendsPo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxCommonsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log
public class SdxCommonsServiceImpl implements SdxCommonsService {
	@Autowired
	private SdxCommonsDao sdxCommonsDao;

	/***
	 * 查询好友动态以及动态下面的评论
	 * @param userId
	 * @author why
	 * @date 2019-12-9
	 */
	@Override
	public List<Map<String, Object>> findFriendTrends(long userId) {
		List<Map<String, Object>> resultList = new ArrayList<>();//存放查询出来的结果集；
		List<Map<String, Object>> resultList2 = new ArrayList<>();//存放查询出来的结果集；
		//1：根据用户ID查询好友列表得到ID
		List<Long> ids = new ArrayList<>();
		//查询我关注的书友列表（包括我互相关注的）
		List<TSdxFocusPo> focus = sdxCommonsDao.findTSdxFocusPoByUserId(userId);
		if (focus != null && !focus.isEmpty()) {
			for (TSdxFocusPo focusPo : focus) {
				//得到好友ID
				ids.add(focusPo.getBookFriendId());
			}
		}
		//查询关注我的书友列表（互相关注）
		List<TSdxFocusPo> focus2 = sdxCommonsDao.findTSdxFocusPoByUserId2(userId);
		if (focus2 != null && !focus2.isEmpty()) {
			for (TSdxFocusPo focusPo2 : focus2) {
				ids.add(focusPo2.getUserId());
			}
		}
		//2：根据好友ID查询好友信息以及动态
		if (!ids.isEmpty()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (Long id : ids) {
				//用户信息
				TCsqUser tCsqUser = sdxCommonsDao.findTCsqUserByUserId(id);
				//将好友信息放入map中
				map.put("Id", tCsqUser.getId());//用户ID
				map.put("Name", tCsqUser.getName());//用户名
				map.put("age", tCsqUser.getAge());//年龄
				map.put("headPic", tCsqUser.getUserHeadPortraitPath());//头像
				resultList.add(map);
				//用户动态
				List<TSdxTrendsPo> tSdxTrendsPo = sdxCommonsDao.findsdxCommonsDaoByUserId(id);
				if (tSdxTrendsPo != null && !tSdxTrendsPo.isEmpty()) {
					List<Map<String, Object>> tSdxTrendsPoList = new ArrayList<>();//存放查询出来的结果集；
					for (TSdxTrendsPo trendsPo : tSdxTrendsPo) {//循环动态表得到评论id
						Map<String, Object> map2 = new HashMap<String, Object>();
						//将动态信息放入map中
						map2.put("trendsId", trendsPo.getId());//动态ID
						map2.put("trentInfo", trendsPo.getTrentInfo());//动态内容；
						map2.put("bookInfoId", trendsPo.getBookInfoId());//书籍信息编号
						map2.put("bookName", trendsPo.getBookName());//书籍名称
						map2.put("boookPic", trendsPo.getBookPic());//书籍图片；
						map2.put("scoreDouban", trendsPo.getScoreDouban());//豆瓣评分
						map2.put("price", trendsPo.getPrice());//价格
						map2.put("address", trendsPo.getAddress());//用户此时地址；
						map2.put("latitude", trendsPo.getLatitude());//纬度；
						map2.put("longitude", trendsPo.getLongitude());//经度；
						tSdxTrendsPoList.add(map2);
						//3：根据动态ID查询出所有的评论
						List<TSdxCommonsPo> commonsPos = sdxCommonsDao.findsdxCommonsDaoByTrendsId(trendsPo.getId());
						if (commonsPos != null && !commonsPos.isEmpty()) {
							List<Map<String, Object>> userList = new ArrayList<>();//存放查询出来的结果集；
							for (TSdxCommonsPo tSdxCommons : commonsPos) {
								Map<String, Object> map3 = new HashMap<String, Object>();
								TCsqUser tCsqUser1 = sdxCommonsDao.findTCsqUserByUserId(tSdxCommons.getUserId());
								map3.put("commonsId", tSdxCommons.getId());//评论ID
								map3.put("friendId", tSdxCommons.getUserId());//评论用户ID
								if (tCsqUser1 != null) {
									map3.put("friendName", tCsqUser1.getName());//评论用户名
									map3.put("friendPic", tCsqUser1.getUserHeadPortraitPath());
								}
								map3.put("contentInfo", tSdxCommons.getContentInfo());//评论内容
								userList.add(map3);
							}
							map2.put("userList", userList);
						}

					}
					map.put("tSdxTrendsPoList", tSdxTrendsPoList);

				}
			}
			resultList.add(map);
		}
		//3：根据动态ID查询出所有的评论
		resultList2 = resultList.stream().distinct().collect(Collectors.toList());
		return resultList2;
	}

	/**
	 * 查询跟你看过同样书的人发的动态(发的动态是同样的书//有同样的书籍信息编号)
	 *
	 * @param userId 用户ID
	 * @author why
	 * @date 2019-12-10
	 */
	@Override
	public Object findFriendTrendsByUserId(Long userId) {
		//1：根据用户ID查询我的动态表得到书籍信息编号列表
		//2：根据书籍信息编号查询动态表得到userID
		//3：根据用户ID查询用户信息
		List<Map<String, Object>> resultList = new ArrayList<>();//存放查询出来的结果集；
		List<Map<String, Object>> resultList2 = new ArrayList<>();//存放查询出来的结果集；

		List<TSdxTrendsPo> tSdxTrendsPoList = sdxCommonsDao.findTSdxTrendsPoByUserId(userId);//查询我的动态；
		if (tSdxTrendsPoList != null && !tSdxTrendsPoList.isEmpty()) {
			for (TSdxTrendsPo trendsPo : tSdxTrendsPoList) {//循环列表
				List<TSdxTrendsPo> tSdxTrendsPoList2 = sdxCommonsDao.findTSdxTrendsPoByBookInfoId(trendsPo.getBookInfoId());//查询我的动态；
				if (tSdxTrendsPoList2 != null && !tSdxTrendsPoList2.isEmpty()) {
					for (TSdxTrendsPo trendsPo2 : tSdxTrendsPoList2) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("userId", trendsPo2.getUserId());//用户ID
						map.put("userName", trendsPo2.getUserName());//用户名
						map.put("userId", trendsPo2.getUserPic());//用户头像
						map.put("address", trendsPo2.getAddress());//地址
						map.put("longitude", trendsPo2.getLongitude());//经度
						map.put("userId", trendsPo2.getLatitude());//纬度
						map.put("trendsId", trendsPo2.getId());//动态ID
						map.put("trendsInfo", trendsPo2.getTrentInfo());//动态内容；
						map.put("bookInfoId", trendsPo2.getBookInfoId());//书籍编号ID
						map.put("bookName", trendsPo2.getBookName());//书名
						map.put("boookPic", trendsPo2.getBookPic());//书籍图片
						map.put("scoreDouban", trendsPo2.getScoreDouban());//豆瓣评分
						map.put("price", trendsPo2.getPrice());//价格
						TCsqUser tCsqUser = sdxCommonsDao.findTCsqUserByUserId(trendsPo2.getUserId());//查询用户信息表；
						if (tCsqUser != null) {
							map.put("age", tCsqUser.getAge());
						}
						resultList.add(map);
					}
				}
			}
		}

		resultList2 = resultList.stream().distinct().collect(Collectors.toList());
		return resultList2;
	}
}
