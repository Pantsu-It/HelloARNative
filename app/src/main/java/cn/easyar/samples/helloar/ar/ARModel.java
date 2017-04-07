package cn.easyar.samples.helloar.ar;

import android.app.Activity;
import android.util.JsonWriter;

import cn.easyar.engine.EasyAR;

/**
 * Created by Pants on 2017/4/6.
 */
public class ARModel {

    /*
    * Steps to create the key for this sample:
    *  1. login www.easyar.com
    *  2. create app with
    *      Name: HelloAR
    *      Package Name: cn.easyar.samples.helloar
    *  3. find the created item in the list and show key
    *  4. set key string bellow
    */
    static String key = "bxwk4A1SnqoCK3qHdOzsqgPD8m2QPBQmHenwIYBgvnWfuqVniMbfId9uLE8YA3zAaQL6pgo0GZSPuqxIfd4n37R79cMKIeUuh3Fm977fac56bf476b9f026a47a5c5a674b63y21Jx6F2IKyflpzqJf83pGp4e6pMSbyvJy708J2JEAa2xGkxBvSIposIPtANmbPRcen";

    static {
        System.loadLibrary("HelloARNative");
    }

    public static native void nativeInitGL();
    public static native void nativeResizeGL(int w, int h);
    public static native void nativeRender();
    public static native boolean nativeInit();
    public static native void nativeDestory();
    public static native void nativeRotationChange(boolean portrait);
    public static native void nativeLoadTarget(String path);

    public static void init(Activity activity) {
        EasyAR.initialize(activity, key);
    }

    public static void loadImage(String path) {
        nativeLoadTarget(path);
    }

}
