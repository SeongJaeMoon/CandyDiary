package goods.cap.app.goodsgoods.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import goods.cap.app.goodsgoods.Adapter.StarPostAdapter;
import goods.cap.app.goodsgoods.Model.Firebase.Post;
import goods.cap.app.goodsgoods.Model.Firebase.Stars;
import goods.cap.app.goodsgoods.R;
import goods.cap.app.goodsgoods.Util.PostImageLoader;
import goods.cap.app.goodsgoods.Util.PostMainSlider;
import me.gujun.android.taggroup.TagGroup;
import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideClickListener;

public class AnotherUserActivity extends AppCompatActivity {
    @BindView(R.id.mainImage)ImageView mainImage;
    @BindView(R.id.imageButton)ImageButton postImage;
    @BindView(R.id.imageButton2)ImageButton commentImage;
    @BindView(R.id.textView)TextView nameText;//이름
    @BindView(R.id.textView2)TextView declarationText;//신고하기
    @BindView(R.id.textView3)TextView starsText;//즐겨찾기 목록
    @BindView(R.id.textView4)TextView postsText;//포스팅 목록
    @BindView(R.id.my_toolbar)Toolbar toolbar;
    @BindView(R.id.postTitle)TextView postTitle;
    @BindView(R.id.post_list)RecyclerView postList;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm aa", Locale.KOREA);
    private static final String logger = UserProfileActivity.class.getSimpleName();
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private BottomSheetDialog bottomSheetDialog;
    private List<Stars> starsList;
    private boolean likeProcess = false;
    private DatabaseReference postRef;
    private DatabaseReference starRef;
    private DatabaseReference likeRef;
    private String anotherUid, anotherName, anotherImg;
    private String uid;
    private FirebaseRecyclerAdapter<Post, AnotherUserActivity.PostViewHolder>firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_user);
        ButterKnife.bind(this);
        final Intent intent = getIntent();
        if(intent != null) {
            anotherUid = intent.getStringExtra("uid");
            auth = FirebaseAuth.getInstance();
            uid = auth.getCurrentUser().getUid();

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            auth = FirebaseAuth.getInstance();

            dbRef = FirebaseDatabase.getInstance().getReference().child("users");
            postRef = FirebaseDatabase.getInstance().getReference().child("posts");
            starRef = FirebaseDatabase.getInstance().getReference().child("stars");
            likeRef = FirebaseDatabase.getInstance().getReference().child("likes");
            likeRef.keepSynced(true);
            postRef.keepSynced(true);
            starRef.keepSynced(true);
            dbRef.keepSynced(true);

            dbRef.child(anotherUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final String name = dataSnapshot.child("name").getValue(String.class);
                    final String pimage = dataSnapshot.child("profile_image").getValue(String.class);
                    anotherName = name;
                    anotherImg = pimage;
                    postTitle.setText(String.format("%s %s", name, getResources().getString(R.string.post_list_another)));
                    nameText.setText(name);
                    final RequestOptions ro = new RequestOptions()
                            .placeholder(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.empty_user))
                            .error(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.empty_user));

                    mainImage.post(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(AnotherUserActivity.this)
                                    .setDefaultRequestOptions(ro)
                                    .load(pimage)
                                    .into(mainImage);
                        }
                    });
                    mainImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(AnotherUserActivity.this, PostImageActivity.class);
                            intent.putExtra("image_uri", anotherImg);
                            startActivity(intent);
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
            Query postsQuery = postRef.orderByChild("uid").equalTo(anotherUid);
            postsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long postsCount = dataSnapshot.getChildrenCount();
                    postsText.setText(String.format(Locale.KOREA, "%s%d", getResources().getString(R.string.my_post_another), postsCount));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
            Query starsQuery = starRef.orderByKey();
            starsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long starsCount = 0;
                    List<String>keyList = new ArrayList<String>();
                    try {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (data.exists()) {
                                Map<String, Object> keyMap = (HashMap<String, Object>)data.getValue();
                                for (String s : keyMap.keySet()) {
                                    if (s.equals(anotherUid)) {
                                        String key = data.getKey();
                                        if(key != null){
                                            keyList.add(key);
                                        }
                                        starsCount += 1;
                                    }
                                }
                            }
                        }
                        starsList = new ArrayList<>(keyList.size());
                        for(String key : keyList){
                            if(key != null){
                                final Stars stars = new Stars();
                                stars.setFkey(key);
                                postRef.child(key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            String uid = dataSnapshot.child("uid").getValue(String.class);
                                            dbRef.child(uid).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    String userName = dataSnapshot.child("name").getValue(String.class);
                                                    String userProfileImage = dataSnapshot.child("profile_image").getValue(String.class);
                                                    stars.setUser(userName);
                                                    stars.setImg(userProfileImage);
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) { }
                                            });
                                            String title = dataSnapshot.child("title").getValue(String.class);
                                            Map<String, Object> tagMap = (HashMap<String, Object>)dataSnapshot.child("tags").getValue();
                                            List<String>tagList;
                                            if(tagMap != null){
                                                tagList = new ArrayList<String>(tagMap.size());
                                                for(Object o : tagMap.values()){
                                                    tagList.add(o.toString());
                                                }
                                                stars.setTags(tagList);
                                            }
                                            stars.setTitle(title);
                                            starsList.add(stars);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                                });
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    starsText.setText(String.format(Locale.KOREA,"%s%d", getResources().getString(R.string.star_post), starsCount));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            postList.setLayoutManager(layoutManager);
            starsText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(starsList.size() != 0){
                        bottomDialog();
                    }
                }
            });
            Slider.init(new PostImageLoader(this));

            declarationText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AnotherUserActivity.this);
                    alertDialog.setTitle(getResources().getString(R.string.declaration_post));
                    alertDialog.setMessage(getResources().getString(R.string.declaration_user));
                    alertDialog.setNegativeButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.setPositiveButton(getResources().getString(R.string.declaration_post), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setDeclaration();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.declaration_success), Toast.LENGTH_LONG).show();
                            //신고접수
                        }
                    });
                    alertDialog.create().show();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
        }
    }
    private void setDeclaration(){
        try {
            final DatabaseReference deRef = FirebaseDatabase.getInstance().getReference().child("declarate").push();
            deRef.child("id").setValue(anotherUid);
            deRef.child("time").setValue(sdf.format(new Date(System.currentTimeMillis())));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void bottomDialog(){
        bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new StarPostAdapter(starsList, getApplicationContext(), new StarPostAdapter.ItemListener() {
            @Override
            public void onItemClick(Stars stars) {
                if (bottomSheetDialog != null) {
                    bottomSheetDialog.dismiss();
                }
                final Intent intent = new Intent(AnotherUserActivity.this, PostDtlActivity.class);
                if(stars.getUser() != null){
                    intent.putExtra("userName", stars.getUser());
                }else{
                    intent.putExtra("userName", "");
                }
                intent.putExtra("postKey", stars.getFkey());
                startActivity(intent);
            }
        }));
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialog = null;
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

    private void initFirebase() {
        Query postsQuery = postRef.orderByChild("uid").equalTo(anotherUid);

        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postsQuery, Post.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, AnotherUserActivity.PostViewHolder>(options) {
            @NonNull
            @Override
            public AnotherUserActivity.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_cardview, parent, false);
                return new AnotherUserActivity.PostViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final AnotherUserActivity.PostViewHolder holder, final int position, @NonNull final Post model) {
                final String postKey = getRef(position).getKey();
                holder.setContext(getApplicationContext());
                DatabaseReference dbRef = postRef.child(postKey);
                dbRef.keepSynced(true);
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            Map<String, Object> imgMap = model.getImages();
                            Map<String, Object> tagMap = model.getTags();
                            String name = "";
                            if(imgMap != null){
                                List<String> tempList = new ArrayList<String>(imgMap.size());
                                for(Object o : imgMap.values()){
                                    tempList.add(o.toString());
                                }
                                PostMainSlider postMainSlider = new PostMainSlider(tempList);
                                if (holder.userNameFeeds.getText() != null) {
                                    name = holder.userNameFeeds.getText().toString();
                                }
                                holder.setPostImages(postMainSlider, name, postKey);
                            }else{
                                holder.setPostImages(null, name, postKey);
                            }
                            if(tagMap != null){
                                List<String>tempList = new ArrayList<String>(tagMap.size());
                                for(Object o : tagMap.values()){
                                    tempList.add(o.toString());
                                }
                                holder.setTags(tempList);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
                holder.setUserImage(getApplicationContext(), anotherImg);
                holder.setUserName(anotherName);
                holder.setTitle(model.getTitle());
                holder.setPostDate(model.getDate());
                holder.setLike(postKey);
                holder.likeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        likeProcess = true;
                        likeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (likeProcess) {
                                    if (dataSnapshot.child(postKey).hasChild(auth.getCurrentUser().getUid())) {
                                        holder.likeBtn.setImageResource(R.drawable.like_grey);
                                        likeRef.child(postKey).child(auth.getCurrentUser().getUid()).removeValue();
                                    } else {
                                        holder.likeBtn.setImageResource(R.drawable.like_orange);
                                        likeRef.child(postKey).child(auth.getCurrentUser().getUid()).setValue(sdf.format(new Date(System.currentTimeMillis())));
                                    }
                                    likeProcess = false;
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                holder.userNameFeeds.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                holder.userImgFeeds.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        };
        postList.setAdapter(firebaseRecyclerAdapter);
    }

    private static class PostViewHolder extends RecyclerView.ViewHolder{
        private View view;
        public CircleImageView userImgFeeds;
        public TextView userNameFeeds;
        private TextView userDateFeeds;
        public ImageView likeBtn;
        private TextView likeCount;
        private DatabaseReference likeRef;
        private FirebaseAuth auth;
        private Context context;

        private PostViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            likeRef = FirebaseDatabase.getInstance().getReference().child("likes");
            likeRef.keepSynced(true);
            auth = FirebaseAuth.getInstance();
            userImgFeeds = (CircleImageView)view.findViewById(R.id.userImgFeeds);
            userNameFeeds = (TextView)view.findViewById(R.id.userNameFeeds);
            userDateFeeds = (TextView)view.findViewById(R.id.userDateFeeds);
            likeBtn = (ImageView)view.findViewById(R.id.likeBtn);
            likeCount = (TextView)view.findViewById(R.id.likeCount);
        }
        public void setContext(Context context){
            this.context = context;
        }
        public void setTitle(String title) {
            TextView tvTitle = (TextView) view.findViewById(R.id.title);
            tvTitle.setText(title);
        }
        public void setPostImages(final PostMainSlider postAdapter, final String name, final String postKey) {
            final Slider slider = view.findViewById(R.id.banner_slider);
            if (postAdapter == null) {
                slider.setVisibility(View.GONE);
            } else {
                slider.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        slider.setAdapter(postAdapter);
                        slider.setSelectedSlide(0);
                        slider.setInterval(5000);
                        slider.setOnSlideClickListener(new OnSlideClickListener() {
                            @Override
                            public void onSlideClick(int position) {
                                Intent intent = new Intent(context.getApplicationContext(), PostDtlActivity.class);
                                if (name != null) {
                                    intent.putExtra("userName", name);
                                }
                                intent.putExtra("postKey", postKey);
                                context.startActivity(intent);
                            }
                        });
                    }
                }, 1500);
            }
        }
        public void setTags(List<String> tags){
            TagGroup tagGroup = (TagGroup)view.findViewById(R.id.tag_group);
            tagGroup.setTags(tags);
        }
        public void setUserImage(Context context, String url){
            RequestOptions ro = new RequestOptions()
                    .placeholder(ContextCompat.getDrawable(context, R.mipmap.empty_user))
                    .error(ContextCompat.getDrawable(context, R.mipmap.empty_user));
            Glide.with(context)
                    .setDefaultRequestOptions(ro)
                    .load(url)
                    .into(userImgFeeds);
        }
        public void setUserName(String userName){
            userNameFeeds.setText(userName);
        }
        public void setPostDate(String date){
            userDateFeeds.setText(date);
        }
        public void setLike(final String postKey){
            likeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(postKey).hasChild(auth.getCurrentUser().getUid())) {
                        likeBtn.setImageResource(R.drawable.like_orange);
                        likeCount.setTextColor(Color.rgb(252, 176, 48));
                    } else {
                        likeCount.setTextColor(Color.rgb(127, 127, 127));
                        likeBtn.setImageResource(R.drawable.like_grey);
                    }
                    long likes = dataSnapshot.child(postKey).getChildrenCount();
                    if(likes == 0){
                        likeCount.setVisibility(View.GONE);
                    }else{
                        likeCount.setVisibility(View.VISIBLE);
                        likeCount.setText(String.valueOf(likes));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(logger, databaseError.getMessage());
                }
            });
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        initFirebase();
        firebaseRecyclerAdapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
