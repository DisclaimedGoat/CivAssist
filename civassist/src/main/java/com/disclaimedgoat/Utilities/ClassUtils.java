package com.disclaimedgoat.Utilities;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ClassUtils {

    public static <T> Map<String, Object> mapClass(T obj) {
        Class<?> clazz = obj.getClass();
        Map<String, Object> myObjectAsDict = new HashMap<>();

        for (Field field : clazz.getFields()) {
            try {
                myObjectAsDict.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {

            }
        }
        return myObjectAsDict;
    }

}
