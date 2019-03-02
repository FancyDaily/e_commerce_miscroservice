package com.e_commerce.miscroservice.commons.exception;

import com.e_commerce.miscroservice.commons.utils.BeanKit;
import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 17:29
 * @Copyright 玖远网络
 */
@Data
public class ErrorHelper extends RuntimeException{

    private int code = 503;
    private String msg;

    private ErrorHelper(String msg) {
        super (msg);
        this.msg = msg;
    }

    private ErrorHelper(int code, String msg) {
        super (msg);
        this.code = code;
        this.msg = msg;
    }

    public static ErrorHelper me(String msg) {
        return new ErrorHelper (msg);
    }

    public static ErrorHelper me(int code, String msg) {
        return new ErrorHelper (code, msg);
    }

    public static void declare(boolean false2Error, String msg) {
        if (! false2Error) {
            throw me (msg);
        }
    }

    public static void declare(boolean false2Error, int code, String msg) {
        if (! false2Error) {
            throw me (code, msg);
        }
    }
    public static void declareNull(Object b, String msg) {
        if (BeanKit.hasNull (b)) {
            throw me (msg);
        }
    }

    public static void declareNull(Object b, int code, String msg) {
        if (BeanKit.hasNull (b)) {
            throw me (code, msg);
        }
    }


}
