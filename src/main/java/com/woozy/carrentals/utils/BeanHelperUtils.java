package com.woozy.carrentals.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanHelperUtils {
    public static void copyProperties(Object dest, Object source) {
        if (source == null || dest == null) {
            throw new IllegalArgumentException("Source and destination must not be null");
        }

        Class<?> sourceClass = source.getClass();
        Class<?> destClass = dest.getClass();

        for (Field sourceField : sourceClass.getDeclaredFields()) {
            sourceField.setAccessible(true);
            try {
                Object value = sourceField.get(source);
                // Ignore null values
                if (value != null) {
                    Field destField;
                    try {
                        destField = destClass.getDeclaredField(sourceField.getName());
                        destField.setAccessible(true);
                        destField.set(dest, value);
                    } catch (NoSuchFieldException ignored) {
                        // Ignore fields that don't exist in the destination class
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to copy properties", e);
            }
        }
    }

    public static boolean areAllPropertiesNull(Object bean) {
        if (bean == null) {
            return false;
        }

        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(bean) != null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }
        return true;
    }
}
