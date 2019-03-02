package com.e_commerce.miscroservice.commons.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/24 10:34
 * @Copyright 玖远网络
 */
public class IOUtils{

    public static void close(Closeable ios) {
        if (ios != null) {
            try {
                ios.close ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }
    }

}
