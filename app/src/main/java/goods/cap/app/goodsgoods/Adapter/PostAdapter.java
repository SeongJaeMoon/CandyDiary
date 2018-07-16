package goods.cap.app.goodsgoods.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import goods.cap.app.goodsgoods.Activity.AnotherUserActivity;
import goods.cap.app.goodsgoods.Activity.PostDtlActivity;
import goods.cap.app.goodsgoods.Activity.TagActivity;
import goods.cap.app.goodsgoods.Activity.UserProfileActivity;
import goods.cap.app.goodsgoods.Model.Firebase.Post;
import goods.cap.app.goodsgoods.R;
import goods.cap.app.goodsgoods.Util.PostMainSlider;
import me.gujun.android.taggroup.TagGroup;
import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideClickListener;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{

    private List<Post>postList;
    private Context context;

    public PostAdapter(Context context){
        this.postList = new ArrayList<Post>();
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_cardview, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.setPost(postList.get(position), context);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void addAll(Post post) {
        postList.add(post);
        notifyDataSetChanged();
    }

    public String getLastItemId() {
        return postList.get(postList.size() - 1).getFkey();
    }

    public void clear(){
        postList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> newPosts) {
        int initialSize = newPosts.size();
        postList.addAll(newPosts);
        notifyItemRangeInserted(initialSize, newPosts.size());
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm aa", Locale.KOREA);
        private View view;
        private CircleImageView userImgFeeds;
        private TextView userNameFeeds;
        private TextView userDateFeeds;
        private TextView titleFeeds;
        private ImageView likeBtn;
        private TextView likeCount;
        private DatabaseReference likeRef;
        private DatabaseReference userRef;
        private FirebaseAuth auth;
        private Slider slider;
        private Context context;
        private boolean likeProcess = false;

        private PostViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            likeRef = FirebaseDatabase.getInstance().getReference().child("likes");
            userRef = FirebaseDatabase.getInstance().getReference().child("users");
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
        private void setPostImages(final Post post) {
            Map<String, Object> imgMap = post.getImages();
            if (imgMap != null) {
                List<String> tempList = new ArrayList<String>(imgMap.size());
                for (Object o : imgMap.values()) {
                    tempList.add(o.toString());
                }
                final PostMainSlider postAdapter = new PostMainSlider(tempList);
                    slider.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            slider.setAdapter(postAdapter);
                            slider.setSelectedSlide(0);
                            slider.setOnSlideClickListener(new OnSlideClickListener() {
                                @Override
                                public void onSlideClick(int position) {
                                    final Intent intent = new Intent(context.getApplicationContext(), PostDtlActivity.class);
                                    if (userNameFeeds.getText() != null && !TextUtils.isEmpty(userNameFeeds.getText().toString())) {
                                        intent.putExtra("userName", userNameFeeds.getText().toString());
                                    }else{
                                        intent.putExtra("userName", "");
                                    }
                                    intent.putExtra("postKey", post.getFkey());
                                    context.startActivity(intent);
                                }
                            });
                        }
                    }, 1000);
            }
        }
        public void setTags(Map<String, Object>tagMap){
            if(tagMap != null) {
                List<String> tempList = new ArrayList<String>(tagMap.size());
                for (Object o : tagMap.values()) {
                    tempList.add(o.toString());
                }
                TagGroup tagGroup = (TagGroup) view.findViewById(R.id.tag_group);
                tagGroup.setTags(tempList);
                tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
                    @Override
                    public void onTagClick(String tag) {
                        //start SearchActivity
                        if (tag != null) {
                            Intent intent = new Intent(context.getApplicationContext(), TagActivity.class);
                            intent.putExtra("tag", tag);
                            context.startActivity(intent);
                        }
                    }
                });
            }
        }
        private void setUser(final String uid){
            userRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        final String userName = dataSnapshot.child("name").getValue(String.class);
                        final String userProfileImage = dataSnapshot.child("profile_image").getValue(String.class);
                        if(userProfileImage != null) {
                            userImgFeeds.post(new Runnable() {
                                @Override
                                public void run() {
                                    RequestOptions ro = new RequestOptions()
                                            .placeholder(ContextCompat.getDrawable(context, R.mipmap.empty_user))
                                            .error(ContextCompat.getDrawable(context, R.mipmap.empty_user));
                                    Glide.with(context).setDefaultRequestOptions(ro).load(userProfileImage).into(userImgFeeds);

                                }
                            });
                            userImgFeeds.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //start AnotherUserActivity
                                    if(!uid.equals(auth.getCurrentUser().getUid())){
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
                        if(userName != null && !TextUtils.isEmpty(userName)){
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
                                    if(!uid.equals(auth.getCurrentUser().getUid())){
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        private void setLike(final String postKey){
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
        private void setLikeBtn(final String postKey){
            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeProcess = true;
                    likeRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (likeProcess) {
                                if (dataSnapshot.child(postKey).hasChild(auth.getCurrentUser().getUid())) {
                                    likeBtn.setImageResource(R.drawable.like_grey);
                                    likeRef.child(postKey).child(auth.getCurrentUser().getUid()).removeValue();
                                } else {
                                    likeBtn.setImageResource(R.drawable.like_orange);
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

        private void setPost(final Post post, Context context){
            setContext(context);
            userDateFeeds.setText(post.getDate());
            titleFeeds.setText(post.getTitle());
            setLike(post.getFkey());
            setLikeBtn(post.getFkey());
            setPostImages(post);
            setTags(post.getTags());
            setUser(post.getUid());
        }
    }

}
