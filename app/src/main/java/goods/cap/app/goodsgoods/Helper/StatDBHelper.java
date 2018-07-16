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

import goods.cap.app.goodsgoods.Model.Firebase.Calorie;
import goods.cap.app.goodsgoods.Model.Statistic;

public class StatDBHelper {

    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;
    private final Context context;
    private static final String DATABASE_NAME = "candystat.db";
    private static final int DATABASE_VERSION = 7;
    private static final String TABLE_NAME = "statCAL";//표준
    private static final String TABLE_NAME1 = "calCAL";//식품정보
    //표준
    private static final String COLUMN_DATE = "dietdate";//날짜
    private static final String COLUMN_TITLE = "title";//식품명(칼로리)
    //사진, 함께한 사람
    private static final String COLUMN_WHO = "whodiet";//함께
    private static final String COLUMN_IMG = "imgdiet";//사진
    //식사 시작 시간, 종료 시간
    private static final String COLUMN_STRTIME = "strtime";//시작
    private static final String COLUMN_ENDTIME = "endtime";//종료
    private static final String _ID = "_id";//아이디

    private static final String COLUMN_CATEGORY = "categories";//카테고리
    private static final String COLUMN_CHO_MG = "chomg";//콜레스테롤
    private static final String COLUMN_DAN_G = "dang";//단백질
    private static final String COLUMN_DANG_G = "dangg";//당류
    private static final String COLUMN_JI_G = "jig";//지방
    private static final String COLUMN_NA_MG = "namg";//나트류
    private static final String COLUMN_NAME = "names";//이름
    private static final String COLUMN_PHO_G = "phog";//포화지방산
    private static final String COLUMN_QUANTY = "quanty";//1회섭취량
    private static final String COLUMN_TAN_G = "tang";//탄수화물
    private static final String COLUMN_TRANS_G = "transg";//트랜스지방
    private static final String COLUMN_KAL = "calkal";

    private static final String DATABASE_CREATE =
            "CREATE TABLE "+ TABLE_NAME +" ("
                    +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +COLUMN_NAME+" TEXT,"
                    +COLUMN_DATE+" TEXT,"
                    +COLUMN_TITLE+ " TEXT,"
                    +COLUMN_WHO+ " TEXT,"
                    +COLUMN_IMG+ " TEXT,"
                    +COLUMN_STRTIME+ " TEXT,"
                    +COLUMN_ENDTIME+ " TEXT" + ");";

    private static final String DATABASE_CREATE1 =
            "CREATE TABLE "+ TABLE_NAME1 +" ("
                    +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +COLUMN_KAL+" REAL,"
                    +COLUMN_DATE+" TEXT,"
                    +COLUMN_CATEGORY+" TEXT,"
                    +COLUMN_CHO_MG+ " TEXT,"
                    +COLUMN_DAN_G+ " TEXT,"
                    +COLUMN_DANG_G+ " TEXT,"
                    +COLUMN_JI_G+ " TEXT,"
                    +COLUMN_PHO_G+ " TEXT,"
                    +COLUMN_QUANTY+ " TEXT,"
                    +COLUMN_TAN_G+ " TEXT,"
                    +COLUMN_TRANS_G+ " TEXT,"
                    +COLUMN_NA_MG+ " TEXT" + ");";

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
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
            onCreate(db);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w("StatDBHelper","CREATE!!!!!!!!!!!!!!");
            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE1);
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
    public long removeEvent1(String id){
        return mDb.delete(TABLE_NAME, "_id"+"=?",new String[]{id});
    }
    public long removeEvent2(String id){
        return mDb.delete(TABLE_NAME1, "_id"+"=?",new String[]{id});
    }
    public long updateData(Statistic statistic){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, statistic.getDietdate());
        contentValues.put(COLUMN_NAME, statistic.getNames());
        contentValues.put(COLUMN_WHO, statistic.getWhodiet());
        contentValues.put(COLUMN_IMG, statistic.getImgdiet());
        contentValues.put(COLUMN_STRTIME, statistic.getStrtime());
        contentValues.put(COLUMN_ENDTIME, statistic.getEndtime());
        return mDb.update(TABLE_NAME,  contentValues,"_id"+"=?", new String[]{statistic.getId()});
    }
    public long updateCalorie(Calorie calorie){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, calorie.getDate());
        contentValues.put(COLUMN_KAL, calorie.getKal());
        contentValues.put(COLUMN_CATEGORY, calorie.getCateorgy());
        contentValues.put(COLUMN_DAN_G, String.valueOf(calorie.getDan_g()));
        contentValues.put(COLUMN_DANG_G, String.valueOf(calorie.getDang_g()));
        contentValues.put(COLUMN_JI_G, String.valueOf(calorie.getJi_g()));
        contentValues.put(COLUMN_PHO_G, String.valueOf(calorie.getPho_g()));
        contentValues.put(COLUMN_QUANTY, String.valueOf(calorie.getQuanty()));
        contentValues.put(COLUMN_TAN_G, String.valueOf(calorie.getTan_g()));
        contentValues.put(COLUMN_TRANS_G, String.valueOf(calorie.getTrans_g()));
        contentValues.put(COLUMN_NA_MG, String.valueOf(calorie.getNa_mg()));
        return mDb.update(TABLE_NAME1, contentValues,"_id"+"=?", new String[]{calorie.getId()});
    }

//    public void updateData(Statistic statistic){
//        String sql = "UPDATE " + TABLE_NAME + " SET "
//                + COLUMN_NAME + "='" + statistic.getNames()
//                + "', " + COLUMN_IMG + "='" + statistic.getImgdiet()
//                + "', " + COLUMN_WHO + "='" +statistic.getWhodiet()
//                + "', " + COLUMN_STRTIME + "='" + statistic.getStrtime()
//                + "', " + COLUMN_ENDTIME + "='" + statistic.getEndtime()
//                + "' WHERE dietdate='" + statistic.getDietdate()
//                + "'" + " AND _id = '" + statistic.getId() + "'";
//        mDb.execSQL(sql);
//    }
//    public void updateCalorie(Calorie calorie){
//        String sql = "UPDATE " + TABLE_NAME + " SET "
//                + COLUMN_CATEGORY + "='" + calorie.getCateorgy()
//                + "', " + COLUMN_KAL + "='" + calorie.getKal()
//                + "', " + COLUMN_NAME + "='" + calorie.getName()
//                + "', " + COLUMN_DAN_G + "='" + calorie.getDan_g()
//                + "', " + COLUMN_DANG_G + "='" + calorie.getDang_g()
//                + "', " + COLUMN_JI_G + "='" + calorie.getJi_g()
//                + "', " + COLUMN_PHO_G + "='" + calorie.getPho_g()
//                + "', " + COLUMN_QUANTY + "='" + calorie.getQuanty()
//                + "', " + COLUMN_TAN_G + "='" + calorie.getTan_g()
//                + "', " + COLUMN_TRANS_G + "='" + calorie.getTrans_g()
//                + "', " + COLUMN_NA_MG + "='" + calorie.getNa_mg()
//                + "' WHERE dietdate='" + calorie.getDate()
//                + "'" + " AND _id = '" + calorie.getId() + "'";
//        mDb.execSQL(sql);
//    }

    public long initDatas(Statistic statistic){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, statistic.getDietdate());
        contentValues.put(COLUMN_NAME, statistic.getNames());
        contentValues.put(COLUMN_WHO, statistic.getWhodiet());
        contentValues.put(COLUMN_IMG, statistic.getImgdiet());
        contentValues.put(COLUMN_STRTIME, statistic.getStrtime());
        contentValues.put(COLUMN_ENDTIME, statistic.getEndtime());
        return mDb.insert(TABLE_NAME, null, contentValues);
    }

    public List<Statistic> getALLStatistic(){
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + _ID;
        List<Statistic> list = new ArrayList<Statistic>();
        mDbHelper = StatDBHelper.DatabaseHelper.getInstance(context);
        mDb =  mDbHelper.getReadableDatabase();
        Cursor cursor =  mDb.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Statistic statistic = new Statistic();
            statistic.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            statistic.setNames(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            statistic.setDietdate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            statistic.setWhodiet(cursor.getString(cursor.getColumnIndex(COLUMN_WHO)));
            statistic.setImgdiet(cursor.getString(cursor.getColumnIndex(COLUMN_IMG)));
            statistic.setStrtime(cursor.getString(cursor.getColumnIndex(COLUMN_STRTIME)));
            statistic.setEndtime(cursor.getString(cursor.getColumnIndex(COLUMN_ENDTIME)));
            list.add(statistic);
            cursor.moveToNext();
        }
        if (cursor.getCount()==0){
            return null;
        }
        cursor.close();
        return list;
    }

    public List<Statistic> getALLStatistic(String date){
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + "='" + date + "' ORDER BY " + _ID;
        List<Statistic> list = new ArrayList<Statistic>();
        mDbHelper = StatDBHelper.DatabaseHelper.getInstance(context);
        mDb =  mDbHelper.getReadableDatabase();
        Cursor cursor =  mDb.rawQuery(sql, null);
        if (cursor.getCount()==0){
            return null;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Statistic statistic = new Statistic();
            statistic.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            statistic.setNames(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            statistic.setDietdate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            statistic.setWhodiet(cursor.getString(cursor.getColumnIndex(COLUMN_WHO)));
            statistic.setImgdiet(cursor.getString(cursor.getColumnIndex(COLUMN_IMG)));
            statistic.setStrtime(cursor.getString(cursor.getColumnIndex(COLUMN_STRTIME)));
            statistic.setEndtime(cursor.getString(cursor.getColumnIndex(COLUMN_ENDTIME)));
            list.add(statistic);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public long initCalorie(Calorie calorie){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, calorie.getDate());
        contentValues.put(COLUMN_KAL, calorie.getKal());
        contentValues.put(COLUMN_CATEGORY, calorie.getCateorgy());
        contentValues.put(COLUMN_DAN_G, String.valueOf(calorie.getDan_g()));
        contentValues.put(COLUMN_DANG_G, String.valueOf(calorie.getDang_g()));
        contentValues.put(COLUMN_JI_G, String.valueOf(calorie.getJi_g()));
        contentValues.put(COLUMN_PHO_G, String.valueOf(calorie.getPho_g()));
        contentValues.put(COLUMN_QUANTY, String.valueOf(calorie.getQuanty()));
        contentValues.put(COLUMN_TAN_G, String.valueOf(calorie.getTan_g()));
        contentValues.put(COLUMN_TRANS_G, String.valueOf(calorie.getTrans_g()));
        contentValues.put(COLUMN_NA_MG, String.valueOf(calorie.getNa_mg()));
        return mDb.insert(TABLE_NAME1, null, contentValues);
    }

    public List<Calorie> getAllCalorie(){
        String sql = "SELECT * FROM " + TABLE_NAME1 + " ORDER BY " + _ID;
        List<Calorie> list = new ArrayList<Calorie>();
        mDbHelper = StatDBHelper.DatabaseHelper.getInstance(context);
        mDb =  mDbHelper.getReadableDatabase();
        Cursor cursor =  mDb.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Calorie calorie = new Calorie();
            calorie.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            calorie.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            calorie.setCateorgy(cursor.getString(cursor.getColumnIndex(COLUMN_WHO)));
            calorie.setDan_g(cursor.getString(cursor.getColumnIndex(COLUMN_DAN_G)));
            calorie.setDang_g(cursor.getString(cursor.getColumnIndex(COLUMN_DANG_G)));
            calorie.setJi_g(cursor.getString(cursor.getColumnIndex(COLUMN_JI_G)));
            calorie.setPho_g(cursor.getString(cursor.getColumnIndex(COLUMN_PHO_G)));
            calorie.setQuanty(cursor.getString(cursor.getColumnIndex(COLUMN_QUANTY)));
            calorie.setTan_g(cursor.getString(cursor.getColumnIndex(COLUMN_TAN_G)));
            calorie.setTrans_g(cursor.getString(cursor.getColumnIndex(COLUMN_TRANS_G)));
            calorie.setNa_mg(cursor.getString(cursor.getColumnIndex(COLUMN_NA_MG)));
            list.add(calorie);

            cursor.moveToNext();
        }
        if (cursor.getCount()==0){
            return null;
        }
        cursor.close();
        return list;
    }

    public List<Calorie> getAllCalorie(String date){
        String sql = "SELECT * FROM " + TABLE_NAME1 + " WHERE " + COLUMN_DATE + "='" + date + "' ORDER BY " + _ID;
        List<Calorie> list = new ArrayList<Calorie>();
        mDbHelper = StatDBHelper.DatabaseHelper.getInstance(context);
        mDb =  mDbHelper.getReadableDatabase();
        Cursor cursor =  mDb.rawQuery(sql, null);
        if (cursor.getCount()==0){
            return null;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Calorie calorie = new Calorie();
            calorie.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            calorie.setKal(cursor.getDouble(cursor.getColumnIndex(COLUMN_KAL)));
            calorie.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            calorie.setCateorgy(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
            calorie.setDan_g(cursor.getString(cursor.getColumnIndex(COLUMN_DAN_G)));
            calorie.setDang_g(cursor.getString(cursor.getColumnIndex(COLUMN_DANG_G)));
            calorie.setJi_g(cursor.getString(cursor.getColumnIndex(COLUMN_JI_G)));
            calorie.setPho_g(cursor.getString(cursor.getColumnIndex(COLUMN_PHO_G)));
            calorie.setQuanty(cursor.getString(cursor.getColumnIndex(COLUMN_QUANTY)));
            calorie.setTan_g(cursor.getString(cursor.getColumnIndex(COLUMN_TAN_G)));
            calorie.setTrans_g(cursor.getString(cursor.getColumnIndex(COLUMN_TRANS_G)));
            calorie.setNa_mg(cursor.getString(cursor.getColumnIndex(COLUMN_NA_MG)));
            list.add(calorie);
            cursor.moveToNext();
        }
        if (cursor.getCount()==0){
            return null;
        }
        cursor.close();
        return list;
    }


//    public Statistic getDateStatistic(String id){
//        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + _ID + "='" + id + "'";
//        Statistic statistic = new Statistic();
//        mDbHelper = StatDBHelper.DatabaseHelper.getInstance(context);
//        mDb =  mDbHelper.getReadableDatabase();
//        Cursor cursor =  mDb.rawQuery(sql, null);
//        if (cursor.getCount()==0){
//            cursor.close();
//            return null;
//        }
//        statistic.setId(cursor.getString(cursor.getColumnIndex(_ID)));
//        statistic.setNames(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
//        statistic.setDietdate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
//        statistic.setWhodiet(cursor.getString(cursor.getColumnIndex(COLUMN_WHO)));
//        statistic.setImgdiet(cursor.getString(cursor.getColumnIndex(COLUMN_IMG)));
//        statistic.setStrtime(cursor.getString(cursor.getColumnIndex(COLUMN_STRTIME)));
//        statistic.setEndtime(cursor.getString(cursor.getColumnIndex(COLUMN_ENDTIME)));
//        cursor.close();
//        return statistic;
//    }

}
