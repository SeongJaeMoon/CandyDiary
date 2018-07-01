package goods.cap.app.goodsgoods.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import goods.cap.app.goodsgoods.Activity.AnotherUserActivity;
import goods.cap.app.goodsgoods.Activity.PostDtlActivity;
import goods.cap.app.goodsgoods.Activity.SearchActivity;
import goods.cap.app.goodsgoods.Activity.UserProfileActivity;
import goods.cap.app.goodsgoods.Model.Firebase.Post;
import goods.cap.app.goodsgoods.R;
import goods.cap.app.goodsgoods.Util.MultiSwipeRefreshLayout;
import goods.cap.app.goodsgoods.Util.PostImageLoader;
import goods.cap.app.goodsgoods.Util.PostMainSlider;
import me.gujun.android.taggroup.TagGroup;
import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideClickListener;

public class ComFragment extends Fragment implements MultiSwipeRefreshLayout.OnRefreshListener{

    private static final String logger = ComFragment.class.getSimpleName();
    public static final String ARG_PAGE = "ARG_PAGE";
    private TextView comTitle;
    private RecyclerView comRView;
    private CircleImageView comImg1, comImg2, comImg3, comImg4;
    private TextView comTxt1, comTxt2, comTxt3, comTxt4;
    private MultiSwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAuth auth;
    private DatabaseReference postRef;
    private DatabaseReference userRef;
    private DatabaseReference likeRef;
    private boolean likeProcess = false;
    private Parcelable parcelable;
    private LinearLayoutManager layoutManager;
    private static final String LIST_STATE_KEY = "RC_STATE";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm aa", Locale.KOREA);
    private boolean isLoading = false;
    private String lastKey = "";
    private FirebaseRecyclerAdapter<Post, PostViewHolder>firebaseRecyclerAdapter;

    public static ComFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ComFragment fragment = new ComFragment();
        fragment.setArguments(args);
        Log.i(logger, "page : "+ args);
        return fragment;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_com, container, false);
        swipeRefreshLayout = (MultiSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        comTitle = (TextView)view.findViewById(R.id.mainTitle);
        comRView = (RecyclerView)view.findViewById(R.id.comRView);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        comRView.setLayoutManager(layoutManager);
        comRView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isLoading && !comRView.canScrollVertically(1) && lastKey != null && !lastKey.equals("")) {
                    Log.w(logger, "load more");
                }
            }
        });
        comImg1 = (CircleImageView)view.findViewById(R.id.com_img1);
        comImg2 = (CircleImageView)view.findViewById(R.id.com_img2);
        comImg3 = (CircleImageView)view.findViewById(R.id.com_img3);
        comImg4 = (CircleImageView)view.findViewById(R.id.com_img4);
        comTxt1 = (TextView)view.findViewById(R.id.com_txt1);
        comTxt2 = (TextView)view.findViewById(R.id.com_txt2);
        comTxt3 = (TextView)view.findViewById(R.id.com_txt3);
        comTxt4 = (TextView)view.findViewById(R.id.com_txt4);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            for (String key : savedInstanceState.keySet()) {
                Log.i(logger, "save key =>" + key);
            }
            parcelable = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        parcelable = layoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, parcelable);
    }

    @Override
    public void onStart(){
        super.onStart();
        initFirebase();
        loadPosts();
        firebaseRecyclerAdapter.startListening();
    }
    private void initFirebase() {
        auth = FirebaseAuth.getInstance();
        postRef = FirebaseDatabase.getInstance().getReference().child("posts");
        postRef.keepSynced(true);
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        userRef.keepSynced(true);
        likeRef = FirebaseDatabase.getInstance().getReference().child("likes");
        likeRef.keepSynced(true);

        Query query = postRef.orderByChild("uid").limitToFirst(4);

        final RequestOptions ro = new RequestOptions()
                .placeholder(ContextCompat.getDrawable(getActivity(), R.mipmap.empty_user))
                .error(ContextCompat.getDrawable(getActivity(), R.mipmap.empty_user));

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        final List<String> tempList = new ArrayList<String>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            String uid = data.child("uid").getValue(String.class);
                            tempList.add(uid);
                        }
                        userRef.child(tempList.get(0)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String img = dataSnapshot.child("profile_image").getValue(String.class);
                                final String name = dataSnapshot.child("name").getValue(String.class);
                                comTxt1.setText(name);
                                comImg1.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide.with(getActivity()).setDefaultRequestOptions(ro).load(img).into(comImg1);
                                    }
                                });
                                comImg1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(), AnotherUserActivity.class);
                                        intent.putExtra("uid", tempList.get(0));
                                        startActivity(intent);
                                    }
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                        });
                        userRef.child(tempList.get(1)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String img = dataSnapshot.child("profile_image").getValue(String.class);
                                final String name = dataSnapshot.child("name").getValue(String.class);
                                comTxt2.setText(name);
                                comImg2.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide.with(getActivity()).setDefaultRequestOptions(ro).load(img).into(comImg2);
                                    }
                                });
                                comImg2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(), AnotherUserActivity.class);
                                        intent.putExtra("uid", tempList.get(1));
                                        startActivity(intent);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                        });
                        userRef.child(tempList.get(2)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String img = dataSnapshot.child("profile_image").getValue(String.class);
                                final String name = dataSnapshot.child("name").getValue(String.class);
                                comTxt3.setText(name);
                                comImg3.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide.with(getActivity()).setDefaultRequestOptions(ro).load(img).into(comImg3);
                                    }
                                });
                                comImg3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(), AnotherUserActivity.class);
                                        intent.putExtra("uid", tempList.get(2));
                                        startActivity(intent);
                                    }
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                        });
                        userRef.child(tempList.get(3)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String img = dataSnapshot.child("profile_image").getValue(String.class);
                                final String name = dataSnapshot.child("name").getValue(String.class);
                                comTxt4.setText(name);
                                comImg4.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide.with(getActivity()).setDefaultRequestOptions(ro).load(img).into(comImg4);
                                    }
                                });
                                comImg4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(), AnotherUserActivity.class);
                                        intent.putExtra("uid", tempList.get(3));
                                        startActivity(intent);
                                    }
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    private void loadPosts(){
        try {
            Slider.init(new PostImageLoader(getActivity()));

            Query query = postRef.orderByKey();

            FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                    .setQuery(query, Post.class)
                    .build();

            firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
                @NonNull
                @Override
                public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_cardview, parent, false);
                    return new PostViewHolder(view);
                }
                @Override
                protected void onBindViewHolder(@NonNull final PostViewHolder holder, int position, @NonNull final Post model) {

                    final String postUid = model.getUid();
                    final String postKey = getRef(position).getKey();
                    Log.i(logger, postKey + ", " + postUid);

                    if (position == 0) {
                        lastKey = getRef(position).getKey();
                    }

                    holder.setContext(getActivity());

                    DatabaseReference dbUserRef = userRef.child(postUid);
                    DatabaseReference dbRef = postRef.child(postKey);
                    dbUserRef.keepSynced(true);
                    dbRef.keepSynced(true);

                    dbUserRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                String userName = dataSnapshot.child("name").getValue(String.class);
                                String userProfileImage = dataSnapshot.child("profile_image").getValue(String.class);
                                holder.setUserImage(userProfileImage, postUid);
                                holder.setUserName(userName, postUid);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                    dbRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                Map<String, Object> imgMap = model.getImages();
                                Map<String, Object> tagMap = model.getTags();
                                String name = "";
                                if (imgMap != null) {
                                    List<String> tempList = new ArrayList<String>(imgMap.size());
                                    for (Object o : imgMap.values()) {
                                        tempList.add(o.toString());
                                    }
                                    PostMainSlider postMainSlider = new PostMainSlider(tempList);
                                    if (holder.userNameFeeds.getText() != null) {
                                        name = holder.userNameFeeds.getText().toString();
                                    }
                                    holder.setPostImages(postMainSlider, name, postKey);
                                } else {
                                    holder.setPostImages(null, name, postKey);
                                }
                                if (tagMap != null) {
                                    List<String> tempList = new ArrayList<String>(tagMap.size());
                                    for (Object o : tagMap.values()) {
                                        tempList.add(o.toString());
                                    }
                                    holder.setTags(tempList);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
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
                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                            });
                        }
                    });
                }
            };
            comRView.setAdapter(firebaseRecyclerAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        private View view;
        private CircleImageView userImgFeeds;
        private TextView userNameFeeds;
        private TextView userDateFeeds;
        private TextView titleFeeds;
        private ImageView likeBtn;
        private TextView likeCount;
        private DatabaseReference likeRef;
        private FirebaseAuth auth;
        private Slider slider;
        private Context context;

        private PostViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            likeRef = FirebaseDatabase.getInstance().getReference().child("likes");
            likeRef.keepSynced(true);
            auth = FirebaseAuth.getInstance();
            titleFeeds = (TextView) view.findViewById(R.id.title);
            userImgFeeds = (CircleImageView)view.findViewById(R.id.userImgFeeds);
            userNameFeeds = (TextView)view.findViewById(R.id.userNameFeeds);
            userDateFeeds = (TextView)view.findViewById(R.id.userDateFeeds);
            likeBtn = (ImageView)view.findViewById(R.id.likeBtn);
            likeCount = (TextView)view.findViewById(R.id.likeCount);
            slider = (Slider)view.findViewById(R.id.banner_slider);
        }
        public void setContext(Context context){
            this.context = context;
        }
        public void setTitle(String title) {
            titleFeeds.setText(title);
        }
        public void setPostImages(final PostMainSlider postAdapter, final String name, final String postKey){
            if(postAdapter == null){
                slider.setVisibility(View.GONE);
            }else {
                slider.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        slider.setAdapter(postAdapter);
                        slider.setSelectedSlide(0);
                        slider.setInterval(5000);
                        slider.setOnSlideClickListener(new OnSlideClickListener() {
                            @Override
                            public void onSlideClick(int position) {
                                final Intent intent = new Intent(context.getApplicationContext(), PostDtlActivity.class);
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
            tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
                @Override
                public void onTagClick(String tag) {
                    //start SearchActivity
                    if(tag != null){
                        Intent intent = new Intent(context.getApplicationContext(), SearchActivity.class);
                        intent.putExtra("tag", tag);
                        context.startActivity(intent);
                    }
                }
            });
        }
        public void setUserImage(String url, final String uid){
            RequestOptions ro = new RequestOptions()
                    .placeholder(ContextCompat.getDrawable(context, R.mipmap.empty_user))
                    .error(ContextCompat.getDrawable(context, R.mipmap.empty_user));
            Glide.with(context)
                    .setDefaultRequestOptions(ro)
                    .load(url)
                    .into(userImgFeeds);
            userImgFeeds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //start AnotherUserActivity
                    if(uid != null && !uid.equals(auth.getCurrentUser().getUid())){
                        Intent intent = new Intent(context.getApplicationContext(), AnotherUserActivity.class);
                        intent.putExtra("uid", uid);
                        context.startActivity(intent);
                    }else{
                        Intent intent = new Intent(context.getApplicationContext(), UserProfileActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        }
        public void setUserName(String userName, final String uid){
            userNameFeeds.setText(userName);
            userNameFeeds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //start AnotherUserActivity
                    if(uid != null && !uid.equals(auth.getCurrentUser().getUid())){
                        Intent intent = new Intent(context.getApplicationContext(), AnotherUserActivity.class);
                        intent.putExtra("uid", uid);
                        context.startActivity(intent);
                    }else{
                        Intent intent = new Intent(context.getApplicationContext(), UserProfileActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
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
    public void onRefresh() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if (parcelable != null) layoutManager.onRestoreInstanceState(parcelable);
    }
    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    public void onStop(){
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    @Override
    public void onDetach(){
        super.onDetach();
    }
}
