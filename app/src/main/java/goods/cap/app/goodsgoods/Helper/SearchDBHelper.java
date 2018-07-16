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

public class SearchDBHelper {

    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;
    private final Context context;
    private static final String DATABASE_NAME = "candysearch.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_NAME = "searchDB";
    private static final String COLUMN_WORD  = "keyword"; //검색 키워드
    private static final String _ID = "_id";
    private static final String DATABASE_CREATE =
            "CREATE TABLE "+ TABLE_NAME +" ("
                    +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +COLUMN_WORD+" TEXT" + ");";

    public SearchDBHelper(Context context){
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
            Log.i("Search", "upgrade to " + newVersion + " from " + oldVersion);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }
    }

    public SearchDBHelper open() throws SQLException{
        mDbHelper = DatabaseHelper.getInstance(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        mDbHelper.close();
        mDb.close();
    }
    public void addSearch(String keyword){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_WORD, keyword);
        mDb.insert(TABLE_NAME,null, cv);
    }
    //존재하면 false, 존재하지 않으면 true
    public boolean isSearchExists(String no){
        boolean ret = true;
        String sql = "SELECT " + COLUMN_WORD + " FROM " + TABLE_NAME + " ORDER BY " + _ID;
        Cursor cursor = mDb.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String temp = cursor.getString(cursor.getColumnIndex(COLUMN_WORD));
            if(temp.equals(no)){
                ret = false;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return ret;
    }

    public List<String> getSearchList(){
        String sql = "SELECT " + COLUMN_WORD + " FROM " + TABLE_NAME + " ORDER BY " + _ID;
        List<String> list = new ArrayList<String>();
        mDbHelper = DatabaseHelper.getInstance(context);
        mDb =  mDbHelper.getReadableDatabase();
        Cursor cursor =  mDb.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String keyword = cursor.getString(cursor.getColumnIndex(COLUMN_WORD));
            list.add(keyword);
            cursor.moveToNext();
        }
        if (cursor.getCount()==0){
            return null;
        }
        cursor.close();
        return list;
    }
}
