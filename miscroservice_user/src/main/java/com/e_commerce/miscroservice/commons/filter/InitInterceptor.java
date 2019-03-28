package com.e_commerce.miscroservice.commons.filter;

import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.JsonUtil;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.*;


@Component
public class InitInterceptor extends HandlerInterceptorAdapter {
	Log logger = Log.getInstance(InitInterceptor.class);

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	RedisUtil redisUtil;

	@Value("${appExcludeMethod}")
	private String appExcludeMethod;

	/**
	 * 在执行controller方法之前进行请求参数处理
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (handler instanceof HandlerMethod) {
			HandlerMethod h = (HandlerMethod) handler;
			Date dateTime = Calendar.getInstance().getTime();
			request.setAttribute("startTime", dateTime.getTime());
			// 对下列方法进行放行
			if (appExcludeMethod.contains(h.getMethod().getName())) {
				logger.info("--------------------方法放行--" + h.getMethod().getName() + "---------------");
				return true;
			} else {
				String token = null;String wholeStr = "";
				if ("application/json".equals(request.getContentType())) {
					BufferedReader br = request.getReader();
					String str;
					//标记
					while((str = br.readLine()) != null){
					         wholeStr += str;
					}
					request.setAttribute("jsonParam", wholeStr);
					Map<String, Object> parseFromJson = JsonUtil.parseFromJson(wholeStr, HashMap.class);
					token = (String)parseFromJson.get("token");
				} else {
					token = request.getParameter("token");
				}
				if (StringUtil.isEmpty(token) || !redisUtil.hasKey(token)) {
					PrintWriter writer = null;
					response.setCharacterEncoding("UTF-8");
					response.setContentType("text/html; charset=utf-8");
					try {
						writer = response.getWriter();
						AjaxResult result = new AjaxResult();
						result.setSuccess(true);
						result.setErrorCode("90001");
						result.setMsg("用户未登录");
						writer.print(JsonUtil.toJson(result));
						logger.info("--------------------用户非法登录-----------------");
						return false;
					} catch (Exception e) {
						logger.error("拦截器错误：" + e.toString());
					}
					return false;
				} else {
					//刷新token时间 为一天
					Object obj = redisUtil.get(token);
					long t1 = Calendar.getInstance().getTimeInMillis();
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, 1);
					long t2 = cal.getTimeInMillis();
					// 更新用户过期时间
					redisUtil.set(token, obj, (t2 - t1) / 1000L);
					logger.info("--------------------token：" + token + " 刷新-----------------");
					request.setAttribute("paramString", wholeStr);
				}
			}
		}
		return true;
	}

	/**
	 * 调用controller方法之后执行日志记录
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		/*
		 * if (handler instanceof HandlerMethod) { long startTime = (Long)
		 * request.getAttribute("startTime");; Date dateTime =
		 * Calendar.getInstance().getTime(); long endTime = dateTime.getTime(); long
		 * executeTime = endTime - startTime; StringBuilder sb = new
		 * StringBuilder(1000);
		 * sb.append("耗时  : ").append(executeTime).append("ms").append("\n"); sb.append(
		 * "-------------------------------------------------------------------------------"
		 * ); logger.info(sb.toString()); }
		 */
	}

	/**
	 * 把map类型参数转为key,value型参数
	 * @param request
	 * @return
	 */
	protected Map<String, Object> getParams(HttpServletRequest request) {
		Enumeration<String> names = request.getParameterNames();
		Map<String, Object> params = new HashMap<>();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			params.put(name, request.getParameter(name));
		}
		return params;
	}

}
