package goods.cap.app.goodsgoods;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class GoodsApplication extends Application {

    private int key = 1; //Diet or Health선택값 0, 1, 2, 3, 4, 5(Health) if 6 < key => search
    private String searchKey; //검색

    @Override
    public void onCreate(){
        super.onCreate();
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
