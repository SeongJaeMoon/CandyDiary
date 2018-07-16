package goods.cap.app.goodsgoods.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import goods.cap.app.goodsgoods.Activity.AnotherUserActivity;
import goods.cap.app.goodsgoods.Activity.UserProfileActivity;
import goods.cap.app.goodsgoods.Adapter.PostAdapter;
import goods.cap.app.goodsgoods.Model.Firebase.Post;
import goods.cap.app.goodsgoods.R;
import goods.cap.app.goodsgoods.Util.MultiSwipeRefreshLayout;
import goods.cap.app.goodsgoods.Util.PostImageLoader;
import ss.com.bannerslider.Slider;


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
    private boolean isLoading = false;
    private String lastKey = "";
    private ProgressDialog progressDialog;
    private PostAdapter postAdapter;

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
        TextView comTitle = (TextView)view.findViewById(R.id.mainTitle);
        comRView = (RecyclerView)view.findViewById(R.id.comRView);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        comRView.setLayoutManager(layoutManager);
        comRView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isLoading && !comRView.canScrollVertically(1) && !lastKey.equals(postAdapter.getLastItemId())) {
                    Log.e(logger, "last-key: " + lastKey +", load-key: " + postAdapter.getLastItemId());
                    loadPosts(postAdapter.getLastItemId());
                    isLoading = true;
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
        Slider.init(new PostImageLoader(getActivity()));
        loadPosts(null);
    }
    private void initFirebase() {
        auth = FirebaseAuth.getInstance();
        postRef = FirebaseDatabase.getInstance().getReference().child("posts");
        postRef.keepSynced(true);
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        userRef.keepSynced(true);
        likeRef = FirebaseDatabase.getInstance().getReference().child("likes");
        likeRef.keepSynced(true);
        postAdapter = new PostAdapter(getActivity());
        comRView.setAdapter(postAdapter);
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
                        HashSet<String>sets = new HashSet<String>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            String uid = data.child("uid").getValue(String.class);
                            if (uid != null && !uid.isEmpty()) {
                                    sets.add(uid);
                            }
                        }
                        if(sets.size() > 0){
                            for(String s : sets) {
                                Collections.addAll(tempList, s);
                            }
                        }
                        if (tempList.size() != 0) {
                            userRef.child(tempList.get(0)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final String img = dataSnapshot.child("profile_image").getValue(String.class);
                                    final String name = dataSnapshot.child("name").getValue(String.class);
                                    comTxt1.setText(name);
                                    comImg1.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (isAdded())
                                                Glide.with(getActivity()).setDefaultRequestOptions(ro).load(img).into(comImg1);
                                        }
                                    });
                                    comImg1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (tempList.get(0).equals(auth.getCurrentUser().getUid())) {
                                                startActivity(new Intent(getActivity(), UserProfileActivity.class));
                                            } else {
                                                Intent intent = new Intent(getActivity(), AnotherUserActivity.class);
                                                intent.putExtra("uid", tempList.get(0));
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
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
                                            if (isAdded())
                                                Glide.with(getActivity()).setDefaultRequestOptions(ro).load(img).into(comImg2);
                                        }
                                    });
                                    comImg2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (tempList.get(1).equals(auth.getCurrentUser().getUid())) {
                                                startActivity(new Intent(getActivity(), UserProfileActivity.class));
                                            } else {
                                                Intent intent = new Intent(getActivity(), AnotherUserActivity.class);
                                                intent.putExtra("uid", tempList.get(1));
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
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
                                            if (isAdded())
                                                Glide.with(getActivity()).setDefaultRequestOptions(ro).load(img).into(comImg3);
                                        }
                                    });
                                    comImg3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (tempList.get(2).equals(auth.getCurrentUser().getUid())) {
                                                startActivity(new Intent(getActivity(), UserProfileActivity.class));
                                            } else {
                                                Intent intent = new Intent(getActivity(), AnotherUserActivity.class);
                                                intent.putExtra("uid", tempList.get(2));
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                            if(tempList.size() > 3) {
                                userRef.child(tempList.get(3)).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final String img = dataSnapshot.child("profile_image").getValue(String.class);
                                        final String name = dataSnapshot.child("name").getValue(String.class);
                                        comTxt4.setText(name);
                                        comImg4.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (isAdded())
                                                    Glide.with(getActivity()).setDefaultRequestOptions(ro).load(img).into(comImg4);
                                            }
                                        });
                                        comImg4.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (tempList.get(3).equals(auth.getCurrentUser().getUid())) {
                                                    startActivity(new Intent(getActivity(), UserProfileActivity.class));
                                                } else {
                                                    Intent intent = new Intent(getActivity(), AnotherUserActivity.class);
                                                    intent.putExtra("uid", tempList.get(3));
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(logger, "error " + databaseError);
            }
        });
        postRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        lastKey = data.getKey();
                        Log.i(logger, "last-key:" + lastKey);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    private void loadPosts(final String key){
        try {
            showProgressDialog();
            Query query;
            if(key == null)
                query = postRef.orderByKey().limitToFirst(3);
            else
                query = postRef.orderByKey().startAt(key).limitToFirst(5);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        List<Post> postList = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                final Post model = data.getValue(Post.class);
                                if (model != null) {
                                    model.setFkey(data.getKey());
                                    Log.e(logger, "key: " + model.getFkey());
                                    postList.add(model);
                                }
                            }
                            if(key != null) postList.remove(0);
                            postAdapter.addAll(postList);
                            isLoading = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    hideProgressDialog();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    isLoading = false;
                    hideProgressDialog();
                }
            });
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
    @Override
    public void onStop(){
        super.onStop();
        if(postAdapter != null){
            postAdapter.clear();
        }
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
