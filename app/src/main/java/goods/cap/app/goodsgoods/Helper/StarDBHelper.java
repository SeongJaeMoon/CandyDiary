package goods.cap.app.goodsgoods.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import goods.cap.app.goodsgoods.Model.Recent;

public class StarDBHelper {

    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;
    private final Context context;
    private static final String DATABASE_NAME = "star.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "starDB";
    private static final String COLUMN_FLAG  = "flag"; //식단, 민간약초 구분
    private static final String COLUMN_URL = "url"; //식단 이미지 경로, 약초 이미지 경로
    private static final String COLUMN_SMY = "smy"; //식단 메인이름 <-> 약초 메인이름
    private static final String COLUMN_NO = "ctnno";//식단 고유번호 <-> 약초 고유번호
    private static final String COLUMN_CN = "cntnt";//식단 정보 <-> 약초 효능
    private static final String _ID = "_id";
    private static final String DATABASE_CREATE =
            "CREATE TABLE "+ TABLE_NAME +" ("
                    +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +COLUMN_URL+" TEXT,"
                    +COLUMN_SMY+ " TEXT,"
                    +COLUMN_NO+ " TEXT,"
                    +COLUMN_CN+ " TEXT,"
                    +COLUMN_FLAG+" REAL" + ");";

    public StarDBHelper(Context context){
        this.context = context;
    }

    static class DatabaseHelper extends SQLiteOpenHelper {
        private static DatabaseHelper sInstance;

        public static synchronized DatabaseHelper getInstance(Context context) {
            if (sInstance == null) {
                sInstance = new DatabaseHelper(context.getApplicationContext());
            }
            return sInstance;
        }

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("RECENT", "upgrade to " + newVersion + " from " + oldVersion);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }
    }
    public StarDBHelper open() throws SQLException{
        //new StarDBHelper.DatabaseHelper(context);
        mDbHelper = DatabaseHelper.getInstance(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        mDbHelper.close();
        mDb.close();
    }
    public void addStar(String url, String summary, String ctnno, String cntnt, int flag){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_URL, url);
        cv.put(COLUMN_SMY, summary);
        cv.put(COLUMN_NO, ctnno);
        cv.put(COLUMN_CN, cntnt);
        cv.put(COLUMN_FLAG, flag);
        mDb.insert(TABLE_NAME,null, cv);
    }

    public boolean isStarExists(String no){
        boolean ret = true;
        String sql = "SELECT " + COLUMN_NO + " FROM " + TABLE_NAME + " ORDER BY " + _ID;
        Cursor cursor = mDb.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String temp = cursor.getString(cursor.getColumnIndex(COLUMN_NO));
            if(temp.equals(no)){
                ret = false;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return ret;
    }
    public List<Recent> getStarList(){
        String sql = "SELECT " + COLUMN_URL + "," + COLUMN_SMY + "," + COLUMN_NO + "," + COLUMN_CN + "," + COLUMN_FLAG
                + " FROM " + TABLE_NAME + " ORDER BY " + _ID;
        List<Recent> list = new ArrayList<Recent>();
        mDbHelper = DatabaseHelper.getInstance(context);
        mDb =  mDbHelper.getReadableDatabase();
        Cursor cursor =  mDb.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String imgUrl = cursor.getString(cursor.getColumnIndex(COLUMN_URL));
            String summary = cursor.getString(cursor.getColumnIndex(COLUMN_SMY));
            String ctnno = cursor.getString(cursor.getColumnIndex(COLUMN_NO));
            String cntnt = cursor.getString(cursor.getColumnIndex(COLUMN_CN));
            int flag = cursor.getInt(cursor.getColumnIndex(COLUMN_FLAG));
            list.add(new Recent(ctnno, imgUrl, summary, cntnt, flag));
            cursor.moveToNext();
        }
        if (cursor.getCount()==0){
            return null;
        }
        cursor.close();
        return list;
    }
    public void removeList(String id){
        mDbHelper = new StarDBHelper.DatabaseHelper(context);
        mDb = mDbHelper.getReadableDatabase();
        mDb.delete(TABLE_NAME, "ctnno = ?",new String[]{String.valueOf(id)});
    }

    public Cursor allListFlag(){
        String sql = "SELECT " + COLUMN_FLAG + " FROM "+ TABLE_NAME +" ORDER BY "+ _ID + " DESC";
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getReadableDatabase();
        return mDb.rawQuery(sql, null);
    }
}
