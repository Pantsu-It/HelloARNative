package cn.easyar.samples.helloar.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import cn.easyar.samples.helloar.R;
import cn.easyar.samples.helloar.beans.Target;
import cn.easyar.samples.helloar.beans.render.Render;
import cn.easyar.samples.helloar.beans.render.RenderType;
import cn.easyar.samples.helloar.main.MainActivity;

/**
 * Created by Pants on 2017/4/14.
 */
public class ViewFactory {

    public static void bindImageView(Context context, ImageView imageView, String filePath) {
        Glide.with(context)
                .load(filePath)
                .centerCrop()
                .into(imageView);
    }

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

        TextView textView = (TextView) view.findViewById(R.id.textView);
        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        switch (render.getType()) {
            case RenderType.TYPE_TEXT:
                textView.setText(render.getContent());
                textView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                break;
            case RenderType.TYPE_IMAGE:
                Glide.with(context)
                        .load(render.getContent())
                        .centerCrop()
                        .into(imageView);
                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                break;
            case RenderType.TYPE_VIDEO:
                Glide.with(context)
                        .load(R.drawable.type_video)
                        .fitCenter()
                        .into(imageView);
                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File file = new File(FileUtils.getThumbDir(context), FileUtils.getThumbFileName(render.getRenderId()));
                        final Bitmap bitmap;
                        if (file.exists() && file.length() > 0) {
                        } else {
                            bitmap = FileUtils.getVideoThumbnail(render.getContent());
                            FileUtils.saveBitmap(bitmap, file);
                        }
                        MainActivity._instance.postOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(context)
                                        .load(file)
                                        .fitCenter()
                                        .into(imageView);
                            }
                        });
                    }
                }).start();
                break;
        }
    }
}
