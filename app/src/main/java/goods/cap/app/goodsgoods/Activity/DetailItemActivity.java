package goods.cap.app.goodsgoods.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.API.Config;
import goods.cap.app.goodsgoods.API.MainHttp;
import goods.cap.app.goodsgoods.Adapter.DetailAdapter;
import goods.cap.app.goodsgoods.Helper.FoodHelper;
import goods.cap.app.goodsgoods.Model.Food;
import goods.cap.app.goodsgoods.Model.FoodResponseModel;
import goods.cap.app.goodsgoods.Model.Recipe;
import goods.cap.app.goodsgoods.R;

public class DetailItemActivity extends AppCompatActivity {

    private static final String logger = DetailItemActivity.class.getSimpleName();
    private List<Food> foodList;

    @BindView(R.id.detail_toolbar)Toolbar toolbar;
    @BindView(R.id.detail_progressbar)ImageButton button;
    @BindView(R.id.coordinator_layout)CoordinatorLayout coordinatorLayout;
    @BindView(R.id.item_title)TextView title;
    @BindView(R.id.item_nation)TextView nation;
    @BindView(R.id.current_star_icon)ImageView starIcon;
    @BindView(R.id.wallpaper)ImageView wallpaper;
    @BindView(R.id.current_item_desc)TextView summary;
    @BindView(R.id.level_nm)TextView level;
    @BindView(R.id.time_nm)TextView time;
    @BindView(R.id.carlorie_nm)TextView calorie;
    @BindView(R.id.detail_img)ImageView detail_img;
    @BindView(R.id.detail_card)CardView cardView;
    @BindView(R.id.stepText)TextView stepText;
    @BindView(R.id.detail_web)TextView detailWeb;
    //@BindView(R.id.detail_list) RecyclerView recyclerView;

    //private RecyclerView.Adapter adapter;
    //private RecyclerView.LayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent != null){
            Gson gson = new Gson();
            final String jsonData = intent.getStringExtra("recipe");
            final Recipe recipe = gson.fromJson(jsonData, Recipe.class);

            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setTitleEnabled(false);
            toolbar.setTitle(recipe.getRecipe_nm_ko());
            setSupportActionBar(toolbar);

            Glide.with(this).load(recipe.getImg_url()).into(wallpaper);
            summary.setText(recipe.getSumry());

            //level.setText(String.format("%s:%s", getResources().getString(R.string.level), recipe.getLevel_nm()));
            //title.setText(recipe.getRecipe_nm_ko());
            //nation.setText(recipe.getNation_nm());

            level.setText(recipe.getLevel_nm());
            time.setText(recipe.getCooking_time());
            calorie.setText(recipe.getCalorie());
            initData(recipe.getRecipe_id());
            detailWeb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(logger, "web view");
                    Uri uri = Uri.parse(recipe.getDet_url());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

        }else{
            Log.w(logger, "Error");
        }
    }

    private void initData(int recipeId){
        MainHttp mainHttp = new MainHttp(DetailItemActivity.this, getResources().getString(R.string.data_refresh_title),
                getResources().getString(R.string.data_refresh), Config.API_KEY);
        mainHttp.setStartIndex(1);
        mainHttp.setEndIndex(10);
        mainHttp.setRecipeId(recipeId);
        mainHttp.getFood(new FoodHelper() {
            @Override
            public void success(FoodResponseModel response) {
                foodList = response.getQuery().getRow();
                StringBuilder sb = new StringBuilder();
                for(Food f : foodList){
                    sb.append(String.format(Locale.KOREA, "(%d). %s\n", f.getCookingNo(),f.getCookingDC()));
                }
                stepText.setText(sb.toString());
                //recyclerView.setHasFixedSize(true);
                //linearLayoutManager = new LinearLayoutManager(DetailItemActivity.this);
                //recyclerView.setLayoutManager(linearLayoutManager);
                //adapter = new DetailAdapter(DetailItemActivity.this, foodList);
                //recyclerView.setAdapter(adapter);
            }

            @Override
            public void failure(String message) {
                Log.i(logger, "error" + message);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
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
}
