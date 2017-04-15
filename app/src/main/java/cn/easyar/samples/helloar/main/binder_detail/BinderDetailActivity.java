package cn.easyar.samples.helloar.main.binder_detail;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.easyar.samples.helloar.R;
import cn.easyar.samples.helloar.main.target_manage.TargetManageFragment;

/**
 * Created by Pants on 2017/4/15.
 */
public class BinderDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_fragment);

        int action = getIntent().getIntExtra(BinderDetailFragment.ARG_ACTION, BinderDetailFragment.ACTION_ADD);
        Fragment fragment = BinderDetailFragment.newInstance(action);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commitAllowingStateLoss();
    }

    public static Intent getIntent(Context context, int action) {
        Intent intent = new Intent(context, BinderDetailActivity.class);
        intent.putExtra(BinderDetailFragment.ARG_ACTION, action);
        return intent;
    }
}
