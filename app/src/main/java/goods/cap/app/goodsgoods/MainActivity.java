package goods.cap.app.goodsgoods;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.Activity.SearchActivity;
import goods.cap.app.goodsgoods.Activity.SignInActivity;
import goods.cap.app.goodsgoods.Activity.UserProfileActivity;
import goods.cap.app.goodsgoods.Adapter.RecentAdapter;
import goods.cap.app.goodsgoods.Fragment.ComFragment;
import goods.cap.app.goodsgoods.Fragment.HomeFragment;
import goods.cap.app.goodsgoods.Fragment.NoticeFragment;
import goods.cap.app.goodsgoods.Fragment.StarFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.astuetz.PagerSlidingTabStrip;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;

import goods.cap.app.goodsgoods.Helper.RecentDBHelper;
import goods.cap.app.goodsgoods.Model.Recent;
import goods.cap.app.goodsgoods.Util.BackHandler;

/* main 화면, created by supermoon. */

public class MainActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    private final String logger = MainActivity.class.getSimpleName();
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MainAdapter adapter;
    @BindView(R.id.mainDrawerLayout)DrawerLayout drawerLayout;
    @BindView(R.id.mainNavigationView)NavigationView navigationView;
    @BindView(R.id.mainRecyclerView) RecyclerView recyclerView;
    @BindView(R.id.list_view_drawer)RecyclerView drawerListView;
    @BindView(R.id.fab_icon) FloatingActionsMenu fab;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private LinearLayoutManager layoutManager;
    private BackHandler backHandler;
    private int isPostion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton dietFab = new FloatingActionButton(this);
        FloatingActionButton petFab = new FloatingActionButton(this);
        FloatingActionButton foodFab = new FloatingActionButton(this);

        setFab(dietFab, getResources().getString(R.string.diet_sort), 0);
        setFab(foodFab, getResources().getString(R.string.food_sort), 0);
        setFab(petFab, getResources().getString(R.string.pet_sort), 1);

        fab.addButton(dietFab);
        fab.addButton(foodFab);
        fab.addButton(petFab);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        drawerListView.setHasFixedSize(true);
        drawerListView.setLayoutManager(linearLayoutManager);
        //SQLite의 저장된 최근 본 항목 초기화.
        setRecentAdapter();

        backHandler = new BackHandler(this, getResources().getString(R.string.confirm_back));

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                fab.setAlpha(1 - slideOffset);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                fab.setVisibility(View.GONE);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if(isPostion == 0) fab.setVisibility(View.VISIBLE);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MainAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // 새로운 페이지가 선택되면 호출되는 메서드.
            @Override
            public void onPageSelected(int position) {
                Log.i(logger, "position:" + position);
                isPostion = position;
            }
            // 헌재 페이지에서 스크롤이 시작되면 호출되는 메서드
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position != 0){
                    fab.setAlpha(1 - positionOffset);
                    fab.setVisibility(View.GONE);
                }else {
                    fab.setVisibility(View.VISIBLE);
                }
            }
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_profile: startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
                        break;
                    case R.id.nav_logout: Logout();
                        break;
                    case R.id.nav_contact:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_search){
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        }
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFab(FloatingActionButton button, String title, int role){
        button.setTitle(title);
        button.setSize((FloatingActionButton.SIZE_MINI));
        if(role == 0){
            button.setIcon(R.drawable.ic_image_search);
        }else{
            button.setIcon(R.drawable.ic_pets_white_18dp);
        }
        button.setColorNormal(getResources().getColor(R.color.colorPrimary));
        button.setColorPressed(getResources().getColor(R.color.colorAccent));
    }

    private List<Recent> getRecentList(Context context){
        List<Recent> ret = null;
        RecentDBHelper recentDBHelper = new RecentDBHelper(context);
        try {
            recentDBHelper.open();
            ret = recentDBHelper.getImgList();
        }catch (Exception e){
            Log.w(logger, e.getMessage());
        }finally {
            recentDBHelper.close();
        }
        return ret;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) { super.onRestoreInstanceState(savedInstanceState); }

    //implements PagerSlidingTabStrip.IconTabProvider
    public class MainAdapter extends FragmentStatePagerAdapter {

        private final CharSequence tabs[] = {"식단보기", "즐겨찾기", "커뮤니티", "공지사항"};

        public MainAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return tabs[position];
        }

        @Override
        public Fragment getItem(int position) {
            Log.i(logger, "Fragment position:" + position);
            Fragment f = null;
            switch (position) {
                case 0:
                    f = HomeFragment.newInstance(position);
                    break;
                case 1:
                    f = StarFragment.newInstance(position);
                    break;
                case 2:
                    f = ComFragment.newInstance(position);
                    break;
                case 3:
                    f = NoticeFragment.newInstance(position);
                    break;
                default:
                    break;
            }
            return f;
        }

        @Override
        public int getItemPosition(@NonNull Object object){
            Log.w(logger, "position : " + object);
            return POSITION_NONE;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.mainDrawerLayout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            backHandler.onBackPressed();
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.w(logger, "onResume");
        if(adapter != null){
            Log.w(logger, "notifyDataSetChanged");
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.w(logger, "onStart");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(logger, "onDestroy");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().remove("dataList").apply();
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
        setRecentAdapter();
    }

    private void setRecentAdapter(){
        List<Recent> recentDrawerMenu = recentDrawerMenu = getRecentList(this);
        if(recentDrawerMenu == null){
            RecentAdapter recentAdapter = new RecentAdapter(getSupportActionBar().getThemedContext(), null);
            drawerListView.setAdapter(recentAdapter);
        }else {
            RecentAdapter recentAdapter = new RecentAdapter(getSupportActionBar().getThemedContext(), recentDrawerMenu);
            drawerListView.setAdapter(recentAdapter);
            recentAdapter.notifyDataSetChanged();
        }
    }

    private void Logout(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getResources().getString(R.string.confirm_logout));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
