package cn.easyar.samples.helloar.ar;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.easyar.samples.helloar.beans.Binder;
import cn.easyar.samples.helloar.data_ctrl.SimpleDBManager;

/**
 * Created by Pants on 2017/4/7.
 */
public class BinderManager {

    public static List<Binder> getBinders(Context context) {
        return SimpleDBManager.getInstance(context).getBinderDBHelper().queryAll();
    }

    public static final String ATTR_IMAGES = "images";

    private static String binderToJson(Binder binder) throws JSONException {
        JSONArray images = new JSONArray();
        images.put(binder.getTarget().getImgUri());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ATTR_IMAGES, images);
        return jsonObject.toString();
    }
}
