/**
* Copyright (c) 2015-2016 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
* EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
* and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
*/

package cn.easyar.samples.helloar.ar;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;

import cn.easyar.engine.EasyAR;
import cn.easyar.samples.helloar.R;


public class ARActivity extends ActionBarActivity{

    static {
        System.loadLibrary("EasyAR");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ARModel.init(this);
        ARModel.nativeInit();

        GLView glView = new GLView(this);
        glView.setRenderer(new Renderer());
        glView.setZOrderMediaOverlay(true);

        ((ViewGroup) findViewById(R.id.preview)).addView(glView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ARModel.nativeRotationChange(getWindowManager().getDefaultDisplay().getRotation() == android.view.Surface.ROTATION_0);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        ARModel.nativeRotationChange(getWindowManager().getDefaultDisplay().getRotation() == android.view.Surface.ROTATION_0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ARModel.nativeDestory();
    }
    @Override
    protected void onResume() {
        super.onResume();
        EasyAR.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EasyAR.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, ARActivity.class);
        return intent;
    }
}
