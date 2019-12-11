package com.e_commerce.miscroservice.sdx_proj.service;

import java.util.List;
import java.util.Map;

public interface SdxCommonsService {
	//查询好友动态以及动态下面的评论
	List<Map<String, Object>> findFriendTrends(long userId);

	//查询跟我看过同样书的人发的动态
	Object findFriendTrendsByUserId(Long userId);
}
