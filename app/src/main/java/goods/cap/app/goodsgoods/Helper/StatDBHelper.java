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

import goods.cap.app.goodsgoods.Model.Statistic;

public class StatDBHelper {

    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;
    private final Context context;
    private static final String DATABASE_NAME = "stat.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME1 = "statCAL"; //표준
    //표준
    private static final String COLUMN_DATE = "dietdate"; //날짜
    private static final String COLUMN_BRKFAST = "brkfast"; //아침 칼로리
    private static final String COLUMN_LUNCH = "lunch"; //점심 칼로리
    private static final String COLUMN_DINNER = "dinner"; //저녁 칼로리
    private static final String COLUMN_SNACK = "snack"; //간식 칼로리
    private static final String COLUMN_DESSERT = "dessert"; //후식 칼로리
    //함께한 사람
    //날짜 포함
    //사진도 넣어야 함!! 아침, 점심 , 저녁, 간식, 후식
    private static final String COLUMN_WHO_BRKFAST = "whobrkfast";
    private static final String COLUMN_IMG_BRKFAST = "imgbrkfast";
    private static final String COLUMN_WHO_LUNCH = "wholunch";
    private static final String COLUMN_IMG_LUNCH = "imglunch";
    private static final String COLUMN_WHO_DINNER = "whodinner";
    private static final String COLUMN_IMG_DINNER = "imgdinner";
    private static final String COLUMN_WHO_SNACK = "whosnack";
    private static final String COLUMN_IMG_SNACK = "imgsnack";
    private static final String COLUMN_WHO_DESSERT = "whodessert";
    private static final String COLUMN_IMG_DESSERT = "imgdessert";
    //식사 시작 시간, 종료 시간
    //날짜 포함
    private static final String COLUMN_BRKFAST_STRTIME = "brkstrtime";
    private static final String COLUMN_BRKFAST_ENDTIME = "brkendtime";
    private static final String COLUMN_LUNCH_STRTIME = "lchstrtime";
    private static final String COLUMN_LUNCH_ENDTIME = "lchendtime";
    private static final String COLUMN_DINNER_STRTIME = "dinstrtime";
    private static final String COLUMN_DINNER_ENDTIME = "dinendtime";
    private static final String COLUMN_SNACK_STRTIME = "snkstrtime";
    private static final String COLUMN_SNACK_ENDTIME = "snkendtime";
    private static final String COLUMN_DESSERT_STRTIME = "desstrtime";
    private static final String COLUMN_DESSERT_ENDTIME = "desendtime";

    private static final String _ID = "_id";

    private static final String DATABASE_CREATE1 =
            "CREATE TABLE "+ TABLE_NAME1 +" ("
                    +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +COLUMN_DATE+" TEXT,"
                    +COLUMN_BRKFAST+ " REAL,"
                    +COLUMN_LUNCH+ " REAL,"
                    +COLUMN_DINNER+ " REAL,"
                    +COLUMN_SNACK+ " REAL,"
                    +COLUMN_DESSERT+ " REAL,"
                    +COLUMN_WHO_BRKFAST+ " TEXT,"
                    +COLUMN_IMG_BRKFAST+ " TEXT,"
                    +COLUMN_WHO_LUNCH+ " TEXT,"
                    +COLUMN_IMG_LUNCH+ " TEXT,"
                    +COLUMN_WHO_DINNER+ " TEXT,"
                    +COLUMN_IMG_DINNER+ " TEXT,"
                    +COLUMN_WHO_SNACK+ " TEXT,"
                    +COLUMN_IMG_SNACK+ " TEXT,"
                    +COLUMN_WHO_DESSERT+ " TEXT,"
                    +COLUMN_IMG_DESSERT+ " TEXT,"
                    +COLUMN_BRKFAST_STRTIME+ " TEXT,"
                    +COLUMN_BRKFAST_ENDTIME+ " TEXT,"
                    +COLUMN_LUNCH_STRTIME+ " TEXT,"
                    +COLUMN_LUNCH_ENDTIME+ " TEXT,"
                    +COLUMN_DINNER_STRTIME+ " TEXT,"
                    +COLUMN_DINNER_ENDTIME+ " TEXT,"
                    +COLUMN_SNACK_STRTIME+ " TEXT,"
                    +COLUMN_SNACK_ENDTIME+ " TEXT,"
                    +COLUMN_DESSERT_STRTIME+ " TEXT,"
                    +COLUMN_DESSERT_ENDTIME+ " TEXT" + ");";

//    private static final String DATABASE_CREATE2 =
//            "CREATE TABLE "+ TABLE_NAME2 +" ("
//                    +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    +COLUMN_DATE+" TEXT,"
//                    +COLUMN_WHO_BRKFAST+ " TEXT,"
//                    +COLUMN_IMG_BRKFAST+ " TEXT,"
//                    +COLUMN_WHO_LUNCH+ " TEXT,"
//                    +COLUMN_IMG_LAUNCH+ " TEXT,"
//                    +COLUMN_WHO_DINNER+ " TEXT,"
//                    +COLUMN_IMG_DINNER+ " TEXT,"
//                    +COLUMN_WHO_SNACK+ " TEXT,"
//                    +COLUMN_IMG_SNACK+ " TEXT,"
//                    +COLUMN_IMG_DESSERT+ " TEXT,"
//                    +COLUMN_WHO_DESSERT+ " TEXT" + ");";
//
//    private static final String DATABASE_CREATE3 =
//            "CREATE TABLE "+ TABLE_NAME3 +" ("
//                    +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    +COLUMN_DATE+" TEXT,"
//                    +COLUMN_BRKFAST_STRTIME+ " TEXT,"
//                    +COLUMN_BRKFAST_ENDTIME+ " REAL,"
//                    +COLUMN_LUNCH_STRTIME+ " TEXT,"
//                    +COLUMN_LUNCH_ENDTIME+ " TEXT,"
//                    +COLUMN_DINNER_STRTIME+ " TEXT,"
//                    +COLUMN_DINNER_ENDTIME+ " TEXT,"
//                    +COLUMN_SNACK_STRTIME+ " TEXT,"
//                    +COLUMN_SNACK_ENDTIME+ " TEXT,"
//                    +COLUMN_DESSERT_STRTIME+ " TEXT,"
//                    +COLUMN_DESSERT_ENDTIME+ " TEXT" + ");";

    public StatDBHelper(Context context){
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
            Log.i("Stat", "upgrade to " + newVersion + " from " + oldVersion);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
            onCreate(db);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w("StatDBHelper","CREATE!!!!!!!!!!!!!!");
            db.execSQL(DATABASE_CREATE1);
//            db.execSQL(DATABASE_CREATE2);
//            db.execSQL(DATABASE_CREATE3);
        }
    }
    public StatDBHelper open() throws SQLException {
        mDbHelper = StatDBHelper.DatabaseHelper.getInstance(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        mDbHelper.close();
        mDb.close();
    }
    //식단
    public void initDiet(String key, String kal, String date){

    }
    //식단
    public void insertDiet(String key, String kal, String date){
        String col = "";
        switch (key){
            case "아침": col = COLUMN_BRKFAST; break;
            case "점심": col = COLUMN_LUNCH; break;
            case "저녁": col = COLUMN_DINNER; break;
            case "간식": col = COLUMN_SNACK; break;
            case "후식": col = COLUMN_DESSERT; break;
        }
        String sql = "UPDATE " + TABLE_NAME1 + " SET " + col + "='" + kal + "' WHERE dietdate='" + date +"'";
        mDb.execSQL(sql);
    }
    //이미지
    public void insertImg(String key, String img, String date){
        String col = "";
        switch (key){
            case "아침": col = COLUMN_IMG_BRKFAST; break;
            case "점심": col = COLUMN_IMG_LUNCH; break;
            case "저녁": col = COLUMN_IMG_DINNER; break;
            case "간식": col = COLUMN_IMG_SNACK; break;
            case "후식": col = COLUMN_IMG_DINNER; break;
        }
        String sql = "UPDATE " + TABLE_NAME1 + " SET " + col + "='" + img + "' WHERE dietdate='" + date + "'";
        mDb.execSQL(sql);
    }
    //이미지
    public void initImg(String key, String img, String date){

    }
    //함께한 사람
    public void insertWith(String key, String with, String date){
        String col = "";
        switch (key){
            case "아침": col = COLUMN_WHO_BRKFAST; break;
            case "점심": col = COLUMN_WHO_LUNCH; break;
            case "저녁": col = COLUMN_WHO_DINNER; break;
            case "간식": col = COLUMN_WHO_SNACK; break;
            case "후식": col = COLUMN_WHO_DESSERT; break;
        }
        String sql = "UPDATE " + TABLE_NAME1 + " SET " + col + "='" + with + "' WHERE dietdate='" + date + "'";
        mDb.execSQL(sql);
    }
    //함께한 사람
    public void initWith(String key, String with, String date){

    }
    //시작, 종료 시간
    public void insertStartEndTime(String key, String start, String end, String date){
        String col = "";
        String col2 = "";
        switch (key){
            case "아침":
                col = COLUMN_BRKFAST_STRTIME;
                col2 = COLUMN_BRKFAST_ENDTIME;
                break;
            case "점심":
                col = COLUMN_LUNCH_STRTIME;
                col2 = COLUMN_LUNCH_ENDTIME;
                break;
            case "저녁":
                col = COLUMN_DINNER_STRTIME;
                col2 = COLUMN_DINNER_ENDTIME;
                break;
            case "간식":
                col = COLUMN_SNACK_STRTIME;
                col2 = COLUMN_SNACK_ENDTIME;
                break;
            case "후식":
                col = COLUMN_DESSERT_STRTIME;
                col2 = COLUMN_DESSERT_ENDTIME;
                break;
        }
        String sql = "UPDATE " + TABLE_NAME1 + " SET " + col + "='" + start + "', " + col2 + "='" + end + "' WHERE dietdate='" + date + "'";
        mDb.execSQL(sql);
    }
    //시작, 종료 시간
    public void initTime(String key, String start, String end, String date){

    }

    public long initDatas(String date){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_BRKFAST, 0);
        contentValues.put(COLUMN_LUNCH, 0);
        contentValues.put(COLUMN_DINNER, 0);
        contentValues.put(COLUMN_SNACK, 0);
        contentValues.put(COLUMN_DESSERT, 0);
        contentValues.put(COLUMN_WHO_BRKFAST, "");
        contentValues.put(COLUMN_IMG_BRKFAST, "");
        contentValues.put(COLUMN_WHO_LUNCH, "");
        contentValues.put(COLUMN_IMG_LUNCH, "");
        contentValues.put(COLUMN_WHO_DINNER, "");
        contentValues.put(COLUMN_IMG_DINNER, "");
        contentValues.put(COLUMN_WHO_SNACK, "");
        contentValues.put(COLUMN_IMG_SNACK, "");
        contentValues.put(COLUMN_WHO_DESSERT, "");
        contentValues.put(COLUMN_IMG_DESSERT, "");
        contentValues.put(COLUMN_BRKFAST_STRTIME, "");
        contentValues.put(COLUMN_BRKFAST_ENDTIME, "");
        contentValues.put(COLUMN_LUNCH_STRTIME, "");
        contentValues.put(COLUMN_LUNCH_ENDTIME, "");
        contentValues.put(COLUMN_DINNER_STRTIME, "");
        contentValues.put(COLUMN_DINNER_ENDTIME, "");
        contentValues.put(COLUMN_SNACK_STRTIME, "");
        contentValues.put(COLUMN_SNACK_ENDTIME, "");
        contentValues.put(COLUMN_DESSERT_STRTIME, "");
        contentValues.put(COLUMN_DESSERT_ENDTIME, "");
        long id = mDb.insert(TABLE_NAME1, null,contentValues);
        return id;
    }

    public void updateWho(String key, String who, String date){
        String col = "";
        switch (key){
            case "아침": col = COLUMN_WHO_BRKFAST; break;
            case "점심": col = COLUMN_WHO_LUNCH; break;
            case "저녁": col = COLUMN_WHO_DINNER; break;
            case "간식": col = COLUMN_WHO_SNACK; break;
            case "후식": col = COLUMN_WHO_DESSERT; break;
        }
        String sql = "UPDATE " + TABLE_NAME1 + " SET " + col + "='" + who + "' WHERE date='" + date + "'";
        mDb.execSQL(sql);
    }
    //String sql = "SELECT " + COLUMN_DATE + "," + COLUMN_NAME +  "," + COLUMN_WEIGHT + "," + COLUMN_BRKFAST + "," + COLUMN_LUNCH
    //+ COLUMN_DINNER + "," + COLUMN_SNACK + "," + COLUMN_DESSERT + " FROM " + TABLE_NAME1 + " ORDER BY " + _ID;
    public List<Statistic> getALLStatistic(){
        String sql = "SELECT * FROM " + TABLE_NAME1 + " ORDER BY " + _ID;
        List<Statistic> list = new ArrayList<Statistic>();
        mDbHelper = StatDBHelper.DatabaseHelper.getInstance(context);
        mDb =  mDbHelper.getReadableDatabase();
        Cursor cursor =  mDb.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Statistic statistic = new Statistic();
            statistic.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            statistic.setBrkfast(cursor.getString(cursor.getColumnIndex(COLUMN_BRKFAST)));
            statistic.setLaunch(cursor.getString(cursor.getColumnIndex(COLUMN_LUNCH)));
            statistic.setDinner(cursor.getString(cursor.getColumnIndex(COLUMN_DINNER)));
            statistic.setSnack(cursor.getString(cursor.getColumnIndex(COLUMN_SNACK)));
            statistic.setDessert(cursor.getString(cursor.getColumnIndex(COLUMN_DESSERT)));
            statistic.setWhobrkfast(cursor.getString(cursor.getColumnIndex(COLUMN_WHO_BRKFAST)));
            statistic.setWholunch(cursor.getString(cursor.getColumnIndex(COLUMN_WHO_LUNCH)));
            statistic.setWhodinner(cursor.getString(cursor.getColumnIndex(COLUMN_WHO_DINNER)));
            statistic.setWhodessert(cursor.getString(cursor.getColumnIndex(COLUMN_WHO_DESSERT)));
            statistic.setImgbrkfast(cursor.getString(cursor.getColumnIndex(COLUMN_IMG_BRKFAST)));
            statistic.setImglunch(cursor.getString(cursor.getColumnIndex(COLUMN_IMG_LUNCH)));
            statistic.setImgdinner(cursor.getString(cursor.getColumnIndex(COLUMN_IMG_DINNER)));
            statistic.setImgsnack(cursor.getString(cursor.getColumnIndex(COLUMN_WHO_SNACK)));
            statistic.setImgdessert(cursor.getString(cursor.getColumnIndex(COLUMN_IMG_DESSERT)));
            statistic.setBrkstrtime(cursor.getString(cursor.getColumnIndex(COLUMN_BRKFAST_STRTIME)));
            statistic.setBrkendtime(cursor.getString(cursor.getColumnIndex(COLUMN_BRKFAST_ENDTIME)));
            statistic.setLchstrtime(cursor.getString(cursor.getColumnIndex(COLUMN_LUNCH_STRTIME)));
            statistic.setLchendtime(cursor.getString(cursor.getColumnIndex(COLUMN_LUNCH_ENDTIME)));
            statistic.setDinstrtime(cursor.getString(cursor.getColumnIndex(COLUMN_DINNER_STRTIME)));
            statistic.setDinendtime(cursor.getString(cursor.getColumnIndex(COLUMN_DINNER_ENDTIME)));
            statistic.setSnkstrtime(cursor.getString(cursor.getColumnIndex(COLUMN_SNACK_STRTIME)));
            statistic.setSnkendtime(cursor.getString(cursor.getColumnIndex(COLUMN_SNACK_ENDTIME)));
            statistic.setDesstrtime(cursor.getString(cursor.getColumnIndex(COLUMN_DESSERT_STRTIME)));
            statistic.setDinendtime(cursor.getString(cursor.getColumnIndex(COLUMN_DESSERT_ENDTIME)));

            list.add(statistic);

            cursor.moveToNext();
        }
        if (cursor.getCount()==0){
            return null;
        }
        cursor.close();
        return list;
    }

    public Statistic getDateStatistic(String dates){
        String sql = "SELECT * FROM " + TABLE_NAME1 + " WHERE " + COLUMN_DATE + "=" + dates + " ORDER BY " + _ID;
        Statistic statistic = new Statistic();
        mDbHelper = StatDBHelper.DatabaseHelper.getInstance(context);
        mDb =  mDbHelper.getReadableDatabase();
        Cursor cursor =  mDb.rawQuery(sql, null);
        if (cursor.getCount()==0){
            cursor.close();
            return null;
        }
        statistic.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
        statistic.setBrkfast(cursor.getString(cursor.getColumnIndex(COLUMN_BRKFAST)));
        statistic.setLaunch(cursor.getString(cursor.getColumnIndex(COLUMN_LUNCH)));
        statistic.setDinner(cursor.getString(cursor.getColumnIndex(COLUMN_DINNER)));
        statistic.setSnack(cursor.getString(cursor.getColumnIndex(COLUMN_SNACK)));
        statistic.setDessert(cursor.getString(cursor.getColumnIndex(COLUMN_DESSERT)));
        statistic.setWhobrkfast(cursor.getString(cursor.getColumnIndex(COLUMN_WHO_BRKFAST)));
        statistic.setWholunch(cursor.getString(cursor.getColumnIndex(COLUMN_WHO_LUNCH)));
        statistic.setWhodinner(cursor.getString(cursor.getColumnIndex(COLUMN_WHO_DINNER)));
        statistic.setWhodessert(cursor.getString(cursor.getColumnIndex(COLUMN_WHO_DESSERT)));
        statistic.setImgbrkfast(cursor.getString(cursor.getColumnIndex(COLUMN_IMG_BRKFAST)));
        statistic.setImglunch(cursor.getString(cursor.getColumnIndex(COLUMN_IMG_LUNCH)));
        statistic.setImgdinner(cursor.getString(cursor.getColumnIndex(COLUMN_IMG_DINNER)));
        statistic.setImgsnack(cursor.getString(cursor.getColumnIndex(COLUMN_WHO_SNACK)));
        statistic.setImgdessert(cursor.getString(cursor.getColumnIndex(COLUMN_IMG_DESSERT)));
        statistic.setBrkstrtime(cursor.getString(cursor.getColumnIndex(COLUMN_BRKFAST_STRTIME)));
        statistic.setBrkendtime(cursor.getString(cursor.getColumnIndex(COLUMN_BRKFAST_ENDTIME)));
        statistic.setLchstrtime(cursor.getString(cursor.getColumnIndex(COLUMN_LUNCH_STRTIME)));
        statistic.setLchendtime(cursor.getString(cursor.getColumnIndex(COLUMN_LUNCH_ENDTIME)));
        statistic.setDinstrtime(cursor.getString(cursor.getColumnIndex(COLUMN_DINNER_STRTIME)));
        statistic.setDinendtime(cursor.getString(cursor.getColumnIndex(COLUMN_DINNER_ENDTIME)));
        statistic.setSnkstrtime(cursor.getString(cursor.getColumnIndex(COLUMN_SNACK_STRTIME)));
        statistic.setSnkendtime(cursor.getString(cursor.getColumnIndex(COLUMN_SNACK_ENDTIME)));
        statistic.setDesstrtime(cursor.getString(cursor.getColumnIndex(COLUMN_DESSERT_STRTIME)));
        statistic.setDinendtime(cursor.getString(cursor.getColumnIndex(COLUMN_DESSERT_ENDTIME)));

        cursor.close();
        return statistic;
    }


//    statistic.setBrkfast(cursor.getDouble(cursor.getColumnIndex(COLUMN_BRKFAST)));
////            statistic.setLaunch(cursor.getDouble(cursor.getColumnIndex(COLUMN_LUNCH)));
////            statistic.setDinner(cursor.getDouble(cursor.getColumnIndex(COLUMN_DINNER)));
////            statistic.setSnack(cursor.getDouble(cursor.getColumnIndex(COLUMN_SNACK)));
////            statistic.setDessert(cursor.getDouble(cursor.getColumnIndex(COLUMN_DESSERT)));
//
//    public void addTimeData(String date, String strBrk, String endBrk, String stLch,
//                            String endLch, String strDin, String endDin, String strSnk, String endSnk, String strDes, String endDes){
//        ContentValues cv = new ContentValues();
//        cv.put(COLUMN_DATE, date);
//        cv.put(COLUMN_BRKFAST_STRTIME, strBrk);
//        cv.put(COLUMN_BRKFAST_ENDTIME, endBrk);
//        cv.put(COLUMN_LUNCH_STRTIME, stLch);
//        cv.put(COLUMN_LUNCH_ENDTIME, endLch);
//        cv.put(COLUMN_DINNER_STRTIME, strDin);
//        cv.put(COLUMN_DINNER_ENDTIME, endDin);
//        cv.put(COLUMN_SNACK_STRTIME, strSnk);
//        cv.put(COLUMN_SNACK_ENDTIME, endSnk);
//        cv.put(COLUMN_DESSERT_STRTIME, strDes);
//        cv.put(COLUMN_DESSERT_ENDTIME, endDes);
//        mDb.insert(TABLE_NAME3,null, cv);
//    }

//    //존재하면 false, 존재하지 않으면 true
//    public boolean isDateExists(String date){
//        boolean ret = true;
//        String sql = "SELECT " + COLUMN_DATE + " FROM " + TABLE_NAME1 + " ORDER BY " + _ID;
//        Cursor cursor = mDb.rawQuery(sql, null);
//        cursor.moveToFirst();
//        while(!cursor.isAfterLast()){
//            String temp = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
//            if(temp.equals(date)){
//                ret = false;
//            }
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return ret;
//    }
//    //존재하면 false, 존재하지 않으면 true
//    public boolean isBrkExists(String date){
//        boolean ret = true;
//        String sql = "SELECT " + COLUMN_BRKFAST + " FROM " + TABLE_NAME1 + " WHERE " + COLUMN_DATE + "=" + date + " ORDER BY " + _ID;
//        Cursor cursor = mDb.rawQuery(sql, null);
//        cursor.moveToFirst();
//        while(!cursor.isAfterLast()){
//            String temp = cursor.getString(cursor.getColumnIndex(COLUMN_BRKFAST));
//            if(temp != null){
//                ret = false;
//            }
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return ret;
//    }
//    //존재하면 false, 존재하지 않으면 true
//    public boolean isLunchExists(String date){
//        boolean ret = true;
//        String sql = "SELECT " + COLUMN_LUNCH + " FROM " + TABLE_NAME1 + " WHERE " + COLUMN_DATE + "=" + date + " ORDER BY " + _ID;
//        Cursor cursor = mDb.rawQuery(sql, null);
//        cursor.moveToFirst();
//        while(!cursor.isAfterLast()){
//            String temp = cursor.getString(cursor.getColumnIndex(COLUMN_LUNCH));
//            if(temp != null){
//                ret = false;
//            }
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return ret;
//    }
//    //존재하면 false, 존재하지 않으면 true
//    public boolean isDinnerExists(String date){
//        boolean ret = true;
//        String sql = "SELECT " + COLUMN_DINNER + " FROM " + TABLE_NAME1 + " WHERE " + COLUMN_DATE + "=" + date + " ORDER BY " + _ID;
//        Cursor cursor = mDb.rawQuery(sql, null);
//        cursor.moveToFirst();
//        while(!cursor.isAfterLast()){
//            String temp = cursor.getString(cursor.getColumnIndex(COLUMN_DINNER));
//            if(temp != null){
//                ret = false;
//            }
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return ret;
//    }
//    //존재하면 false, 존재하지 않으면 true
//    public boolean isSnackExists(String date){
//        boolean ret = true;
//        String sql = "SELECT " + COLUMN_SNACK + " FROM " + TABLE_NAME1 + " WHERE " + COLUMN_DATE + "=" + date + " ORDER BY " + _ID;
//        Cursor cursor = mDb.rawQuery(sql, null);
//        cursor.moveToFirst();
//        while(!cursor.isAfterLast()){
//            String temp = cursor.getString(cursor.getColumnIndex(COLUMN_SNACK));
//            if(temp != null){
//                ret = false;
//            }
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return ret;
//    }
//    //존재하면 false, 존재하지 않으면 true
//    public boolean isDessertExists(String date){
//        boolean ret = true;
//        String sql = "SELECT " + COLUMN_DESSERT + " FROM " + TABLE_NAME1 + " WHERE " + COLUMN_DATE + "=" + date + " ORDER BY " + _ID;
//        Cursor cursor = mDb.rawQuery(sql, null);
//        cursor.moveToFirst();
//        while(!cursor.isAfterLast()){
//            String temp = cursor.getString(cursor.getColumnIndex(COLUMN_DESSERT));
//            if(temp != null){
//                ret = false;
//            }
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return ret;
//    }
//
//    public List<Statistic> getStandardList(){
//        String sql = "SELECT " + COLUMN_DATE + "," + COLUMN_NAME +  "," + COLUMN_WEIGHT + "," + COLUMN_BRKFAST + "," + COLUMN_LUNCH
//                + COLUMN_DINNER + "," + COLUMN_SNACK + "," + COLUMN_DESSERT + " FROM " + TABLE_NAME1 + " ORDER BY " + _ID;
//        List<Statistic> list = new ArrayList<Statistic>();
//        mDbHelper = SearchDBHelper.DatabaseHelper.getInstance(context);
//        mDb =  mDbHelper.getReadableDatabase();
//        Cursor cursor =  mDb.rawQuery(sql, null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
//            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
//            String weight = cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT));
//            String brk = cursor.getString(cursor.getColumnIndex(COLUMN_BRKFAST));
//            String lunch = cursor.getString(cursor.getColumnIndex(COLUMN_LUNCH));
//            String dinner = cursor.getString(cursor.getColumnIndex(COLUMN_DINNER));
//            String snack = cursor.getString(cursor.getColumnIndex(COLUMN_SNACK));
//            String dessert = cursor.getString(cursor.getColumnIndex(COLUMN_DESSERT));
//            //list.add(new Statistic(date, name, weight, brk, lunch, dinner, snack, dessert));
//            cursor.moveToNext();
//        }
//        if (cursor.getCount()==0){
//            return null;
//        }
//        cursor.close();
//        return list;
//    }
//    public List<Statistic> getWhoList(){
//        String sql = "SELECT " + COLUMN_DATE + "," + COLUMN_WHO_BRKFAST + "," + COLUMN_WHO_LUNCH + ","
//                + COLUMN_WHO_DINNER + "," + COLUMN_WHO_SNACK + "," + COLUMN_WHO_DESSERT
//                +" FROM " + TABLE_NAME2 + " ORDER BY " + _ID;
//        List<Statistic> list = new ArrayList<Statistic>();
//        mDbHelper = SearchDBHelper.DatabaseHelper.getInstance(context);
//        mDb =  mDbHelper.getReadableDatabase();
//        Cursor cursor =  mDb.rawQuery(sql, null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
//            String brk = cursor.getString(cursor.getColumnIndex(COLUMN_WHO_BRKFAST));
//            String lunch = cursor.getString(cursor.getColumnIndex(COLUMN_WHO_LUNCH));
//            String dinner = cursor.getString(cursor.getColumnIndex(COLUMN_WHO_DINNER));
//            String snack = cursor.getString(cursor.getColumnIndex(COLUMN_WHO_SNACK));
//            String dessert = cursor.getString(cursor.getColumnIndex(COLUMN_WHO_DESSERT));
//            list.add(new Statistic(date, brk, lunch, dinner, snack, dessert));
//            cursor.moveToNext();
//        }
//        if (cursor.getCount()==0){
//            return null;
//        }
//        cursor.close();
//        return list;
//    }
//    public List<Statistic> getTimeList(){
//        String sql = "SELECT " + COLUMN_DATE + "," + COLUMN_BRKFAST_STRTIME + "," + COLUMN_BRKFAST_ENDTIME + ","
//                + COLUMN_LUNCH_STRTIME + "," + COLUMN_LUNCH_ENDTIME + "," + COLUMN_DINNER_STRTIME + ","
//                + COLUMN_DINNER_ENDTIME + "," + COLUMN_SNACK_STRTIME + "," + COLUMN_SNACK_ENDTIME + "," + COLUMN_DESSERT_STRTIME
//                + "," + COLUMN_DESSERT_ENDTIME  + " FROM " + TABLE_NAME3 + " ORDER BY " + _ID;
//        List<Statistic> list = new ArrayList<Statistic>();
//        mDbHelper = SearchDBHelper.DatabaseHelper.getInstance(context);
//        mDb =  mDbHelper.getReadableDatabase();
//        Cursor cursor =  mDb.rawQuery(sql, null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
//            String strBrk = cursor.getString(cursor.getColumnIndex(COLUMN_BRKFAST_STRTIME));
//            String endBrk = cursor.getString(cursor.getColumnIndex(COLUMN_BRKFAST_ENDTIME));
//            String strLunch = cursor.getString(cursor.getColumnIndex(COLUMN_LUNCH_STRTIME));
//            String endLunch = cursor.getString(cursor.getColumnIndex(COLUMN_LUNCH_ENDTIME));
//            String strDinner = cursor.getString(cursor.getColumnIndex(COLUMN_DINNER_STRTIME));
//            String endDinner = cursor.getString(cursor.getColumnIndex(COLUMN_DINNER_ENDTIME));
//            String strSnack = cursor.getString(cursor.getColumnIndex(COLUMN_SNACK_STRTIME));
//            String endSnack = cursor.getString(cursor.getColumnIndex(COLUMN_SNACK_ENDTIME));
//            String strDessert = cursor.getString(cursor.getColumnIndex(COLUMN_DESSERT_STRTIME));
//            String endDessert = cursor.getString(cursor.getColumnIndex(COLUMN_DESSERT_ENDTIME));
//            list.add(new Statistic(date, strBrk, endBrk, strLunch, endLunch, strDinner, endDinner, strSnack, endSnack, strDessert, endDessert));
//            cursor.moveToNext();
//        }
//        if (cursor.getCount()==0){
//            return null;
//        }
//        cursor.close();
//        return list;
//    }
//    public void insertWho(String key, String who, String date){
//        ContentValues cv = new ContentValues();
//        String col = "";
//        switch (key){
//            case "brk": col = COLUMN_WHO_BRKFAST; break;
//            case "lch": col = COLUMN_WHO_LUNCH; break;
//            case "din": col = COLUMN_WHO_DINNER; break;
//            case "snk": col = COLUMN_WHO_SNACK; break;
//            case "des": col = COLUMN_WHO_DESSERT; break;
//        }
//        cv.put(key, who);
//        mDb.insert(TABLE_NAME2, COLUMN_DATE +"=?", cv);
//    }

}
