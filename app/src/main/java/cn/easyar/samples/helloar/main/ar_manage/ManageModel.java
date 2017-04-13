package cn.easyar.samples.helloar.main.ar_manage;

import android.content.Context;

import cn.easyar.samples.helloar.beans.Binder;

/**
 * Created by Pants on 2017/4/7.
 */
public class ManageModel {

    private static ManageModel _instance;

    public static ManageModel getInstance(Context context) {
        if (_instance == null) {
            _instance = new ManageModel(context);
        }
        return _instance;
    }

    private Context mContext;

    public ManageModel(Context context) {
        mContext = context;
    }

    private Binder mTarget;

    public void saveTarget(Binder target) {
        mTarget = target;
    }

    public Binder getTarget() {
        return mTarget;
    }
}
