package goods.cap.app.goodsgoods.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.API.Config;
import goods.cap.app.goodsgoods.Fragment.HomeFragment;
import goods.cap.app.goodsgoods.MainActivity;
import goods.cap.app.goodsgoods.R;
import me.gujun.android.taggroup.TagGroup;

/* keyword search 화면, created by supermoon. */

/*===========================================================================
 * -> 키워드 검색, 해시 태그 검색 or 사용자 검색 추천 -> Firebase DB 데이터 비교 및 검색 데이터 저장.
 * -> 좋아요(인기순), 함께 많이 본 데이터 보여주기(Firebase DB 연동), 추천(추천 알고리즘 적용 => 식단 정보 바탕)
 * */
public class SearchActivity extends AppCompatActivity implements TagGroup.OnTagClickListener {

    @BindView(R.id.tag_group)TagGroup tagGroup;
    @BindView(R.id.rvSearch)RecyclerView rvSearch;
    @BindView(R.id.rvRecommend)RecyclerView rvRecommend;
    @BindView(R.id.search_view)MaterialSearchView searchView;
    @BindView(R.id.toolbar)Toolbar toolbar;
    private final String logger = SearchActivity.class.getSimpleName();
    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private String[] tagList;
    //검색 키워드
    private String searchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //ComFragment 포스팅 tag 클릭시 넘어 오는 intent 값
        Intent intent = getIntent();
        String tag = intent.getStringExtra("tag");
        if(tag != null){
            searchKey = tag;
        }
        ButterKnife.bind(this);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }
            @Override
            public void onSearchViewClosed() {

            }
        });
        rvRecommend.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
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
    // 검색 목록
    static class SearchViewHolder extends RecyclerView.ViewHolder{

        public SearchViewHolder(View itemView) {
            super(itemView);
        }
    }
    // 검색 추천
    static class SearchListHolder extends RecyclerView.ViewHolder{

        public SearchListHolder(View itemView) {
            super(itemView);
        }
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
