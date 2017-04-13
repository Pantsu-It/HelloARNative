package cn.easyar.samples.helloar.data_ctrl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pants on 2017/4/13.
 */
public class SimpleDBManager extends SQLiteOpenHelper {

    private static String dbName = "ar.db";
    private static int dbVersion = 1;

    public static final String SQL_CREATE_TARGET = "create table target(" +
            "target_id integer primary key autoincrement," +
            "target_uri varchar" +
            ")";

    public static final String SQL_CREATE_RENDER = "create table render(" +
            "render_id integer primary key autoincrement," +
            "render_uri varchar," +
            "render_type integer" +
            ")";

    public static final String SQL_CREATE_BINDER = "create table binder(" +
            "target_id integer primary key," +
            "render_id integer" +
            ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TARGET);
        db.execSQL(SQL_CREATE_RENDER);
        db.execSQL(SQL_CREATE_BINDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static SimpleDBManager _instance;

    private TargetDBHelper targetDBHelper;
    private RenderDBHelper renderDBHelper;
    private BinderDBHelper binderDBHelper;

    public static SimpleDBManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new SimpleDBManager(context, dbName, null, dbVersion);
        }
        return _instance;
    }

    public SimpleDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        SQLiteDatabase database = getWritableDatabase();
        targetDBHelper = new TargetDBHelper(database);
        renderDBHelper = new RenderDBHelper(database);
        binderDBHelper = new BinderDBHelper(database);
    }

    public TargetDBHelper getTargetDBHelper() {
        return targetDBHelper;
    }

    public RenderDBHelper getRenderDBHelper() {
        return renderDBHelper;
    }

    public BinderDBHelper getBinderDBHelper() {
        return binderDBHelper;
    }

}
