package cn.easyar.samples.helloar.main.binder_detail;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

import cn.easyar.samples.helloar.R;
import cn.easyar.samples.helloar.beans.Binder;
import cn.easyar.samples.helloar.beans.Target;
import cn.easyar.samples.helloar.beans.render.Render;
import cn.easyar.samples.helloar.beans.render.RenderType;
import cn.easyar.samples.helloar.data_ctrl.SimpleDBManager;
import cn.easyar.samples.helloar.main.MainActivity;
import cn.easyar.samples.helloar.main.binder_manage.BinderManageFragment;
import cn.easyar.samples.helloar.main.render_manage.RenderManageFragment;
import cn.easyar.samples.helloar.main.render_manage.RenderSelectActivity;
import cn.easyar.samples.helloar.main.target_manage.TargetManageFragment;
import cn.easyar.samples.helloar.main.target_manage.TargetSelectActivity;
import cn.easyar.samples.helloar.tool.FileUtils;
import cn.easyar.samples.helloar.tool.XUtils;
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
    public static final String ARG_BINDER = "arg_binder";
    public static final String ARG_FROM = "arg_from";
    public static final int FROM_FRAGMENT = 0x31;
    public static final int FROM_ACTIVITY = 0x32;


    private int action;
    private Binder binder;
    private int from;

    ImageView targetView;
    ImageView renderView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_binder_detail, container, false);

        initData();
        initTitle(mRootView);
        initView(mRootView);

        return mRootView;
    }

    private void initView(View mRootView) {
        targetView = (ImageView) mRootView.findViewById(R.id.iv_target);
        renderView = (ImageView) mRootView.findViewById(R.id.iv_src);

        if (binder.getTarget() != null) {
            targetView.setImageURI(Uri.fromFile(new File(binder.getTarget().getImgUri())));
        }
        // TODO: 2017/4/16 Render多类型显示
        if (binder.getRender() != null) {
            renderView.setImageURI(Uri.fromFile(new File(binder.getRender().getContent())));
        }

        targetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TargetSelectActivity.getIntent(getActivity());
                startActivityForResult(intent, REQUEST_TARGET);
            }
        });

        renderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RenderSelectActivity.getIntent(getActivity());
                startActivityForResult(intent, REQUEST_SRC);
            }
        });
    }

    private void initTitle(View rootView) {
        CommonTitleView titleView = (CommonTitleView) rootView.findViewById(R.id.title);
        // TODO: 2017/4/15 编辑和添加Binder的 界面跳转形式
        if (action == ACTION_ADD) {
            titleView.setTitle("添加AR绑定");
        } else if (action == ACTION_EDIT) {
            titleView.setTitle("编辑AR绑定");
        }
        if (from == FROM_FRAGMENT) {
            titleView.addLeftAction("返回", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity._instance.replaceFragment(BinderManageFragment.newInstance());
                }
            });
        } else if (from == FROM_ACTIVITY) {
            titleView.addLeftAction("返回", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }

        titleView.addRightAction("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binder.getTarget() == null || binder.getRender() == null) {
                    XUtils.toast("该AR绑定不合法，请指定识别图片和渲染信息");
                    return;
                }
                if (action == ACTION_ADD) {
                    SimpleDBManager.getInstance(getActivity()).getBinderDBHelper().insert(binder);
                } else if (action == ACTION_EDIT) {
                    SimpleDBManager.getInstance(getActivity()).getBinderDBHelper().update(binder);
                }
                if (from == FROM_FRAGMENT) {
                    MainActivity._instance.replaceFragment(BinderManageFragment.newInstance());
                } else if (from == FROM_ACTIVITY) {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }

            }
        });
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            action = bundle.getInt(ARG_ACTION);
            binder = (Binder) bundle.getSerializable(ARG_BINDER);
            if (binder == null) {
                binder = new Binder(null, null);
            }
            from = bundle.getInt(ARG_FROM);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_TARGET) {
            Target target = (Target) data.getSerializableExtra(TargetManageFragment.RETURN_DATA);
            binder.setTarget(target);

            Bitmap bitmap = FileUtils.decodeBitmapFromFile(target.getImgUri(), 200, 200);
            if (bitmap != null) {
                targetView.setImageBitmap(bitmap);
            }
        } else if (requestCode == REQUEST_SRC) {
            Render render = (Render) data.getSerializableExtra(RenderManageFragment.RETURN_DATA);
            binder.setRender(render);

            switch (render.getType()) {
                case RenderType.TYPE_TEXT:
                    renderView.setImageResource(R.drawable.type_text);
                    break;
                case RenderType.TYPE_IMAGE:
                    Bitmap bitmap = FileUtils.decodeBitmapFromFile(render.getContent(), 200, 200);
                    if (bitmap != null) {
                        renderView.setImageBitmap(bitmap);
                    }
                    break;
                case RenderType.TYPE_VIDEO:
                    renderView.setImageResource(R.drawable.type_video);
                    break;
            }
        }
    }

    public static Fragment newInstance(int action) {
        Fragment fragment = new BinderDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_ACTION, action);
        bundle.putInt(ARG_FROM, FROM_FRAGMENT);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstance(int action, Binder binder) {
        Fragment fragment = new BinderDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_ACTION, action);
        bundle.putSerializable(ARG_BINDER, binder);
        bundle.putInt(ARG_FROM, FROM_ACTIVITY);
        fragment.setArguments(bundle);
        return fragment;
    }
}
