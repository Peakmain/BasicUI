package com.peakmain.ui.utils.database;

import android.text.TextUtils;

import java.util.Locale;

public class DaoUtil {
    private DaoUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获得类名
     */
    public static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    /**
     * 数据库操作的时候根据类型进行转换
     */
    public static String getColumnType(String type) {
        String value = null;
        if (type.contains("String")) {
            value = " text";
        } else if (type.contains("int")) {
            value = " integer";
        } else if (type.contains("boolean")) {
            value = " boolean";
        } else if (type.contains("float")) {
            value = " float";
        } else if (type.contains("double")) {
            value = " double";
        } else if (type.contains("char")) {
            value = " varchar";
        } else if (type.contains("long")) {
            value = " long";
        }
        return value;
    }

    /**
     * 查询数据：cursor.getInt();将int第一个字符进行转换成大写
     */
    public static String capitalize(String string) {
        if (!TextUtils.isEmpty(string)) {
            return string.substring(0, 1).toUpperCase(Locale.US) + string.substring(1);
        }
        return string == null ? null : "";
    }
}
