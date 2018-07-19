package goods.cap.app.goodsgoods.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.kakaolink.v2.model.ButtonObject;
import com.kakao.kakaolink.v2.model.ContentObject;
import com.kakao.kakaolink.v2.model.FeedTemplate;
import com.kakao.kakaolink.v2.model.LinkObject;
import com.kakao.kakaolink.v2.model.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;
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
import goods.cap.app.goodsgoods.Helper.StarDBHelper;
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
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.KOREA);
    private boolean isLikeProcess = false;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    private String shareText;
    private String imgUrl;
    private String shareTitle;
    private long like, comment, share;
    private StarDBHelper starDBHelper;
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

            imgUrl = diet.getFilePath();
            shareTitle = diet.getFdNm();
            shareText = diet.getCntntsSj();
            shareDialog = new ShareDialog(this);
            callbackManager = CallbackManager.Factory.create();
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
//                    Log.i(logger, "facebook => "+result.toString());
                }
                @Override
                public void onCancel() { }
                @Override
                public void onError(FacebookException error) {
//                    Log.i(logger, "facebook => "+error.toString());
                }
            });

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
                    if(calorie == null) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.static_no),Toast.LENGTH_SHORT).show();
                    }else {
                        customDialog = new CustomDialog(DetailItemActivity.this, calorie);
                        customDialog.show();
                    }
                }
            });
            //좋아요 버튼
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setStar(diet);
                }
            });
            initFirebase(contentNo);
            starDBHelper = new StarDBHelper(this);
            try{
                starDBHelper.open();
                if(!starDBHelper.isStarExists(diet.getCntntsNo())){
                    imageView.setImageResource(R.drawable.ic_star_white_18dp);
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.star_default_error), Toast.LENGTH_SHORT).show();
//                Log.w(logger, e.getMessage());
            }
        }else{
//            Log.w(logger, "Error");
        }
    }

    private void initData(String cntntsNo){
        try {
            MainHttp mainHttp = new MainHttp(DetailItemActivity.this, getResources().getString(R.string.data_refresh_title), getResources().getString(R.string.data_refresh), Config.API_KEY2);
            mainHttp.setCntntsNo(cntntsNo);
            mainHttp.getDietDtl(new DietDtlHelper() {
                @Override
                public void success(DietDtlResponseModel response) {
                    List<DietDtl> list = response.getBody().getItems().getDietDtlList();
                    adapter = new DetailAdapter(DetailItemActivity.this, list);
                    recyclerView.setAdapter(adapter);
                    GoodsApplication goodsApplication = GoodsApplication.getInstance();
                    int key = goodsApplication.getKey();
                    if (key < 3) {
                        calorie = list.get(0).getDietNtrsmallInfo().split(",");
                    } else {
                        calorie = null;
                    }
                }

                @Override
                public void failure(String message) {
//                    Log.i(logger, "error" + message);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
            DetailItemActivity.this.finish();
        }
    }

    private void initFirebase(String cntntsNo) {
        try {
            final FirebaseDatabase db = FirebaseDatabase.getInstance();
            //사용자 정보
            final FirebaseAuth auth = FirebaseAuth.getInstance();
            final String uid = auth.getCurrentUser().getUid();
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
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (isLikeProcess) {
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
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            shareImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailItemActivity.this);
                    alertDialog.setTitle(getResources().getString(R.string.shareTitle));
                    alertDialog.setItems(new CharSequence[]{getResources().getString(R.string.facebookShare), getResources().getString(R.string.kakaoShare), getResources().getString(R.string.close)}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                dbRef3.push().child(uid).setValue(sdf.format(new Date(System.currentTimeMillis())));
                                //마켓URL 넣기
                                //Uri.parse(imgUrl)
                                ShareLinkContent content = new ShareLinkContent.Builder()
                                        .setContentUrl(Uri.parse(imgUrl))
                                        .setQuote(getResources().getString(R.string.shareText))
                                        .build();
                                shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                            } else if (which == 1) {
                                dbRef3.push().child(uid).setValue(sdf.format(new Date(System.currentTimeMillis())));
                                FeedTemplate params = FeedTemplate
                                        .newBuilder(ContentObject.newBuilder(shareTitle, imgUrl,
                                                LinkObject.newBuilder().setWebUrl("https://play.google.com/store/apps/details?id=goods.cap.app.goodsgoods")
                                                        .setMobileWebUrl("market://details?id=goods.cap.app.goodsgoods").build())
                                                .setDescrption(shareText)
                                                .build())
                                        .setSocial(SocialObject.newBuilder().setLikeCount((int) like).setCommentCount((int) comment)
                                                .setSharedCount(((int) share)).build())
                                        .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                                                .setMobileWebUrl("market://details?id=goods.cap.app.goodsgoods")
                                                .setAndroidExecutionParams("market://details?id=goods.cap.app.goodsgoods")
                                                .setWebUrl("https://play.google.com/store/apps/details?id=goods.cap.app.goodsgoods")
                                                .build()))
                                        .build();

                                KakaoLinkService.getInstance().sendDefault(DetailItemActivity.this, params, new ResponseCallback<KakaoLinkResponse>() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        Log.i(logger, errorResult.getErrorMessage() + ", " + errorResult.getException());
//                                        Toast.makeText(getApplicationContext(),'' errorResult.getErrorCode(), Toast.LENGTH_SHORT).show();
//                                        Logger.w(errorResult.toString());
                                    }

                                    @Override
                                    public void onSuccess(KakaoLinkResponse result) {
                                        Log.i(logger, result.toString());
                                    }
                                });
                            } else {
                                dialog.cancel();
                            }
                        }
                    });
                    alertDialog.create().show();
                }
            });

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(uid)) {
                        likeImg.setImageResource(R.drawable.like_orange);
                    } else {
                        likeImg.setImageResource(R.drawable.ic_favorite_white_24dp);
                    }
                    long likesCount = dataSnapshot.getChildrenCount();
                    like = likesCount;
                    likes.setText(String.valueOf(likesCount));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            dbRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long commetCount = dataSnapshot.getChildrenCount();
                    comment = commetCount;
                    comments.setText(String.valueOf(commetCount));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            dbRef3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long sharesCount = dataSnapshot.getChildrenCount();
                    share = sharesCount;
                    shares.setText(String.valueOf(sharesCount));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
            DetailItemActivity.this.finish();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
        } finally {
            recentDBHelper.close();
        }
    }
    private void setStar(Diet diet){
        try{
            if(starDBHelper.isStarExists(diet.getCntntsNo())){
                starDBHelper.addStar(diet.getFilePath(), diet.getFdNm(), diet.getCntntsNo(), diet.getCntntsSj(),0);
                imageView.setImageResource(R.drawable.ic_star_white_18dp);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.star_item), Toast.LENGTH_SHORT).show();
            }else{
                starDBHelper.removeList(diet.getCntntsNo());
                imageView.setImageResource(R.drawable.ic_star_border_white_24dp);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.star_delete), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.star_default_error), Toast.LENGTH_SHORT).show();
        }
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
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(customDialog!=null){
            customDialog.cancel();
        }
    }
    @Override
    protected void onStop(){
        super.onStop();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
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
