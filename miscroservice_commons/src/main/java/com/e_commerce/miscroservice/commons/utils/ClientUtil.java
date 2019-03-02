package com.e_commerce.miscroservice.commons.utils;

public class ClientUtil {

    /**
     * 比较两个版本号
     * 
     * @return -1 小于；0，等于；1，大于
     */
    public static int compareTo(String ver1, String ver2) {
        String[] v1 = ver1.split("\\.");
        String[] v2 = ver2.split("\\.");

        int l1 = v1.length;
        int l2 = v2.length;
        for (int i = 0; i < Math.max(l1, l2); i++) {
            int k1 = 0;
            if (i < l1)
                k1 = Integer.parseInt(v1[i]);

            int k2 = 0;
            if (i < l2)
                k2 = Integer.parseInt(v2[i]);

            if (k1 != k2)
                return k1 > k2 ? 1 : -1;
        }
        return 0;
    }
}
