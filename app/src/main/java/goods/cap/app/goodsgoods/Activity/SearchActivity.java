package goods.cap.app.goodsgoods.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.R;
import me.gujun.android.taggroup.TagGroup;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, TagGroup.OnTagClickListener {

    @BindView(R.id.tag_group)TagGroup tagGroup;
    private final String logger = SearchActivity.class.getSimpleName();
    @BindView(R.id.rvSearch)RecyclerView rvSearch;
    private DatabaseReference mdbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        rvSearch.setLayoutManager(new LinearLayoutManager(this));

        String[] tagList = {"수험생을 위한 식단", "美를 위한 다이어트 식단", "가정을 위한 식단", "특별한 날 이벤트 식단", "기분이 좋아지는 식단"};
        tagGroup.setTags(tagList);
        tagGroup.setOnTagClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchMenu = menu.findItem(R.id.action_search_activity);
        SearchView searchView = (SearchView)searchMenu.getActionView();
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onTagClick(String tag) {

    }

    static class SearchViewHolder extends RecyclerView.ViewHolder{

        public SearchViewHolder(View itemView) {
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
