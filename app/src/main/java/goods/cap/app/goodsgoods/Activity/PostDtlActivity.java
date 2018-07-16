package goods.cap.app.goodsgoods.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.MainActivity;
import goods.cap.app.goodsgoods.Model.Firebase.Post;
import goods.cap.app.goodsgoods.R;
import goods.cap.app.goodsgoods.Util.PostMainSlider;
import me.gujun.android.taggroup.TagGroup;
import ss.com.bannerslider.Slider;

public class PostDtlActivity extends AppCompatActivity {
    @BindView(R.id.tag_cn)TagGroup tag_cn; //태그 그룹
    @BindView(R.id.detail_toolbar)Toolbar toolbar;
    @BindView(R.id.coordinator_layout)CoordinatorLayout coordinatorLayout;
    @BindView(R.id.wallpaper)Slider wallpaper;
    @BindView(R.id.contents_title)TextView contents_title;
    @BindView(R.id.contents)TextView contents;
    @BindView(R.id.like_img)ImageView likeImg;//좋아요 이미지
    @BindView(R.id.likes)TextView likes;//좋아요 수
    @BindView(R.id.share_img)ImageView shareImg;//신고 이미지
    @BindView(R.id.share_nm)TextView shareNm;
    @BindView(R.id.comment_img)ImageView commentImg;//댓글 이미지
    @BindView(R.id.comment_detail)TextView commentDetail;//댓글보기
    @BindView(R.id.comments)TextView comments;//댓글 수
    @BindView(R.id.dateText)TextView dateText; // 등록일
    @BindView(R.id.star_img)ImageView starImg; //즐겨찾기 추가
    private boolean isLikeProcess = false;
    private boolean isLikeStarProcess = false;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    private String isAnotherUid;
    private String shareTitle = "";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.KOREA);
    private boolean isMyPost = false;
    private String deleteKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_dtl);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent != null){
            final String postKey = intent.getStringExtra("postKey");
            deleteKey = postKey;
            final String userName = intent.getStringExtra("userName");
            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("posts").child(postKey);
            postRef.keepSynced(true);
            postRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if(dataSnapshot.exists()) {
                            Post post = dataSnapshot.getValue(Post.class);
                            if(post != null){
                              Map<String, Object>imgMap = post.getImages();
                              if(imgMap != null){
                                  List<String> imgList = new ArrayList<String>(imgMap.size());
                                  for(Object o : imgMap.values()){
                                      imgList.add(o.toString());
                                  }
                                  final PostMainSlider postMainSlider = new PostMainSlider(imgList);
                                  wallpaper.postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          wallpaper.setAdapter(postMainSlider);
                                          wallpaper.setSelectedSlide(0);
                                          wallpaper.setInterval(5000);
                                      }
                                  }, 1000);
                              }
                              Map<String, Object>tagMap = post.getTags();
                              if(tagMap != null){
                                  List<String>tagList = new ArrayList<String>(tagMap.size());
                                  for(Object o : tagMap.values()){
                                      tagList.add(o.toString());
                                  }
                                  tag_cn.setTags(tagList);
                              }
                              String date = post.getDate();
                              dateText.setText(String.format("%s에 작성",date));
                              String title = post.getTitle();
                              contents_title.setText(title);
                              shareTitle = title;
                              String desc = post.getDesc();
                                if (desc != null) {
                                    contents.setText(desc);
                            }
                              setHeadLayout(String.format("%s님이 작성한 글", userName));
                            }
                            isAnotherUid = dataSnapshot.child("uid").getValue(String.class);
                            if(isAnotherUid != null) {
                                if (isAnotherUid.equals(uid)) {
                                    shareImg.setVisibility(View.GONE);
                                    shareNm.setVisibility(View.GONE);
                                    starImg.setVisibility(View.GONE);
                                    isMyPost = true;
                                }
                            }
                        }
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            commentDetail.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PostDtlActivity.this, CommentActivity.class);
                    intent.putExtra("cntntno", postKey);
                    intent.putExtra("fdnm", shareTitle);
                    startActivity(intent);
                }
            });
            initFirebase(postKey);
            shareDialog = new ShareDialog(this);
            callbackManager = CallbackManager.Factory.create();
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) { }

                @Override
                public void onCancel() { }

                @Override
                public void onError(FacebookException error) { }
            });
            if(tag_cn!=null){
                tag_cn.setOnTagClickListener(new TagGroup.OnTagClickListener() {
                    @Override
                    public void onTagClick(String tag) {
                        //start SearchActivity
                        if(tag != null){
                            Intent intent = new Intent(getApplicationContext(), TagActivity.class);
                            intent.putExtra("tag", tag);
                            startActivity(intent);
                        }
                    }
                });
            }
        }
    }
    private void initFirebase(final String postKey){
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        //사용자 정보
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final String uid = auth.getCurrentUser().getUid();
        //좋아요
        final DatabaseReference dbRef = db.getReference().child("likes").child(postKey);
        final DatabaseReference dbRef2 = db.getReference().child("comments").child(postKey);
        final DatabaseReference dbRef3 = db.getReference().child("shares").child(postKey);
        final DatabaseReference dbRef4 = db.getReference().child("stars").child(postKey);
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
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PostDtlActivity.this);
                alertDialog.setTitle(getResources().getString(R.string.declaration_post));
                alertDialog.setMessage(getResources().getString(R.string.declaration_noti));
                alertDialog.setNegativeButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setPositiveButton(getResources().getString(R.string.declaration_post), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setDeclaration(postKey);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.declaration_success), Toast.LENGTH_LONG).show();
                        //신고접수
                    }
                });
                alertDialog.create().show();
            }
        });
        starImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLikeStarProcess = true;
                dbRef4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(isLikeStarProcess) {
                            if(dataSnapshot.hasChild(uid)) {
                                dbRef4.child(uid).removeValue();
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.star_post_click2), Toast.LENGTH_LONG).show();
                                starImg.setImageResource(R.drawable.ic_star_border_white_24dp);
                            } else {
                                dbRef4.child(uid).setValue(sdf.format(new Date(System.currentTimeMillis())));
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.star_post_click1), Toast.LENGTH_LONG).show();
                                starImg.setImageResource(R.drawable.ic_star_white_18dp);
                            }
                            isLikeStarProcess = false;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }
        });
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uid)) {
                    likeImg.setImageResource(R.drawable.like_orange);
                } else {
                    likeImg.setImageResource(R.drawable.ic_favorite_white_24dp);
                }
                long likesCount = dataSnapshot.getChildrenCount();
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
                comments.setText(String.valueOf(commetCount));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dbRef4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!isMyPost) {
                    if (dataSnapshot.hasChild(uid)) {
                        starImg.setImageResource(R.drawable.ic_star_white_18dp);
                    } else {
                        starImg.setImageResource(R.drawable.ic_star_border_white_24dp);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setDeclaration(String postUid){
        try {
            final DatabaseReference deRef = FirebaseDatabase.getInstance().getReference().child("declarate").push();
            deRef.child("pid").setValue(postUid);
            deRef.child("time").setValue(sdf.format(new Date(System.currentTimeMillis())));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setHeadLayout(String title){
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isMyPost){
                    Intent intent = new Intent(getApplicationContext(), AnotherUserActivity.class);
                    intent.putExtra("uid", isAnotherUid);
                    startActivity(intent);
                }
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        if(!isMyPost) {
            MenuItem registrar = menu.findItem(R.id.action_delete);
            registrar.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.action_delete){
            deleteClick();
        }
        return super.onOptionsItemSelected(item);
    }
    private void deleteClick(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(PostDtlActivity.this);
        builder.setMessage(getResources().getString(R.string.confrim_delete));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    final FirebaseDatabase db = FirebaseDatabase.getInstance();
                    final DatabaseReference dbRef = db.getReference().child("likes").child(deleteKey);
                    final DatabaseReference dbRef2 = db.getReference().child("comments").child(deleteKey);
                    final DatabaseReference dbRef3 = db.getReference().child("shares").child(deleteKey);
                    final DatabaseReference dbRef4 = db.getReference().child("stars").child(deleteKey);
                    final DatabaseReference dbRef5 = db.getReference().child("posts").child(deleteKey);
                    dbRef.removeValue();
                    dbRef2.removeValue();
                    dbRef3.removeValue();
                    dbRef4.removeValue();
                    dbRef5.removeValue();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.success_delete),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostDtlActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.fail_delete_post), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
        this.finish();
        super.onBackPressed();
    }
}
