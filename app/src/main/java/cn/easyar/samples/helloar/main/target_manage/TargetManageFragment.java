package cn.easyar.samples.helloar.main.target_manage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import cn.easyar.samples.helloar.R;
import cn.easyar.samples.helloar.beans.Target;
import cn.easyar.samples.helloar.beans.render.Render;
import cn.easyar.samples.helloar.beans.render.RenderFactory;
import cn.easyar.samples.helloar.data_ctrl.SimpleDBManager;
import cn.easyar.samples.helloar.tool.FileUtils;
import cn.easyar.samples.helloar.view.CommonTitleView;

/**
 * Created by Pants on 2017/4/14.
 */
public class TargetManageFragment extends Fragment {

    private static final String ARG_ACTION = "arg_action";
    public static final int ACTION_MANAGE = 0x1;
    public static final int ACTION_SELECT = 0x2;

    public static final String RETURN_DATA = "return_data";

    public static final int REQUEST_TARGET = 0x11;

    private int action;

    GridView gridView;
    TargetAdapter targetAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_target_manage, null);

        initData();
        initTitle(mRootView);
        initView(mRootView);

        return mRootView;
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            action = bundle.getInt(ARG_ACTION);
        }
    }

    private void initTitle(View rootView) {
        CommonTitleView titleView = (CommonTitleView) rootView.findViewById(R.id.title);
        if (action == ACTION_MANAGE) {
            titleView.setTitle("管理AR识别目标");
        } else if (action == ACTION_SELECT) {
            titleView.setTitle("选择AR识别目标");
        }
        titleView.addRightAction(R.drawable.create, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = FileUtils.selectImage(getActivity());
                startActivityForResult(intent, REQUEST_TARGET);
            }
        });
    }

    private void initView(View rootView) {
        List<Target> targets = SimpleDBManager.getInstance(getActivity()).getTargetDBHelper().queryAll();
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        targetAdapter = new TargetAdapter(getActivity(), targets);
        gridView.setNumColumns(TargetAdapter.column);
        gridView.setAdapter(targetAdapter);
        gridView.setOnItemClickListener(mItemClickListener);
        gridView.setOnItemLongClickListener(mItemLongClickListener);
    }

    private void refreshView() {
        List<Target> targets = SimpleDBManager.getInstance(getActivity()).getTargetDBHelper().queryAll();
        targetAdapter.setData(targets);
        targetAdapter.notifyDataSetChanged();
    }

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (action == ACTION_SELECT) {
                Intent intent = new Intent();
                intent.putExtra(RETURN_DATA, (Serializable) parent.getItemAtPosition(position));
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        }
    };

    AdapterView.OnItemLongClickListener mItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(new String[]{"删除"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            Target target = (Target) parent.getItemAtPosition(position);
                            SimpleDBManager.getInstance(getActivity()).getTargetDBHelper().delete(target);
                            refreshView();
                            break;
                    }
                }
            }).create().show();

            return true;
        }
    };

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
                InputStream inputStream = cr.openInputStream(uri);
                bmp = BitmapFactory.decodeStream(inputStream);
                File file = FileUtils.saveBitmap(bmp, FileUtils.getRendersDir(getActivity()),
                        FileUtils.getRenderFileName(uri.getPath()));
                FileUtils.saveBitmap(bmp, file);
                Target target = new Target(file.getAbsolutePath());
                SimpleDBManager.getInstance(getActivity()).getTargetDBHelper().insert(target);

                refreshView();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Fragment newInstance(int action) {
        Fragment fragment = new TargetManageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_ACTION, action);
        fragment.setArguments(bundle);
        return fragment;
    }
}
