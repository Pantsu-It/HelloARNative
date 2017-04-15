package cn.easyar.samples.helloar.main;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import cn.easyar.samples.helloar.R;
import cn.easyar.samples.helloar.ar.ARActivity;
import cn.easyar.samples.helloar.main.binder_detail.BinderDetailFragment;
import cn.easyar.samples.helloar.main.render_manage.RenderManageFragment;
import cn.easyar.samples.helloar.main.target_manage.TargetManageFragment;
import cn.easyar.samples.helloar.tool.XUtils;


public class MainActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static MainActivity _instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _instance = this;
        XUtils.init(getApplicationContext());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(BinderDetailFragment.newInstance(BinderDetailFragment.ACTION_ADD));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _instance = null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_scan:
                startActivity(ARActivity.getIntent(this));
                break;
            case R.id.nav_binder_manage:
                break;
            case R.id.nav_binder_detail:
                fragment = BinderDetailFragment.newInstance(BinderDetailFragment.ACTION_ADD);
                break;
            case R.id.nav_target_manage:
                fragment = TargetManageFragment.newInstance(TargetManageFragment.ACTION_MANAGE);
                break;
            case R.id.nav_render_manage:
                fragment = RenderManageFragment.newInstance(TargetManageFragment.ACTION_MANAGE);
                break;
        }
        if (fragment != null) {
            replaceFragment(fragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
            return;
    }
}
