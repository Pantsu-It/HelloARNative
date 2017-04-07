package cn.easyar.samples.helloar.main;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.io.FileNotFoundException;

import cn.easyar.samples.helloar.R;
import cn.easyar.samples.helloar.ar.ARActivity;
import cn.easyar.samples.helloar.manage.ManageActivity;
import cn.easyar.samples.helloar.tool.FileUtils;
import cn.easyar.samples.helloar.tool.XUtils;

/**
 * Created by Pants on 2017/4/6.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        XUtils.init(getApplicationContext());
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_manage:
                startActivity(ManageActivity.getIntent(this));
                break;
            case R.id.btn_ar:
                startActivity(ARActivity.getIntent(this));
                break;
        }
    }


}
