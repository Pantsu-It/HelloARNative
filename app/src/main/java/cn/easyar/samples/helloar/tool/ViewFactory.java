package cn.easyar.samples.helloar.tool;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.easyar.samples.helloar.R;
import cn.easyar.samples.helloar.beans.Target;
import cn.easyar.samples.helloar.beans.render.Render;
import cn.easyar.samples.helloar.beans.render.RenderType;

/**
 * Created by Pants on 2017/4/14.
 */
public class ViewFactory {

    public static void bindView(Context context, View view, Target target) {
        ImageView imageView = (ImageView) view;
        Glide.with(context)
                .load(target.getImgUri())
                .centerCrop()
                .into(imageView);
    }

    /**
     * View@layout: R.layout.item_render
     */
    public static void bindView(final Context context, View view, final Render render) {
        ImageView imageView = (ImageView) view;
        switch (render.getType()) {
            case RenderType.TYPE_TEXT:
                Glide.with(context)
                        .load(R.drawable.type_text)
                        .fitCenter()
                        .into(imageView);
                break;
            case RenderType.TYPE_IMAGE:
                Glide.with(context)
                        .load(render.getContent())
                        .centerCrop()
                        .into(imageView);
                break;
            case RenderType.TYPE_VIDEO:
                Glide.with(context)
                        .load(R.drawable.type_video)
                        .fitCenter()
                        .into(imageView);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse(render.getContent());
                        intent.setDataAndType(uri, "video/*");
                        context.startActivity(intent);
                    }
                });
                break;
        }
    }
}
