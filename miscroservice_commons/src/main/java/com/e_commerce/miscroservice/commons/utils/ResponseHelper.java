package com.e_commerce.miscroservice.commons.utils;

import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/27 16:05
 * @Copyright 玖远网络
 */
public class ResponseHelper{

    private final ResponseHelper me = this;

    private ResponseHelper() {
    }

    public static Response errorHandler(Exception e) {
        if (e instanceof ErrorHelper) {
            ErrorHelper errorHelper = ((ErrorHelper) e);
            String msg = errorHelper.getMsg ();
            return Response.errorAndMsg (errorHelper.getCode (), msg, msg);
        }
        else {
            e.printStackTrace ();
            return Response.crash ();
        }
    }

    public static Response noLogin() {
        return Response.error (PubAccountLoginUtils.CODE, PubAccountLoginUtils.TIP);
    }


    public static ResponseHelper canShouldNotLogin() {
        return new ResponseHelper ();
    }

    private Long userId;
    private boolean mustLogin = Boolean.FALSE;
    private Function<Long, ?> function;
    private List<Consumer<Long>> consumerPool;
    private Integer successCode;
    private Integer errorCode;

    public static ResponseHelper shouldLogin() {
//        DebugUtils.todo ("测试");
//        return shouldLogin (Long.parseLong (WebUtil.getRequest ().getParameter ("userId")));
        return shouldLogin (WxLoginHelper.getUserId ());
    }


    public static ResponseHelper shouldLogin(Optional<Long> userToken) {
        ResponseHelper captain = new ResponseHelper ();
        captain.mustLogin = Boolean.TRUE;
        userToken.ifPresent (id -> captain.userId = id);
        return captain;
    }



    public static ResponseHelper shouldLogin(Long userId) {
        return shouldLogin(Optional.ofNullable (userId));
    }

    public static  ResponseHelper shouldLogin(Integer userId) {
//        DebugUtils.todo ("测试用户");
//        if (true) {
//            userId = 3;
//        }
        return userId == null ? shouldLogin (Optional.empty ()) :  shouldLogin(userId.longValue ());
    }



    public ResponseHelper invokeHasReturnVal(Function<Long, ?> function) {
        if (this.function != null) {
            throw new IllegalStateException ("已经有返回值了,不可以定义多个返回值");
        }
        this.function = function;
        return me;
    }

    /**
     * 调用方法, 没有返回值
     *
     * @return com.e_commerce.miscroservice.commons.utils.ResponseHelper<T>
     * @author Charlie
     * @date 2018/11/24 8:18
     */
    public ResponseHelper invokeNoReturnVal(Consumer<Long> consumer) {
        if (consumer != null) {
            if (consumerPool == null) {
                consumerPool = new ArrayList<> (1);
            }
            consumerPool.add (consumer);
        }
        return me;
    }


    /**
     * 返回response
     * <p>自动抓取ErrorHelper</p>
     *
     * @return com.e_commerce.miscroservice.commons.utils.ResponseHelper<T>
     * @author Charlie
     * @date 2018/11/24 8:18
     */
    public Response returnResponse(Boolean catchMyError) {
        return catchMyError ? doInvokeAndCatch () : doInvoke ();
    }

    /**
     * 返回response
     *
     * @return com.e_commerce.miscroservice.commons.utils.ResponseHelper<T>
     * @author Charlie
     * @date 2018/11/24 8:18
     */
    public Response returnResponse() {
        return returnResponse (Boolean.TRUE);
    }


    private Response doInvokeAndCatch() {
        try {
            return doInvoke ();
        } catch (ErrorHelper e) {
            return errorCode == null ? ResponseHelper.errorHandler (e) : Response.error (errorCode, e.getMsg ());
        }
    }

    private Response doInvoke() {
        if (mustLogin && BeanKit.hasNull (userId)) {
            //没有登录
            return ResponseHelper.noLogin ();
        }
        if (! ObjectUtils.isEmpty (consumerPool)) {
            consumerPool.stream ().forEach (action -> action.accept (userId));
        }
        if (function != null) {
            Object value = function.apply ((Long)userId);
            return successCode == null ? Response.success (value) : Response.success (successCode, value);
        }
        else {
            return Response.success ();
        }

    }


    public ResponseHelper setSuccessCode(int successCode) {
        this.successCode = successCode;
        return me;
    }

    public ResponseHelper setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
        return me;
    }
}
