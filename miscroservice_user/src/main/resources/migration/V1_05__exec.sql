-- 项目下有多个则命令为 V1,V2 递增，中间01,02代表当前sql的版本新增，每次新增的请把之前的sql注释,最后代表当前的sql的描述


-- APP用户表加店中店openId
-- ALTER TABLE yjj_StoreBusiness ADD in_shop_open_id VARCHAR (50) COMMENT '店中店openId';







-- ========================== 暂时不提交到线上的版本 ===========================
-- APP用户表加小程序店铺类型表 18/12/17
-- ALTER TABLE yjj_StoreBusiness ADD in_shop_member_id bigint(20) DEFAULT '0' COMMENT '店中店的memberId';
-- ALTER TABLE yjj_StoreBusiness ADD wxa_business_type tinyint(4) DEFAULT '0' COMMENT '当前小程序店铺类型,0无,1共享版,2专享版';
-- -- 初始化原有小程序店铺类型 18/12/17
--  UPDATE yjj_storebusiness
--  SET wxa_business_type = 2
--  WHERE Id IN (
--   SELECT store_id
--   FROM jiuy_store_wxa
--  );


--
-- UPDATE yjj_storebusiness
-- set BusinessNumber = id + 800000000
-- where id = 12963
