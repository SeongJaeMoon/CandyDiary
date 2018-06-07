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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.API.Config;
import goods.cap.app.goodsgoods.API.MainHttp;
import goods.cap.app.goodsgoods.Adapter.DetailAdapter;
import goods.cap.app.goodsgoods.GoodsApplication;
import goods.cap.app.goodsgoods.Helper.DietDtlHelper;
import goods.cap.app.goodsgoods.Helper.RecentDBHelper;
import goods.cap.app.goodsgoods.Model.Diet.Diet;
import goods.cap.app.goodsgoods.Model.Diet.DietDtl;
import goods.cap.app.goodsgoods.Model.Diet.DietDtlResponseModel;
import goods.cap.app.goodsgoods.R;

import goods.cap.app.goodsgoods.Util.CustomDialog;
import jp.wasabeef.glide.transformations.BlurTransformation;
import me.gujun.android.taggroup.TagGroup;


/* 특정 식단 선택 Detail 화면, created by supermoon. */
// cntntsNo -> 크롤링 <-> Firebase DB 저장 (child tree 구성).

//검색, 추천, 통계 <-> 달력(내 식단 관리) << 연결 시키기 >> 포스트(슬라이드 파일 이미지),
public class DetailItemActivity extends AppCompatActivity {

    @BindView(R.id.diet_cn)TagGroup diet_cn; //태그 그룹
    @BindView(R.id.detail_toolbar)Toolbar toolbar;
    @BindView(R.id.coordinator_layout)CoordinatorLayout coordinatorLayout;
    @BindView(R.id.wallpaper)ImageView wallpaper;
    @BindView(R.id.detail_list) RecyclerView recyclerView;
    @BindView(R.id.detail_calorie)TextView calorieView;//칼로리 보기
    @BindView(R.id.like_img)ImageView likeImg;//좋아요 이미지
    @BindView(R.id.likes)TextView likes;//좋아요 수
    @BindView(R.id.share_img)ImageView shareImg;//공유 이미지
    @BindView(R.id.share_nm)TextView shares;//공유 수
    @BindView(R.id.comment_img)ImageView commentImg; //댓글 이미지
    @BindView(R.id.comment_detail)TextView commentDetail;//댓글보기
    @BindView(R.id.comments)TextView comments;//댓글 수
    @BindView(R.id.detail_img)ImageView imageView;
    private static final String logger = DetailItemActivity.class.getSimpleName();
    private RecyclerView.Adapter adapter;
    private CustomDialog customDialog;
    private String[] calorie;
    private FirebaseDatabase db;
    private FirebaseAuth fAuth;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm", Locale.KOREA);
    private boolean isLikeProcess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent != null){
            //JSON 데이터 파싱
            Gson gson = new Gson();
            final String jsonData = intent.getStringExtra("diet");
            final Diet diet = gson.fromJson(jsonData, Diet.class);

            //레이아웃, DB Setting
            setHeadLayout(diet.getFdNm());
            setRecent(diet);
            //태그 Setting
            String[] tag_group = diet.getCntntsSj().split(",");
            diet_cn.setTags(tag_group);
            //WallPaper 이미지 처리
            RequestOptions ro = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 1)));

            Glide.with(this)
                    .setDefaultRequestOptions(ro)
                    .load(diet.getFilePath())
                    .into(wallpaper);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            String contentNo = diet.getCntntsNo();

            initData(contentNo);

            commentDetail.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailItemActivity.this, CommentActivity.class);
                    intent.putExtra("cntntno", diet.getCntntsNo());
                    intent.putExtra("fdnm", diet.getFdNm());
                    startActivity(intent);
                }
            });
            calorieView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(logger, "calroie");
                    if(calorie == null) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.static_no),Toast.LENGTH_SHORT).show();
                    }else {
                        customDialog = new CustomDialog(DetailItemActivity.this, calorie);
                        customDialog.show();
                    }
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            initFirebase(contentNo);

        }else{
            Log.w(logger, "Error");
        }

    }

    private void initData(String cntntsNo){
        MainHttp mainHttp = new MainHttp(DetailItemActivity.this, getResources().getString(R.string.data_refresh_title), getResources().getString(R.string.data_refresh), Config.API_KEY2);
        mainHttp.setCntntsNo(cntntsNo);
        mainHttp.getDietDtl(new DietDtlHelper() {
            @Override
            public void success(DietDtlResponseModel response) {
                List<DietDtl> list = response.getBody().getItems().getDietDtlList();
                adapter = new DetailAdapter(DetailItemActivity.this, list);
                recyclerView.setAdapter(adapter);
                GoodsApplication goodsApplication = (GoodsApplication)getApplicationContext();
                int key = goodsApplication.getKey();
                if(key < 3){
                    calorie = list.get(0).getDietNtrsmallInfo().split(",");
                }else{
                    calorie = null;
                }

            }
            @Override
            public void failure(String message) {
                Log.i(logger, "error" + message);
            }
        });
    }
    private void initFirebase(String cntntsNo) {
        db = FirebaseDatabase.getInstance();
        //사용자 정보
        //fAuth = FirebaseAuth.getInstance();
        //String uid = fAuth.getCurrentUser().getUid();
        //DatabaseReference userRef = db.getReference().child("users").child(uid);

        //test code
        final String uid = "lmyx6ViQaKeejs2jUQBLq76ZcKt1";
        //좋아요
        final DatabaseReference dbRef = db.getReference().child("likes").child(cntntsNo);
        final DatabaseReference dbRef2 = db.getReference().child("comments").child(cntntsNo);
        final DatabaseReference dbRef3 = db.getReference().child("shares").child(cntntsNo);

        dbRef.keepSynced(true);
        dbRef2.keepSynced(true);
        dbRef3.keepSynced(true);

        likeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLikeProcess = true;
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(isLikeProcess) {
                            if (dataSnapshot.hasChild(uid)) {
                                dbRef.child(uid).removeValue();
                                likeImg.setImageResource(R.drawable.ic_favorite_white_24dp);
                            } else {
                                dbRef.child(uid).setValue(sdf.format(new Date(System.currentTimeMillis())));
                                likeImg.setImageResource(R.drawable.like_orange);
                            }
                            isLikeProcess = false;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    likeImg.setImageResource(R.drawable.like_orange);
                }else {
                    likeImg.setImageResource(R.drawable.ic_favorite_white_24dp);
                }
                long likesCount = dataSnapshot.getChildrenCount();
                likes.setText(String.valueOf(likesCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dbRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long likesCount = dataSnapshot.getChildrenCount();
                comments.setText(String.valueOf(likesCount));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

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

    private void setRecent(Diet diet) {
        RecentDBHelper recentDBHelper = new RecentDBHelper(this);
        try {
            recentDBHelper.open();
            if(recentDBHelper.isRecentExists(diet.getCntntsNo())) {
                recentDBHelper.addRecent(diet.getFilePath(), diet.getFdNm(), diet.getCntntsNo(), diet.getCntntsSj(),0);
                //데이터 삭제 코드
                recentDBHelper.deletOlder();
            }
        } catch (Exception e) {
            Log.w(logger, e.getMessage());
        } finally {
            recentDBHelper.close();
        }
    }

    private void animationTitle(TextView view){
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(2000);
        animation.setStartOffset(2000);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        view.startAnimation(animation);
    }

    private void setHeadLayout(String title){
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
