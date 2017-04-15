package cn.easyar.samples.helloar.data_ctrl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.easyar.samples.helloar.beans.render.Render;

/**
 * Created by Pants on 2017/4/13.
 */
public class RenderDBHelper {

    private SQLiteDatabase mDatabase;

    private String TABLE_NAME = "render";
    private String columnRenderId = "render_id";
    private String columnRenderUri = "render_uri";
    private String columnRenderType = "render_type";

    public RenderDBHelper(SQLiteDatabase database) {
        mDatabase = database;
    }

    private ContentValues objectToValues(Render render) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnRenderId, render.getRenderId());
        contentValues.put(columnRenderUri, render.getFileUri());
        contentValues.put(columnRenderType, render.getType());
        return contentValues;
    }

    private Render cursorToObject(Cursor cursor) {
        int renderId = cursor.getInt(cursor.getColumnIndex(columnRenderId));
        String renderUri = cursor.getString(cursor.getColumnIndex(columnRenderUri));
        int renderType = cursor.getInt(cursor.getColumnIndex(columnRenderType));

        Render render = new Render(renderId, renderUri, renderType);
        return render;
    }

    public void insert(Render render) {
        mDatabase.insertWithOnConflict(TABLE_NAME, "", objectToValues(render), SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void update(Render render) {
        mDatabase.updateWithOnConflict(TABLE_NAME, objectToValues(render),
                "", null, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public int delete(Render render) {
        int imfact = mDatabase.delete(TABLE_NAME, "render_id=?",
                new String[]{render.getRenderId() + ""});
        return imfact;
    }

//    public Render query(Render render) {
//        Cursor cursor = mDatabase.rawQuery("select * from render where render_id=?",
//                new String[]{render.getRenderId() + ""});
//        if (cursor.moveToFirst()) {
//            return cursorToObject(cursor);
//        }
//        return null;
//    }

    public List<Render> queryByType(int type) {
        Cursor cursor = mDatabase.rawQuery("select * from render where render_type=?", new String[]{type + ""});
        List<Render> renders = new ArrayList<>(cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Render render = cursorToObject(cursor);
                renders.add(render);
            } while (cursor.moveToNext());
        }
        return renders;
    }

    public List<Render> queryAll() {
        Cursor cursor = mDatabase.rawQuery("select * from render", null);
        List<Render> renders = new ArrayList<>(cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Render render = cursorToObject(cursor);
                renders.add(render);
            } while (cursor.moveToNext());
        }
        return renders;
    }

}
