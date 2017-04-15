package cn.easyar.samples.helloar.main.render_manage;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.easyar.samples.helloar.R;

/**
 * Created by Pants on 2017/4/15.
 */
public class RenderSelectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_fragment);

        Fragment fragment = RenderManageFragment.newInstance(RenderManageFragment.ACTION_SELECT);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commitAllowingStateLoss();
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, RenderSelectActivity.class);
        return intent;
    }
}
