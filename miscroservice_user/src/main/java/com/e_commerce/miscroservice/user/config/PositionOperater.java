package com.e_commerce.miscroservice.user.config;

import java.util.HashMap;
import java.util.Map;

public class PositionOperater {


    private PositionOperater(String content) {
        lineBuilder = new StringBuilder(content);
    }

    private StringBuilder lineBuilder;


    private Map<Integer, Integer> superLinkCache = new HashMap<>();

    public void add(int start, int len, String content) {
        if (start < 0 || len < 0 || content == null || content.isEmpty()) {
            return;
        }
        start = start - 1;

        int end = start + len;


        superLinkCache.put(start, end);
        if (start >= lineBuilder.length() - len) {
            lineBuilder.append(content);
        } else {
            int origin = getLatelyValue(start);
            int insertIndex = end >= origin ? end + 1 : start;
            if (insertIndex >= lineBuilder.length() - len) {

                lineBuilder.append(content);
            } else {

                lineBuilder.insert(insertIndex, content);
            }

        }

    }


    private int getLatelyValue(int start) {

        //找出最近的
        int latelyIndex = binarysearchKey(superLinkCache.keySet().toArray(new Integer[superLinkCache.size()]), start);
        //取出原始的记录
        return latelyIndex == 0 ? 0 : superLinkCache.get(latelyIndex);
    }

    public String change(int start, int len) {

        if (start > lineBuilder.length() - len) {
            return "";
        }
        start = start - 1;

        int newEnd = start + len;

        int origin = getLatelyValue(start);


        return lineBuilder.substring(start, origin > newEnd ? newEnd : origin);


    }



    private int binarysearchKey(Integer[] array, int targetNum) {

        int left = 0, right = 0;
        for (right = array.length - 1; left != right; ) {
            int midIndex = (right + left) / 2;
            int mid = (right - left);
            int midValue = array[midIndex];
            if (targetNum == midValue) {
                return midIndex;
            }

            if (targetNum > midValue) {
                left = midIndex;
            } else {
                right = midIndex;
            }

            if (mid <= 2) {
                break;
            }
        }
        return ((array[right] - array[left]) / 2 > targetNum ? array[right]
                : array[left]);
    }

}
