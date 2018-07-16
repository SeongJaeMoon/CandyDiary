package goods.cap.app.goodsgoods.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import goods.cap.app.goodsgoods.R;

public class SplashActivity extends AppCompatActivity {

    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        MobileAds.initialize(this, "ca-app-pub-1405827106256391~2329171756");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //테스트 종료 후 setAds로 변경 필요
                setAds();
            }
        }, 3000);
    }
    //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
    //.addTestDevice("E44604670F4699F07069260DD700FEA7")
    //Admob 전면 광고
    public void setAds(){
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.ads_id));
        interstitialAd.loadAd(new AdRequest.Builder()
                .build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                //광고 로딩 완료 후 이벤트 발생
                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                }
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                //광고 요청 또는 표출 실패 후 이벤트 발생
                Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                SplashActivity.this.finish();
            }

            @Override
            public void onAdOpened() {
                //광고 표출 후 이벤트 발생
            }

            @Override
            public void onAdLeftApplication() {
                //광고 클릭 후 이벤트 발생
            }

            @Override
            public void onAdClosed() {
                //광고가 표출한 뒤 사라질 때 이벤트 발생
                Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        });
    }
}
