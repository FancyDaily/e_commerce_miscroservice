package com.e_commerce.miscroservice.order.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.commons.util.colligate.SnowflakeIdWorker;
import com.e_commerce.miscroservice.message.controller.MessageCommonController;
import com.e_commerce.miscroservice.order.dao.OrderDao;
import com.e_commerce.miscroservice.product.dao.ProductDao;
import com.e_commerce.miscroservice.product.dao.ProductDescDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @author 马晓晨
 * @date 2019/3/4
 */
public class BaseService {
	/**
	 * 有效
	 */
	protected String IS_VALID_YES = "1";
	/**
	 * 无效
	 */
	protected String IS_VALID_NO = "0";
	/**
	 * 是封面图片
	 */
	protected String IS_COVER_YES = "1";
	/**
	 * 是组织账户
	 */
	protected Integer IS_COMPANY_ACCOUNT_YES = 1;
	/**
	 * 用户类型为公益组织
	 */
	protected String USER_TYPE_ORGANIZATION = "2";

	@Autowired
	protected OrderServiceImpl orderServiceImpl;

	@Autowired
	protected RedisUtil redisUtil;

	@Autowired
	protected MessageCommonController messageCommonController;


	protected Log logger = Log.getInstance(BaseService.class);

	@Autowired
	protected ProductDao productDao;
	@Autowired
	protected ProductDescDao productDescribeDao;
	@Autowired
	protected OrderDao orderDao;




	/**
	 * 赋值servcieDesc公共字段
	 * @param user
	 * @param desc
	 */
	protected void setCommonServcieDescField(TUser user, TServiceDescribe desc) {
		long currentTime = System.currentTimeMillis();
		desc.setCreateUser(user.getId());
		desc.setCreateUserName(user.getName());
		desc.setCreateTime(currentTime);
		desc.setUpdateUser(user.getId());
		desc.setUpdateUserName(user.getName());
		desc.setUpdateTime(currentTime);
		desc.setIsValid(IS_VALID_YES);
	}

	/**
	 * 赋值service的公共字段
	 *
	 * @param user 当前用户
	 * @param service 发布的服务求助
	 */
	protected void setServiceCommonField(TUser user, TService service) {
		long currentTime = System.currentTimeMillis();
		service.setCreateUser(user.getId());
		service.setCreateUserName(user.getName());
		service.setCreateTime(currentTime);
		service.setUpdateUser(user.getId());
		service.setUpdateUserName(user.getName());
		service.setUpdateTime(currentTime);
		service.setIsValid(IS_VALID_YES);
	}




	/**
	 *
	 * 功能描述:输出错误消息
	 * 作者:马晓晨
	 * 创建时间:2018年12月9日 下午2:26:43
	 * @param e
	 * @return
	 */
	public String errInfo(Exception e) {
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			// 将出错的栈信息输出到printWriter中
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (pw != null) {
				pw.close();
			}
		}
		return sw.toString();
	}


	/**
	 * 将正常字符转换为Unicode
	 * @param source
	 * @return
	 */
	public String unicode(String source) {
		StringBuffer sb = new StringBuffer();
		char[] source_char = source.toCharArray();
		String unicode = null;
		for (int i = 0; i < source_char.length; i++) {
			unicode = Integer.toHexString(source_char[i]);
			if (unicode.length() <= 2) {
				unicode = "00" + unicode;
			}
			sb.append("\\u" + unicode);
		}
		return sb.toString();
	}

	/**
	 * 将Unicode编码转换为正常字符
	 * @param unicode
	 * @return
	 */
	public String decodeUnicode(String unicode) {
		if (unicode.isEmpty()) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer();
			String[] hex = unicode.split("\\\\u");
			for (int i = 1; i < hex.length; i++) {
				int data = Integer.parseInt(hex[i], 16);
				sb.append((char) data);
			}
			return sb.toString();
		}
	}

	/**
	 * 功能描述: 将unicode转换成正常字符，包含html转码
	 * 作者: 许方毅
	 * 创建时间: 2018年11月12日 下午3:48:57
	 * @param str
	 * @return
	 */
	public String htmlDeUnicode(String str) {
		return HtmlUtils.htmlUnescape(decodeUnicode(str));
	}

	/**
	 * 功能描述: 默认用户redis有效时间
	 * 作者: 许方毅
	 * 创建时间: 2018年11月13日 下午3:04:18
	 * @return
	 */
	public Long getUserTokenInterval() {
		// Generate token for user
		Long t1 = Calendar.getInstance().getTimeInMillis();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		Long t2 = cal.getTimeInMillis() - t1;
		return t2 / 1000;
	}
	/**
	 *
	 * 功能描述:分钟数转为*小时*分钟 显示
	 * 作者:姜修弘
	 * 创建时间:2018年11月22日 下午2:46:28
	 * @param time
	 * @return
	 */
	public String timeChange(Long time) {
		int hour = (int) (time / 60) ;
		int minute = (int) (time % 60) ;
		String showTime = hour+"小时"+minute+"分钟";
		if (hour == 0) {
			showTime = minute+"分钟";
		}
		if (minute == 0) {
			showTime = hour+"小时";
		}
		return showTime;
	}

	/**
	 *
	 * 功能描述:修改时间为yyyy-MM-dd HH:mm
	 * 作者:姜修弘
	 * 创建时间:2019年1月15日 下午1:59:21
	 * @param time
	 * @return
	 */
	public String changeTime(Long time) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String startTime = simpleDateFormat.format(time);
		return startTime;
	}
	/**
	 *
	 * 功能描述:修改时间为汉字的日期 yyyy年MM月dd日 HH:mm
	 * 作者:姜修弘
	 * 创建时间:2019年1月25日 上午10:31:47
	 * @param time
	 * @return
	 */
	public String changeTimeToChian(Long time) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		String startTime = simpleDateFormat.format(time);
		return startTime;
	}
	/**
	 *
	 * 功能描述:修改时间为时分 HH:mm
	 * 作者:姜修弘
	 * 创建时间:2019年1月25日 上午10:32:24
	 * @param time
	 * @return
	 */
	public String changeTimeToHour(Long time) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		String startTime = simpleDateFormat.format(time);
		return startTime;
	}



	/**
	 *
	 * 功能描述:修改时间为yyyy-MM
	 * 作者:姜修弘
	 * 创建时间:2019年1月18日 下午12:04:56
	 * @param time
	 * @return
	 */
	public String changeTimeForTimeRecord(Long time) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
		String startTime = simpleDateFormat.format(time);
		return startTime;
	}

	/**
	 *
	 * 功能描述:修改时间为年份
	 * 作者:姜修弘
	 * 创建时间:2019年1月18日 下午12:04:40
	 * @param time
	 * @return
	 */
	public String changeTimeToYear(Long time) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
		String startTime = simpleDateFormat.format(time);
		return startTime;
	}

	/**
	 *
	 * 功能描述:判断一个人是否微信授权，是否有可用的formid
	 * 作者:姜修弘
	 * 创建时间:2018年12月5日 下午6:30:21
	 * @param nowTime
	 * @param toTUser
	 * @return
	 */
	protected TFormid findFormId(long nowTime, TUser toTUser) {
		TFormid formid = null;
		long formIdTime = nowTime - 7 * 24 * 60 * 60 * 1000;
		if (toTUser.getVxOpenId() != null) {
			//查出所以七天内有效的fomid
			formid = messageCommonController.selectCanUseFormId(formIdTime , toTUser.getId());
		}
		return formid;
	}

	/**
	 *
	 * 功能描述:修改服务通知地址
	 * 作者:姜修弘
	 * 创建时间:2019年1月23日 下午4:38:38
	 * @param address
	 * @return
	 */
	public String changeAddress(String address) {
		if (address.isEmpty()) {
			return "线上";
		}
		return address.replace("&" , "");
	}

}
