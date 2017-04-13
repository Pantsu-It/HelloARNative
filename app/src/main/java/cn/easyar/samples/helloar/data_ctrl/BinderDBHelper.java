package cn.easyar.samples.helloar.data_ctrl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.easyar.samples.helloar.beans.Binder;
import cn.easyar.samples.helloar.beans.Target;
import cn.easyar.samples.helloar.beans.render.Render;

/**
 * Created by Pants on 2017/4/13.
 */
public class BinderDBHelper {

    private SQLiteDatabase mDatabase;

    private String TABLE_NAME = "binder";
    private String columnTargetId = "target_id";
    private String columnRenderId = "render_id";

    public BinderDBHelper(SQLiteDatabase database) {
        mDatabase = database;
    }

    private ContentValues objectToValues(Binder binder) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnTargetId, binder.getTarget().getTargetId());
        contentValues.put(columnRenderId, binder.getRender().getRenderId());
        return contentValues;
    }

    private Binder cursorToObject(Cursor cursor) {
        int targetId = cursor.getInt(cursor.getColumnIndex("target_id"));
        String targetUri = cursor.getString(cursor.getColumnIndex("target_uri"));
        int renderId = cursor.getInt(cursor.getColumnIndex("render_id"));
        String renderUri = cursor.getString(cursor.getColumnIndex("render_uri"));
        int renderType = cursor.getInt(cursor.getColumnIndex("render_type"));

        Target target = new Target(targetId, targetUri);
        Render render = new Render(renderId, renderUri, renderType);
        Binder binder = new Binder(target, render);
        return binder;
    }

    public void insert(Binder binder) {
        mDatabase.insertWithOnConflict(TABLE_NAME, "", objectToValues(binder), SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void update(Binder binder) {
        mDatabase.updateWithOnConflict(TABLE_NAME, objectToValues(binder),
                "target_id=?", new String[]{binder.getTarget().getTargetId() + ""}, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public int delete(Target target) {
        int imfact = mDatabase.delete(TABLE_NAME, "target_id=?",
                new String[]{target.getTargetId() + ""});
        return imfact;
    }

    public void delete(Binder binder) {
        mDatabase.delete(TABLE_NAME, "target_id=? & render_id=?",
                new String[]{binder.getTarget().getTargetId() + "", binder.getRender().getRenderId() + ""});
    }

    public Binder query(Target target) {
        Cursor cursor = mDatabase.rawQuery("select * from binder where target_id=?",
                new String[]{target.getTargetId() + ""});
        if (cursor.moveToFirst()) {
            return cursorToObject(cursor);
        }
        return null;
    }

    public Binder query(Binder binder) {
        Cursor cursor = mDatabase.rawQuery("select * from binder natural join target natural join render where target_id=? & render_id=?",
                new String[]{binder.getTarget().getTargetId() + "", binder.getRender().getRenderId() + ""});
        if (cursor.moveToFirst()) {
            return cursorToObject(cursor);
        }
        return null;
    }

    public List<Binder> queryAll() {
        Cursor cursor = mDatabase.rawQuery("select * from binder", null);
        List<Binder> binders = new ArrayList<>(cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Binder binder = cursorToObject(cursor);
                binders.add(binder);
            } while (cursor.moveToNext());
        }
        return binders;
    }
}
