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
                XUtils.toast("manage");
                Intent intent = FileUtils.selectImage(this);
                startActivityForResult(intent, 123);
                break;
            case R.id.btn_ar:
                startActivity(ARActivity.getIntent(this));
                XUtils.toast("ar");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                System.out.println("requestCode" + requestCode);
                Uri uri = data.getData();
                System.out.println(uri.getPath());

                ContentResolver cr = this.getContentResolver();
                Bitmap bmp;
                try {
                    bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    FileUtils.saveBitmap(this, bmp, FileUtils.getTargetsDir(this),
                            String.valueOf(Math.random() * 10) + ".png");
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }
}
