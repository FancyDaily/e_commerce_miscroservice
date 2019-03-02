package com.e_commerce.miscroservice.commons.helper.util.service;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Response implements Serializable {

    private static final int SUCCESS_CODE = 200;
    private static final int REGISTER_TMP_CODE = 201;
    private static final int ERROR_CODE = 400;
    private static final int FAIL_CODE = 500;
    private static final int SERVICE_CRASH = 503;
    private static final int SERVICE_RETRY = 504;
    private static final int SERVICE_VALIDATE = 505;

    public static <E> ResponseData success(E data) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = 200;
        return responseData;
    }

    public static <E> ResponseData successRegisterTmp() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = 201;
        return responseData;
    }

    public static <E> ResponseData success() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = 200;
        return responseData;
    }

    public static <E> ResponseData successMsg(String msg) {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = 200;
        responseData.msg = msg;
        return responseData;
    }

    public static <E> ResponseData successDataMsg(E data, String msg) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = 200;
        responseData.msg = msg;
        return responseData;
    }

    public static <E> ResponseData arraySuccess(E data) {
        ResponseData responseData = new ResponseData();
        List<E> list = new ArrayList();
        list.add(data);
        responseData.data = list;
        responseData.code = 200;
        return responseData;
    }

    public static <E> ResponseData success(int code, E data) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = code;
        return responseData;
    }

    public static <E> ResponseData successByArray() {
        ResponseData responseData = new ResponseData();
        responseData.data = new ArrayList();
        responseData.code = 200;
        return responseData;
    }

    public static <E> ResponseData successByMap(Map<String, Object> map) {
        ResponseData responseData = new ResponseData();
        responseData.data = map;
        responseData.code = 200;
        return responseData;
    }

    public static <E> ResponseData error(E data) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = 400;
        return responseData;
    }

    public static <E> ResponseData errorAndMsg(int code, String msg, E data) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = code;
        responseData.msg = msg;
        return responseData;
    }

    public static <E> ResponseData error(int code, E data) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = code;
        return responseData;
    }

    public static <E> ResponseData errorByArray() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = 400;
        return responseData;
    }

    public static ResponseData response(int code) {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = code;
        return responseData;
    }

    public static <E> ResponseData response(int code, E data) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = code;
        return responseData;
    }

    public static <E> ResponseData errorByMap() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = 400;
        return responseData;
    }

    public static <E> ResponseData error() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = 400;
        return responseData;
    }

    public static <E> ResponseData errorMsg(String msg) {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = 400;
        responseData.msg = msg;
        return responseData;
    }


    public static <E> ResponseData fail() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = 500;
        return responseData;
    }

    public static <E> ResponseData crash() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = 503;
        return responseData;
    }

    public static <E> ResponseData retry() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = 504;
        return responseData;
    }

    public static <E> ResponseData validate() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = 505;
        return responseData;
    }

    private Response() {
    }

    public Object readResolve() {
        return this;
    }

    public static class ResponseData<T> extends Response {
        private String msg;
        private int code;
        private T data;

        public String getMsg() {
            return this.msg;
        }

        public int getCode() {
            return this.code;
        }

        public T getData() {
            return this.data;
        }

        public ResponseData() {
            //super(null);
            this.msg = "".intern();
        }
    }
}
