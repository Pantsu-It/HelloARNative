package cn.easyar.samples.helloar.main.target_manage;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.easyar.samples.helloar.R;

/**
 * Created by Pants on 2017/4/15.
 */
public class TargetSelectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_fragment);

        Fragment fragment = TargetManageFragment.newInstance(TargetManageFragment.ACTION_SELECT);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commitAllowingStateLoss();
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, TargetSelectActivity.class);
        return intent;
    }
}
