package goods.cap.app.goodsgoods.Fragment;

import android.app.ProgressDialog;
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
import android.text.TextUtils;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import goods.cap.app.goodsgoods.Activity.AnotherUserActivity;
import goods.cap.app.goodsgoods.Activity.PostDtlActivity;
import goods.cap.app.goodsgoods.Activity.TagActivity;
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
    private RecyclerView comRView;
    private CircleImageView comImg1, comImg2, comImg3, comImg4;
    private TextView comTxt1, comTxt2, comTxt3, comTxt4;
    private MultiSwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAuth auth;
    private DatabaseReference postRef;
    private DatabaseReference userRef;
    private DatabaseReference likeRef;
    private Parcelable parcelable;
    private LinearLayoutManager layoutManager;
    private static final String LIST_STATE_KEY = "RC_STATE";
    private ProgressDialog progressDialog;
    private boolean likeProcess = false;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm aa", Locale.KOREA);

    public static ComFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ComFragment fragment = new ComFragment();
        fragment.setArguments(args);
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
        comRView = (RecyclerView)view.findViewById(R.id.comRView);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        comRView.setLayoutManager(layoutManager);
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
        Slider.init(new PostImageLoader(getActivity()));
        loadPosts();
        firebaseRecyclerAdapter.startListening();
    }
    private TreeMap<String, Long> sortKey(final HashMap map){
        Comparator<String> comparator = new ValueComparator(map);
        TreeMap<String, Long> result = new TreeMap<String, Long>(comparator);
        result.putAll(map);
        return result;
    }
    static class ValueComparator implements Comparator<String>{
        HashMap<String, Long> map = new HashMap<String, Long>();
        public ValueComparator(HashMap<String, Long> map){
            this.map.putAll(map);
        }
        @Override
        public int compare(String s1, String s2) {
            if(map.get(s1) >= map.get(s2)){
                return -1;
            }else{
                return 1;
            }
        }
    }
    private void initFirebase() {
        try {
            auth = FirebaseAuth.getInstance();
            postRef = FirebaseDatabase.getInstance().getReference().child("posts");
            postRef.keepSynced(true);
            userRef = FirebaseDatabase.getInstance().getReference().child("users");
            userRef.keepSynced(true);
            likeRef = FirebaseDatabase.getInstance().getReference().child("likes");
            likeRef.keepSynced(true);
            final HashMap<String, Long> keyList = new HashMap<>();
            likeRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            final String key = data.getKey();
                            if (key != null) {
                                if (!isNum(key)) {
                                    keyList.put(key, data.getChildrenCount());
                                }
                            }
                        }
                        setUserRef(keyList);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setUserRef(final HashMap<String, Long> keyList){
        final List<String> postList = new ArrayList<String>();
        int keyLen = keyList.size();
        if (keyLen > 0) {
            TreeMap<String, Long> key = sortKey(keyList);
            for (Map.Entry<String, Long> it : key.entrySet()) {
                postList.add(it.getKey());
                //정렬 후 4개 까지만 저장
                Log.i(logger, "good-key: " + (String) it.getKey());
                if (postList.size() > 3) {
                    break;
                }
            }
        }
        if (postList.size() > 0) {
            final List<String> userList = new ArrayList<>();
            postRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            for (String postkey : postList) {
                                if (postkey.equals(data.getKey())) {
                                    String uid = data.child("uid").getValue(String.class);
                                    if (uid != null && !userList.contains(uid)) {
                                        userList.add(uid);
                                    }
                                }
                            }
                        }
                        final int userLen = userList.size();
                        if (userLen != 0) {
                            userRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (int i = 0; i < userLen; ++i) {
                                            String key = userList.get(i);
                                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                if (key.equals(data.getKey())) {
                                                    String img = data.child("profile_image").getValue(String.class);
                                                    String name = data.child("name").getValue(String.class);
                                                    if(img == null){
                                                        img = "";
                                                    }
                                                    if(name == null){
                                                        name = "";
                                                    }
                                                    switch (i){
                                                        case 0:setTopUser(comImg1, comTxt1, name, img, key);
                                                            break;
                                                        case 1:setTopUser(comImg2, comTxt2, name, img, key);
                                                            break;
                                                        case 2:setTopUser(comImg3, comTxt3, name, img, key);
                                                            break;
                                                        case 3:setTopUser(comImg4, comTxt4, name, img, key);
                                                            break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                            });
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }
    private void setTopUser(final ImageView imageView, TextView textView, final String name, final String img, final String uid){
        textView.setText(name);
        imageView.post(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    RequestOptions ro = new RequestOptions()
                            .placeholder(ContextCompat.getDrawable(getActivity(), R.mipmap.empty_user))
                            .error(ContextCompat.getDrawable(getActivity(), R.mipmap.empty_user));
                    Glide.with(getActivity()).setDefaultRequestOptions(ro).load(img).into(imageView);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uid.equals(auth.getCurrentUser().getUid())) {
                    startActivity(new Intent(getActivity(), UserProfileActivity.class));
                } else {
                    Intent intent = new Intent(getActivity(), AnotherUserActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                }
            }
        });
    }
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.data_refresh));
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }
    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    private void loadPosts(){
        try {
            showProgressDialog();
            Query query = postRef.orderByKey().limitToFirst(100);

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
                    holder.setContext(getActivity());
                    try {
                        userRef.child(postUid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String userName = dataSnapshot.child("name").getValue(String.class);
                                String userProfileImage = dataSnapshot.child("profile_image").getValue(String.class);
                                if (userProfileImage != null && !TextUtils.isEmpty(userProfileImage))
                                    holder.setUserImage(userProfileImage, postUid);
                                if (userName != null && !TextUtils.isEmpty(userName))
                                    holder.setUserName(userName, postUid);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                hideProgressDialog();
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
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
                                public void onCancelled(@NonNull DatabaseError databaseError) { hideProgressDialog();}
                            });
                        }
                    });
                }
            };
            hideProgressDialog();
            comRView.setAdapter(firebaseRecyclerAdapter);
        }catch (Exception e){
            e.printStackTrace();
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
    private boolean isNum(String s){
            return Pattern.matches("^[0-9\\.]*$", s);
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
                }, 1000);
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
                        Intent intent = new Intent(context.getApplicationContext(), TagActivity.class);
                        intent.putExtra("tag", tag);
                        context.startActivity(intent);
                    }
                }
            });
        }
        public void setUserImage(final String url, final String uid){
            final RequestOptions ro = new RequestOptions()
                    .placeholder(ContextCompat.getDrawable(context, R.mipmap.empty_user))
                    .error(ContextCompat.getDrawable(context, R.mipmap.empty_user));
            userImgFeeds.post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(context)
                            .setDefaultRequestOptions(ro)
                            .load(url)
                            .into(userImgFeeds);
                }
            });
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
        public void setUserName(final String userName, final String uid){
            userNameFeeds.post(new Runnable() {
                @Override
                public void run() {
                    userNameFeeds.setText(userName);
                }
            });
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
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }
}
