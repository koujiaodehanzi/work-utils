package com.wyk.utils;

import java.lang.reflect.Field;

/**
 * @author wuyankun
 * @title: BeanFieldUtil
 * @projectName work-utils
 * @description: TODO
 * @date 2020/7/23 14:06
 */
public class BeanFieldUtil {

    /**
     * 将对象中String类型的字段去除空白后缀
     * @param obj
     */
    public static void stringFieldTrim(Object obj){
        if (obj == null) return;
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field f : fields){
                if (f.getType().equals(String.class)){
                    f.setAccessible(true);
                    String value = (String) f.get(obj);
                    if (value != null){
                        f.set(obj,value.trim());
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
