package cn.easyar.samples.helloar.main.render_manage;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import cn.easyar.samples.helloar.R;
import cn.easyar.samples.helloar.beans.render.Render;
import cn.easyar.samples.helloar.beans.render.RenderType;
import cn.easyar.samples.helloar.tool.FileUtils;
import cn.easyar.samples.helloar.tool.ViewFactory;

/**
 * Created by Pants on 2017/4/14.
 */
public class RenderAdapter extends BaseAdapter {

    Context mContext;
    List<Render> mRenders;

    public RenderAdapter(Context context, List<Render> renders) {
        mContext = context;
        mRenders = renders;
    }

    public void setData(List<Render> renders) {
        mRenders = renders;
    }

    @Override
    public int getCount() {
        return mRenders == null ? 0 : mRenders.size();
    }

    @Override
    public Render getItem(int position) {
        return mRenders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_render, null);
            convertView.setLayoutParams(getLayoutParams(column, spaceDpi));
        }
        itemView = convertView;

        Render render = getItem(position);
        ViewFactory.bindView(mContext, itemView, render);
        return itemView;
    }

    public static final int column = 3;
    public static final int spaceDpi = 6;

    private ViewGroup.LayoutParams getLayoutParams(int column, int spaceDpi) {
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        int spacePixels = (int) mContext.getResources().getDisplayMetrics().density * spaceDpi;
        int cell = (widthPixels - (column + 1) * spacePixels) / column;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(cell, cell);
        return params;
    }
}
