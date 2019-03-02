package com.e_commerce.miscroservice.commons.enums;

import java.util.*;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 15:03
 * @Copyright 玖远网络
 */
public enum EmptyEnum{

    ;
    /**
     * 空HashSet
     *
     * @return 空HashSet
     * @author Charlie(唐静)
     * @date 2018/6/22 7:09
     */
    public static <T> Set<T> set() {
        return SET;
    }

    /**
     * 空ArrayList
     *
     * @return 空ArrayList
     * @author Charlie(唐静)
     * @date 2018/6/22 7:09
     */
    public static <T> List<T> list() {
        return LIST;
    }

    /**
     * 空HashMap
     *
     * @return 空HashMap
     * @author Charlie(唐静)
     * @date 2018/6/22 7:10
     */
    public static <K, V> Map<K, V> map() {
        return MAP;
    }

    /**
     * 空string
     */
    public static final String string = "";


    private static final Set SET = new EmptySet();
    private static final List LIST = new EmptyList();
    private static final Map MAP = new EmptyMap<>();

    private static class EmptySet<E> extends HashSet<E>{
        @Override
        public boolean add(E e) {
            throw new IllegalAccessError(EmptyEnum.class.getName() + "@SET不支持插入元素");
        }
    }

    private static class EmptyList<E> extends ArrayList<E>{
        @Override
        public boolean add(E e) {
            throw new IllegalAccessError(EmptyEnum.class.getName() + "@LIST不支持插入元素");
        }
    }

    private static class EmptyMap<K, V> extends HashMap<K, V>{
        @Override
        public V put(K key, V value) {
            throw new IllegalAccessError(EmptyEnum.class.getName() + "@MAP不支持插入元素");
        }
    }



}
