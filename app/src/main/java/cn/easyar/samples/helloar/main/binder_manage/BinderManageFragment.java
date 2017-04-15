package cn.easyar.samples.helloar.main.binder_manage;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.easyar.samples.helloar.R;

/**
 * Created by Pants on 2017/4/14.
 */
public class BinderManageFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_binder_manage, null);
        return mRootView;
    }

    public static Fragment newInstance() {
        Fragment fragment = new BinderManageFragment();
        return fragment;
    }
}
