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

public class RecentDBHelper {
    //DB 변경 필요 -> Recent => Deatail에서 필요한 값들 저장 필요 (getRtnImageDc, getRtnStreFileNm, getFdNm, getCntntsNo)
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;
    private final Context context;
    private static final String DATABASE_NAME = "recent.db";
    private static final int DATABASE_VERSION = 6;
    private static final String TABLE_NAME = "recentDB";
    // recent -> 0: Diet, 1: Health
    private static final String COLUMN_FLAG  = "flag"; //식단, 민간약초 구분
    private static final String COLUMN_URL = "url"; //식단 이미지 경로, 약초 이미지 경로
    private static final String COLUMN_SMY = "smy"; //식단 메인이름 <-> 약초 메인이름
    private static final String COLUMN_NO = "ctnno";//식단 고유번호 <-> 약초 고유번호
    private static final String COLUMN_CN = "cntnt";//식단 정보 <-> 약초 효능

    private static final String _ID = "id";
    private static final String DATABASE_CREATE =
            "CREATE TABLE "+ TABLE_NAME +" ("
                    +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +COLUMN_URL+" TEXT,"
                    +COLUMN_SMY+ " TEXT,"
                    +COLUMN_NO+ " TEXT,"
                    +COLUMN_CN+ " TEXT,"
                    +COLUMN_FLAG+" REAL" + ");";

    public RecentDBHelper(Context context){
        this.context = context;
    }

    static class DatabaseHelper extends SQLiteOpenHelper {

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

    public RecentDBHelper open() throws SQLException{
        mDbHelper = new RecentDBHelper.DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
        mDb.close();
    }
    // 0. Diet인 경우, DB에 식단과 민간약초 구분(0), 메인 설명, 고유번호, 이미지 경로, 식단정보를 저장한다.
    //    Therapy인 경우, 식단과 민간약초 구분(1), 메인 설명, 고유 번호, 이미지 경로, 약초효능을 저장한다.
    public void addRecent(String url, String summary, String ctnno, String cntnt, int flag){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_URL, url);
        cv.put(COLUMN_SMY, summary);
        cv.put(COLUMN_NO, ctnno);
        cv.put(COLUMN_CN, cntnt);
        cv.put(COLUMN_FLAG, flag);
        mDb.insert(TABLE_NAME,null, cv);
    }
    // 1. DB에 이미 들어있다면, 데이터를 저장하지 않는다.(데이터 확인)
    public boolean isRecentExists(String no){
        String sql = "SELECT " + COLUMN_NO + " FROM " + TABLE_NAME + " ORDER BY " + _ID + " DESC LIMIT 3";
        Cursor cursor = mDb.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String temp = cursor.getString(cursor.getColumnIndex(COLUMN_NO));
            if(temp.equals(no)){
                cursor.close();
                return false;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return true;
    }

    // 2. DB에 데이터를 저장할 때, 최신 상태를 유지하기 위해서 마지막에 저장된 3개의 데이터를 제외하곤 삭제한다.
    public void deletOlder(){
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE " + _ID
                + " NOT IN (SELECT " + _ID + " FROM " + TABLE_NAME + " ORDER BY "+ _ID + " DESC LIMIT 3)";
        mDb.execSQL(sql);
    }

    //3. DB에 저장된 데이터를 List로 반환한다.
    public List<Recent> getList(){
        String sql = "SELECT " + COLUMN_URL + "," + COLUMN_SMY + "," + COLUMN_NO + "," + COLUMN_CN + "," + COLUMN_FLAG
                + " FROM " + TABLE_NAME + " ORDER BY " + _ID + " DESC LIMIT 3";
        List<Recent> list = new ArrayList<Recent>();
        mDbHelper = new DatabaseHelper(context);
        mDb =  mDbHelper.getReadableDatabase();
        Cursor res =  mDb.rawQuery(sql, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            String imgUrl = res.getString(res.getColumnIndex(COLUMN_URL));
            String summary = res.getString(res.getColumnIndex(COLUMN_SMY));
            String ctnno = res.getString(res.getColumnIndex(COLUMN_NO));
            String cntnt = res.getString(res.getColumnIndex(COLUMN_CN));
            int flag = res.getInt(res.getColumnIndex(COLUMN_FLAG));
            list.add(new Recent(ctnno, imgUrl, summary, cntnt, flag));
            res.moveToNext();
        }
        if (res.getCount()==0){
            res.close();
            return null;
        }
        res.close();
        return list;
    }
}
