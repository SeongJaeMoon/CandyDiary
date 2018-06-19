package goods.cap.app.goodsgoods.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import goods.cap.app.goodsgoods.Model.Firebase.Post;
import goods.cap.app.goodsgoods.R;
import goods.cap.app.goodsgoods.Util.MultiSwipeRefreshLayout;
import goods.cap.app.goodsgoods.Util.PostImageLoader;
import goods.cap.app.goodsgoods.Util.PostMainSlider;
import me.gujun.android.taggroup.TagGroup;
import ss.com.bannerslider.Slider;

public class ComFragment extends Fragment{

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
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        likeRef = FirebaseDatabase.getInstance().getReference().child("likes");
        postRef = FirebaseDatabase.getInstance().getReference().child("posts");
        userRef.keepSynced(true);
        likeRef.keepSynced(true);
        postRef.keepSynced(true);
        Slider.init(new PostImageLoader(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_com, container, false);
        swipeRefreshLayout = (MultiSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
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
    }
    private void initFirebase() {
        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postRef.orderByKey(), Post.class)
                .build();
        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_cardview, parent, false);
                return new PostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final PostViewHolder holder, int position, @NonNull Post model) {
                final String postUid = model.getUid();
                final String postKey = getRef(position).getKey();
                DatabaseReference dbUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(postKey);
                dbUserRef.keepSynced(true);
                dbUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String userName = dataSnapshot.child("name").getValue(String.class);
                        String userProfileImage = dataSnapshot.child("profile_image").getValue(String.class);
                        holder.setUserImage(getActivity(), userProfileImage);
                        holder.setUserName(userName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
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
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (likeProcess) {
                                    if (dataSnapshot.child(postKey).hasChild(auth.getCurrentUser().getUid())) {
                                        holder.likeBtn.setImageResource(R.drawable.ic_favorite_white_24dp);
                                        likeRef.child(postKey).child(auth.getCurrentUser().getUid()).removeValue();
                                    } else {
                                        holder.likeBtn.setImageResource(R.drawable.like_orange);
                                        likeRef.child(postKey).child(auth.getCurrentUser().getUid()).setValue(sdf.format(new Date(System.currentTimeMillis())));
                                    }
                                    likeProcess = false;
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

                if(model.getTagMap() != null){
                    List<Object>temp = new ArrayList<>(model.getTagMap().values());
                    List<String>tagList = new ArrayList<>(temp.size());
                    for (Object object : temp) {
                        tagList.add(Objects.toString(object, null));
                    }
                    holder.setTags(tagList);
                }
                if(model.getImageMap() != null){
                    List<Object>temp = new ArrayList<>(model.getImageMap().values());
                    List<String>imageList = new ArrayList<>(temp.size());
                    for (Object object : temp) {
                        imageList.add(Objects.toString(object, null));
                    }
                    PostMainSlider postMainSlider = new PostMainSlider(getActivity(), imageList);
                    holder.setPostImages(postMainSlider);
                }
            }
        };
    }

    private static class PostViewHolder extends RecyclerView.ViewHolder{

        private View view;
        private CircleImageView userImgFeeds;
        private TextView userNameFeeds;
        private TextView userDateFeeds;
        public ImageView likeBtn;
        private TextView likeCount;
        private DatabaseReference likeRef;
        private FirebaseAuth auth;

        public PostViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            likeRef = FirebaseDatabase.getInstance().getReference().child("likes");
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
        public void setPostImages(PostMainSlider slider){
            Slider bannerSlider = (Slider)view.findViewById(R.id.banner_slider);
            bannerSlider.setAdapter(slider);
        }
        public void setTags(List<String> tags){
            TagGroup tagGroup = (TagGroup)view.findViewById(R.id.tag_group);
            tagGroup.setTags(tags);
        }
        public void setUserImage(Context context, String url){
            Glide.with(context)
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
                        likeBtn.setImageResource(R.drawable.ic_favorite_white_24dp);
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
