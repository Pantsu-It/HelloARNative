package cn.easyar.samples.helloar.main.render_manage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.List;

import cn.easyar.samples.helloar.R;
import cn.easyar.samples.helloar.beans.render.Render;
import cn.easyar.samples.helloar.beans.render.RenderFactory;
import cn.easyar.samples.helloar.beans.render.RenderType;
import cn.easyar.samples.helloar.data_ctrl.RenderDBHelper;
import cn.easyar.samples.helloar.data_ctrl.SimpleDBManager;
import cn.easyar.samples.helloar.main.target_manage.TargetAdapter;
import cn.easyar.samples.helloar.part.VideoActivity;
import cn.easyar.samples.helloar.tool.FileUtils;
import cn.easyar.samples.helloar.tool.XUtils;
import cn.easyar.samples.helloar.view.CommonTitleView;

/**
 * Created by Pants on 2017/4/14.
 */
public class RenderManageFragment extends Fragment {

    private static final String ARG_ACTION = "arg_action";
    public static final int ACTION_MANAGE = 0x1;
    public static final int ACTION_SELECT = 0x2;

    public static final String RETURN_DATA = "return_data";

    public static final int REQUEST_TEXT = 0x11;
    public static final int REQUEST_IMAGE = 0x12;
    public static final int REQUEST_VIDEO = 0x13;

    private int action;

    GridView gridViewText;
    GridView gridViewImage;
    GridView gridViewVideo;
    RenderAdapter renderAdapter1, renderAdapter2, renderAdapter3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_render_manage, null);

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
            titleView.setTitle("管理AR渲染素材");
        } else if (action == ACTION_SELECT) {
            titleView.setTitle("选择AR渲染素材");
        }
        titleView.addRightAction(R.drawable.create, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(new String[]{"文字", "图片", "视频"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                final TextInputEditText editText = new TextInputEditText(getActivity());


                                AlertDialog.Builder _builder = new AlertDialog.Builder(getActivity());
                                _builder.setTitle("请输入文字：")
                                        .setView(editText)
                                        .setNegativeButton("取消", null)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String inputText = editText.getText().toString().trim();
                                                if (inputText.length() == 0) {
                                                    XUtils.toast("文字内容无效");
                                                    return;
                                                }
                                                Render render = RenderFactory.createText(inputText);
                                                SimpleDBManager.getInstance(getActivity()).getRenderDBHelper().insert(render);

                                                refreshView();
                                            }
                                        })
                                        .create().show();
                                break;
                            case 1:
                                startActivityForResult(FileUtils.selectImage(getActivity()), REQUEST_IMAGE);
                                break;
                            case 2:
                                startActivityForResult(FileUtils.selectVideo(getActivity()), REQUEST_VIDEO);
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    private void initView(View rootView) {
        RenderDBHelper helper = SimpleDBManager.getInstance(getActivity()).getRenderDBHelper();

        List<Render> renders1 = helper.queryByType(RenderType.TYPE_TEXT);
        gridViewText = (GridView) rootView.findViewById(R.id.gridViewText);
        renderAdapter1 = new RenderAdapter(getActivity(), renders1);
        gridViewText.setNumColumns(TargetAdapter.column);
        gridViewText.setAdapter(renderAdapter1);
        gridViewText.setOnItemClickListener(mItemClickListener);
        gridViewText.setOnItemLongClickListener(mItemLongClickListener);

        List<Render> renders2 = helper.queryByType(RenderType.TYPE_IMAGE);
        gridViewImage = (GridView) rootView.findViewById(R.id.gridViewImage);
        renderAdapter2 = new RenderAdapter(getActivity(), renders2);
        gridViewImage.setNumColumns(TargetAdapter.column);
        gridViewImage.setAdapter(renderAdapter2);
        gridViewImage.setOnItemClickListener(mItemClickListener);
        gridViewImage.setOnItemLongClickListener(mItemLongClickListener);

        List<Render> renders3 = helper.queryByType(RenderType.TYPE_VIDEO);
        gridViewVideo = (GridView) rootView.findViewById(R.id.gridViewVideo);
        renderAdapter3 = new RenderAdapter(getActivity(), renders3);
        gridViewVideo.setNumColumns(TargetAdapter.column);
        gridViewVideo.setAdapter(renderAdapter3);
        gridViewVideo.setOnItemClickListener(mItemClickListener);
        gridViewVideo.setOnItemLongClickListener(mItemLongClickListener);
    }

    private void refreshView() {
        RenderDBHelper helper = SimpleDBManager.getInstance(getActivity()).getRenderDBHelper();

        List<Render> targets1 = helper.queryByType(RenderType.TYPE_TEXT);
        renderAdapter1.setData(targets1);
        renderAdapter1.notifyDataSetChanged();

        List<Render> targets2 = helper.queryByType(RenderType.TYPE_IMAGE);
        renderAdapter2.setData(targets2);
        renderAdapter2.notifyDataSetChanged();

        List<Render> targets3 = helper.queryByType(RenderType.TYPE_VIDEO);
        renderAdapter3.setData(targets3);
        renderAdapter3.notifyDataSetChanged();
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
            builder.setItems(new String[]{"查看", "删除"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Render render = (Render) parent.getItemAtPosition(position);
                    switch (which) {
                        case 0:
                            switch (render.getType()) {
                                case RenderType.TYPE_TEXT:
                                    break;
                                case RenderType.TYPE_IMAGE:
                                    break;
                                case RenderType.TYPE_VIDEO:
                                    Intent intent = VideoActivity.getIntent(getActivity(), render.getContent());
                                    startActivity(intent);
                                    break;
                            }
                            break;
                        case 1:
                            SimpleDBManager.getInstance(getActivity()).getRenderDBHelper().delete(render);
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
        if (requestCode == REQUEST_IMAGE) {
            Uri uri = data.getData();
            ContentResolver cr = getActivity().getContentResolver();
            Bitmap bmp;
            try {
                bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
                File file = FileUtils.saveBitmap(bmp, FileUtils.getTargetsDir(getActivity()),
                        FileUtils.getTargetFileName(uri.getPath()));
                Render render = RenderFactory.createImage(file.getAbsolutePath());
                SimpleDBManager.getInstance(getActivity()).getRenderDBHelper().insert(render);
                refreshView();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_VIDEO) {
            Uri uri = data.getData();
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                // String imgNo = cursor.getString(0); // 图片编号
                String v_path = cursor.getString(1); // 图片文件路径
                String v_size = cursor.getString(2); // 图片大小
                String v_name = cursor.getString(3); // 图片文件名

                Render render = RenderFactory.createVideo(v_path);
                SimpleDBManager.getInstance(getActivity()).getRenderDBHelper().insert(render);
                refreshView();
            }
        }
    }

    public static Fragment newInstance(int action) {
        Fragment fragment = new RenderManageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_ACTION, action);
        fragment.setArguments(bundle);
        return fragment;
    }
}
