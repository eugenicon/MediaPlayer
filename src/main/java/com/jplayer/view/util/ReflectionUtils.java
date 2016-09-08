package com.jplayer.view.util;

import java.lang.reflect.Field;

public class ReflectionUtils {

    @SuppressWarnings("unchecked")
    public static <T> T getObjectField(Object o, String fieldName) {
        Field declaredField = null;
        Class oClass = o.getClass();
        while (declaredField == null && oClass != null) {
            try {
                declaredField = oClass.getDeclaredField(fieldName);
                declaredField.setAccessible(true);
                return (T) declaredField.get(o);
            } catch (Exception e) {
                oClass = oClass.getSuperclass();
            }
        }
        return null;
    }
}