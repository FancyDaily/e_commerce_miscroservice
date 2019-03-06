package com.e_commerce.miscroservice.commons.constant.colligate;

import org.bouncycastle.asn1.crmf.PKIPublicationInfo;

/**
 * 功能描述:应用系统相关的常量 模块: 项目: 版本号: 部门:技术研发部 公司:浙江晓时信息技术有限公司 作者:丁卫江
 * 邮箱:1664315156@qq.com 创建时间:Sep 11, 2017 2:04:40 PM
 *************************************
 *************************************
 * 修改人: 修改时间: 修改内容: 1. 2.
 */

public interface AppConstant {

	/**
	 * 用户token信息
	 */
	public String USER_TOKEN = "token";

	public String VALID_LOGIN = "loginMethod";

	/**
	 * 获取公钥的key
	 */
	public String PUBLIC_KEY = "RSAPublicKey";

	/**
	 * 获取私钥的key
	 */
	public String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * 签名密钥
	 */
	public String APP_KEY = "appkey";

	/**
	 * 成功
	 */
	public String APP_SUCCESS = "success";

	/**
	 * 剔除的方法
	 */

	public String APP_EXCLUDE_METHOD = "excludeMethod";

	/**
	 * 渠道标志
	 */
	public String APP_CHANNEL = "channel";

	/**
	 * 产品代码
	 */
	public String APP_APICODE = "apiCode";

	/**
	 * 商户号
	 */
	public String APP_PARTNER = "partner";

	public String APP_TRADENO = "tradeNo";

	/**
	 * 外部交易流水
	 */
	public String APP_OUT_TRADENO = "outTradeNo";

	/**
	 * 外部交易流水
	 */
	public String APP_INNNER_TRADENO = "innerTradeNo";

	/**
	 * 加密信息
	 */
	public String APP_ENCODESTR = "encodestr";

	/**
	 * 取用户参数对象
	 */
	public String APP_PARAMS = "AppParams";

	/**
	 * POST请求
	 */
	public String APP_POST = "post";

	/**
	 * GET请求
	 */
	public String APP_GET = "get";

	/**
	 * SIGN签名
	 */
	public String APP_SIGN = "sign";

	/**
	 * APP 地址
	 */
	public static String APP_URL = "http://localhost:8080/timebank/";

	public static String VALID_CODE = "validCode";

	public static String TOKEN_KEY = "timebank";

	public static String REDIS_USER = "redis_user";

	/**
	 * 短信模板
	 */
	public static String SMS_TEMPLATE = "sms_template";

	public static long SMS_EXPIRED = 600;

	/**
	 * 短信发送间隔毫秒数
	 */
	public static long SMS_INTERVAL_MILLIS = 60000;

	/**
	 * 以http开头
	 */
	public static String STARTS_WITH_HTTP = "http";

	/**
	 * 图片前缀
	 */
	public static String IMG_PREFIX = "imgPrefix";

	/**
	 * 图片后缀
	 */
	public static String IMG_SUFFIX_JPEG = ".jpeg";

	/**
	 * 调试状态：测试
	 */
	public static String DEBUG_STATUS_TRUE = "true";

	/**
	 * 调试状态：生产
	 */
	public static String DEBUG_STATUS_FALSE = "false";

	/**
	 * redis过期时间
	 */
	public static String REDIS_SCORE = "redis_score";

	/**
	 * OCR相关
	 */
	public static String OCR_APP_KEY = "app_key";

	public static String OCR_APP_SECRET = "app_secret";

	public static String OCR_HOST = "ocr_host";

	public static String OCR_PATH = "ocr_path";

	public static String OCR_IMAGE_BASE64 = "img_base64";

	public static String OCR_SIDE = "side";

	/**
	 * 注册模版
	 */
	public static String REGISTER_TITLE = "register_title";

	public static String REGISTER_CONTENT = "register_content";

	public static String REGISTER_BONUS = "register_bonus";

	/**
	 * 实名模版
	 */
	public static String CERTUSERSUCCESS_TITLE = "certUserSuccess_title";

	public static String CERTUSERSUCCESS_CONTENT = "certUserSuccess_content";

	public static String CERTUSERSUCCESS_BONUS = "certUserSuccess_bonus";

	public static String CERTUSERFAILED_TITLE = "certUserFailed_title";

	public static String CERTUSERFAILED_CONTENT = "certUserFailed_content";

	/**
	 * 邀请模版
	 */
	public static String INVITE_TITLE = "invite_title";

	public static String INVITE_CONTENT = "invite_content";

	public static String INVITE_BONUS = "invite_bonus";

	/**
	 * 技能模版
	 */
	public static String TECH_TITLE = "tech_title";

	public static String TECH_CONTENT_PREFIX = "tech_content_prefix";

	public static String TECH_CONTENT_SUFFIX = "tech_content_suffix";

	public static String TECH_CONTENT_SUFFIX_FINAL = "tech_content_suffix_final";

	public static String TECH_BONUS = "tech_bonus";

	/**
	 * 配置文件模版选择常量
	 */
	public static String APPLICATION_DEV_PATH = "application-dev.properties";

	public static String APPLICATION_PROD_PATH = "application-prod.properties";

	public static String APPLICATION_PATH = "application.properties";

	/**
	 * 默认头像、默认背景
	 */
	public String DEFAULT_HEADURL = "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png";
	public String DEFAULT_BACKGROUNDPIC = "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_background.png";

	/**
	 * 性别
	 */
	public Integer SEX_UNKNOWN = 0;
	public Integer SEX_MALE = 1;
	public Integer SEX_FEMALE = 2;
	public Integer DEFAULT_SEX = SEX_UNKNOWN;	//未设置
	
	/**
	 * 默认最高学历
	 */
	public String DEFAULT_EDUCATION = "未设置";

	/**
	 * 实名类型
	 */
	public Integer AUTH_TYPE_PERSON = 1;
	public Integer AUTH_TYPE_CORP = 2;
	public Integer DEFAULT_AUTH_TYPE = AUTH_TYPE_PERSON;

	/**
	 * 实名状态
	 */
	public Integer AUTH_STATUS_NO = 1;
	public Integer AUTH_STATUS_YES = 2;
	public Integer DEFAULT_AUTH_STATUS = AUTH_STATUS_NO;
	
	/**
	 * 默认职业
	 */
	public String DEFAULT_OCCUPATION = "无业游民";
	
	/**
	 * 等级（）
	 */
	public Integer LEVEL_ONE = 1;
	public Integer LEVEL_TWO = 2;
	public Integer LEVEL_THREE = 3;
	public Integer LEVEL_FOUR = 4;
	public Integer DEFAULT_LEVEL = LEVEL_ONE;

	/**
	 * 默认昵称
	 */
//	public String DEFAULT_NAME = "晓主" + UUIDGenerator.nextSerial().substring(10, 18);

	/**
	 * 记录有效性
	 */
	public String IS_VALID_YES = "1";
	public String IS_VALID_NO = "0";
	public String IS_VALID_DEFAULT = IS_VALID_YES;

	/**
	 * 消息类型相关常量 TODO
	 */
	/*
	 * 注册
	 */
	public Integer NOTICE_TYPE_RIGESTER = 1;	
	public String NOTICE_CONTENT_RIGESTER = "恭喜您成功注册，感恩与您同行，壹晓时在此赠送您30分钟互助时，您可以在您的时间账户中查看。"; //内容 TODO
	
	/**
	 * 用户履历类型 -> 教育、工作
	 */
	public Integer RECORD_TYPE_EDUCATION = 1;
	public Integer RECORD_TYPE_JOB = 2;
	
	/**
	 * 用户常量
	 */
	public String USER = "user";

	/**
	 * 发布名义(个人、组织)
	 */
	public Integer SERV_TYPE_PERSON = 1;
	public Integer SERV_TYPE_CORP = 2;
	public Integer SERV_AUTH_TYPE = AUTH_TYPE_PERSON;

	/**
	 * 发布类型
	 */
	public Integer PUBLISH_TYPE_SERV = 1;
	public Integer PUBLISH_TYPE_REQR = 2;
	public Integer PUBLISH_TYPE_ACTY = 3;

	/**
	 * 服务求助状态 -> 待审核 
	 */
	public Integer SERV_STATUS_UNAUDITED = 1;
	
	/**
	 * 服务求助状态 -> 已取消 发布者取消需求
	 */
	public Integer SERV_STATUS_MASTERCANCLE = 6;
	
	/**
	 * 服务求助状态 -> 下架（超时） 
	 */
	public Integer SERV_STATUS_OUT_OF_TIME = 7;
	
	/**
	 * 服务求助状态 -> 被取消 服务着取消服务（下架不可见【已重新发布的】）
	 */
	public Integer SERV_STATUS_GUESTCANCLE = 8;
	
	/**
	 * 服务求助状态 -> 需求被系统拒绝审核
	 */
	public Integer SERV_STATUS_FAILPASS = 9;
	
	/**
	 * 服务求助状态 -> 可报名(没人报名)
	 */
	public Integer SERV_STATUS_AVALIBLE_FOR_JOIN = 2;
	
	/**
	 * 服务求助状态 -> 可报名(有人报名还可以报名)
	 */
	public Integer SERV_STATUS_JOIN_UNDONE = 3;
	
	/**
	 * 服务求助状态 -> 上架中-进行中
	 */
	public Integer SERV_STATUS_PROCESSING = 4;
	
	/**
	 * 服务求助状态 -> 下架-结束
	 */
	public Integer SERV_STATUS_DONE = 5;
	

	/**
	 * 时间轨迹筛选类别 -> all、in、out
	 */
	public String PAYMENTS_OPTION_ALL = "0"; 
	public String PAYMENTS_OPTION_IN = "1";
	public String PAYMENTS_OPTION_OUT = "2";

	/**
	 * 收取类型 -> 支出
	 */
	public Integer PAY_TYPE_OUT = 1;

	/**
	 * 服务 -> 1对1需求
	 */
	public Integer SERV_TYPE_P2PREQURIEMENT = 1;

	/**
	 * 服务 -> 1对多需求
	 */
	public Integer SERV_TYPE_P2MREQURIEMENT = 2;
	
	/**
	 * 服务 -> 1对1服务
	 */
	public Integer SERV_TYPE_P2PSERVICE = 3;
	
	/**
	 * 服务 -> 1对多服务
	 */
	public Integer SERV_TYPE_P2MSERVICE = 4;
	
	/**
	 * 服务 -> 活动
	 */
	public Integer SERV_TYPE_IN_ACTY = 5;

	/**
	 * 服务状态 -> 未完结
	 */
	public Integer SERV_STATUS_UNDONE = 9;

	/**
	 * 时间账单 -> 收入
	 */
	public Integer TIME_TYPE_IN = 1;

	/**
	 * 时间账单 -> 支出
	 */
	public Integer TIME_TYPE_OUT = 2;

	/**
	 * 单位认证进度 -> 未认证（提交后审核中）
	 */
	public Integer CORP_CERT_STATUS_NOT_YET = 0;

	/**
	 * 单位认证进度 ->	 认证通过
	 */
	public Integer CORP_CERT_STATUS_YES = 1;
	
	/**
	 * 单位认证进度 -> 认证不通过
	 */
	public Integer CORP_CERT_STATUS_NO = 2;

	/**
	 * 当需求刚刚发布（未有人报名）时的状态	//TODO 相关接口：冻结明细
	 */
	public Integer SERV_DEFAULT_STAUS = 0;
	
	/**
	 * 最后签到日:昨天、今天、其他日期
	 */
	public String LAST_SIGN_UP_DAY_YESTERDAY = "0";
	
	public String LAST_SIGN_UP_DAY_TODAY = "1";
	
	public String LAST_SIGN_UP_DAY_OTHERS = "-1";
	
	public String SIGN_UP_ALMOST_EDGE = "6";
	
	public String SIGN_UP_EDGE = "7";

	/**
	 * 签到奖励 -> 普通
	 */
	public long SIGN_UP_BONUS = 3;
	
	public long LEVEL_ONE_MIN = 0;
	
	public long LEVEL_ONE_MAX = 15;
	
	public long LEVEL_TWO_MIN = 15;
	
	public long LEVEL_TWO_MAX = 70;
	
	public long LEVEL_THREE_MIN = 70;
	
	public long LEVEL_THREE_MAX = 150;
	
	public long LEVEL_FOUR_MIN = 150;
	
	public long lEVEL_FOUR_MAX = 300;
	
	public long LEVEL_FIVE_MIN = 300;
	
	public long LEVEL_FIVE_MAX = 500;
	
	public long LEVEL_SIX_MIN = 500;
	
	public long LEVEL_SIX_MAX = 800;
	
	public long LEVEL_SEVEN_MIN = 800;

	/**
	 * 生日成长值加倍
	 */
	public Integer GROWTH_VALUE_RATE_BIRTHDAY = 2;	//TODO

	/**
	 * REPORT TYPE -> 用户反馈
	 */
	public Integer REPORT_TYPE_FEED_BACK = 2;
	
	/**
	 * 生日类型的target id，对应t_publish表json串相应的id
	 */
	public Long TARGET_ID_INTEREST_BIRTHDAY = 102l;

	/**
	 * 普通用户权限
	 */
	public Integer JURISDICTION_NORMAL = 0;

	/**
	 * 微信授权状态
	 */
	public Integer ACCREDIT_STATIS_NO = 0;
	
	public Integer ACCREDIT_STATUS_YES = 1;
	
	public Integer ACCREDIT_STATUS_DEFAULT = ACCREDIT_STATIS_NO;	//默认授权状态

	/**
	 * 组织角色 -> 成员、管理员
	 */
	public Integer CORP_ROLE_MEMBER = 1;
	public Integer CORP_ROLE_ADMIN = 2;

	/**
	 * 个人信息完整度任务 -> 80(百分比) 
	 */
	public Integer COMPLETE_TASK_NUM = 80;

	/**
	 * 未知的创建者
	 */
	public String CREATE_USER_NAME_UNKNOWN = "未知用户名";

	/**
	 * 短信发送次数限制（24小时）
	 */
	public int SMS_SEND_LIMIT = 10;

	/**
	 * METAL
	 */
	public String METAL = "metal";

	/**
	 * 任务target_id  ->  TASK_REMARK
	 */
	public long TARGET_ID_TASK_RIGESTER = 100l;
	
	public Long TARGET_ID_TASK_AUTH = 101l;
	
	public Long TARGET_ID_TASK_PAGE = 102l;
	
	public Long TARGET_ID_SIGN_UP = 103l;
	
	public long TARGET_ID_INVITE = 104l;
	
	public long TARGET_ID_FIRST_HELP = 105l;

	public String NOTICE_TITLE_RIGESTER = "注册成功";
	
	/**
	 * 系统级别的用户 -> id
	 */
	public long SYSTEM_CREATER = 1l;
	
	/**
	 * 系统级别的用户 -> name
	 */
	public String SYSTEM_CREATER_NAME = "兢兢业业的晓时时间银行开发人员";

	
	public Integer DEFAULT_SYS_MSG_TYPE = 1;

	public int MEDAL_TYPE_INVITER = 2;	//邀请类勋章对应枚举类型

	/**
	 * 达人标记
	 */
	public Integer MASTER_STATUS_NO = 0;
	public Integer MASTER_STATUS_YES = 1;
	public Integer MASTER_STATUS_DEFAULT = MASTER_STATUS_NO;

	/**
	 * 微信基本信息授权状态
	 */
	public Integer WECHAT_BASIC_AUTH_STATUS_NO = 0;
	public Integer WECHAT_BASIC_AUTH_STATUS_YES = 1;
	public Integer WECHAT_BASIC_AUTH_STATUS_DEFAULT = WECHAT_BASIC_AUTH_STATUS_NO;

	/**
	 * 是否编辑过基本信息
	 */
	public Integer BASIC_INFO_NOT_MODIFY = 0;
	public Integer BASIC_INFO_ALREADY_MODIFY = 1;
	public Integer BASIC_INFO_DEFAULT_MODIFY = BASIC_INFO_NOT_MODIFY;

	/**
	 * 关注状态
	 */
	public Integer IS_ATTEN_NO = 0;
	public Integer IS_ATTEN_YES = 1;
	
	/**
	 * redis - key前缀
	 */
	public String REDIS_PREFIX_COMPANY = "t_company_";
	public String REDIS_PREFIX_EVALUATE = "t_evaluate_";
	public String REDIS_PREFIX_FORMID = "t_formid_";
	public String REDIS_PREFIX_IDENTIFY_IMAGE = "t_identify_image_";
	public String REDIS_PREFIX_LABEL = "t_label_";
	public String REDIS_PREFIX_MESSAGE = "t_message_";
	public String REDIS_PREFIX_MESSAGE_NOTICE = "t_message_notice_";
	public String REDIS_PREFIX_PUBLISH = "t_publish_";
	public String REDIS_PREFIX_REPORT = "t_report_";
	public String REDIS_PREFIX_SERVICE = "t_service_";
	public String REDIS_PREFIX_SERVICE_DESCRIBE = "t_service_describe_";
	public String REDIS_PREFIX_SERVICE_RECEIPT = "t_service_receipt_";
	public String REDIS_PREFIX_TYPE_DICTIONARIES = "t_type_dictionaries_";
	public String REDIS_PREFIX_TYPE_RECORD = "t_type_record_";
	public String REDIS_PREFIX_USER = "t_user_";
	public String REDIS_PREFIX_USER_AUTH = "t_user_auth_";
	public String REDIS_PREFIX_USER_COLLECTION = "t_user_collection_";
	public String REDIS_PREFIX_USER_COMPANY = "t_user_company_";
	public String REDIS_PREFIX_USER_FOLLOW = "t_user_follow_";
	public String REDIS_PREFIX_USER_FREEZE = "t_user_freeze_";
	public String REDIS_PREFIX_USER_RECORD = "t_user_record_";
	public String REDIS_PREFIX_USER_TIME_RECORD = "t_user_time_record_";
	
	/**
	 * 建议处理进度
	 */
	public Integer HANDLE_REPORT_NO = 0;
	public Integer HANDLE_REPORT_YES = 1;

	/**
	 * 默认的可用状态
	 */
	public String AVALIABLE_STATUS_AVALIABLE = "1";
	public String AVALIABLE_STATUS_NOT_AVALIABLE = "0";

	public String AES_KEY = "xiaoshitimebank!xiaoshitimebank!xiaoshitimebank!xiaoshitimebank!xiaoshitimebank!xiaoshitimebank!xiaoshitimebank!xiaoshitimebank!";

	/**
	 * 最高职位：公司的创建者
	 */
	public Integer JOB_COMPANY_CREATER = 0;
	
	/**
	 * 职位：普通成员
	 */
	public Integer JOB_COMPANY_MEMBER = 1;

	/**
	 * 服务类型-求助
	 */
	public Integer SERV_TYPE_HELP = 1;
	
	/**
	 * 服务类型-服务
	 */
	public Integer SERV_TYPE_SERV = 2;
	
	/**
	 * 服务来源-个人
	 */
	public Integer SERV_SOURCE_PERSON = 1;
	
	/**
	 * 服务来源-组织
	 */
	public Integer SERV_SOURCE_COMPANY = 2;

	
	/**
	 * publish表-main_key
	 */
	public String MAIN_KEY_CATEGORY = "category";

	/**
	 * 为假用户
	 */
	public Integer IS_FAKE_YES = 1;

	/**
	 * 分组权限（类型）=> 默认分组
	 */
	public Integer GROUP_AUTH_DEFAULT = 0;
	
	/**
	 * 分组权限（类型）=> 用户创建分组
	 */
	public Integer GROUP_AUTH_CREATED = 1;

	/**
	 * 默认用户组织内昵称
	 */
	public String NICKNAME_COMPANY_DEFAULT = "未设置";
	
	public Integer JOIN_STATE_COMPANY_NOT_YET = 1;
	public Integer JOIN_STATE_COMPANY_PASS = 2;
	public Integer JOIN_STATE_COMPANY_REFUSE = 3;

	/**
	 * 组织账号 
	 */
	public Integer IS_COMPANY_ACCOUNT_YES = 1;
	/**
	 * 普通账号
	 */
	public Integer IS_COMPANY_ACCOUNT_NO = 2;

	/**
	 * 真实用户
	 */
	public Integer IS_FAKE_NO = 0;

	public String PUBLIC_RESOURCE_BUCKET = "timebank-public-resource";

	public String IS_ATTEN_THE_SAME_YES = "1";
	
	public String IS_ATTEN_THE_SAME_NO = "0";

	/**
	 * 服务有效期(包含当日)
	 */
	public Integer SERVCIE_VALIDITY_TERM = 7;

	/**
	 * 可重复周期	//TODO如名称有改动应当同步修改
	 */
	public String[] WEEK_DAY_STRS = {"周一","周二","周三","周四","周五","周六","周日"};

	/**
	 * 已报名的状态数组
	 */
	public int[] SERV_REIPT_NOT_AVAILABLE_ARRAY = {2,7,8,9,10,11,12};

	/**
	 * 状态 - 待领取 
	 */
	public Integer BONUS_PACKAGE_NOT_DONE = 1;
	
	/**
	 * 状态 - 已领取 
	 */
	public Integer BONUS_PACKAGE_DONE = 2;
	
	/**
	 * 状态 - 被退回
	 */
	public Integer BONUS_PACKAGE_SEND_BACK = 3;
	
}
