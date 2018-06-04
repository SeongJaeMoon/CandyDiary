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
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.API.Config;
import goods.cap.app.goodsgoods.Fragment.HomeFragment;
import goods.cap.app.goodsgoods.MainActivity;
import goods.cap.app.goodsgoods.R;
import me.gujun.android.taggroup.TagGroup;

public class SearchActivity extends AppCompatActivity implements TagGroup.OnTagClickListener {

    @BindView(R.id.tag_group)TagGroup tagGroup;
    @BindView(R.id.rvSearch)RecyclerView rvSearch;
    @BindView(R.id.search_view)MaterialSearchView searchView;
    @BindView(R.id.toolbar)Toolbar toolbar;
    private final String logger = SearchActivity.class.getSimpleName();
    private DatabaseReference mdbRef;
    private String[] tagList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
            this.finish();
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

    static class SearchViewHolder extends RecyclerView.ViewHolder{

        public SearchViewHolder(View itemView) {
            super(itemView);
        }

    }

    static class SearchListHolder extends RecyclerView.ViewHolder{

        public SearchListHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(logger, "onStart");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.w(logger, "onResume");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(logger, "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.w(logger, "onStop");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.w(logger, "onPause");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.w(logger, "onRestart");
    }
}
