package cn.easyar.samples.helloar.tool;

import android.graphics.Bitmap;
import android.util.LruCache;
import android.util.SparseArray;

/**
 * Created by Pants on 2017/4/18.
 */

public class ImageCache {

    static LruCache<Integer, Bitmap> bitmapLruCache;

    public static void init() {
        bitmapLruCache = new LruCache<Integer, Bitmap>(6 * 1024 * 1024) {

            @Override
            protected int sizeOf(Integer key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    public static Bitmap get(int renderId) {
        return bitmapLruCache.get(renderId);
    }
    public static void put(int renderId, Bitmap bitmap) {
        bitmapLruCache.put(renderId, bitmap);
    }
}
