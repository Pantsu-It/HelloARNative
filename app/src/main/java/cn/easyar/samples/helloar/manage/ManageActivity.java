package cn.easyar.samples.helloar.manage;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class ManageActivity extends Fragment {

    public static final int REQUEST_TARGET = 0x11;
    public static final int REQUEST_SRC = 0x12;

    ImageView targetView;
    ImageView srcView;

    Target target = new Target();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.activity_manage, container, false);
        targetView = (ImageView) mRootView.findViewById(R.id.iv_target);
        srcView = (ImageView) mRootView.findViewById(R.id.iv_src);

        Target target2 = ManageModel.getInstance(getActivity()).getTarget();
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
                Intent intent = FileUtils.selectImage(getActivity());
                startActivityForResult(intent, REQUEST_TARGET);
            }
        });

        srcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = FileUtils.selectImage(getActivity());
                startActivityForResult(intent, REQUEST_SRC);
            }
        });

        return mRootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_TARGET) {
            Uri uri = data.getData();
            System.out.println(uri.getPath());

            ContentResolver cr = getActivity().getContentResolver();
            Bitmap bmp;
            try {
                bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
                File file = FileUtils.saveBitmap(getActivity(), bmp, FileUtils.getTargetsDir(getActivity()),
                        FileUtils.getTargetFileName(uri.getPath()));
                targetView.setImageURI(Uri.fromFile(file));
                target.setImage(file.getAbsolutePath());
                ManageModel.getInstance(getActivity()).saveTarget(target);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_SRC) {
            Uri uri = data.getData();
            System.out.println(uri.getPath());

            ContentResolver cr = getActivity().getContentResolver();
            Bitmap bmp;
            try {
                bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
                File file = FileUtils.saveBitmap(getActivity(), bmp, FileUtils.getTargetsDir(getActivity()),
                        FileUtils.getSrcFileName(uri.getPath()));
                srcView.setImageURI(Uri.fromFile(file));
                target.setSrc(SrcType.TYPE_IMAGE, file.getAbsolutePath());
                ManageModel.getInstance(getActivity()).saveTarget(target);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static Fragment newInstance() {
        return new ManageActivity();
    }
}
