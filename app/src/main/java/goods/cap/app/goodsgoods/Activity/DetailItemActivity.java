package goods.cap.app.goodsgoods.Activity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.API.Config;
import goods.cap.app.goodsgoods.API.MainHttp;
import goods.cap.app.goodsgoods.Adapter.DetailAdapter;
import goods.cap.app.goodsgoods.Helper.DietDtlHelper;
import goods.cap.app.goodsgoods.Helper.RecentDBHelper;
import goods.cap.app.goodsgoods.Model.Diet;
import goods.cap.app.goodsgoods.Model.DietDtl;
import goods.cap.app.goodsgoods.Model.DietDtlResponseModel;
import goods.cap.app.goodsgoods.R;

import goods.cap.app.goodsgoods.Util.CustomDialog;
import jp.wasabeef.glide.transformations.BlurTransformation;


/* 특정 식단 선택 Detail 화면, created by supermoon. */

public class DetailItemActivity extends AppCompatActivity {

    private static final String logger = DetailItemActivity.class.getSimpleName();

    @BindView(R.id.diet_cn)TextView diet_cn;
    @BindView(R.id.detail_toolbar)Toolbar toolbar;
    @BindView(R.id.coordinator_layout)CoordinatorLayout coordinatorLayout;
    @BindView(R.id.wallpaper)ImageView wallpaper;
    @BindView(R.id.detail_list) RecyclerView recyclerView;
    @BindView(R.id.detail_calorie)TextView calorieView;
    private RecyclerView.Adapter adapter;
    private CustomDialog customDialog;
    private String[] calorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent != null){
            Gson gson = new Gson();
            final String jsonData = intent.getStringExtra("diet");
            final Diet diet = gson.fromJson(jsonData, Diet.class);

            String oldPath = diet.getRtnImageDc();
            String newPath = diet.getRtnStreFileNm();
            String filePath = Config.getAbUrl(oldPath, newPath);
            setRecent(filePath, diet.getFdNm(),1);

            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setTitleEnabled(false);
            toolbar.setTitle(diet.getFdNm());
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            RequestOptions ro = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 1)));

            Glide.with(this)
                    .setDefaultRequestOptions(ro)
                    .load(filePath)
                    .into(wallpaper);

            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            initData(diet.getCntntsNo());

            calorieView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(logger, "calroie");
                    customDialog = new CustomDialog(DetailItemActivity.this, calorie);
                    customDialog.show();
                }
            });

        }else{
            Log.w(logger, "Error");
        }
    }

    private void initData(String cntntsNo){

        MainHttp mainHttp = new MainHttp(DetailItemActivity.this, getResources().getString(R.string.data_refresh_title),
                getResources().getString(R.string.data_refresh), Config.API_KEY2);
        mainHttp.setCntntsNo(cntntsNo);
        mainHttp.getDietDtl(new DietDtlHelper() {
            @Override
            public void success(DietDtlResponseModel response) {
                List<DietDtl> list = response.getBody().getItems().getDietDtlList();
                diet_cn.setText(list.get(0).getDietCn());
                animationTitle(diet_cn);
                adapter = new DetailAdapter(DetailItemActivity.this, list);
                recyclerView.setAdapter(adapter);
                calorie = list.get(0).getDietNtrsmallInfo().split(",");
            }
            @Override
            public void failure(String message) {
                Log.i(logger, "error" + message);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRecent(String url, String summary, int recent) {
        RecentDBHelper recentDBHelper = new RecentDBHelper(this);
        try {
            recentDBHelper.open();
            if(recentDBHelper.isRecentExists(url)) {
                recentDBHelper.addRecent(url, summary, recent);
                //삭제 코드
                recentDBHelper.deletOlder();
            }
        } catch (Exception e) {
            Log.w(logger, e.getMessage());
        } finally {
            recentDBHelper.close();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    private void animationTitle(TextView view){
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(2000);
        animation.setStartOffset(2000);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        view.startAnimation(animation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(logger, "onStart");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(logger, "onDestroy");
        if(customDialog!=null){
            customDialog.cancel();
        }
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.w(logger, "onStop");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.w(logger, "onPause");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.w(logger, "onRestart");
    }

    @Override
    public void onBackPressed() {
        if(customDialog!=null){
            if(customDialog.isShowing()) {
                customDialog.cancel();
            }
        }else{
            this.finish();
        }
        super.onBackPressed();
    }
}
