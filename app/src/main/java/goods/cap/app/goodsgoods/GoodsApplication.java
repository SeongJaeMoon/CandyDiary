package goods.cap.app.goodsgoods;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class GoodsApplication extends Application {

    private int key = 1; //Diet 선택값 0, 1, 2, 3, 4 if 5 > key => search
    private boolean isDietFood = true; //Diet(true) or Food(fasle)
    private String searchKey; //검색
    private Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        this.context = getApplicationContext();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isDietFood() {
        return isDietFood;
    }

    public void setDietFood(boolean dietFood) {
        isDietFood = dietFood;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
