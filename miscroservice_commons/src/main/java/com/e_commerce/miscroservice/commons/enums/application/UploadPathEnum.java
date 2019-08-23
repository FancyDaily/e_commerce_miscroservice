package com.e_commerce.miscroservice.commons.enums.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 功能描述: oss上传路径枚举类
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2018年11月19日 下午4:58:50
 * ************************************
 * ************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
@Component
public class UploadPathEnum {
	/*
	 * 读取配置文件
	 */

	static private String SAVEPATH_SERVICE;

	static private String SAVEPATH_IDCARD;

	static private String SAVEPATH_PERSON;

	static private String CSQ_SAVEPATH_SERVICE = "csq/fund";

	static private String CSQ_SAVEPATH_FUND = "csq/person";

	static private String CSQ_SAVEPATH_PERSON = "csq/service";

	static private String CSQ_SAVEPATH_CERTIFICATE = "csq/certificate";

	static private String CSQ_SAVEPATH_OPENIDMATCHER = "csq/openid_matcher";

	@Value("${savepath.service}")
	public void setSAVEPATH_SERVICE(String sAVEPATH_SERVICE) {
		SAVEPATH_SERVICE = sAVEPATH_SERVICE;
	}

	@Value("${savepath.idcard}")
	public void setSAVEPATH_IDCARD(String sAVEPATH_IDCARD) {
		SAVEPATH_IDCARD = sAVEPATH_IDCARD;
	}

	@Value("${savepath.person}")
	public void setSAVEPATH_PERSON(String sAVEPATH_PERSON) {
		SAVEPATH_PERSON = sAVEPATH_PERSON;
	}

	@Value("${savepath.csq.fund}")
	public void setCsqSavepathFund(String value) {
		CSQ_SAVEPATH_FUND = value == null ? CSQ_SAVEPATH_FUND : value;
	}

	@Value("${savepath.csq.service}")
	public void setCsqSavepathService(String value) {
		CSQ_SAVEPATH_SERVICE = value == null ? CSQ_SAVEPATH_SERVICE : value;
	}

	@Value("${savepath.csq.person}")
	public void setCsqSavepathPerson(String value) {
		CSQ_SAVEPATH_PERSON = value == null ? CSQ_SAVEPATH_PERSON : value;
	}

	@Value("${savepath.csq.certificate}")
	public static void setCsqSavepathCertificate(String csqSavepathCertificate) {
		CSQ_SAVEPATH_CERTIFICATE = csqSavepathCertificate == null ? CSQ_SAVEPATH_CERTIFICATE : csqSavepathCertificate;
	}

	@Value("${savepath.csq.openidmatcher}")
	public static void setCsqSavepathOpenidmatcher(String csqSavepathOpenidmatcher) {
		CSQ_SAVEPATH_OPENIDMATCHER = csqSavepathOpenidmatcher == null ? CSQ_SAVEPATH_OPENIDMATCHER : csqSavepathOpenidmatcher;
	}

	public enum innerEnum {

		SERVICE(1, SAVEPATH_SERVICE), IDCARD(2, SAVEPATH_IDCARD), PERSON(3, SAVEPATH_PERSON),
		CSQ_FUND(0, CSQ_SAVEPATH_FUND), CSQ_SERVICE(1, CSQ_SAVEPATH_SERVICE),
		CSQ_PERSON(2, CSQ_SAVEPATH_PERSON), CSQ_CERTIFICATE(3, CSQ_SAVEPATH_CERTIFICATE), CSQ_OPENIDMATCHER(4, CSQ_SAVEPATH_OPENIDMATCHER);

		private int code;
		private String path;

		public int getCode() {
			return code;
		}

		public String getPath() {
			return path;
		}

		private innerEnum(int code, String path) {
			this.code = code;
			this.path = path;
		}

	}
}
