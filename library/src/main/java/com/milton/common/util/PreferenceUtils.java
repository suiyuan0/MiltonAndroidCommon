
package com.milton.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 对SharedPreference的使用做了建议的封装，对外公布出put，get，remove，clear等等方法； 注意一点，里面所有的commit操作使用了SharedPreferencesCompat.apply进行了替代，目的是尽可能的使用apply代替commit
 * 首先说下为什么，因为commit方法是同步的，并且我们很多时候的commit操作都是UI线程中，毕竟是IO操作，尽可能异步； 所以我们使用apply进行替代，apply异步的进行写入； 但是apply相当于commit来说是new API呢，为了更好的兼容，我们做了适配；
 * SharedPreferencesCompat也可以给大家创建兼容类提供了一定的参考
 *
 * @author v-ming.lin
 */
public class PreferenceUtils {
    public static String getPrefString(Context context, String key, final String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
    }

    public static void setPrefString(Context context, final String key, final String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).commit();
    }

    public static boolean getPrefBoolean(Context context, final String key, final boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static boolean hasKey(Context context, final String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
    }

    public static void setPrefBoolean(Context context, final String key, final boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, value).commit();
    }

    public static void setPrefInt(Context context, final String key, final int value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value).commit();
    }

    public static int getPrefInt(Context context, final String key, final int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defaultValue);
    }

    public static void setPrefFloat(Context context, final String key, final float value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putFloat(key, value).commit();
    }

    public static float getPrefFloat(Context context, final String key, final float defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(key, defaultValue);
    }

    public static void setSettingLong(Context context, final String key, final long value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(key, value).commit();
    }

    public static long getPrefLong(Context context, final String key, final long defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, defaultValue);
    }

    public static void clearPreference(Context context, final SharedPreferences p) {
        final Editor editor = p.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 多进程Preferences数据共享
     * 
     * @param ctx
     * @param key
     * @param value
     */
    public static void putStringProcess(Context ctx, String key, String value) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("preference_mu", Context.MODE_MULTI_PROCESS);
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringProcess(Context ctx, String key, String defValue) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("preference_mu", Context.MODE_MULTI_PROCESS);
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({
                "unchecked", "rawtypes"
        })
        private static Method findApplyMethod() {
            try {
                Class clz = Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

}
