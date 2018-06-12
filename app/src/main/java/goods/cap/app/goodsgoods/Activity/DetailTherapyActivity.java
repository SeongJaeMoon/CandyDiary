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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.API.Config;
import goods.cap.app.goodsgoods.API.MainHttp;
import goods.cap.app.goodsgoods.Helper.RecentDBHelper;
import goods.cap.app.goodsgoods.Helper.TherapyDtlHelper;
import goods.cap.app.goodsgoods.Model.Therapy.Therapy;
import goods.cap.app.goodsgoods.Model.Therapy.TherapyDtl;
import goods.cap.app.goodsgoods.Model.Therapy.TherapyDtlResponseModel;
import goods.cap.app.goodsgoods.R;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class DetailTherapyActivity extends AppCompatActivity {

    @BindView(R.id.diet_cn)TextView diet_cn;//학술 명칭(bneNm)
    @BindView(R.id.hbdcNm)TextView hbdcNm;//학술명
    @BindView(R.id.prvateTherpy)TextView prvateTherpy;//약초 효능
    @BindView(R.id.stle)TextView stil;//약초 설명
    @BindView(R.id.detail_toolbar)Toolbar toolbar;
    @BindView(R.id.coordinator_layout)CoordinatorLayout coordinatorLayout;
    @BindView(R.id.wallpaper)ImageView wallpaper;
    @BindView(R.id.like_img)ImageView likeImg;//좋아요 이미지
    @BindView(R.id.likes)TextView likes;//좋아요 수
    @BindView(R.id.share_img)ImageView shareImg;//공유 이미지
    @BindView(R.id.share_nm)TextView shares;//공유 수
    @BindView(R.id.comment_img)ImageView commentImg; //댓글 이미지
    @BindView(R.id.comment_detail)TextView commentDetail;//댓글보기
    @BindView(R.id.comments)TextView comments;//댓글 수
    @BindView(R.id.detail_img)ImageView imageView; //즐겨찾기
    private String shareText;
    private String imgUrl;
    private String shareTitle;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private static final String logger = DetailTherapyActivity.class.getSimpleName();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.KOREA);
    private boolean isLikeProcess = false;
    private long like, comment, share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_therapy);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent != null){
            Gson gson = new Gson();
            final String jsonData = intent.getStringExtra("therapy");
            final Therapy therapy = gson.fromJson(jsonData, Therapy.class);

            diet_cn.setText(therapy.getBneNm());

            //WallPaper 이미지 처리
            RequestOptions ro = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.none)
                    .placeholder(R.drawable.none)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 1)));

            Glide.with(this)
                    .setDefaultRequestOptions(ro)
                    .load(therapy.getImgUrl())
                    .into(wallpaper);

            setHeadLayout(therapy.getCntntsSj());
            setRecent(therapy);

            //for share
            imgUrl = therapy.getImgUrl();
            shareDialog = new ShareDialog(this);
            callbackManager = CallbackManager.Factory.create();
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Log.i(logger, "facebook => "+result.toString());
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {
                    Log.i(logger, "facebook => "+error.toString());
                }
            });
            shareTitle = therapy.getCntntsSj();

            String contentNo = therapy.getCntntsNo();

            initFirebase(contentNo);
            initData(contentNo);

            commentDetail.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailTherapyActivity.this, CommentActivity.class);
                    intent.putExtra("cntntno", therapy.getCntntsNo());
                    intent.putExtra("fdnm", therapy.getCntntsSj());
                    startActivity(intent);
                }
            });
            //좋아요 버튼
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.star_item), Toast.LENGTH_SHORT).show();
                }
            });
            initFirebase(contentNo);
        }else{
            Log.w(logger, "Error");
        }
    }
    private void setRecent(Therapy therapy) {
        RecentDBHelper recentDBHelper = new RecentDBHelper(this);
        try {
            recentDBHelper.open();
            if(recentDBHelper.isRecentExists(therapy.getCntntsNo())) {
                recentDBHelper.addRecent(therapy.getImgUrl(), therapy.getBneNm(), therapy.getCntntsNo(), therapy.getCntntsSj(),1);
                //데이터 삭제 코드
                recentDBHelper.deletOlder();
            }
        } catch (Exception e) {
            Log.w(logger, e.getMessage());
        } finally {
            recentDBHelper.close();
        }
    }

    private void initFirebase(String cntntsNo) {
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
                        if(isLikeProcess) {
                            if(dataSnapshot.hasChild(uid)) {
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
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailTherapyActivity.this);
                alertDialog.setTitle(getResources().getString(R.string.shareTitle));
                alertDialog.setItems(new CharSequence[]{getResources().getString(R.string.facebookShare), getResources().getString(R.string.kakaoShare), getResources().getString(R.string.close)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            dbRef3.push().child(uid).setValue(sdf.format(new Date(System.currentTimeMillis())));
                            //마켓URL 넣기
                            ShareLinkContent content = new ShareLinkContent.Builder()
                                    .setContentUrl(Uri.parse(imgUrl))
                                    .setQuote(getResources().getString(R.string.shareText))
                                    .build();
                            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                        } else if(which == 1){
                            dbRef3.push().child(uid).setValue(sdf.format(new Date(System.currentTimeMillis())));
                            FeedTemplate params = FeedTemplate
                                    .newBuilder(ContentObject.newBuilder(shareTitle, imgUrl,
                                            LinkObject.newBuilder().setWebUrl("market://details?id=goods.cap.app.goodsgoods")
                                                    .setMobileWebUrl("market://details?id=goods.cap.app.goodsgoods").build())
                                            .setDescrption(shareText)
                                            .build())
                                    .setSocial(SocialObject.newBuilder().setLikeCount((int)like).setCommentCount((int)comment)
                                            .setSharedCount(((int)share)).build())
                                    .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                                            .setMobileWebUrl("'market://details?id=goods.cap.app.goodsgoods")
                                            .setAndroidExecutionParams("market://details?id=goods.cap.app.goodsgoods")
                                            .setWebUrl("'market://details?id=goods.cap.app.goodsgoods")
                                            .build()))
                                    .build();

                            KakaoLinkService.getInstance().sendDefault(DetailTherapyActivity.this, params, new ResponseCallback<KakaoLinkResponse>() {
                                @Override
                                public void onFailure(ErrorResult errorResult) {
                                    Logger.w(errorResult.toString());
                                }
                                @Override
                                public void onSuccess(KakaoLinkResponse result) {

                                }
                            });
                        }else{
                            dialog.cancel();
                        }
                    }
                });
                alertDialog.create().show();
            }
        });

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uid)) {
                    likeImg.setImageResource(R.drawable.like_orange);
                } else {
                    likeImg.setImageResource(R.drawable.ic_favorite_white_24dp);
                }
                long likesCount = dataSnapshot.getChildrenCount();
                like = likesCount;
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
                comment = likesCount;
                comments.setText(String.valueOf(likesCount));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dbRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long sharesCount = dataSnapshot.getChildrenCount();
                share = sharesCount;
                shares.setText(String.valueOf(sharesCount));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initData(String cntntsNo){
        MainHttp mainHttp = new MainHttp(DetailTherapyActivity.this, getResources().getString(R.string.data_refresh_title), getResources().getString(R.string.data_refresh), Config.API_KEY2);
        mainHttp.setCntntsNo(cntntsNo);
        mainHttp.getTherapyDtl(new TherapyDtlHelper() {
            @Override
            public void success(TherapyDtlResponseModel response) {
                TherapyDtl therapyDtl = response.getBody().getItem();
                hbdcNm.setText(therapyDtl.getHbdcNm());
                prvateTherpy.setText(therapyDtl.getPrvateTherpy());
                shareText = therapyDtl.getPrvateTherpy();
                String temp = "";
                try {
                    temp = removeTag(therapyDtl.getStle());
                }catch (Exception e){
                    e.printStackTrace();
                }
                stil.setText(temp);
            }
            @Override
            public void failure(String message) {
                Log.i(logger, "error" + message);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String removeTag(String html) throws Exception {
        return html.replaceAll("<(/)?[bB][rR](\\s)*(/)?>", "\n");
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

    private void setHeadLayout(String title){
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onResume(){
        super.onResume();
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
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
