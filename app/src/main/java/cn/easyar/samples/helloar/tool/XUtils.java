package cn.easyar.samples.helloar.tool;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Pants on 2017/4/6.
 */
public class XUtils {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static void toast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }
}
