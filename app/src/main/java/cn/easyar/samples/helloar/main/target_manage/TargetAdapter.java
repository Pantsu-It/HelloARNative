package cn.easyar.samples.helloar.main.target_manage;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import cn.easyar.samples.helloar.R;
import cn.easyar.samples.helloar.beans.Target;
import cn.easyar.samples.helloar.tool.FileUtils;
import cn.easyar.samples.helloar.tool.ViewFactory;

/**
 * Created by Pants on 2017/4/14.
 */
public class TargetAdapter extends BaseAdapter {

    Context mContext;
    List<Target> mTargets;

    public TargetAdapter(Context context, List<Target> targets) {
        mContext = context;
        mTargets = targets;
    }

    public void setData(List<Target> targets) {
        mTargets = targets;
    }

    @Override
    public int getCount() {
        return mTargets == null ? 0 : mTargets.size();
    }

    @Override
    public Target getItem(int position) {
        return mTargets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_target, null);
            convertView.setLayoutParams(getLayoutParams(column, spaceDpi));
        }
        imageView = (ImageView) convertView;

        Target target = getItem(position);
        ViewFactory.bindView(mContext, imageView, target);
        return imageView;
    }

    public static final int column = 3;
    public static final int spaceDpi = 6;

    private ViewGroup.LayoutParams getLayoutParams(int column, int spaceDpi) {
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        int spacePixels = (int) mContext.getResources().getDisplayMetrics().density * spaceDpi;
        int cell = (widthPixels - (column - 1) * spacePixels) / column;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(cell, cell);
        return params;
    }
}
