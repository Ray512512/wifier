package com.traffic.wifiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Base64;

import com.traffic.wifiapp.common.WifiApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by xiaoyu_pc on 2016/3/27.
 */
public class SPUtils {
    private static final String KEY = "wifiapp.traffic.com.wifier.spkey";
    private static SharedPreferences mPreferences=
            WifiApplication.getInstance().getSharedPreferences(KEY, Context.MODE_PRIVATE);

    public static  String getStrValue(String key) {
        return mPreferences.getString(key, "");
    }


    public static boolean getBooleanValue(String key){
        return mPreferences.getBoolean(key, false);
    }
    public static boolean getBooleanValue(String key, boolean defaultValue){
        return mPreferences.getBoolean(key, defaultValue);
    }

    /**
     * 存入某个key对应的value值
     *
     * @param key
     * @param value
     */
    public static void put(String key, Object value) {
        SharedPreferences sp =mPreferences;
        SharedPreferences.Editor edit = sp.edit();
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        }
        SharedPreferencesCompat.EditorCompat.getInstance().apply(edit);
    }


    /**
     * 得到某个key对应的值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static Object get(String key, Object defValue) {
        SharedPreferences sp = mPreferences;
        if (defValue instanceof String) {
            return sp.getString(key, (String) defValue);
        } else if (defValue instanceof Integer) {
            return sp.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Float) {
            return sp.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Long) {
            return sp.getLong(key, (Long) defValue);
        }
        return null;
    }


    public static void setObject(String key, Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(key, objectVal);
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T getObject(String key) {
        if (mPreferences.contains(key)) {
            String objectVal = mPreferences.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


}
