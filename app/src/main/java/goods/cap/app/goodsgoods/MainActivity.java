package goods.cap.app.goodsgoods;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.Activity.SearchActivity;
import goods.cap.app.goodsgoods.Activity.SignInActivity;
import goods.cap.app.goodsgoods.Activity.UserProfileActivity;
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

import com.astuetz.PagerSlidingTabStrip;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import goods.cap.app.goodsgoods.Util.BackHandler;

/* main 화면, created by supermoon. */

public class MainActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    private final String logger = MainActivity.class.getSimpleName();
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MainAdapter adapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private FloatingActionsMenu fab;
//    private FloatingActionButton groceryFab;
//    private FloatingActionButton recipeFab;

    private BackHandler backHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionsMenu) findViewById(R.id.fab_icon);

        FloatingActionButton recipeFab = new FloatingActionButton(this);
        FloatingActionButton groceryFab = new FloatingActionButton(this);

        recipeFab.setTitle(getResources().getString(R.string.recipe_sort));
        recipeFab.setSize((FloatingActionButton.SIZE_MINI));
        recipeFab.setIcon(R.drawable.ic_image_search);
        recipeFab.setColorNormal(getResources().getColor(R.color.colorPrimary));
        recipeFab.setColorPressed(getResources().getColor(R.color.colorAccent));
        groceryFab.setTitle(getResources().getString(R.string.grocery_sort));
        groceryFab.setIcon(R.drawable.ic_image_search);
        groceryFab.setSize((FloatingActionButton.SIZE_MINI));
        groceryFab.setColorNormal(getResources().getColor(R.color.colorPrimary));
        groceryFab.setColorPressed(getResources().getColor(R.color.colorAccent));

        fab.addButton(recipeFab);
        fab.addButton(groceryFab);


        drawerLayout = (DrawerLayout) findViewById(R.id.mainDrawerLayout);
        navigationView = (NavigationView) findViewById(R.id.mainNavigationView);

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
                fab.setVisibility(View.VISIBLE);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MainAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        // pager.setCurrentItem(0);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // 새로운 페이지가 선택되면 호출되는 메서드.
            @Override
            public void onPageSelected(int position) {
                Log.i(logger, "position:" + position);

            }

            // 헌재 페이지에서 스크롤이 시작되면 호출되는 메서드
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    class MainAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        private int tabIcons[] = {R.drawable.ic_restaurant_18dp, R.drawable.ic_star_black_18dp,
                R.drawable.ic_people_outline_black_18dp, R.drawable.ic_menu_black_18dp};

        public MainAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabIcons.length;
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
        public int getPageIconResId(int position) {
            return tabIcons[position];
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
