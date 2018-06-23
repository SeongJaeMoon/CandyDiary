package goods.cap.app.goodsgoods.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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
import de.hdodenhof.circleimageview.CircleImageView;
import goods.cap.app.goodsgoods.Activity.PostDtlActivity;
import goods.cap.app.goodsgoods.Model.Firebase.Post;
import goods.cap.app.goodsgoods.R;
import goods.cap.app.goodsgoods.Util.MultiSwipeRefreshLayout;
import goods.cap.app.goodsgoods.Util.PostImageLoader;
import goods.cap.app.goodsgoods.Util.PostMainSlider;
import me.gujun.android.taggroup.TagGroup;
import ss.com.bannerslider.Slider;

public class ComFragment extends Fragment implements MultiSwipeRefreshLayout.OnRefreshListener{

    private static final String logger = ComFragment.class.getSimpleName();
    public static final String ARG_PAGE = "ARG_PAGE";
    private TextView comTitle;
    private RecyclerView comRView;
    private CircleImageView comImg1, comImg2, comImg3, comImg4;
    private MultiSwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAuth auth;
    private DatabaseReference likeRef;
    private DatabaseReference userRef;
    private DatabaseReference postRef;
    private boolean likeProcess = false;
    private Parcelable parcelable;
    private LinearLayoutManager layoutManager;
    private static final String LIST_STATE_KEY = "RC_STATE";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss", Locale.KOREA);
    private FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter;

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
        comImg1 = (CircleImageView)view.findViewById(R.id.com_img1);
        comImg2 = (CircleImageView)view.findViewById(R.id.com_img2);
        comImg3 = (CircleImageView)view.findViewById(R.id.com_img3);
        comImg4 = (CircleImageView)view.findViewById(R.id.com_img4);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            for (String key : savedInstanceState.keySet()) {
                Log.i(logger, "key : " + key);
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
        firebaseRecyclerAdapter.startListening();
    }
    private void initFirebase() {
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        likeRef = FirebaseDatabase.getInstance().getReference().child("likes");
        postRef = FirebaseDatabase.getInstance().getReference().child("posts");
        userRef.keepSynced(true);
        likeRef.keepSynced(true);
        postRef.keepSynced(true);
        Slider.init(new PostImageLoader(getActivity()));

        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postRef.orderByKey(), Post.class)
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
                DatabaseReference dbUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(postUid);
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("posts").child(postKey);
                dbUserRef.keepSynced(true);
                dbRef.keepSynced(true);
                dbUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            String userName = dataSnapshot.child("name").getValue(String.class);
                            String userProfileImage = dataSnapshot.child("profile_image").getValue(String.class);
                            holder.setUserImage(getActivity(), userProfileImage);
                            holder.setUserName(userName);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            Map<String, Object> imgMap = model.getImages();
                            Map<String, Object> tagMap = model.getTags();
                            if(imgMap != null){
                                List<String> tempList = new ArrayList<String>(imgMap.size());
                                for(Object o : imgMap.values()){
                                    tempList.add(o.toString());
                                }
                                PostMainSlider postMainSlider = new PostMainSlider(tempList);
                                holder.setPostImages(postMainSlider);
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
                holder.moreItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent intent = new Intent(getActivity(), PostDtlActivity.class);
                        if(holder.userNameFeeds.getText() != null){
                            intent.putExtra("userName", holder.userNameFeeds.getText().toString());
                        }else{
                            intent.putExtra("userName", "");
                        }
                        intent.putExtra("postKey", postKey);
                        startActivity(intent);
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
        comRView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onRefresh() {
        initFirebase();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private static class PostViewHolder extends RecyclerView.ViewHolder{

        private View view;
        public CircleImageView userImgFeeds;
        public TextView userNameFeeds;
        private TextView userDateFeeds;
        public ImageView likeBtn;
        private TextView likeCount;
        private TextView moreItem;
        private DatabaseReference likeRef;
        private FirebaseAuth auth;

        public PostViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            likeRef = FirebaseDatabase.getInstance().getReference().child("likes");
            likeRef.keepSynced(true);
            moreItem = (TextView)view.findViewById(R.id.moreItem);
            auth = FirebaseAuth.getInstance();
            userImgFeeds = (CircleImageView)view.findViewById(R.id.userImgFeeds);
            userNameFeeds = (TextView)view.findViewById(R.id.userNameFeeds);
            userDateFeeds = (TextView)view.findViewById(R.id.userDateFeeds);
            likeBtn = (ImageView)view.findViewById(R.id.likeBtn);
            likeCount = (TextView)view.findViewById(R.id.likeCount);
        }
        public void setTitle(String title) {
            TextView tvTitle = (TextView) view.findViewById(R.id.title);
            tvTitle.setText(title);
        }
        public void setPostImages(final PostMainSlider postAdapter){
            final Slider slider = view.findViewById(R.id.banner_slider);
            slider.postDelayed(new Runnable() {
                @Override
                public void run() {
                    slider.setAdapter(postAdapter);
                    slider.setSelectedSlide(0);
                    slider.setInterval(5000);
                }
            }, 1500);
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
