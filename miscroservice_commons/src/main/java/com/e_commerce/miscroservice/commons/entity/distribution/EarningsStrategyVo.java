package com.e_commerce.miscroservice.commons.entity.distribution;

import com.e_commerce.miscroservice.commons.annotations.application.IsEmptyAnnotation;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DstbEarningsStrategy;
import com.e_commerce.miscroservice.commons.enums.EmptyEnum;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.StrategyEarningsTypeEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 13:33
 * @Copyright 玖远网络
 */
@Data
public class EarningsStrategyVo{

    private static Log logger = Log.getInstance(EarningsStrategyVo.class);

    /**
     * 0 无等级 1 店长 2分销商 3合伙人
     */
    @IsEmptyAnnotation( message = "角色类型不能为空" )
    private Integer userRoleType;
    /**
     * 自分佣比例
     */
    @IsEmptyAnnotation( message = "自分佣比例不能为空" )
    private BigDecimal selfCommissionEarningsRatio;
    /**
     * 一级粉丝分佣比例
     */
    @IsEmptyAnnotation( message = "一级粉丝分佣比例不能为空" )
    private BigDecimal fans1CommissionEarningsRatio;
    /**
     * 二级粉丝分佣比例
     */
    @IsEmptyAnnotation( message = "二级粉丝分佣比例不能为空" )
    private BigDecimal fans2CommissionEarningsRatio;
    /**
     * 分佣金币,RMB比例
     */
    @IsEmptyAnnotation( message = "分佣-金币,RMB比例不能为空" )
    private BigDecimal commissionCurrencyRatio;
    /**
     * 团队管理提成比例
     */
    private BigDecimal managerEarningsRatio;
    /**
     * 团队管理提成-金币,RMB比例
     */
    private BigDecimal managerCurrencyRatio;

    /**
     * 策略转化成自己
     *
     * @param strategys strategys
     * @return
     * key:角色 {@link EarningsStrategyVo#userRoleType}
     * @author Charlie
     * @date 2018/10/17 15:18
     */
    public static Map<String, EarningsStrategyVo> build(List<DstbEarningsStrategy> strategys) {
        if (ObjectUtils.isEmpty (strategys)) {
            return EmptyEnum.map ();
        }
        Map<String, EarningsStrategyVo> roleStrategy = new HashMap<> (4);
        logger.info ("组装分销收益策略 =========>");
        strategys.forEach (strategy -> {
            Integer userRoleType = strategy.getUserRoleType ();
            logger.info ("userRoleType={}", userRoleType);
            String userRoleTypeIndex =  String.valueOf (userRoleType);
            EarningsStrategyVo history = roleStrategy.get (userRoleTypeIndex);
            if (BeanKit.hasNull (history)) {
                history = new EarningsStrategyVo ();
                history.setUserRoleType (userRoleType);
            }
            Integer earningsType = strategy.getEarningsType ();
            BigDecimal earningsRatio = strategy.getEarningsRatio ();
            BigDecimal currencyRatio = strategy.getCurrencyRatio ();
            if (StrategyEarningsTypeEnum.SELF_COMMISSION_EARNINGS.isThis (earningsType)) {
                logger.info ("自分佣");
                history.setSelfCommissionEarningsRatio (earningsRatio);
                history.setCommissionCurrencyRatio (currencyRatio);
            }
            else if (StrategyEarningsTypeEnum.FANS1_COMMISSION_EARNINGS.isThis (earningsType)) {
                logger.info ("1级粉丝");
                history.setFans1CommissionEarningsRatio (earningsRatio);
                history.setCommissionCurrencyRatio (currencyRatio);
            }
            else if (StrategyEarningsTypeEnum.FANS2_COMMISSION_EARNINGS.isThis (earningsType)) {
                logger.info ("2级粉丝");
                history.setFans2CommissionEarningsRatio (earningsRatio);
                history.setCommissionCurrencyRatio (currencyRatio);
            }
            else if (StrategyEarningsTypeEnum.MANAGER.isThis (earningsType)) {
                logger.info ("团队收益");
                history.setManagerEarningsRatio (earningsRatio);
                history.setManagerCurrencyRatio (currencyRatio);
            }
            else {
                //就不引入日志了...
                logger.error ("未知的策略收益类型:earningsType ={} ",earningsType);
                throw ErrorHelper.me ("未知的策略收益类型");
            }
            roleStrategy.put (userRoleTypeIndex, history);
        });

        return roleStrategy;
    }


    /**
     * 转化成实体类
     *
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DstbEarningsStrategy>
     * @author Charlie
     * @date 2018/10/17 15:13
     */
    public List<DstbEarningsStrategy> format2Entity() {
        List<DstbEarningsStrategy> strategyList = new ArrayList<> (4);
        DstbEarningsStrategy selfCms = new DstbEarningsStrategy ();
        selfCms.setUserRoleType (this.getUserRoleType ());
        selfCms.setEarningsType (StrategyEarningsTypeEnum.SELF_COMMISSION_EARNINGS.getCode ());
        selfCms.setEarningsRatio (this.getSelfCommissionEarningsRatio ());
        selfCms.setCurrencyRatio (this.getCommissionCurrencyRatio ());
        strategyList.add (selfCms);

        DstbEarningsStrategy fans1Cms = new DstbEarningsStrategy ();
        fans1Cms.setUserRoleType (this.getUserRoleType ());
        fans1Cms.setEarningsType (StrategyEarningsTypeEnum.FANS1_COMMISSION_EARNINGS.getCode ());
        fans1Cms.setEarningsRatio (this.getFans1CommissionEarningsRatio ());
        fans1Cms.setCurrencyRatio (this.getCommissionCurrencyRatio ());
        strategyList.add (fans1Cms);

        DstbEarningsStrategy fans2Cms = new DstbEarningsStrategy ();
        fans2Cms.setUserRoleType (this.getUserRoleType ());
        fans2Cms.setEarningsType (StrategyEarningsTypeEnum.FANS2_COMMISSION_EARNINGS.getCode ());
        fans2Cms.setEarningsRatio (this.getFans2CommissionEarningsRatio ());
        fans2Cms.setCurrencyRatio (this.getCommissionCurrencyRatio ());
        strategyList.add (fans2Cms);

        DstbEarningsStrategy manager = new DstbEarningsStrategy ();
        manager.setUserRoleType (this.getUserRoleType ());
        manager.setEarningsType (StrategyEarningsTypeEnum.MANAGER.getCode ());
        manager.setEarningsRatio (this.getManagerEarningsRatio ());
        manager.setCurrencyRatio (this.getManagerCurrencyRatio ());
        strategyList.add (manager);

        return strategyList;
    }
}
