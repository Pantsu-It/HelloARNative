package cn.easyar.samples.helloar.manage;

import android.content.Context;

import cn.easyar.samples.helloar.beans.Target;

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

    private Target mTarget;

    public void saveTarget(Target target) {
        mTarget = target;
    }

    public Target getTarget() {
        return mTarget;
    }
}
