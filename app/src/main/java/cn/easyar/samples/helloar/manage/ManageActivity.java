package cn.easyar.samples.helloar.manage;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;

import cn.easyar.samples.helloar.R;
import cn.easyar.samples.helloar.beans.SrcType;
import cn.easyar.samples.helloar.beans.Target;
import cn.easyar.samples.helloar.tool.FileUtils;

/**
 * Created by Pants on 2017/4/7.
 */
public class ManageActivity extends Activity {

    public static final int REQUEST_TARGET = 0x11;
    public static final int REQUEST_SRC = 0x12;

    ImageView targetView;
    ImageView srcView;

    Target target = new Target();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        targetView = (ImageView) findViewById(R.id.iv_target);
        srcView = (ImageView) findViewById(R.id.iv_src);

        Target target2 = ManageModel.getInstance(this).getTarget();
        if(target2 != null) {
            target = target2;
            if(target.getImage() != null) {
                targetView.setImageURI(Uri.fromFile(new File(target.getImage())));
            }
            if(target.getSrcUrl() != null) {
                srcView.setImageURI(Uri.fromFile(new File(target.getSrcUrl())));
            }
        }

        targetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = FileUtils.selectImage(ManageActivity.this);
                startActivityForResult(intent, REQUEST_TARGET);
            }
        });

        srcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = FileUtils.selectImage(ManageActivity.this);
                startActivityForResult(intent, REQUEST_SRC);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_TARGET) {
            Uri uri = data.getData();
            System.out.println(uri.getPath());

            ContentResolver cr = this.getContentResolver();
            Bitmap bmp;
            try {
                bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
                File file = FileUtils.saveBitmap(this, bmp, FileUtils.getTargetsDir(this),
                        FileUtils.getTargetFileName(uri.getPath()));
                targetView.setImageURI(Uri.fromFile(file));
                target.setImage(file.getAbsolutePath());
                ManageModel.getInstance(this).saveTarget(target);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_SRC) {
            Uri uri = data.getData();
            System.out.println(uri.getPath());

            ContentResolver cr = this.getContentResolver();
            Bitmap bmp;
            try {
                bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
                File file = FileUtils.saveBitmap(this, bmp, FileUtils.getTargetsDir(this),
                        FileUtils.getSrcFileName(uri.getPath()));
                srcView.setImageURI(Uri.fromFile(file));
                target.setSrc(SrcType.TYPE_IMAGE, file.getAbsolutePath());
                ManageModel.getInstance(this).saveTarget(target);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, ManageActivity.class);
        return intent;
    }
}
