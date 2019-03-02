package com.e_commerce.miscroservice.commons.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.DistributionGradeEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 分销系统 等级 添加自身，添加下级，修改等级，查找
 * @author hyf
 * @version V1.0
 * @date 2018/10/11 16:53
 * @Copyright 玖远网络
 */
@Component
public class DistributionSystemUtil {
    private Log logger = Log.getInstance(DistributionSystemUtil.class);


    @Autowired
    @Qualifier("userJsonObjHashRedisTemplate")
    private ValueOperations<String, JSONObject> distributionRedis;
    /**
     * 上级，上上级，合伙人
     */
    private static String DISTRIBUTION_GRADE_PARTNER_USERID="distribution:grade:%s";
    private static final  String PARTNER = "partner";
    private static final  String DISTRIBUTOR = "distributor";
    private static final  String HIGHER = "higher";
    private static final  String TOP = "top";
    private static final  String GRADE = "grade";
    private static final  String SON_LIST = "sonList";

    public JSONObject find(Long userId) {

        JSONObject jasonObject = distributionRedis.get(String.format(DISTRIBUTION_GRADE_PARTNER_USERID,userId));
        if (jasonObject==null){
            logger.warn("不存在分销信息");
            return null;
        }
        return jasonObject;
    }

    /**
     * 添加上级
     *
     * @param userId
     * @param upUserId
     * @return
     */
    public Response add(Long userId, Long upUserId) {
        logger.info("分销体系---userId={}添加上级upUserI={}",userId,upUserId);
        if (userId.equals(upUserId)){
            logger.warn("自己不能添加为自己的上级");
            return Response.errorMsg("自己不能添加为自己的上级");
        }
        //查找 该用户的 上级关系
        JSONObject jasonObject = distributionRedis.get(String.format(DISTRIBUTION_GRADE_PARTNER_USERID,userId));
        Long higher = null;
        if (jasonObject!=null){
            higher = jasonObject.getLong(HIGHER);
        }

        if (higher!=null){
            logger.warn("分销体系---上级已存在");
            return Response.errorMsg("上级已存在 不可重复添加");
        }
        JSONObject upJsonObj = distributionRedis.get(String.format(DISTRIBUTION_GRADE_PARTNER_USERID,upUserId));
        //若其上级 第一次添加
        if (upJsonObj==null){
            logger.warn("分销体系---添加的上级不存在");
            return Response.errorMsg("上级不存在");
        }
        jasonObject=new JSONObject();
        //设置上级
        jasonObject.put(HIGHER,upUserId);
        //添加我的上级的下级集合
        JSONArray jsonArray = upJsonObj.getJSONArray(SON_LIST);
        if (jsonArray!=null&&jsonArray.size()>0){
            jsonArray.add(userId);
        }else {
            jsonArray = new JSONArray();
            jsonArray.add(userId);
        }
        upJsonObj.put(SON_LIST,jsonArray);
        distributionRedis.set(String.format(DISTRIBUTION_GRADE_PARTNER_USERID,upUserId),upJsonObj);

        //获取合伙人id
        Long partnerId = upJsonObj.getLong(PARTNER);
        logger.info("分销体系---设置分销商partnerId={}",partnerId);
        if (partnerId!=null){
            logger.info("分销体系---设置合伙人");
            jasonObject.put(PARTNER,partnerId);
        }
        //获取分销商id
        Long distributorId = upJsonObj.getLong(DISTRIBUTOR);
        logger.info("分销体系---设置分销商distributorId={}",distributorId);
        if (distributorId!=null){
            logger.info("分销体系---设置分销商");
            jasonObject.put(DISTRIBUTOR,distributorId);
        }
        //查找上上级
        Long higher2 =  upJsonObj.getLong(HIGHER);
        if (higher2!=null){
            jasonObject.put(TOP,higher2);
        }

        //设置自己的默认等级
        jasonObject.put(GRADE, DistributionGradeEnum.COMMON.getCode());
        distributionRedis.set(String.format(DISTRIBUTION_GRADE_PARTNER_USERID,userId),jasonObject);
        return Response.success(jasonObject);
    }

    public Response update(Long userId, Integer grade) {
        JSONObject jsonObject = distributionRedis.get(String.format(DISTRIBUTION_GRADE_PARTNER_USERID,userId));
        Integer  originalGrade = jsonObject.getInteger(GRADE);
        JSONArray jsonArray = jsonObject.getJSONArray(SON_LIST);
        if (originalGrade.equals(grade)){
            logger.warn("等级相同无需改动");
            return Response.errorMsg("等级相同无需改动");
        }
        if (originalGrade<grade){
            logger.info("用户userId={}晋升grade={}",userId,grade);
            //若晋升为分销商  则 脱离原有的分销商体系  修改自己为上级分销商
            if (grade.equals(DistributionGradeEnum.DISTRIBUTOR.getCode())){
                jsonObject.put(DISTRIBUTOR,userId);
            }
            //若晋升为合伙人  则 脱离原有的合伙人体系  修改自己为上级分销商 修改自己为上级合伙人
            if (grade.equals( DistributionGradeEnum.PARTNER.getCode())){
                jsonObject.put(DISTRIBUTOR,userId);
                jsonObject.put(PARTNER,userId);
            }
            if (grade.equals(DistributionGradeEnum.STORE.getCode())){
                jsonObject.put(GRADE,grade);
            }
            //若其等级 为分销商或 合伙人 则更新 其下级的
            if (grade>= DistributionGradeEnum.DISTRIBUTOR.getCode()){
                jsonObject.put(GRADE,grade);
                if (jsonArray!=null&&jsonArray.size()>0){
                    upSunInformation(userId,userId,jsonArray,grade);
                }
            }
        }else {
            logger.info("用户userId={}降级grade={}",userId,grade);
            //若原等级为 分销商
            if (originalGrade.equals( DistributionGradeEnum.DISTRIBUTOR.getCode())){
                jsonObject.put(GRADE,grade);
                //获取上级分销商
                Long getDistributor = getDistributorId(jsonObject.getLong(HIGHER));
                jsonObject.put(DISTRIBUTOR,getDistributor);
                if (jsonArray!=null&&jsonArray.size()>0){
                    upSunInformation(getDistributor,userId,jsonArray, DistributionGradeEnum.DISTRIBUTOR.getCode());
                }
            }
            //若原等级为  合伙人
            if (originalGrade.equals( DistributionGradeEnum.PARTNER.getCode())){
                jsonObject.put(GRADE,grade);
                //获取上级合伙人
                Long getPartner = getPartnerId(jsonObject.getLong(HIGHER));
                //获取上级分销商
                Long getDistributor = getDistributorId(jsonObject.getLong(HIGHER));
                jsonObject.put(DISTRIBUTOR,getDistributor);
                jsonObject.put(PARTNER,getPartner);
                if (jsonArray!=null&&jsonArray.size()>0){
                    upSunInformation(getPartner,userId,jsonArray, DistributionGradeEnum.PARTNER.getCode());
                }

            }
        }
        //更新等级 分销商 合伙人
        distributionRedis.set(String.format(DISTRIBUTION_GRADE_PARTNER_USERID,userId),jsonObject);
        return Response.success();
    }

    /**
     * 查询子集 修改
     * @param userId 升级：升级的id|降级 上级 id
     * @param userId2 升级：升级的id | 降级：降级的id
     * @param jsonArray 子集
     * @param grade 升级：升级的等级 |　降级：原等级
     */
    public void upSunInformation( Long userId,  Long userId2, JSONArray jsonArray, Integer grade){
        logger.info("分销体系---修改userId={}下级的管理分佣关系",userId);
        for (int i=0;i<jsonArray.size();i++){
            //下级的分销数据
            Long sunId = jsonArray.getLong(i);
            JSONObject json = distributionRedis.get(String.format(DISTRIBUTION_GRADE_PARTNER_USERID,sunId));
            //当他的下级为合伙人以下时才进行修改
            if (json!=null){
                Integer change = 0;
                //若上级为合伙人 且 下级为店长及以下时
                //合伙人和分销商为同一个人
                if (json.getInteger(GRADE)< DistributionGradeEnum.DISTRIBUTOR.getCode()&&grade.equals( DistributionGradeEnum.PARTNER.getCode())){
                    Long dis = json.getLong(DISTRIBUTOR);
                    //上级无 合伙人
                    if (userId==null&&userId2.equals(dis)){
                        json.put(DISTRIBUTOR,userId2);
                    }
                    if (userId==null&&userId2.equals(dis)&&dis.equals(json.getLong(PARTNER))){
                        json.put(DISTRIBUTOR,userId);
                    }
                    if (userId!=null&&userId.equals(dis)||dis==null){
                        json.put(DISTRIBUTOR,userId);
                    }
                    json.put(PARTNER,userId);
                    change++;
                }
                //若上级为合伙人 自己为分销商 则 自己的 上级合伙人 为 该合伙人
                //上级分销商为自己
                if (json.getInteger(GRADE).equals( DistributionGradeEnum.DISTRIBUTOR.getCode())&&grade.equals( DistributionGradeEnum.PARTNER.getCode())){
                    json.put(PARTNER,userId);
                    json.put(DISTRIBUTOR,sunId);
                    change++;
                }
                //若上级为分销商 且下级 小于分销商级别
                //此时设置该分销商为 下级的上级分销商
                if (json.getInteger(GRADE)< DistributionGradeEnum.DISTRIBUTOR.getCode()&&grade.equals( DistributionGradeEnum.DISTRIBUTOR.getCode())){
                    json.put(DISTRIBUTOR,userId);
                    change++;
                }
                if (change>0){
                    distributionRedis.set(String.format(DISTRIBUTION_GRADE_PARTNER_USERID,sunId),json);
                    JSONArray sunArray = json.getJSONArray(SON_LIST);
                    if (sunArray!=null&&sunArray.size()>0){
                        //若下级集合存在
                        upSunInformation(userId,userId2,sunArray,grade);
                    }
                }

            }
        }
    }

    //获取higher上级合伙人
    public Long getPartnerId(Long userId){
        logger.info("分销体系---userId={}获取合伙人",userId);
        JSONObject higherJson = distributionRedis.get(String.format(DISTRIBUTION_GRADE_PARTNER_USERID,userId));
        if (higherJson!=null){
            //获取上级级别
            Integer grade = higherJson.getInteger(GRADE);
            //获取上级id
            Long higher = higherJson.getLong(HIGHER);
            //若为合伙人则返回 合伙人id
            if (grade.equals( DistributionGradeEnum.PARTNER.getCode())){
                return userId;
            }
            if (higher!=null){
                return getPartnerId(higher);
            }
        }
        return null;
    }

    /**
     * 添加自己
     * @param userId
     * @param grade
     * @return
     */
    public Response addSelf(Long userId, Integer grade) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(GRADE,grade);
        distributionRedis.set(String.format(DISTRIBUTION_GRADE_PARTNER_USERID,userId),jsonObject);
        return Response.success();
    }
    //获取分销商
    public Long getDistributorId(Long userId){
        logger.info("分销体系---userId={}获取合伙人",userId);
        JSONObject higherJson = distributionRedis.get(String.format(DISTRIBUTION_GRADE_PARTNER_USERID,userId));
        if (higherJson!=null){
            //获取上级级别
            Integer grade = higherJson.getInteger(GRADE);
            if (grade.equals( DistributionGradeEnum.DISTRIBUTOR.getCode())||grade.equals( DistributionGradeEnum.PARTNER.getCode())){
                return userId;
            }
            //获取上上级id
            Long higher = higherJson.getLong(HIGHER);
            if (higher!=null){
                return getDistributorId(higher);
            }
        }
        return null;
    }


    public JSONArray getAllSunListByUserAndGrade(Long userId,Integer grade){
        JSONObject jsonObject = distributionRedis.get(String.format(DISTRIBUTION_GRADE_PARTNER_USERID,userId));
        if (jsonObject==null){
            logger.warn("userId={}不存在分销信息",userId);
            return null;
        }
        //子集
        JSONArray jsonArray = jsonObject.getJSONArray(SON_LIST);
        if (jsonArray==null||jsonArray.size()==0){
            logger.warn("userId={}子集为空",userId);
            return null;
        }
        JSONArray sunJsonArray = sunJsonArray(jsonArray,grade);
//        jsonArray.addAll(sunJsonArray);
        return sunJsonArray;
    }
    public JSONArray sunJsonArray( JSONArray jsonArray,Integer grade ){
        for (int i = 0;i<jsonArray.size();i++){
            //获取一个下级
            Long id = jsonArray.getLong(i);
            //判断是否等级低于 该用户
            //若低于则 查询所有子集
            JSONObject sun = find(id);
            if (sun.getInteger(GRADE)<grade){
                JSONArray sunJSONArray = sun.getJSONArray(SON_LIST);
                if (sunJSONArray!=null&&sunJSONArray.size()>0){
//                    System.out.println("=====id========"+id);
//                    System.out.println("======sunJSONArray======="+sunJSONArray);
                    jsonArray.addAll(sunJSONArray);
//                    System.out.println("=====jsonArray========"+jsonArray);
                    JSONArray list = sunJsonArray(sunJSONArray,grade);
                }

            }
        }
        return jsonArray;
    }

}
