package cn.easyar.samples.helloar.main.ar_detail;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;

import cn.easyar.samples.helloar.R;
import cn.easyar.samples.helloar.beans.Target;
import cn.easyar.samples.helloar.beans.render.ImageRender;
import cn.easyar.samples.helloar.beans.Binder;
import cn.easyar.samples.helloar.main.ar_manage.ManageModel;
import cn.easyar.samples.helloar.tool.FileUtils;
import cn.easyar.samples.helloar.view.CommonTitleView;

/**
 * Created by Pants on 2017/4/7.
 */
public class BinderDetailFragment extends Fragment {

    public static final int REQUEST_TARGET = 0x11;
    public static final int REQUEST_SRC = 0x12;

    public static final String ARG_ACTION = "arg_action";
    public static final int ACTION_ADD = 0x21;
    public static final int ACTION_EDIT = 0x22;

    private int action;

    ImageView targetView;
    ImageView srcView;

    Binder target = new Binder(null, null);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.activity_binder_detail, container, false);
        targetView = (ImageView) mRootView.findViewById(R.id.iv_target);
        srcView = (ImageView) mRootView.findViewById(R.id.iv_src);

        Binder target2 = ManageModel.getInstance(getActivity()).getTarget();
        if(target2 != null) {
            target = target2;
            if(target.getTarget() != null) {
                targetView.setImageURI(Uri.fromFile(new File(target.getTarget().getImgUri())));
            }
            if(target.getRender() != null) {
                srcView.setImageURI(Uri.fromFile(new File(target.getRender().getFileUri())));
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

        initTitle(mRootView);
        initData();

        return mRootView;
    }

    private void initTitle(View rootView) {
        CommonTitleView titleView = (CommonTitleView) rootView.findViewById(R.id.title);
        titleView.setTitle("编辑AR绑定");
        titleView.addLeftBackAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        titleView.addRightAction("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initData() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            action = bundle.getInt(ARG_ACTION);
        }
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
                target.setTarget(new Target(file.getAbsolutePath()));
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
                target.setRender(new ImageRender(file.getAbsolutePath()));
                ManageModel.getInstance(getActivity()).saveTarget(target);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static Fragment newInstance(int action) {
        Fragment fragment = new BinderDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_ACTION, action);
        fragment.setArguments(bundle);
        return fragment;
    }
}
