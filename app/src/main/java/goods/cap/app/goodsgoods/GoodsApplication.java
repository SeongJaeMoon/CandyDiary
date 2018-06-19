package goods.cap.app.goodsgoods;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import goods.cap.app.goodsgoods.Util.PostImageLoader;
import ss.com.bannerslider.Slider;

public class GoodsApplication extends Application {

    private int key = 1; //Diet or Health 선택값 0, 1, 2, 3, 4, 5(Health) if 6 < key => search
    private String searchKey; //검색
    private static GoodsApplication goodsApplication;

    @Override
    public void onCreate(){
        super.onCreate();
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        goodsApplication = this;
    }

    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public static GoodsApplication getInstance(){
        return goodsApplication;
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
