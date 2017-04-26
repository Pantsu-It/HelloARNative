package cn.easyar.samples.helloar.ar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import cn.easyar.engine.EasyAR;
import cn.easyar.samples.helloar.beans.Binder;
import cn.easyar.samples.helloar.beans.Target;
import cn.easyar.samples.helloar.beans.render.Render;
import cn.easyar.samples.helloar.tool.FileUtils;

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
    private static native void nativeLoadTarget(String path, String uid);
    private static native void nativeSendTextureId(int textureId);
    private static native void nativeSendBitmap(Bitmap bitmap);

    public static void init(Activity activity) {
        EasyAR.initialize(activity, key);
    }

    public static void loadBinder(Binder binder) {
        Target target = binder.getTarget();
        Render render = binder.getRender();

        String path = target.getImgUri();
        String uid = "type:" + render.getType() + ", content:" + render.getContent();
        loadImage(path, uid);
    }

    public static void loadImage(String path, String uid) {
        nativeLoadTarget(path, uid);
    }

    public static void sendImageTexture(String filePath) {
        Bitmap bitmap = FileUtils.decodeBitmapFromFile(filePath, 200, 200);

        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

//        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 20, 20, 0, bitmap);

        int textureId = textures[0];
        nativeSendTextureId(textureId);
    }

    public static void sendBitmap(String filePath) {
        Bitmap bitmap = FileUtils.decodeBitmapFromFile(filePath, 200, 200);
        nativeSendBitmap(bitmap);
    }
}
