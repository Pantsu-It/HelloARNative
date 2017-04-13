package cn.easyar.samples.helloar.data_ctrl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.easyar.samples.helloar.beans.Target;

/**
 * Created by Pants on 2017/4/13.
 */
public class TargetDBHelper {

    private SQLiteDatabase mDatabase;

    private String TABLE_NAME = "target";
    private String columnTargetId = "target_id";
    private String columnTargetUri = "target_uri";

    public TargetDBHelper(SQLiteDatabase database) {
        mDatabase = database;
    }

    private ContentValues objectToValues(Target target) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnTargetUri, target.getImgUri());
        return contentValues;
    }

    private Target cursorToObject(Cursor cursor) {
        int targetId = cursor.getInt(cursor.getColumnIndex("target_id"));
        String targetUri = cursor.getString(cursor.getColumnIndex("target_uri"));

        Target target = new Target(targetId, targetUri);
        return target;
    }

    public void insert(Target target) {
        mDatabase.insertWithOnConflict(TABLE_NAME, "", objectToValues(target), SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void update(Target target) {
        mDatabase.updateWithOnConflict(TABLE_NAME, objectToValues(target),
                "", null, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public int delete(Target target) {
        int imfact = mDatabase.delete(TABLE_NAME, "target_id=?",
                new String[]{target.getTargetId() + ""});
        return imfact;
    }

//    public Target query(Target target) {
//        Cursor cursor = mDatabase.rawQuery("select * from target where target_id=?",
//                new String[]{target.getTargetId() + ""});
//        if (cursor.moveToFirst()) {
//            return cursorToObject(cursor);
//        }
//        return null;
//    }

    public List<Target> queryAll() {
        Cursor cursor = mDatabase.rawQuery("select * from target", null);
        List<Target> targets = new ArrayList<>(cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Target target = cursorToObject(cursor);
                targets.add(target);
            } while (cursor.moveToNext());
        }
        return targets;
    }

}
