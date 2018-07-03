package goods.cap.app.goodsgoods;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.Map;

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
        remoteConfigInit();
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

    private void remoteConfigInit(){
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        Map<String, Object> remoteConfigDefaults = new HashMap<String, Object>();
        remoteConfigDefaults.put("latest_version", "1.0.0");
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setConfigSettings(configSettings);
        firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        firebaseRemoteConfig.fetch(60).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("RemoteConfig", "remote config is fetched.");
                    firebaseRemoteConfig.activateFetched();
                }
            }
        });
    }
}
