/**
* Copyright (c) 2015-2016 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
* EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
* and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
*/

package cn.easyar.samples.helloar.ar;

import android.opengl.GLSurfaceView;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.easyar.samples.helloar.beans.Binder;
import cn.easyar.samples.helloar.beans.render.RenderType;
import cn.easyar.samples.helloar.main.MainActivity;

public class Renderer implements GLSurfaceView.Renderer {

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        ARModel.nativeInitGL();
        load();
    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
        ARModel.nativeResizeGL(w, h);
    }

    public void onDrawFrame(GL10 gl) {
        ARModel.nativeRender();
    }

    void load() {
        boolean a = true;
        List<Binder> binders = BinderManager.getBinders(MainActivity._instance);
        for (Binder binder : binders) {
            ARModel.loadBinder(binder);
            if (a && binder.getRender().getType() == RenderType.TYPE_IMAGE) {
                a = true;

//                ARModel.sendImageTexture(binder.getRender().getContent());
                ARModel.sendBitmap(binder.getRender().getContent());
            }
        }
    }

}
