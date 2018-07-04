package goods.cap.app.goodsgoods.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.API.Config;
import goods.cap.app.goodsgoods.Adapter.SearchAdapter;
import goods.cap.app.goodsgoods.Fragment.ComFragment;
import goods.cap.app.goodsgoods.Fragment.HomeFragment;
import goods.cap.app.goodsgoods.MainActivity;
import goods.cap.app.goodsgoods.Model.Firebase.Post;
import goods.cap.app.goodsgoods.R;
import goods.cap.app.goodsgoods.Util.PostMainSlider;
import goods.cap.app.goodsgoods.Util.Search;
import me.gujun.android.taggroup.TagGroup;

/* keyword search 화면, created by supermoon. */

/*===========================================================================
 * -> 키워드 검색, 해시 태그 검색 or 사용자 검색 추천 -> Firebase DB 데이터 비교 및 검색 데이터 저장.
 * -> 좋아요(인기순), 함께 많이 본 데이터 보여주기(Firebase DB 연동), 추천(추천 알고리즘 적용 => 식단 정보 바탕)
 * */
public class SearchActivity extends AppCompatActivity implements TagGroup.OnTagClickListener {

    @BindView(R.id.tag_group)TagGroup tagGroup;
    @BindView(R.id.rvSearch)RecyclerView rvSearch; //검색 RecyclerView
    @BindView(R.id.rvRecommend)RecyclerView rvRecommend; //추천 RecyclerView
    @BindView(R.id.search_view)MaterialSearchView searchView;
    @BindView(R.id.toolbar)Toolbar toolbar;
    private final String logger = SearchActivity.class.getSimpleName();
    private DatabaseReference postRef;
    private DatabaseReference therapyRef;
    private DatabaseReference dietRef;
    private DatabaseReference userRef;
    private String[] tagList;
    private FirebaseAuth auth;
    //검색 키워드
    private SearchAdapter searchAdapter;
    private List<String> searchList = new ArrayList<String>();
    private ProgressDialog progressDialog;
    private boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        postRef = FirebaseDatabase.getInstance().getReference().child("posts");
        postRef.keepSynced(true);
        therapyRef = FirebaseDatabase.getInstance().getReference().child("therapies");
        therapyRef.keepSynced(true);
        dietRef = FirebaseDatabase.getInstance().getReference().child("diets");
        dietRef.keepSynced(true);
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        userRef.keepSynced(true);

        rvRecommend.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        rvSearch.setLayoutManager(layoutManager);

        searchAdapter = new SearchAdapter(getApplicationContext());
        rvSearch.setAdapter(searchAdapter);

        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(TextUtils.isEmpty(query) || query.length() < 3) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.search_text), Toast.LENGTH_SHORT).show();
                }else{
                    searching(query);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //rv
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchAdapter.clear();
            }
            @Override
            public void onSearchViewClosed() {

            }
        });

        //0, 1, 2, 3, 4
        tagList = Config.tabList;
        tagGroup.setTags(tagList);
        tagGroup.setOnTagClickListener(this);
        searchView.setEllipsize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SearchActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTagClick(String tag) {
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        int len = tagList.length;
        for(int i = 0; i < len; ++i){
            if(tag.equals(tagList[i])) intent.putExtra("tagSearch", i);
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().remove("dataList").apply();
        HomeFragment.pageNo = 1;
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    // 검색 추천
    static class SearchListHolder extends RecyclerView.ViewHolder{

        public SearchListHolder(View itemView) {
            super(itemView);
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(SearchActivity.this);
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
    //태그, 약초 효능, 식단
    private void searching(final String text){
        showProgressDialog();
        if(text.charAt(0) == '#') {
            postRef.orderByChild("date").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.exists()) {
                            final List<String> keyList = new ArrayList<String>();
                            for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                Map<String, Object> tagMap = (HashMap<String, Object>) datas.child("tags").getValue();
                                if (tagMap != null) {
                                    for (Object o : tagMap.values()) {
                                        String val = (String) o;
                                        if (val.equals(text)) {
                                            if (!keyList.contains(datas.getKey())) {
                                                Log.w(logger, "equal val => " + datas.getKey());
                                                keyList.add(datas.getKey());
                                            }
                                        }
                                    }
                                }
                            }
                            for (String key : keyList) {
                                if (key != null) {
                                    final Search search = new Search();
                                    search.setFkey(key);
                                    postRef.child(key).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                String uid = dataSnapshot.child("uid").getValue(String.class);
                                                userRef.child(uid).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String userName = dataSnapshot.child("name").getValue(String.class);
                                                        search.setUserName(userName);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    }
                                                });
                                                String title = dataSnapshot.child("title").getValue(String.class);
                                                Map<String, Object> tagMap = (HashMap<String, Object>) dataSnapshot.child("tags").getValue();
                                                List<String> tagList;
                                                if (tagMap != null) {
                                                    tagList = new ArrayList<String>(tagMap.size());
                                                    for (Object o : tagMap.values()) {
                                                        tagList.add(o.toString());
                                                    }
                                                    search.setTags(tagList);
                                                }
                                                Map<String, Object> imgMap = (HashMap<String, Object>) dataSnapshot.child("images").getValue();
                                                List<String> imgList;
                                                if (imgMap != null) {
                                                    imgList = new ArrayList<String>(imgMap.size());
                                                    for (Object o : imgMap.values()) {
                                                        imgList.add(o.toString());
                                                    }
                                                    search.setImage(imgList.get(0));
                                                }
                                                search.setFlag(0);
                                                search.setMainText(title);
                                                searchAdapter.addAll(search);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                }
                            }
                        }
                        hideProgressDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    hideProgressDialog();
                }
            });
            noMatchQuery();
        } else if(text.charAt(0) == '!') {
            showProgressDialog();
            dietRef.orderByChild("cnt_sj").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot datas : dataSnapshot.getChildren()) {
                            String cnt_sj = datas.child("cnt_sj").getValue(String.class);
                            if (cnt_sj != null) {
                                if (cnt_sj.contains(text)) {
                                    String cnt_no = datas.child("cnt_no").getValue(String.class);
                                    Log.w(logger, "dietKey: " + datas.getKey() + " cnt_sj: " + cnt_sj + " cnt_no: " + cnt_no);
                                }
                            }
                        }
                    }
                    hideProgressDialog();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    hideProgressDialog();
                }
            });
            noMatchQuery();
        } else if(text.charAt(0) == '@'){
            therapyRef.orderByChild("prvate").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for (DataSnapshot datas : dataSnapshot.getChildren()) {
                            String prvate = datas.child("prvate").getValue(String.class);
                            if (prvate != null) {
                                if (prvate.contains(text)) {
                                    String cnt_no = datas.child("cnt_no").getValue(String.class);
                                    Log.w(logger, "therapyKey: " + datas.getKey() + " cnt_sj: " + prvate + " cnt_no: " + cnt_no);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    hideProgressDialog();
                }
            });
            noMatchQuery();
        }else{
            hideProgressDialog();
        }
    }

    private void noMatchQuery(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (searchAdapter.getItemCount() == 0) {
                    Search noMatch = new Search();
                    noMatch.setImage(null);
                    searchAdapter.addAll(noMatch);
                }
            }
        }, 3000);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume(){
        super.onResume();
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
}
