package cn.easyar.samples.helloar.main.binder_manage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import cn.easyar.samples.helloar.R;
import cn.easyar.samples.helloar.beans.Binder;
import cn.easyar.samples.helloar.callback.OnItemDeleteCallback;
import cn.easyar.samples.helloar.data_ctrl.SimpleDBManager;
import cn.easyar.samples.helloar.main.binder_detail.BinderDetailActivity;
import cn.easyar.samples.helloar.main.binder_detail.BinderDetailFragment;
import cn.easyar.samples.helloar.view.CommonTitleView;

/**
 * Created by Pants on 2017/4/14.
 */
public class BinderManageFragment extends Fragment implements OnItemDeleteCallback<Binder> {

    public static final int REQUEST_ADD_BINDER = 0x11;
    public static final int REQUEST_EDIT_BINDER = 0x12;

    private ListView mListView;
    private BinderManageAdapter mBinderAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_binder_manage, null);

        initTitle(mRootView);
        initView(mRootView);

        return mRootView;
    }

    private void initTitle(View rootView) {
        CommonTitleView titleView = (CommonTitleView) rootView.findViewById(R.id.title);
        titleView.setTitle("管理AR绑定信息");
        titleView.addRightAction(R.drawable.create, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = BinderDetailActivity.getIntent(getActivity(), BinderDetailFragment.ACTION_ADD);
                startActivityForResult(intent, REQUEST_ADD_BINDER);
            }
        });
    }

    private void initView(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.listView);

        List<Binder> binders = SimpleDBManager.getInstance(getActivity()).getBinderDBHelper().queryAll();
        mBinderAdapter = new BinderManageAdapter(getActivity(), binders, this);
        mListView.setAdapter(mBinderAdapter);
        mListView.setOnItemClickListener(mItemClickListener);
    }

    void refreshView() {
        List<Binder> binders = SimpleDBManager.getInstance(getActivity()).getBinderDBHelper().queryAll();
        mBinderAdapter.setData(binders);
        mBinderAdapter.notifyDataSetChanged();
    }

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = BinderDetailActivity.getIntent(getActivity(), BinderDetailFragment.ACTION_EDIT, (Binder) parent.getItemAtPosition(position));
            startActivityForResult(intent, REQUEST_EDIT_BINDER);
        }
    };

    @Override
    public void onDelete(final Binder binder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.setTitle("提示")
                .setMessage("删除后将无法恢复该AR绑定")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SimpleDBManager.getInstance(getActivity()).getBinderDBHelper().delete(binder);
                        refreshView();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            refreshView();
        }
    }

    public static Fragment newInstance() {
        Fragment fragment = new BinderManageFragment();
        return fragment;
    }
}
