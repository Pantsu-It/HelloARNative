package cn.easyar.samples.helloar.main.binder_manage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cn.easyar.samples.helloar.R;
import cn.easyar.samples.helloar.beans.Binder;
import cn.easyar.samples.helloar.callback.OnItemDeleteCallback;
import cn.easyar.samples.helloar.tool.ViewFactory;

/**
 * Created by Pants on 2017/4/14.
 */
public class BinderAdapter extends BaseAdapter {

    Context mContext;
    List<Binder> mBinders;
    OnItemDeleteCallback<Binder> mItemDeleteCallback;

    public BinderAdapter(Context context, List<Binder> binders,
                         OnItemDeleteCallback<Binder> onItemDeleteCallback) {
        mContext = context;
        mBinders = binders;
        mItemDeleteCallback = onItemDeleteCallback;
    }

    public void setData(List<Binder> binders) {
        mBinders = binders;
    }

    @Override
    public int getCount() {
        return mBinders == null ? 0 : mBinders.size();
    }

    @Override
    public Binder getItem(int position) {
        return mBinders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_binder, null);
        }
        itemView = convertView;

        Binder binder = getItem(position);

        View targetView = itemView.findViewById(R.id.targetView);
        ViewFactory.bindView(mContext, targetView, binder.getTarget());
        View renderView = itemView.findViewById(R.id.renderView);
        ViewFactory.bindView(mContext, renderView, binder.getRender());

        View btnDelete = itemView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemDeleteCallback != null) {
                    mItemDeleteCallback.onDelete(getItem(position));
                }
            }
        });

        return itemView;
    }
}
