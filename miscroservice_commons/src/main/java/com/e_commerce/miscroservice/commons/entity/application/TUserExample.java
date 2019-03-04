package com.e_commerce.miscroservice.commons.entity.application;

import java.util.ArrayList;
import java.util.List;

public class TUserExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TUserExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNull() {
            addCriterion("password is null");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNotNull() {
            addCriterion("password is not null");
            return (Criteria) this;
        }

        public Criteria andPasswordEqualTo(String value) {
            addCriterion("password =", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotEqualTo(String value) {
            addCriterion("password <>", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThan(String value) {
            addCriterion("password >", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("password >=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThan(String value) {
            addCriterion("password <", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThanOrEqualTo(String value) {
            addCriterion("password <=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLike(String value) {
            addCriterion("password like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotLike(String value) {
            addCriterion("password not like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordIn(List<String> values) {
            addCriterion("password in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotIn(List<String> values) {
            addCriterion("password not in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordBetween(String value1, String value2) {
            addCriterion("password between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotBetween(String value1, String value2) {
            addCriterion("password not between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andUserAccountIsNull() {
            addCriterion("user_account is null");
            return (Criteria) this;
        }

        public Criteria andUserAccountIsNotNull() {
            addCriterion("user_account is not null");
            return (Criteria) this;
        }

        public Criteria andUserAccountEqualTo(String value) {
            addCriterion("user_account =", value, "userAccount");
            return (Criteria) this;
        }

        public Criteria andUserAccountNotEqualTo(String value) {
            addCriterion("user_account <>", value, "userAccount");
            return (Criteria) this;
        }

        public Criteria andUserAccountGreaterThan(String value) {
            addCriterion("user_account >", value, "userAccount");
            return (Criteria) this;
        }

        public Criteria andUserAccountGreaterThanOrEqualTo(String value) {
            addCriterion("user_account >=", value, "userAccount");
            return (Criteria) this;
        }

        public Criteria andUserAccountLessThan(String value) {
            addCriterion("user_account <", value, "userAccount");
            return (Criteria) this;
        }

        public Criteria andUserAccountLessThanOrEqualTo(String value) {
            addCriterion("user_account <=", value, "userAccount");
            return (Criteria) this;
        }

        public Criteria andUserAccountLike(String value) {
            addCriterion("user_account like", value, "userAccount");
            return (Criteria) this;
        }

        public Criteria andUserAccountNotLike(String value) {
            addCriterion("user_account not like", value, "userAccount");
            return (Criteria) this;
        }

        public Criteria andUserAccountIn(List<String> values) {
            addCriterion("user_account in", values, "userAccount");
            return (Criteria) this;
        }

        public Criteria andUserAccountNotIn(List<String> values) {
            addCriterion("user_account not in", values, "userAccount");
            return (Criteria) this;
        }

        public Criteria andUserAccountBetween(String value1, String value2) {
            addCriterion("user_account between", value1, value2, "userAccount");
            return (Criteria) this;
        }

        public Criteria andUserAccountNotBetween(String value1, String value2) {
            addCriterion("user_account not between", value1, value2, "userAccount");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andUserTelIsNull() {
            addCriterion("user_tel is null");
            return (Criteria) this;
        }

        public Criteria andUserTelIsNotNull() {
            addCriterion("user_tel is not null");
            return (Criteria) this;
        }

        public Criteria andUserTelEqualTo(String value) {
            addCriterion("user_tel =", value, "userTel");
            return (Criteria) this;
        }

        public Criteria andUserTelNotEqualTo(String value) {
            addCriterion("user_tel <>", value, "userTel");
            return (Criteria) this;
        }

        public Criteria andUserTelGreaterThan(String value) {
            addCriterion("user_tel >", value, "userTel");
            return (Criteria) this;
        }

        public Criteria andUserTelGreaterThanOrEqualTo(String value) {
            addCriterion("user_tel >=", value, "userTel");
            return (Criteria) this;
        }

        public Criteria andUserTelLessThan(String value) {
            addCriterion("user_tel <", value, "userTel");
            return (Criteria) this;
        }

        public Criteria andUserTelLessThanOrEqualTo(String value) {
            addCriterion("user_tel <=", value, "userTel");
            return (Criteria) this;
        }

        public Criteria andUserTelLike(String value) {
            addCriterion("user_tel like", value, "userTel");
            return (Criteria) this;
        }

        public Criteria andUserTelNotLike(String value) {
            addCriterion("user_tel not like", value, "userTel");
            return (Criteria) this;
        }

        public Criteria andUserTelIn(List<String> values) {
            addCriterion("user_tel in", values, "userTel");
            return (Criteria) this;
        }

        public Criteria andUserTelNotIn(List<String> values) {
            addCriterion("user_tel not in", values, "userTel");
            return (Criteria) this;
        }

        public Criteria andUserTelBetween(String value1, String value2) {
            addCriterion("user_tel between", value1, value2, "userTel");
            return (Criteria) this;
        }

        public Criteria andUserTelNotBetween(String value1, String value2) {
            addCriterion("user_tel not between", value1, value2, "userTel");
            return (Criteria) this;
        }

        public Criteria andJurisdictionIsNull() {
            addCriterion("jurisdiction is null");
            return (Criteria) this;
        }

        public Criteria andJurisdictionIsNotNull() {
            addCriterion("jurisdiction is not null");
            return (Criteria) this;
        }

        public Criteria andJurisdictionEqualTo(Integer value) {
            addCriterion("jurisdiction =", value, "jurisdiction");
            return (Criteria) this;
        }

        public Criteria andJurisdictionNotEqualTo(Integer value) {
            addCriterion("jurisdiction <>", value, "jurisdiction");
            return (Criteria) this;
        }

        public Criteria andJurisdictionGreaterThan(Integer value) {
            addCriterion("jurisdiction >", value, "jurisdiction");
            return (Criteria) this;
        }

        public Criteria andJurisdictionGreaterThanOrEqualTo(Integer value) {
            addCriterion("jurisdiction >=", value, "jurisdiction");
            return (Criteria) this;
        }

        public Criteria andJurisdictionLessThan(Integer value) {
            addCriterion("jurisdiction <", value, "jurisdiction");
            return (Criteria) this;
        }

        public Criteria andJurisdictionLessThanOrEqualTo(Integer value) {
            addCriterion("jurisdiction <=", value, "jurisdiction");
            return (Criteria) this;
        }

        public Criteria andJurisdictionIn(List<Integer> values) {
            addCriterion("jurisdiction in", values, "jurisdiction");
            return (Criteria) this;
        }

        public Criteria andJurisdictionNotIn(List<Integer> values) {
            addCriterion("jurisdiction not in", values, "jurisdiction");
            return (Criteria) this;
        }

        public Criteria andJurisdictionBetween(Integer value1, Integer value2) {
            addCriterion("jurisdiction between", value1, value2, "jurisdiction");
            return (Criteria) this;
        }

        public Criteria andJurisdictionNotBetween(Integer value1, Integer value2) {
            addCriterion("jurisdiction not between", value1, value2, "jurisdiction");
            return (Criteria) this;
        }

        public Criteria andUserHeadPortraitPathIsNull() {
            addCriterion("user_head_portrait_path is null");
            return (Criteria) this;
        }

        public Criteria andUserHeadPortraitPathIsNotNull() {
            addCriterion("user_head_portrait_path is not null");
            return (Criteria) this;
        }

        public Criteria andUserHeadPortraitPathEqualTo(String value) {
            addCriterion("user_head_portrait_path =", value, "userHeadPortraitPath");
            return (Criteria) this;
        }

        public Criteria andUserHeadPortraitPathNotEqualTo(String value) {
            addCriterion("user_head_portrait_path <>", value, "userHeadPortraitPath");
            return (Criteria) this;
        }

        public Criteria andUserHeadPortraitPathGreaterThan(String value) {
            addCriterion("user_head_portrait_path >", value, "userHeadPortraitPath");
            return (Criteria) this;
        }

        public Criteria andUserHeadPortraitPathGreaterThanOrEqualTo(String value) {
            addCriterion("user_head_portrait_path >=", value, "userHeadPortraitPath");
            return (Criteria) this;
        }

        public Criteria andUserHeadPortraitPathLessThan(String value) {
            addCriterion("user_head_portrait_path <", value, "userHeadPortraitPath");
            return (Criteria) this;
        }

        public Criteria andUserHeadPortraitPathLessThanOrEqualTo(String value) {
            addCriterion("user_head_portrait_path <=", value, "userHeadPortraitPath");
            return (Criteria) this;
        }

        public Criteria andUserHeadPortraitPathLike(String value) {
            addCriterion("user_head_portrait_path like", value, "userHeadPortraitPath");
            return (Criteria) this;
        }

        public Criteria andUserHeadPortraitPathNotLike(String value) {
            addCriterion("user_head_portrait_path not like", value, "userHeadPortraitPath");
            return (Criteria) this;
        }

        public Criteria andUserHeadPortraitPathIn(List<String> values) {
            addCriterion("user_head_portrait_path in", values, "userHeadPortraitPath");
            return (Criteria) this;
        }

        public Criteria andUserHeadPortraitPathNotIn(List<String> values) {
            addCriterion("user_head_portrait_path not in", values, "userHeadPortraitPath");
            return (Criteria) this;
        }

        public Criteria andUserHeadPortraitPathBetween(String value1, String value2) {
            addCriterion("user_head_portrait_path between", value1, value2, "userHeadPortraitPath");
            return (Criteria) this;
        }

        public Criteria andUserHeadPortraitPathNotBetween(String value1, String value2) {
            addCriterion("user_head_portrait_path not between", value1, value2, "userHeadPortraitPath");
            return (Criteria) this;
        }

        public Criteria andUserPicturePathIsNull() {
            addCriterion("user_picture_path is null");
            return (Criteria) this;
        }

        public Criteria andUserPicturePathIsNotNull() {
            addCriterion("user_picture_path is not null");
            return (Criteria) this;
        }

        public Criteria andUserPicturePathEqualTo(String value) {
            addCriterion("user_picture_path =", value, "userPicturePath");
            return (Criteria) this;
        }

        public Criteria andUserPicturePathNotEqualTo(String value) {
            addCriterion("user_picture_path <>", value, "userPicturePath");
            return (Criteria) this;
        }

        public Criteria andUserPicturePathGreaterThan(String value) {
            addCriterion("user_picture_path >", value, "userPicturePath");
            return (Criteria) this;
        }

        public Criteria andUserPicturePathGreaterThanOrEqualTo(String value) {
            addCriterion("user_picture_path >=", value, "userPicturePath");
            return (Criteria) this;
        }

        public Criteria andUserPicturePathLessThan(String value) {
            addCriterion("user_picture_path <", value, "userPicturePath");
            return (Criteria) this;
        }

        public Criteria andUserPicturePathLessThanOrEqualTo(String value) {
            addCriterion("user_picture_path <=", value, "userPicturePath");
            return (Criteria) this;
        }

        public Criteria andUserPicturePathLike(String value) {
            addCriterion("user_picture_path like", value, "userPicturePath");
            return (Criteria) this;
        }

        public Criteria andUserPicturePathNotLike(String value) {
            addCriterion("user_picture_path not like", value, "userPicturePath");
            return (Criteria) this;
        }

        public Criteria andUserPicturePathIn(List<String> values) {
            addCriterion("user_picture_path in", values, "userPicturePath");
            return (Criteria) this;
        }

        public Criteria andUserPicturePathNotIn(List<String> values) {
            addCriterion("user_picture_path not in", values, "userPicturePath");
            return (Criteria) this;
        }

        public Criteria andUserPicturePathBetween(String value1, String value2) {
            addCriterion("user_picture_path between", value1, value2, "userPicturePath");
            return (Criteria) this;
        }

        public Criteria andUserPicturePathNotBetween(String value1, String value2) {
            addCriterion("user_picture_path not between", value1, value2, "userPicturePath");
            return (Criteria) this;
        }

        public Criteria andVxOpenIdIsNull() {
            addCriterion("vx_open_id is null");
            return (Criteria) this;
        }

        public Criteria andVxOpenIdIsNotNull() {
            addCriterion("vx_open_id is not null");
            return (Criteria) this;
        }

        public Criteria andVxOpenIdEqualTo(String value) {
            addCriterion("vx_open_id =", value, "vxOpenId");
            return (Criteria) this;
        }

        public Criteria andVxOpenIdNotEqualTo(String value) {
            addCriterion("vx_open_id <>", value, "vxOpenId");
            return (Criteria) this;
        }

        public Criteria andVxOpenIdGreaterThan(String value) {
            addCriterion("vx_open_id >", value, "vxOpenId");
            return (Criteria) this;
        }

        public Criteria andVxOpenIdGreaterThanOrEqualTo(String value) {
            addCriterion("vx_open_id >=", value, "vxOpenId");
            return (Criteria) this;
        }

        public Criteria andVxOpenIdLessThan(String value) {
            addCriterion("vx_open_id <", value, "vxOpenId");
            return (Criteria) this;
        }

        public Criteria andVxOpenIdLessThanOrEqualTo(String value) {
            addCriterion("vx_open_id <=", value, "vxOpenId");
            return (Criteria) this;
        }

        public Criteria andVxOpenIdLike(String value) {
            addCriterion("vx_open_id like", value, "vxOpenId");
            return (Criteria) this;
        }

        public Criteria andVxOpenIdNotLike(String value) {
            addCriterion("vx_open_id not like", value, "vxOpenId");
            return (Criteria) this;
        }

        public Criteria andVxOpenIdIn(List<String> values) {
            addCriterion("vx_open_id in", values, "vxOpenId");
            return (Criteria) this;
        }

        public Criteria andVxOpenIdNotIn(List<String> values) {
            addCriterion("vx_open_id not in", values, "vxOpenId");
            return (Criteria) this;
        }

        public Criteria andVxOpenIdBetween(String value1, String value2) {
            addCriterion("vx_open_id between", value1, value2, "vxOpenId");
            return (Criteria) this;
        }

        public Criteria andVxOpenIdNotBetween(String value1, String value2) {
            addCriterion("vx_open_id not between", value1, value2, "vxOpenId");
            return (Criteria) this;
        }

        public Criteria andVxIdIsNull() {
            addCriterion("vx_id is null");
            return (Criteria) this;
        }

        public Criteria andVxIdIsNotNull() {
            addCriterion("vx_id is not null");
            return (Criteria) this;
        }

        public Criteria andVxIdEqualTo(String value) {
            addCriterion("vx_id =", value, "vxId");
            return (Criteria) this;
        }

        public Criteria andVxIdNotEqualTo(String value) {
            addCriterion("vx_id <>", value, "vxId");
            return (Criteria) this;
        }

        public Criteria andVxIdGreaterThan(String value) {
            addCriterion("vx_id >", value, "vxId");
            return (Criteria) this;
        }

        public Criteria andVxIdGreaterThanOrEqualTo(String value) {
            addCriterion("vx_id >=", value, "vxId");
            return (Criteria) this;
        }

        public Criteria andVxIdLessThan(String value) {
            addCriterion("vx_id <", value, "vxId");
            return (Criteria) this;
        }

        public Criteria andVxIdLessThanOrEqualTo(String value) {
            addCriterion("vx_id <=", value, "vxId");
            return (Criteria) this;
        }

        public Criteria andVxIdLike(String value) {
            addCriterion("vx_id like", value, "vxId");
            return (Criteria) this;
        }

        public Criteria andVxIdNotLike(String value) {
            addCriterion("vx_id not like", value, "vxId");
            return (Criteria) this;
        }

        public Criteria andVxIdIn(List<String> values) {
            addCriterion("vx_id in", values, "vxId");
            return (Criteria) this;
        }

        public Criteria andVxIdNotIn(List<String> values) {
            addCriterion("vx_id not in", values, "vxId");
            return (Criteria) this;
        }

        public Criteria andVxIdBetween(String value1, String value2) {
            addCriterion("vx_id between", value1, value2, "vxId");
            return (Criteria) this;
        }

        public Criteria andVxIdNotBetween(String value1, String value2) {
            addCriterion("vx_id not between", value1, value2, "vxId");
            return (Criteria) this;
        }

        public Criteria andOccupationIsNull() {
            addCriterion("occupation is null");
            return (Criteria) this;
        }

        public Criteria andOccupationIsNotNull() {
            addCriterion("occupation is not null");
            return (Criteria) this;
        }

        public Criteria andOccupationEqualTo(String value) {
            addCriterion("occupation =", value, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationNotEqualTo(String value) {
            addCriterion("occupation <>", value, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationGreaterThan(String value) {
            addCriterion("occupation >", value, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationGreaterThanOrEqualTo(String value) {
            addCriterion("occupation >=", value, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationLessThan(String value) {
            addCriterion("occupation <", value, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationLessThanOrEqualTo(String value) {
            addCriterion("occupation <=", value, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationLike(String value) {
            addCriterion("occupation like", value, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationNotLike(String value) {
            addCriterion("occupation not like", value, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationIn(List<String> values) {
            addCriterion("occupation in", values, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationNotIn(List<String> values) {
            addCriterion("occupation not in", values, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationBetween(String value1, String value2) {
            addCriterion("occupation between", value1, value2, "occupation");
            return (Criteria) this;
        }

        public Criteria andOccupationNotBetween(String value1, String value2) {
            addCriterion("occupation not between", value1, value2, "occupation");
            return (Criteria) this;
        }

        public Criteria andAgeIsNull() {
            addCriterion("age is null");
            return (Criteria) this;
        }

        public Criteria andAgeIsNotNull() {
            addCriterion("age is not null");
            return (Criteria) this;
        }

        public Criteria andAgeEqualTo(Integer value) {
            addCriterion("age =", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotEqualTo(Integer value) {
            addCriterion("age <>", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeGreaterThan(Integer value) {
            addCriterion("age >", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeGreaterThanOrEqualTo(Integer value) {
            addCriterion("age >=", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeLessThan(Integer value) {
            addCriterion("age <", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeLessThanOrEqualTo(Integer value) {
            addCriterion("age <=", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeIn(List<Integer> values) {
            addCriterion("age in", values, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotIn(List<Integer> values) {
            addCriterion("age not in", values, "age");
            return (Criteria) this;
        }

        public Criteria andAgeBetween(Integer value1, Integer value2) {
            addCriterion("age between", value1, value2, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotBetween(Integer value1, Integer value2) {
            addCriterion("age not between", value1, value2, "age");
            return (Criteria) this;
        }

        public Criteria andBirthdayIsNull() {
            addCriterion("birthday is null");
            return (Criteria) this;
        }

        public Criteria andBirthdayIsNotNull() {
            addCriterion("birthday is not null");
            return (Criteria) this;
        }

        public Criteria andBirthdayEqualTo(Long value) {
            addCriterion("birthday =", value, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayNotEqualTo(Long value) {
            addCriterion("birthday <>", value, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayGreaterThan(Long value) {
            addCriterion("birthday >", value, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayGreaterThanOrEqualTo(Long value) {
            addCriterion("birthday >=", value, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayLessThan(Long value) {
            addCriterion("birthday <", value, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayLessThanOrEqualTo(Long value) {
            addCriterion("birthday <=", value, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayIn(List<Long> values) {
            addCriterion("birthday in", values, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayNotIn(List<Long> values) {
            addCriterion("birthday not in", values, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayBetween(Long value1, Long value2) {
            addCriterion("birthday between", value1, value2, "birthday");
            return (Criteria) this;
        }

        public Criteria andBirthdayNotBetween(Long value1, Long value2) {
            addCriterion("birthday not between", value1, value2, "birthday");
            return (Criteria) this;
        }

        public Criteria andSexIsNull() {
            addCriterion("sex is null");
            return (Criteria) this;
        }

        public Criteria andSexIsNotNull() {
            addCriterion("sex is not null");
            return (Criteria) this;
        }

        public Criteria andSexEqualTo(Integer value) {
            addCriterion("sex =", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotEqualTo(Integer value) {
            addCriterion("sex <>", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThan(Integer value) {
            addCriterion("sex >", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThanOrEqualTo(Integer value) {
            addCriterion("sex >=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThan(Integer value) {
            addCriterion("sex <", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThanOrEqualTo(Integer value) {
            addCriterion("sex <=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexIn(List<Integer> values) {
            addCriterion("sex in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotIn(List<Integer> values) {
            addCriterion("sex not in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexBetween(Integer value1, Integer value2) {
            addCriterion("sex between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotBetween(Integer value1, Integer value2) {
            addCriterion("sex not between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andMaxEducationIsNull() {
            addCriterion("max_education is null");
            return (Criteria) this;
        }

        public Criteria andMaxEducationIsNotNull() {
            addCriterion("max_education is not null");
            return (Criteria) this;
        }

        public Criteria andMaxEducationEqualTo(String value) {
            addCriterion("max_education =", value, "maxEducation");
            return (Criteria) this;
        }

        public Criteria andMaxEducationNotEqualTo(String value) {
            addCriterion("max_education <>", value, "maxEducation");
            return (Criteria) this;
        }

        public Criteria andMaxEducationGreaterThan(String value) {
            addCriterion("max_education >", value, "maxEducation");
            return (Criteria) this;
        }

        public Criteria andMaxEducationGreaterThanOrEqualTo(String value) {
            addCriterion("max_education >=", value, "maxEducation");
            return (Criteria) this;
        }

        public Criteria andMaxEducationLessThan(String value) {
            addCriterion("max_education <", value, "maxEducation");
            return (Criteria) this;
        }

        public Criteria andMaxEducationLessThanOrEqualTo(String value) {
            addCriterion("max_education <=", value, "maxEducation");
            return (Criteria) this;
        }

        public Criteria andMaxEducationLike(String value) {
            addCriterion("max_education like", value, "maxEducation");
            return (Criteria) this;
        }

        public Criteria andMaxEducationNotLike(String value) {
            addCriterion("max_education not like", value, "maxEducation");
            return (Criteria) this;
        }

        public Criteria andMaxEducationIn(List<String> values) {
            addCriterion("max_education in", values, "maxEducation");
            return (Criteria) this;
        }

        public Criteria andMaxEducationNotIn(List<String> values) {
            addCriterion("max_education not in", values, "maxEducation");
            return (Criteria) this;
        }

        public Criteria andMaxEducationBetween(String value1, String value2) {
            addCriterion("max_education between", value1, value2, "maxEducation");
            return (Criteria) this;
        }

        public Criteria andMaxEducationNotBetween(String value1, String value2) {
            addCriterion("max_education not between", value1, value2, "maxEducation");
            return (Criteria) this;
        }

        public Criteria andFollowNumIsNull() {
            addCriterion("follow_num is null");
            return (Criteria) this;
        }

        public Criteria andFollowNumIsNotNull() {
            addCriterion("follow_num is not null");
            return (Criteria) this;
        }

        public Criteria andFollowNumEqualTo(Integer value) {
            addCriterion("follow_num =", value, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumNotEqualTo(Integer value) {
            addCriterion("follow_num <>", value, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumGreaterThan(Integer value) {
            addCriterion("follow_num >", value, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("follow_num >=", value, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumLessThan(Integer value) {
            addCriterion("follow_num <", value, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumLessThanOrEqualTo(Integer value) {
            addCriterion("follow_num <=", value, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumIn(List<Integer> values) {
            addCriterion("follow_num in", values, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumNotIn(List<Integer> values) {
            addCriterion("follow_num not in", values, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumBetween(Integer value1, Integer value2) {
            addCriterion("follow_num between", value1, value2, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumNotBetween(Integer value1, Integer value2) {
            addCriterion("follow_num not between", value1, value2, "followNum");
            return (Criteria) this;
        }

        public Criteria andReceiptNumIsNull() {
            addCriterion("receipt_num is null");
            return (Criteria) this;
        }

        public Criteria andReceiptNumIsNotNull() {
            addCriterion("receipt_num is not null");
            return (Criteria) this;
        }

        public Criteria andReceiptNumEqualTo(Integer value) {
            addCriterion("receipt_num =", value, "receiptNum");
            return (Criteria) this;
        }

        public Criteria andReceiptNumNotEqualTo(Integer value) {
            addCriterion("receipt_num <>", value, "receiptNum");
            return (Criteria) this;
        }

        public Criteria andReceiptNumGreaterThan(Integer value) {
            addCriterion("receipt_num >", value, "receiptNum");
            return (Criteria) this;
        }

        public Criteria andReceiptNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("receipt_num >=", value, "receiptNum");
            return (Criteria) this;
        }

        public Criteria andReceiptNumLessThan(Integer value) {
            addCriterion("receipt_num <", value, "receiptNum");
            return (Criteria) this;
        }

        public Criteria andReceiptNumLessThanOrEqualTo(Integer value) {
            addCriterion("receipt_num <=", value, "receiptNum");
            return (Criteria) this;
        }

        public Criteria andReceiptNumIn(List<Integer> values) {
            addCriterion("receipt_num in", values, "receiptNum");
            return (Criteria) this;
        }

        public Criteria andReceiptNumNotIn(List<Integer> values) {
            addCriterion("receipt_num not in", values, "receiptNum");
            return (Criteria) this;
        }

        public Criteria andReceiptNumBetween(Integer value1, Integer value2) {
            addCriterion("receipt_num between", value1, value2, "receiptNum");
            return (Criteria) this;
        }

        public Criteria andReceiptNumNotBetween(Integer value1, Integer value2) {
            addCriterion("receipt_num not between", value1, value2, "receiptNum");
            return (Criteria) this;
        }

        public Criteria andRemarksIsNull() {
            addCriterion("remarks is null");
            return (Criteria) this;
        }

        public Criteria andRemarksIsNotNull() {
            addCriterion("remarks is not null");
            return (Criteria) this;
        }

        public Criteria andRemarksEqualTo(String value) {
            addCriterion("remarks =", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksNotEqualTo(String value) {
            addCriterion("remarks <>", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksGreaterThan(String value) {
            addCriterion("remarks >", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksGreaterThanOrEqualTo(String value) {
            addCriterion("remarks >=", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksLessThan(String value) {
            addCriterion("remarks <", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksLessThanOrEqualTo(String value) {
            addCriterion("remarks <=", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksLike(String value) {
            addCriterion("remarks like", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksNotLike(String value) {
            addCriterion("remarks not like", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksIn(List<String> values) {
            addCriterion("remarks in", values, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksNotIn(List<String> values) {
            addCriterion("remarks not in", values, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksBetween(String value1, String value2) {
            addCriterion("remarks between", value1, value2, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksNotBetween(String value1, String value2) {
            addCriterion("remarks not between", value1, value2, "remarks");
            return (Criteria) this;
        }

        public Criteria andLevelIsNull() {
            addCriterion("level is null");
            return (Criteria) this;
        }

        public Criteria andLevelIsNotNull() {
            addCriterion("level is not null");
            return (Criteria) this;
        }

        public Criteria andLevelEqualTo(Integer value) {
            addCriterion("level =", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotEqualTo(Integer value) {
            addCriterion("level <>", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThan(Integer value) {
            addCriterion("level >", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("level >=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThan(Integer value) {
            addCriterion("level <", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThanOrEqualTo(Integer value) {
            addCriterion("level <=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelIn(List<Integer> values) {
            addCriterion("level in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotIn(List<Integer> values) {
            addCriterion("level not in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelBetween(Integer value1, Integer value2) {
            addCriterion("level between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("level not between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andGrowthValueIsNull() {
            addCriterion("growth_value is null");
            return (Criteria) this;
        }

        public Criteria andGrowthValueIsNotNull() {
            addCriterion("growth_value is not null");
            return (Criteria) this;
        }

        public Criteria andGrowthValueEqualTo(Long value) {
            addCriterion("growth_value =", value, "growthValue");
            return (Criteria) this;
        }

        public Criteria andGrowthValueNotEqualTo(Long value) {
            addCriterion("growth_value <>", value, "growthValue");
            return (Criteria) this;
        }

        public Criteria andGrowthValueGreaterThan(Long value) {
            addCriterion("growth_value >", value, "growthValue");
            return (Criteria) this;
        }

        public Criteria andGrowthValueGreaterThanOrEqualTo(Long value) {
            addCriterion("growth_value >=", value, "growthValue");
            return (Criteria) this;
        }

        public Criteria andGrowthValueLessThan(Long value) {
            addCriterion("growth_value <", value, "growthValue");
            return (Criteria) this;
        }

        public Criteria andGrowthValueLessThanOrEqualTo(Long value) {
            addCriterion("growth_value <=", value, "growthValue");
            return (Criteria) this;
        }

        public Criteria andGrowthValueIn(List<Long> values) {
            addCriterion("growth_value in", values, "growthValue");
            return (Criteria) this;
        }

        public Criteria andGrowthValueNotIn(List<Long> values) {
            addCriterion("growth_value not in", values, "growthValue");
            return (Criteria) this;
        }

        public Criteria andGrowthValueBetween(Long value1, Long value2) {
            addCriterion("growth_value between", value1, value2, "growthValue");
            return (Criteria) this;
        }

        public Criteria andGrowthValueNotBetween(Long value1, Long value2) {
            addCriterion("growth_value not between", value1, value2, "growthValue");
            return (Criteria) this;
        }

        public Criteria andSeekHelpNumIsNull() {
            addCriterion("seek_help_num is null");
            return (Criteria) this;
        }

        public Criteria andSeekHelpNumIsNotNull() {
            addCriterion("seek_help_num is not null");
            return (Criteria) this;
        }

        public Criteria andSeekHelpNumEqualTo(Integer value) {
            addCriterion("seek_help_num =", value, "seekHelpNum");
            return (Criteria) this;
        }

        public Criteria andSeekHelpNumNotEqualTo(Integer value) {
            addCriterion("seek_help_num <>", value, "seekHelpNum");
            return (Criteria) this;
        }

        public Criteria andSeekHelpNumGreaterThan(Integer value) {
            addCriterion("seek_help_num >", value, "seekHelpNum");
            return (Criteria) this;
        }

        public Criteria andSeekHelpNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("seek_help_num >=", value, "seekHelpNum");
            return (Criteria) this;
        }

        public Criteria andSeekHelpNumLessThan(Integer value) {
            addCriterion("seek_help_num <", value, "seekHelpNum");
            return (Criteria) this;
        }

        public Criteria andSeekHelpNumLessThanOrEqualTo(Integer value) {
            addCriterion("seek_help_num <=", value, "seekHelpNum");
            return (Criteria) this;
        }

        public Criteria andSeekHelpNumIn(List<Integer> values) {
            addCriterion("seek_help_num in", values, "seekHelpNum");
            return (Criteria) this;
        }

        public Criteria andSeekHelpNumNotIn(List<Integer> values) {
            addCriterion("seek_help_num not in", values, "seekHelpNum");
            return (Criteria) this;
        }

        public Criteria andSeekHelpNumBetween(Integer value1, Integer value2) {
            addCriterion("seek_help_num between", value1, value2, "seekHelpNum");
            return (Criteria) this;
        }

        public Criteria andSeekHelpNumNotBetween(Integer value1, Integer value2) {
            addCriterion("seek_help_num not between", value1, value2, "seekHelpNum");
            return (Criteria) this;
        }

        public Criteria andServeNumIsNull() {
            addCriterion("serve_num is null");
            return (Criteria) this;
        }

        public Criteria andServeNumIsNotNull() {
            addCriterion("serve_num is not null");
            return (Criteria) this;
        }

        public Criteria andServeNumEqualTo(Integer value) {
            addCriterion("serve_num =", value, "serveNum");
            return (Criteria) this;
        }

        public Criteria andServeNumNotEqualTo(Integer value) {
            addCriterion("serve_num <>", value, "serveNum");
            return (Criteria) this;
        }

        public Criteria andServeNumGreaterThan(Integer value) {
            addCriterion("serve_num >", value, "serveNum");
            return (Criteria) this;
        }

        public Criteria andServeNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("serve_num >=", value, "serveNum");
            return (Criteria) this;
        }

        public Criteria andServeNumLessThan(Integer value) {
            addCriterion("serve_num <", value, "serveNum");
            return (Criteria) this;
        }

        public Criteria andServeNumLessThanOrEqualTo(Integer value) {
            addCriterion("serve_num <=", value, "serveNum");
            return (Criteria) this;
        }

        public Criteria andServeNumIn(List<Integer> values) {
            addCriterion("serve_num in", values, "serveNum");
            return (Criteria) this;
        }

        public Criteria andServeNumNotIn(List<Integer> values) {
            addCriterion("serve_num not in", values, "serveNum");
            return (Criteria) this;
        }

        public Criteria andServeNumBetween(Integer value1, Integer value2) {
            addCriterion("serve_num between", value1, value2, "serveNum");
            return (Criteria) this;
        }

        public Criteria andServeNumNotBetween(Integer value1, Integer value2) {
            addCriterion("serve_num not between", value1, value2, "serveNum");
            return (Criteria) this;
        }

        public Criteria andSurplusTimeIsNull() {
            addCriterion("surplus_time is null");
            return (Criteria) this;
        }

        public Criteria andSurplusTimeIsNotNull() {
            addCriterion("surplus_time is not null");
            return (Criteria) this;
        }

        public Criteria andSurplusTimeEqualTo(Long value) {
            addCriterion("surplus_time =", value, "surplusTime");
            return (Criteria) this;
        }

        public Criteria andSurplusTimeNotEqualTo(Long value) {
            addCriterion("surplus_time <>", value, "surplusTime");
            return (Criteria) this;
        }

        public Criteria andSurplusTimeGreaterThan(Long value) {
            addCriterion("surplus_time >", value, "surplusTime");
            return (Criteria) this;
        }

        public Criteria andSurplusTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("surplus_time >=", value, "surplusTime");
            return (Criteria) this;
        }

        public Criteria andSurplusTimeLessThan(Long value) {
            addCriterion("surplus_time <", value, "surplusTime");
            return (Criteria) this;
        }

        public Criteria andSurplusTimeLessThanOrEqualTo(Long value) {
            addCriterion("surplus_time <=", value, "surplusTime");
            return (Criteria) this;
        }

        public Criteria andSurplusTimeIn(List<Long> values) {
            addCriterion("surplus_time in", values, "surplusTime");
            return (Criteria) this;
        }

        public Criteria andSurplusTimeNotIn(List<Long> values) {
            addCriterion("surplus_time not in", values, "surplusTime");
            return (Criteria) this;
        }

        public Criteria andSurplusTimeBetween(Long value1, Long value2) {
            addCriterion("surplus_time between", value1, value2, "surplusTime");
            return (Criteria) this;
        }

        public Criteria andSurplusTimeNotBetween(Long value1, Long value2) {
            addCriterion("surplus_time not between", value1, value2, "surplusTime");
            return (Criteria) this;
        }

        public Criteria andFreezeTimeIsNull() {
            addCriterion("freeze_time is null");
            return (Criteria) this;
        }

        public Criteria andFreezeTimeIsNotNull() {
            addCriterion("freeze_time is not null");
            return (Criteria) this;
        }

        public Criteria andFreezeTimeEqualTo(Long value) {
            addCriterion("freeze_time =", value, "freezeTime");
            return (Criteria) this;
        }

        public Criteria andFreezeTimeNotEqualTo(Long value) {
            addCriterion("freeze_time <>", value, "freezeTime");
            return (Criteria) this;
        }

        public Criteria andFreezeTimeGreaterThan(Long value) {
            addCriterion("freeze_time >", value, "freezeTime");
            return (Criteria) this;
        }

        public Criteria andFreezeTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("freeze_time >=", value, "freezeTime");
            return (Criteria) this;
        }

        public Criteria andFreezeTimeLessThan(Long value) {
            addCriterion("freeze_time <", value, "freezeTime");
            return (Criteria) this;
        }

        public Criteria andFreezeTimeLessThanOrEqualTo(Long value) {
            addCriterion("freeze_time <=", value, "freezeTime");
            return (Criteria) this;
        }

        public Criteria andFreezeTimeIn(List<Long> values) {
            addCriterion("freeze_time in", values, "freezeTime");
            return (Criteria) this;
        }

        public Criteria andFreezeTimeNotIn(List<Long> values) {
            addCriterion("freeze_time not in", values, "freezeTime");
            return (Criteria) this;
        }

        public Criteria andFreezeTimeBetween(Long value1, Long value2) {
            addCriterion("freeze_time between", value1, value2, "freezeTime");
            return (Criteria) this;
        }

        public Criteria andFreezeTimeNotBetween(Long value1, Long value2) {
            addCriterion("freeze_time not between", value1, value2, "freezeTime");
            return (Criteria) this;
        }

        public Criteria andCreditLimitIsNull() {
            addCriterion("credit_limit is null");
            return (Criteria) this;
        }

        public Criteria andCreditLimitIsNotNull() {
            addCriterion("credit_limit is not null");
            return (Criteria) this;
        }

        public Criteria andCreditLimitEqualTo(Long value) {
            addCriterion("credit_limit =", value, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitNotEqualTo(Long value) {
            addCriterion("credit_limit <>", value, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitGreaterThan(Long value) {
            addCriterion("credit_limit >", value, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitGreaterThanOrEqualTo(Long value) {
            addCriterion("credit_limit >=", value, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitLessThan(Long value) {
            addCriterion("credit_limit <", value, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitLessThanOrEqualTo(Long value) {
            addCriterion("credit_limit <=", value, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitIn(List<Long> values) {
            addCriterion("credit_limit in", values, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitNotIn(List<Long> values) {
            addCriterion("credit_limit not in", values, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitBetween(Long value1, Long value2) {
            addCriterion("credit_limit between", value1, value2, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andCreditLimitNotBetween(Long value1, Long value2) {
            addCriterion("credit_limit not between", value1, value2, "creditLimit");
            return (Criteria) this;
        }

        public Criteria andPublicWelfareTimeIsNull() {
            addCriterion("public_welfare_time is null");
            return (Criteria) this;
        }

        public Criteria andPublicWelfareTimeIsNotNull() {
            addCriterion("public_welfare_time is not null");
            return (Criteria) this;
        }

        public Criteria andPublicWelfareTimeEqualTo(Long value) {
            addCriterion("public_welfare_time =", value, "publicWelfareTime");
            return (Criteria) this;
        }

        public Criteria andPublicWelfareTimeNotEqualTo(Long value) {
            addCriterion("public_welfare_time <>", value, "publicWelfareTime");
            return (Criteria) this;
        }

        public Criteria andPublicWelfareTimeGreaterThan(Long value) {
            addCriterion("public_welfare_time >", value, "publicWelfareTime");
            return (Criteria) this;
        }

        public Criteria andPublicWelfareTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("public_welfare_time >=", value, "publicWelfareTime");
            return (Criteria) this;
        }

        public Criteria andPublicWelfareTimeLessThan(Long value) {
            addCriterion("public_welfare_time <", value, "publicWelfareTime");
            return (Criteria) this;
        }

        public Criteria andPublicWelfareTimeLessThanOrEqualTo(Long value) {
            addCriterion("public_welfare_time <=", value, "publicWelfareTime");
            return (Criteria) this;
        }

        public Criteria andPublicWelfareTimeIn(List<Long> values) {
            addCriterion("public_welfare_time in", values, "publicWelfareTime");
            return (Criteria) this;
        }

        public Criteria andPublicWelfareTimeNotIn(List<Long> values) {
            addCriterion("public_welfare_time not in", values, "publicWelfareTime");
            return (Criteria) this;
        }

        public Criteria andPublicWelfareTimeBetween(Long value1, Long value2) {
            addCriterion("public_welfare_time between", value1, value2, "publicWelfareTime");
            return (Criteria) this;
        }

        public Criteria andPublicWelfareTimeNotBetween(Long value1, Long value2) {
            addCriterion("public_welfare_time not between", value1, value2, "publicWelfareTime");
            return (Criteria) this;
        }

        public Criteria andAuthenticationStatusIsNull() {
            addCriterion("authentication_status is null");
            return (Criteria) this;
        }

        public Criteria andAuthenticationStatusIsNotNull() {
            addCriterion("authentication_status is not null");
            return (Criteria) this;
        }

        public Criteria andAuthenticationStatusEqualTo(Integer value) {
            addCriterion("authentication_status =", value, "authenticationStatus");
            return (Criteria) this;
        }

        public Criteria andAuthenticationStatusNotEqualTo(Integer value) {
            addCriterion("authentication_status <>", value, "authenticationStatus");
            return (Criteria) this;
        }

        public Criteria andAuthenticationStatusGreaterThan(Integer value) {
            addCriterion("authentication_status >", value, "authenticationStatus");
            return (Criteria) this;
        }

        public Criteria andAuthenticationStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("authentication_status >=", value, "authenticationStatus");
            return (Criteria) this;
        }

        public Criteria andAuthenticationStatusLessThan(Integer value) {
            addCriterion("authentication_status <", value, "authenticationStatus");
            return (Criteria) this;
        }

        public Criteria andAuthenticationStatusLessThanOrEqualTo(Integer value) {
            addCriterion("authentication_status <=", value, "authenticationStatus");
            return (Criteria) this;
        }

        public Criteria andAuthenticationStatusIn(List<Integer> values) {
            addCriterion("authentication_status in", values, "authenticationStatus");
            return (Criteria) this;
        }

        public Criteria andAuthenticationStatusNotIn(List<Integer> values) {
            addCriterion("authentication_status not in", values, "authenticationStatus");
            return (Criteria) this;
        }

        public Criteria andAuthenticationStatusBetween(Integer value1, Integer value2) {
            addCriterion("authentication_status between", value1, value2, "authenticationStatus");
            return (Criteria) this;
        }

        public Criteria andAuthenticationStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("authentication_status not between", value1, value2, "authenticationStatus");
            return (Criteria) this;
        }

        public Criteria andAuthenticationTypeIsNull() {
            addCriterion("authentication_type is null");
            return (Criteria) this;
        }

        public Criteria andAuthenticationTypeIsNotNull() {
            addCriterion("authentication_type is not null");
            return (Criteria) this;
        }

        public Criteria andAuthenticationTypeEqualTo(Integer value) {
            addCriterion("authentication_type =", value, "authenticationType");
            return (Criteria) this;
        }

        public Criteria andAuthenticationTypeNotEqualTo(Integer value) {
            addCriterion("authentication_type <>", value, "authenticationType");
            return (Criteria) this;
        }

        public Criteria andAuthenticationTypeGreaterThan(Integer value) {
            addCriterion("authentication_type >", value, "authenticationType");
            return (Criteria) this;
        }

        public Criteria andAuthenticationTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("authentication_type >=", value, "authenticationType");
            return (Criteria) this;
        }

        public Criteria andAuthenticationTypeLessThan(Integer value) {
            addCriterion("authentication_type <", value, "authenticationType");
            return (Criteria) this;
        }

        public Criteria andAuthenticationTypeLessThanOrEqualTo(Integer value) {
            addCriterion("authentication_type <=", value, "authenticationType");
            return (Criteria) this;
        }

        public Criteria andAuthenticationTypeIn(List<Integer> values) {
            addCriterion("authentication_type in", values, "authenticationType");
            return (Criteria) this;
        }

        public Criteria andAuthenticationTypeNotIn(List<Integer> values) {
            addCriterion("authentication_type not in", values, "authenticationType");
            return (Criteria) this;
        }

        public Criteria andAuthenticationTypeBetween(Integer value1, Integer value2) {
            addCriterion("authentication_type between", value1, value2, "authenticationType");
            return (Criteria) this;
        }

        public Criteria andAuthenticationTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("authentication_type not between", value1, value2, "authenticationType");
            return (Criteria) this;
        }

        public Criteria andTotalEvaluateIsNull() {
            addCriterion("total_evaluate is null");
            return (Criteria) this;
        }

        public Criteria andTotalEvaluateIsNotNull() {
            addCriterion("total_evaluate is not null");
            return (Criteria) this;
        }

        public Criteria andTotalEvaluateEqualTo(Integer value) {
            addCriterion("total_evaluate =", value, "totalEvaluate");
            return (Criteria) this;
        }

        public Criteria andTotalEvaluateNotEqualTo(Integer value) {
            addCriterion("total_evaluate <>", value, "totalEvaluate");
            return (Criteria) this;
        }

        public Criteria andTotalEvaluateGreaterThan(Integer value) {
            addCriterion("total_evaluate >", value, "totalEvaluate");
            return (Criteria) this;
        }

        public Criteria andTotalEvaluateGreaterThanOrEqualTo(Integer value) {
            addCriterion("total_evaluate >=", value, "totalEvaluate");
            return (Criteria) this;
        }

        public Criteria andTotalEvaluateLessThan(Integer value) {
            addCriterion("total_evaluate <", value, "totalEvaluate");
            return (Criteria) this;
        }

        public Criteria andTotalEvaluateLessThanOrEqualTo(Integer value) {
            addCriterion("total_evaluate <=", value, "totalEvaluate");
            return (Criteria) this;
        }

        public Criteria andTotalEvaluateIn(List<Integer> values) {
            addCriterion("total_evaluate in", values, "totalEvaluate");
            return (Criteria) this;
        }

        public Criteria andTotalEvaluateNotIn(List<Integer> values) {
            addCriterion("total_evaluate not in", values, "totalEvaluate");
            return (Criteria) this;
        }

        public Criteria andTotalEvaluateBetween(Integer value1, Integer value2) {
            addCriterion("total_evaluate between", value1, value2, "totalEvaluate");
            return (Criteria) this;
        }

        public Criteria andTotalEvaluateNotBetween(Integer value1, Integer value2) {
            addCriterion("total_evaluate not between", value1, value2, "totalEvaluate");
            return (Criteria) this;
        }

        public Criteria andCreditEvaluateIsNull() {
            addCriterion("credit_evaluate is null");
            return (Criteria) this;
        }

        public Criteria andCreditEvaluateIsNotNull() {
            addCriterion("credit_evaluate is not null");
            return (Criteria) this;
        }

        public Criteria andCreditEvaluateEqualTo(Integer value) {
            addCriterion("credit_evaluate =", value, "creditEvaluate");
            return (Criteria) this;
        }

        public Criteria andCreditEvaluateNotEqualTo(Integer value) {
            addCriterion("credit_evaluate <>", value, "creditEvaluate");
            return (Criteria) this;
        }

        public Criteria andCreditEvaluateGreaterThan(Integer value) {
            addCriterion("credit_evaluate >", value, "creditEvaluate");
            return (Criteria) this;
        }

        public Criteria andCreditEvaluateGreaterThanOrEqualTo(Integer value) {
            addCriterion("credit_evaluate >=", value, "creditEvaluate");
            return (Criteria) this;
        }

        public Criteria andCreditEvaluateLessThan(Integer value) {
            addCriterion("credit_evaluate <", value, "creditEvaluate");
            return (Criteria) this;
        }

        public Criteria andCreditEvaluateLessThanOrEqualTo(Integer value) {
            addCriterion("credit_evaluate <=", value, "creditEvaluate");
            return (Criteria) this;
        }

        public Criteria andCreditEvaluateIn(List<Integer> values) {
            addCriterion("credit_evaluate in", values, "creditEvaluate");
            return (Criteria) this;
        }

        public Criteria andCreditEvaluateNotIn(List<Integer> values) {
            addCriterion("credit_evaluate not in", values, "creditEvaluate");
            return (Criteria) this;
        }

        public Criteria andCreditEvaluateBetween(Integer value1, Integer value2) {
            addCriterion("credit_evaluate between", value1, value2, "creditEvaluate");
            return (Criteria) this;
        }

        public Criteria andCreditEvaluateNotBetween(Integer value1, Integer value2) {
            addCriterion("credit_evaluate not between", value1, value2, "creditEvaluate");
            return (Criteria) this;
        }

        public Criteria andMajorEvaluateIsNull() {
            addCriterion("major_evaluate is null");
            return (Criteria) this;
        }

        public Criteria andMajorEvaluateIsNotNull() {
            addCriterion("major_evaluate is not null");
            return (Criteria) this;
        }

        public Criteria andMajorEvaluateEqualTo(Integer value) {
            addCriterion("major_evaluate =", value, "majorEvaluate");
            return (Criteria) this;
        }

        public Criteria andMajorEvaluateNotEqualTo(Integer value) {
            addCriterion("major_evaluate <>", value, "majorEvaluate");
            return (Criteria) this;
        }

        public Criteria andMajorEvaluateGreaterThan(Integer value) {
            addCriterion("major_evaluate >", value, "majorEvaluate");
            return (Criteria) this;
        }

        public Criteria andMajorEvaluateGreaterThanOrEqualTo(Integer value) {
            addCriterion("major_evaluate >=", value, "majorEvaluate");
            return (Criteria) this;
        }

        public Criteria andMajorEvaluateLessThan(Integer value) {
            addCriterion("major_evaluate <", value, "majorEvaluate");
            return (Criteria) this;
        }

        public Criteria andMajorEvaluateLessThanOrEqualTo(Integer value) {
            addCriterion("major_evaluate <=", value, "majorEvaluate");
            return (Criteria) this;
        }

        public Criteria andMajorEvaluateIn(List<Integer> values) {
            addCriterion("major_evaluate in", values, "majorEvaluate");
            return (Criteria) this;
        }

        public Criteria andMajorEvaluateNotIn(List<Integer> values) {
            addCriterion("major_evaluate not in", values, "majorEvaluate");
            return (Criteria) this;
        }

        public Criteria andMajorEvaluateBetween(Integer value1, Integer value2) {
            addCriterion("major_evaluate between", value1, value2, "majorEvaluate");
            return (Criteria) this;
        }

        public Criteria andMajorEvaluateNotBetween(Integer value1, Integer value2) {
            addCriterion("major_evaluate not between", value1, value2, "majorEvaluate");
            return (Criteria) this;
        }

        public Criteria andAttitudeEvaluateIsNull() {
            addCriterion("attitude_evaluate is null");
            return (Criteria) this;
        }

        public Criteria andAttitudeEvaluateIsNotNull() {
            addCriterion("attitude_evaluate is not null");
            return (Criteria) this;
        }

        public Criteria andAttitudeEvaluateEqualTo(Integer value) {
            addCriterion("attitude_evaluate =", value, "attitudeEvaluate");
            return (Criteria) this;
        }

        public Criteria andAttitudeEvaluateNotEqualTo(Integer value) {
            addCriterion("attitude_evaluate <>", value, "attitudeEvaluate");
            return (Criteria) this;
        }

        public Criteria andAttitudeEvaluateGreaterThan(Integer value) {
            addCriterion("attitude_evaluate >", value, "attitudeEvaluate");
            return (Criteria) this;
        }

        public Criteria andAttitudeEvaluateGreaterThanOrEqualTo(Integer value) {
            addCriterion("attitude_evaluate >=", value, "attitudeEvaluate");
            return (Criteria) this;
        }

        public Criteria andAttitudeEvaluateLessThan(Integer value) {
            addCriterion("attitude_evaluate <", value, "attitudeEvaluate");
            return (Criteria) this;
        }

        public Criteria andAttitudeEvaluateLessThanOrEqualTo(Integer value) {
            addCriterion("attitude_evaluate <=", value, "attitudeEvaluate");
            return (Criteria) this;
        }

        public Criteria andAttitudeEvaluateIn(List<Integer> values) {
            addCriterion("attitude_evaluate in", values, "attitudeEvaluate");
            return (Criteria) this;
        }

        public Criteria andAttitudeEvaluateNotIn(List<Integer> values) {
            addCriterion("attitude_evaluate not in", values, "attitudeEvaluate");
            return (Criteria) this;
        }

        public Criteria andAttitudeEvaluateBetween(Integer value1, Integer value2) {
            addCriterion("attitude_evaluate between", value1, value2, "attitudeEvaluate");
            return (Criteria) this;
        }

        public Criteria andAttitudeEvaluateNotBetween(Integer value1, Integer value2) {
            addCriterion("attitude_evaluate not between", value1, value2, "attitudeEvaluate");
            return (Criteria) this;
        }

        public Criteria andSkillIsNull() {
            addCriterion("skill is null");
            return (Criteria) this;
        }

        public Criteria andSkillIsNotNull() {
            addCriterion("skill is not null");
            return (Criteria) this;
        }

        public Criteria andSkillEqualTo(String value) {
            addCriterion("skill =", value, "skill");
            return (Criteria) this;
        }

        public Criteria andSkillNotEqualTo(String value) {
            addCriterion("skill <>", value, "skill");
            return (Criteria) this;
        }

        public Criteria andSkillGreaterThan(String value) {
            addCriterion("skill >", value, "skill");
            return (Criteria) this;
        }

        public Criteria andSkillGreaterThanOrEqualTo(String value) {
            addCriterion("skill >=", value, "skill");
            return (Criteria) this;
        }

        public Criteria andSkillLessThan(String value) {
            addCriterion("skill <", value, "skill");
            return (Criteria) this;
        }

        public Criteria andSkillLessThanOrEqualTo(String value) {
            addCriterion("skill <=", value, "skill");
            return (Criteria) this;
        }

        public Criteria andSkillLike(String value) {
            addCriterion("skill like", value, "skill");
            return (Criteria) this;
        }

        public Criteria andSkillNotLike(String value) {
            addCriterion("skill not like", value, "skill");
            return (Criteria) this;
        }

        public Criteria andSkillIn(List<String> values) {
            addCriterion("skill in", values, "skill");
            return (Criteria) this;
        }

        public Criteria andSkillNotIn(List<String> values) {
            addCriterion("skill not in", values, "skill");
            return (Criteria) this;
        }

        public Criteria andSkillBetween(String value1, String value2) {
            addCriterion("skill between", value1, value2, "skill");
            return (Criteria) this;
        }

        public Criteria andSkillNotBetween(String value1, String value2) {
            addCriterion("skill not between", value1, value2, "skill");
            return (Criteria) this;
        }

        public Criteria andIntegrityIsNull() {
            addCriterion("integrity is null");
            return (Criteria) this;
        }

        public Criteria andIntegrityIsNotNull() {
            addCriterion("integrity is not null");
            return (Criteria) this;
        }

        public Criteria andIntegrityEqualTo(Integer value) {
            addCriterion("integrity =", value, "integrity");
            return (Criteria) this;
        }

        public Criteria andIntegrityNotEqualTo(Integer value) {
            addCriterion("integrity <>", value, "integrity");
            return (Criteria) this;
        }

        public Criteria andIntegrityGreaterThan(Integer value) {
            addCriterion("integrity >", value, "integrity");
            return (Criteria) this;
        }

        public Criteria andIntegrityGreaterThanOrEqualTo(Integer value) {
            addCriterion("integrity >=", value, "integrity");
            return (Criteria) this;
        }

        public Criteria andIntegrityLessThan(Integer value) {
            addCriterion("integrity <", value, "integrity");
            return (Criteria) this;
        }

        public Criteria andIntegrityLessThanOrEqualTo(Integer value) {
            addCriterion("integrity <=", value, "integrity");
            return (Criteria) this;
        }

        public Criteria andIntegrityIn(List<Integer> values) {
            addCriterion("integrity in", values, "integrity");
            return (Criteria) this;
        }

        public Criteria andIntegrityNotIn(List<Integer> values) {
            addCriterion("integrity not in", values, "integrity");
            return (Criteria) this;
        }

        public Criteria andIntegrityBetween(Integer value1, Integer value2) {
            addCriterion("integrity between", value1, value2, "integrity");
            return (Criteria) this;
        }

        public Criteria andIntegrityNotBetween(Integer value1, Integer value2) {
            addCriterion("integrity not between", value1, value2, "integrity");
            return (Criteria) this;
        }

        public Criteria andAccreditStatusIsNull() {
            addCriterion("accredit_status is null");
            return (Criteria) this;
        }

        public Criteria andAccreditStatusIsNotNull() {
            addCriterion("accredit_status is not null");
            return (Criteria) this;
        }

        public Criteria andAccreditStatusEqualTo(Integer value) {
            addCriterion("accredit_status =", value, "accreditStatus");
            return (Criteria) this;
        }

        public Criteria andAccreditStatusNotEqualTo(Integer value) {
            addCriterion("accredit_status <>", value, "accreditStatus");
            return (Criteria) this;
        }

        public Criteria andAccreditStatusGreaterThan(Integer value) {
            addCriterion("accredit_status >", value, "accreditStatus");
            return (Criteria) this;
        }

        public Criteria andAccreditStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("accredit_status >=", value, "accreditStatus");
            return (Criteria) this;
        }

        public Criteria andAccreditStatusLessThan(Integer value) {
            addCriterion("accredit_status <", value, "accreditStatus");
            return (Criteria) this;
        }

        public Criteria andAccreditStatusLessThanOrEqualTo(Integer value) {
            addCriterion("accredit_status <=", value, "accreditStatus");
            return (Criteria) this;
        }

        public Criteria andAccreditStatusIn(List<Integer> values) {
            addCriterion("accredit_status in", values, "accreditStatus");
            return (Criteria) this;
        }

        public Criteria andAccreditStatusNotIn(List<Integer> values) {
            addCriterion("accredit_status not in", values, "accreditStatus");
            return (Criteria) this;
        }

        public Criteria andAccreditStatusBetween(Integer value1, Integer value2) {
            addCriterion("accredit_status between", value1, value2, "accreditStatus");
            return (Criteria) this;
        }

        public Criteria andAccreditStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("accredit_status not between", value1, value2, "accreditStatus");
            return (Criteria) this;
        }

        public Criteria andMasterStatusIsNull() {
            addCriterion("master_status is null");
            return (Criteria) this;
        }

        public Criteria andMasterStatusIsNotNull() {
            addCriterion("master_status is not null");
            return (Criteria) this;
        }

        public Criteria andMasterStatusEqualTo(Integer value) {
            addCriterion("master_status =", value, "masterStatus");
            return (Criteria) this;
        }

        public Criteria andMasterStatusNotEqualTo(Integer value) {
            addCriterion("master_status <>", value, "masterStatus");
            return (Criteria) this;
        }

        public Criteria andMasterStatusGreaterThan(Integer value) {
            addCriterion("master_status >", value, "masterStatus");
            return (Criteria) this;
        }

        public Criteria andMasterStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("master_status >=", value, "masterStatus");
            return (Criteria) this;
        }

        public Criteria andMasterStatusLessThan(Integer value) {
            addCriterion("master_status <", value, "masterStatus");
            return (Criteria) this;
        }

        public Criteria andMasterStatusLessThanOrEqualTo(Integer value) {
            addCriterion("master_status <=", value, "masterStatus");
            return (Criteria) this;
        }

        public Criteria andMasterStatusIn(List<Integer> values) {
            addCriterion("master_status in", values, "masterStatus");
            return (Criteria) this;
        }

        public Criteria andMasterStatusNotIn(List<Integer> values) {
            addCriterion("master_status not in", values, "masterStatus");
            return (Criteria) this;
        }

        public Criteria andMasterStatusBetween(Integer value1, Integer value2) {
            addCriterion("master_status between", value1, value2, "masterStatus");
            return (Criteria) this;
        }

        public Criteria andMasterStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("master_status not between", value1, value2, "masterStatus");
            return (Criteria) this;
        }

        public Criteria andAuthStatusIsNull() {
            addCriterion("auth_status is null");
            return (Criteria) this;
        }

        public Criteria andAuthStatusIsNotNull() {
            addCriterion("auth_status is not null");
            return (Criteria) this;
        }

        public Criteria andAuthStatusEqualTo(Integer value) {
            addCriterion("auth_status =", value, "authStatus");
            return (Criteria) this;
        }

        public Criteria andAuthStatusNotEqualTo(Integer value) {
            addCriterion("auth_status <>", value, "authStatus");
            return (Criteria) this;
        }

        public Criteria andAuthStatusGreaterThan(Integer value) {
            addCriterion("auth_status >", value, "authStatus");
            return (Criteria) this;
        }

        public Criteria andAuthStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("auth_status >=", value, "authStatus");
            return (Criteria) this;
        }

        public Criteria andAuthStatusLessThan(Integer value) {
            addCriterion("auth_status <", value, "authStatus");
            return (Criteria) this;
        }

        public Criteria andAuthStatusLessThanOrEqualTo(Integer value) {
            addCriterion("auth_status <=", value, "authStatus");
            return (Criteria) this;
        }

        public Criteria andAuthStatusIn(List<Integer> values) {
            addCriterion("auth_status in", values, "authStatus");
            return (Criteria) this;
        }

        public Criteria andAuthStatusNotIn(List<Integer> values) {
            addCriterion("auth_status not in", values, "authStatus");
            return (Criteria) this;
        }

        public Criteria andAuthStatusBetween(Integer value1, Integer value2) {
            addCriterion("auth_status between", value1, value2, "authStatus");
            return (Criteria) this;
        }

        public Criteria andAuthStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("auth_status not between", value1, value2, "authStatus");
            return (Criteria) this;
        }

        public Criteria andInviteCodeIsNull() {
            addCriterion("invite_code is null");
            return (Criteria) this;
        }

        public Criteria andInviteCodeIsNotNull() {
            addCriterion("invite_code is not null");
            return (Criteria) this;
        }

        public Criteria andInviteCodeEqualTo(String value) {
            addCriterion("invite_code =", value, "inviteCode");
            return (Criteria) this;
        }

        public Criteria andInviteCodeNotEqualTo(String value) {
            addCriterion("invite_code <>", value, "inviteCode");
            return (Criteria) this;
        }

        public Criteria andInviteCodeGreaterThan(String value) {
            addCriterion("invite_code >", value, "inviteCode");
            return (Criteria) this;
        }

        public Criteria andInviteCodeGreaterThanOrEqualTo(String value) {
            addCriterion("invite_code >=", value, "inviteCode");
            return (Criteria) this;
        }

        public Criteria andInviteCodeLessThan(String value) {
            addCriterion("invite_code <", value, "inviteCode");
            return (Criteria) this;
        }

        public Criteria andInviteCodeLessThanOrEqualTo(String value) {
            addCriterion("invite_code <=", value, "inviteCode");
            return (Criteria) this;
        }

        public Criteria andInviteCodeLike(String value) {
            addCriterion("invite_code like", value, "inviteCode");
            return (Criteria) this;
        }

        public Criteria andInviteCodeNotLike(String value) {
            addCriterion("invite_code not like", value, "inviteCode");
            return (Criteria) this;
        }

        public Criteria andInviteCodeIn(List<String> values) {
            addCriterion("invite_code in", values, "inviteCode");
            return (Criteria) this;
        }

        public Criteria andInviteCodeNotIn(List<String> values) {
            addCriterion("invite_code not in", values, "inviteCode");
            return (Criteria) this;
        }

        public Criteria andInviteCodeBetween(String value1, String value2) {
            addCriterion("invite_code between", value1, value2, "inviteCode");
            return (Criteria) this;
        }

        public Criteria andInviteCodeNotBetween(String value1, String value2) {
            addCriterion("invite_code not between", value1, value2, "inviteCode");
            return (Criteria) this;
        }

        public Criteria andAvaliableStatusIsNull() {
            addCriterion("avaliable_status is null");
            return (Criteria) this;
        }

        public Criteria andAvaliableStatusIsNotNull() {
            addCriterion("avaliable_status is not null");
            return (Criteria) this;
        }

        public Criteria andAvaliableStatusEqualTo(String value) {
            addCriterion("avaliable_status =", value, "avaliableStatus");
            return (Criteria) this;
        }

        public Criteria andAvaliableStatusNotEqualTo(String value) {
            addCriterion("avaliable_status <>", value, "avaliableStatus");
            return (Criteria) this;
        }

        public Criteria andAvaliableStatusGreaterThan(String value) {
            addCriterion("avaliable_status >", value, "avaliableStatus");
            return (Criteria) this;
        }

        public Criteria andAvaliableStatusGreaterThanOrEqualTo(String value) {
            addCriterion("avaliable_status >=", value, "avaliableStatus");
            return (Criteria) this;
        }

        public Criteria andAvaliableStatusLessThan(String value) {
            addCriterion("avaliable_status <", value, "avaliableStatus");
            return (Criteria) this;
        }

        public Criteria andAvaliableStatusLessThanOrEqualTo(String value) {
            addCriterion("avaliable_status <=", value, "avaliableStatus");
            return (Criteria) this;
        }

        public Criteria andAvaliableStatusLike(String value) {
            addCriterion("avaliable_status like", value, "avaliableStatus");
            return (Criteria) this;
        }

        public Criteria andAvaliableStatusNotLike(String value) {
            addCriterion("avaliable_status not like", value, "avaliableStatus");
            return (Criteria) this;
        }

        public Criteria andAvaliableStatusIn(List<String> values) {
            addCriterion("avaliable_status in", values, "avaliableStatus");
            return (Criteria) this;
        }

        public Criteria andAvaliableStatusNotIn(List<String> values) {
            addCriterion("avaliable_status not in", values, "avaliableStatus");
            return (Criteria) this;
        }

        public Criteria andAvaliableStatusBetween(String value1, String value2) {
            addCriterion("avaliable_status between", value1, value2, "avaliableStatus");
            return (Criteria) this;
        }

        public Criteria andAvaliableStatusNotBetween(String value1, String value2) {
            addCriterion("avaliable_status not between", value1, value2, "avaliableStatus");
            return (Criteria) this;
        }

        public Criteria andIsCompanyAccountIsNull() {
            addCriterion("is_company_account is null");
            return (Criteria) this;
        }

        public Criteria andIsCompanyAccountIsNotNull() {
            addCriterion("is_company_account is not null");
            return (Criteria) this;
        }

        public Criteria andIsCompanyAccountEqualTo(Integer value) {
            addCriterion("is_company_account =", value, "isCompanyAccount");
            return (Criteria) this;
        }

        public Criteria andIsCompanyAccountNotEqualTo(Integer value) {
            addCriterion("is_company_account <>", value, "isCompanyAccount");
            return (Criteria) this;
        }

        public Criteria andIsCompanyAccountGreaterThan(Integer value) {
            addCriterion("is_company_account >", value, "isCompanyAccount");
            return (Criteria) this;
        }

        public Criteria andIsCompanyAccountGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_company_account >=", value, "isCompanyAccount");
            return (Criteria) this;
        }

        public Criteria andIsCompanyAccountLessThan(Integer value) {
            addCriterion("is_company_account <", value, "isCompanyAccount");
            return (Criteria) this;
        }

        public Criteria andIsCompanyAccountLessThanOrEqualTo(Integer value) {
            addCriterion("is_company_account <=", value, "isCompanyAccount");
            return (Criteria) this;
        }

        public Criteria andIsCompanyAccountIn(List<Integer> values) {
            addCriterion("is_company_account in", values, "isCompanyAccount");
            return (Criteria) this;
        }

        public Criteria andIsCompanyAccountNotIn(List<Integer> values) {
            addCriterion("is_company_account not in", values, "isCompanyAccount");
            return (Criteria) this;
        }

        public Criteria andIsCompanyAccountBetween(Integer value1, Integer value2) {
            addCriterion("is_company_account between", value1, value2, "isCompanyAccount");
            return (Criteria) this;
        }

        public Criteria andIsCompanyAccountNotBetween(Integer value1, Integer value2) {
            addCriterion("is_company_account not between", value1, value2, "isCompanyAccount");
            return (Criteria) this;
        }

        public Criteria andUserTypeIsNull() {
            addCriterion("user_type is null");
            return (Criteria) this;
        }

        public Criteria andUserTypeIsNotNull() {
            addCriterion("user_type is not null");
            return (Criteria) this;
        }

        public Criteria andUserTypeEqualTo(String value) {
            addCriterion("user_type =", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeNotEqualTo(String value) {
            addCriterion("user_type <>", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeGreaterThan(String value) {
            addCriterion("user_type >", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeGreaterThanOrEqualTo(String value) {
            addCriterion("user_type >=", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeLessThan(String value) {
            addCriterion("user_type <", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeLessThanOrEqualTo(String value) {
            addCriterion("user_type <=", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeLike(String value) {
            addCriterion("user_type like", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeNotLike(String value) {
            addCriterion("user_type not like", value, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeIn(List<String> values) {
            addCriterion("user_type in", values, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeNotIn(List<String> values) {
            addCriterion("user_type not in", values, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeBetween(String value1, String value2) {
            addCriterion("user_type between", value1, value2, "userType");
            return (Criteria) this;
        }

        public Criteria andUserTypeNotBetween(String value1, String value2) {
            addCriterion("user_type not between", value1, value2, "userType");
            return (Criteria) this;
        }

        public Criteria andIsFakeIsNull() {
            addCriterion("is_fake is null");
            return (Criteria) this;
        }

        public Criteria andIsFakeIsNotNull() {
            addCriterion("is_fake is not null");
            return (Criteria) this;
        }

        public Criteria andIsFakeEqualTo(Integer value) {
            addCriterion("is_fake =", value, "isFake");
            return (Criteria) this;
        }

        public Criteria andIsFakeNotEqualTo(Integer value) {
            addCriterion("is_fake <>", value, "isFake");
            return (Criteria) this;
        }

        public Criteria andIsFakeGreaterThan(Integer value) {
            addCriterion("is_fake >", value, "isFake");
            return (Criteria) this;
        }

        public Criteria andIsFakeGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_fake >=", value, "isFake");
            return (Criteria) this;
        }

        public Criteria andIsFakeLessThan(Integer value) {
            addCriterion("is_fake <", value, "isFake");
            return (Criteria) this;
        }

        public Criteria andIsFakeLessThanOrEqualTo(Integer value) {
            addCriterion("is_fake <=", value, "isFake");
            return (Criteria) this;
        }

        public Criteria andIsFakeIn(List<Integer> values) {
            addCriterion("is_fake in", values, "isFake");
            return (Criteria) this;
        }

        public Criteria andIsFakeNotIn(List<Integer> values) {
            addCriterion("is_fake not in", values, "isFake");
            return (Criteria) this;
        }

        public Criteria andIsFakeBetween(Integer value1, Integer value2) {
            addCriterion("is_fake between", value1, value2, "isFake");
            return (Criteria) this;
        }

        public Criteria andIsFakeNotBetween(Integer value1, Integer value2) {
            addCriterion("is_fake not between", value1, value2, "isFake");
            return (Criteria) this;
        }

        public Criteria andExtendIsNull() {
            addCriterion("extend is null");
            return (Criteria) this;
        }

        public Criteria andExtendIsNotNull() {
            addCriterion("extend is not null");
            return (Criteria) this;
        }

        public Criteria andExtendEqualTo(String value) {
            addCriterion("extend =", value, "extend");
            return (Criteria) this;
        }

        public Criteria andExtendNotEqualTo(String value) {
            addCriterion("extend <>", value, "extend");
            return (Criteria) this;
        }

        public Criteria andExtendGreaterThan(String value) {
            addCriterion("extend >", value, "extend");
            return (Criteria) this;
        }

        public Criteria andExtendGreaterThanOrEqualTo(String value) {
            addCriterion("extend >=", value, "extend");
            return (Criteria) this;
        }

        public Criteria andExtendLessThan(String value) {
            addCriterion("extend <", value, "extend");
            return (Criteria) this;
        }

        public Criteria andExtendLessThanOrEqualTo(String value) {
            addCriterion("extend <=", value, "extend");
            return (Criteria) this;
        }

        public Criteria andExtendLike(String value) {
            addCriterion("extend like", value, "extend");
            return (Criteria) this;
        }

        public Criteria andExtendNotLike(String value) {
            addCriterion("extend not like", value, "extend");
            return (Criteria) this;
        }

        public Criteria andExtendIn(List<String> values) {
            addCriterion("extend in", values, "extend");
            return (Criteria) this;
        }

        public Criteria andExtendNotIn(List<String> values) {
            addCriterion("extend not in", values, "extend");
            return (Criteria) this;
        }

        public Criteria andExtendBetween(String value1, String value2) {
            addCriterion("extend between", value1, value2, "extend");
            return (Criteria) this;
        }

        public Criteria andExtendNotBetween(String value1, String value2) {
            addCriterion("extend not between", value1, value2, "extend");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNull() {
            addCriterion("create_user is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNotNull() {
            addCriterion("create_user is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserEqualTo(Long value) {
            addCriterion("create_user =", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotEqualTo(Long value) {
            addCriterion("create_user <>", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThan(Long value) {
            addCriterion("create_user >", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThanOrEqualTo(Long value) {
            addCriterion("create_user >=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThan(Long value) {
            addCriterion("create_user <", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThanOrEqualTo(Long value) {
            addCriterion("create_user <=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserIn(List<Long> values) {
            addCriterion("create_user in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotIn(List<Long> values) {
            addCriterion("create_user not in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserBetween(Long value1, Long value2) {
            addCriterion("create_user between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotBetween(Long value1, Long value2) {
            addCriterion("create_user not between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameIsNull() {
            addCriterion("create_user_name is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameIsNotNull() {
            addCriterion("create_user_name is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameEqualTo(String value) {
            addCriterion("create_user_name =", value, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameNotEqualTo(String value) {
            addCriterion("create_user_name <>", value, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameGreaterThan(String value) {
            addCriterion("create_user_name >", value, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("create_user_name >=", value, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameLessThan(String value) {
            addCriterion("create_user_name <", value, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameLessThanOrEqualTo(String value) {
            addCriterion("create_user_name <=", value, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameLike(String value) {
            addCriterion("create_user_name like", value, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameNotLike(String value) {
            addCriterion("create_user_name not like", value, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameIn(List<String> values) {
            addCriterion("create_user_name in", values, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameNotIn(List<String> values) {
            addCriterion("create_user_name not in", values, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameBetween(String value1, String value2) {
            addCriterion("create_user_name between", value1, value2, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateUserNameNotBetween(String value1, String value2) {
            addCriterion("create_user_name not between", value1, value2, "createUserName");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Long value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Long value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Long value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Long value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Long value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Long> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Long> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Long value1, Long value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Long value1, Long value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIsNull() {
            addCriterion("update_user is null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIsNotNull() {
            addCriterion("update_user is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserEqualTo(Long value) {
            addCriterion("update_user =", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotEqualTo(Long value) {
            addCriterion("update_user <>", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThan(Long value) {
            addCriterion("update_user >", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThanOrEqualTo(Long value) {
            addCriterion("update_user >=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThan(Long value) {
            addCriterion("update_user <", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThanOrEqualTo(Long value) {
            addCriterion("update_user <=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIn(List<Long> values) {
            addCriterion("update_user in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotIn(List<Long> values) {
            addCriterion("update_user not in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserBetween(Long value1, Long value2) {
            addCriterion("update_user between", value1, value2, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotBetween(Long value1, Long value2) {
            addCriterion("update_user not between", value1, value2, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNameIsNull() {
            addCriterion("update_user_name is null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNameIsNotNull() {
            addCriterion("update_user_name is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNameEqualTo(String value) {
            addCriterion("update_user_name =", value, "updateUserName");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNameNotEqualTo(String value) {
            addCriterion("update_user_name <>", value, "updateUserName");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNameGreaterThan(String value) {
            addCriterion("update_user_name >", value, "updateUserName");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("update_user_name >=", value, "updateUserName");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNameLessThan(String value) {
            addCriterion("update_user_name <", value, "updateUserName");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNameLessThanOrEqualTo(String value) {
            addCriterion("update_user_name <=", value, "updateUserName");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNameLike(String value) {
            addCriterion("update_user_name like", value, "updateUserName");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNameNotLike(String value) {
            addCriterion("update_user_name not like", value, "updateUserName");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNameIn(List<String> values) {
            addCriterion("update_user_name in", values, "updateUserName");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNameNotIn(List<String> values) {
            addCriterion("update_user_name not in", values, "updateUserName");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNameBetween(String value1, String value2) {
            addCriterion("update_user_name between", value1, value2, "updateUserName");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNameNotBetween(String value1, String value2) {
            addCriterion("update_user_name not between", value1, value2, "updateUserName");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Long value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Long value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Long value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Long value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Long value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Long> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Long> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Long value1, Long value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Long value1, Long value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andIsValidIsNull() {
            addCriterion("is_valid is null");
            return (Criteria) this;
        }

        public Criteria andIsValidIsNotNull() {
            addCriterion("is_valid is not null");
            return (Criteria) this;
        }

        public Criteria andIsValidEqualTo(String value) {
            addCriterion("is_valid =", value, "isValid");
            return (Criteria) this;
        }

        public Criteria andIsValidNotEqualTo(String value) {
            addCriterion("is_valid <>", value, "isValid");
            return (Criteria) this;
        }

        public Criteria andIsValidGreaterThan(String value) {
            addCriterion("is_valid >", value, "isValid");
            return (Criteria) this;
        }

        public Criteria andIsValidGreaterThanOrEqualTo(String value) {
            addCriterion("is_valid >=", value, "isValid");
            return (Criteria) this;
        }

        public Criteria andIsValidLessThan(String value) {
            addCriterion("is_valid <", value, "isValid");
            return (Criteria) this;
        }

        public Criteria andIsValidLessThanOrEqualTo(String value) {
            addCriterion("is_valid <=", value, "isValid");
            return (Criteria) this;
        }

        public Criteria andIsValidLike(String value) {
            addCriterion("is_valid like", value, "isValid");
            return (Criteria) this;
        }

        public Criteria andIsValidNotLike(String value) {
            addCriterion("is_valid not like", value, "isValid");
            return (Criteria) this;
        }

        public Criteria andIsValidIn(List<String> values) {
            addCriterion("is_valid in", values, "isValid");
            return (Criteria) this;
        }

        public Criteria andIsValidNotIn(List<String> values) {
            addCriterion("is_valid not in", values, "isValid");
            return (Criteria) this;
        }

        public Criteria andIsValidBetween(String value1, String value2) {
            addCriterion("is_valid between", value1, value2, "isValid");
            return (Criteria) this;
        }

        public Criteria andIsValidNotBetween(String value1, String value2) {
            addCriterion("is_valid not between", value1, value2, "isValid");
            return (Criteria) this;
        }

        public Criteria andPraiseIsNull() {
            addCriterion("praise is null");
            return (Criteria) this;
        }

        public Criteria andPraiseIsNotNull() {
            addCriterion("praise is not null");
            return (Criteria) this;
        }

        public Criteria andPraiseEqualTo(Integer value) {
            addCriterion("praise =", value, "praise");
            return (Criteria) this;
        }

        public Criteria andPraiseNotEqualTo(Integer value) {
            addCriterion("praise <>", value, "praise");
            return (Criteria) this;
        }

        public Criteria andPraiseGreaterThan(Integer value) {
            addCriterion("praise >", value, "praise");
            return (Criteria) this;
        }

        public Criteria andPraiseGreaterThanOrEqualTo(Integer value) {
            addCriterion("praise >=", value, "praise");
            return (Criteria) this;
        }

        public Criteria andPraiseLessThan(Integer value) {
            addCriterion("praise <", value, "praise");
            return (Criteria) this;
        }

        public Criteria andPraiseLessThanOrEqualTo(Integer value) {
            addCriterion("praise <=", value, "praise");
            return (Criteria) this;
        }

        public Criteria andPraiseIn(List<Integer> values) {
            addCriterion("praise in", values, "praise");
            return (Criteria) this;
        }

        public Criteria andPraiseNotIn(List<Integer> values) {
            addCriterion("praise not in", values, "praise");
            return (Criteria) this;
        }

        public Criteria andPraiseBetween(Integer value1, Integer value2) {
            addCriterion("praise between", value1, value2, "praise");
            return (Criteria) this;
        }

        public Criteria andPraiseNotBetween(Integer value1, Integer value2) {
            addCriterion("praise not between", value1, value2, "praise");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}