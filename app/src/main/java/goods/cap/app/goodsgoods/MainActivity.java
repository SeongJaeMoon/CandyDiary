package goods.cap.app.goodsgoods;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import goods.cap.app.goodsgoods.Activity.PermissionActivity;
import goods.cap.app.goodsgoods.Activity.PostActivity;
import goods.cap.app.goodsgoods.Activity.SearchActivity;
import goods.cap.app.goodsgoods.Activity.SignInActivity;
import goods.cap.app.goodsgoods.Activity.StatActivity;
import goods.cap.app.goodsgoods.Activity.UserProfileActivity;
import goods.cap.app.goodsgoods.Adapter.RecentAdapter;
import goods.cap.app.goodsgoods.Fragment.ComFragment;
import goods.cap.app.goodsgoods.Fragment.HomeFragment;
import goods.cap.app.goodsgoods.Fragment.NoticeFragment;
import goods.cap.app.goodsgoods.Fragment.StarFragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import android.net.NetworkInfo;
import android.net.ConnectivityManager;

import com.bumptech.glide.Glide;
import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.request.RequestOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.util.List;
import goods.cap.app.goodsgoods.Helper.RecentDBHelper;
import goods.cap.app.goodsgoods.Helper.StarDBHelper;
import goods.cap.app.goodsgoods.Model.Recent;
import goods.cap.app.goodsgoods.Util.BackHandler;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/* main 화면, created by supermoon. */

/*===========================================================================
* -> 구분 필요 : 사용자가 태그 선택(Config.Value ~), 사용자가 검색, 사용자가 Diet or Food 선택
* */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainDrawerLayout)DrawerLayout drawerLayout;
    @BindView(R.id.mainNavigationView)NavigationView navigationView;
    @BindView(R.id.mainRecyclerView) RecyclerView recyclerView;
    @BindView(R.id.list_view_drawer)RecyclerView drawerListView;
    @BindView(R.id.fab_icon) FloatingActionButton fab;
    private CircleImageView profileImage;
    private TextView profileName, profileEmail;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private final String logger = MainActivity.class.getSimpleName();
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MainAdapter adapter;
    private BackHandler backHandler;
    private int isPostion = 0;
    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String PACKAGE_URL_SCHEME = "package:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(!isConnected){
            Log.i(logger, "no-connect");
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        drawerListView.setHasFixedSize(true);
        drawerListView.setLayoutManager(linearLayoutManager);
        //SQLite의 저장된 최근 본 항목 초기화.
        setRecentAdapter();
        deinitDataList();

        fab.setVisibility(View.GONE);

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
                setRecentAdapter();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if(isPostion == 2) fab.setVisibility(View.VISIBLE);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View naviHeader = navigationView.getHeaderView(0);

        profileImage = (CircleImageView) naviHeader.findViewById(R.id.profieImage);
        profileEmail = (TextView)naviHeader.findViewById(R.id.profieEmail);
        profileName = (TextView)naviHeader.findViewById(R.id.profileName);

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
                if(position != 2){
                    fab.setVisibility(View.GONE);
                }else {
                    fab.setVisibility(View.VISIBLE);
                }
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
                        startActivity(new Intent(MainActivity.this, StatActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                }
                return true;
            }
        });
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        };
        dbRef = FirebaseDatabase.getInstance().getReference().child("users");
        dbRef.keepSynced(true);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = auth.getCurrentUser();
                if(user != null){
                    String name = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);
                    String email = dataSnapshot.child(user.getUid()).child("email").getValue(String.class);
                    String pimage = dataSnapshot.child(user.getUid()).child("profile_image").getValue(String.class);

                    profileName.setText(name);
                    profileEmail.setText(email);

                    RequestOptions ro = new RequestOptions()
                            .placeholder(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.empty_user))
                            .error(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.empty_user));

                    Glide.with(getApplicationContext())
                            .setDefaultRequestOptions(ro)
                            .load(pimage)
                            .into(profileImage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.CAMERA)
                                && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage(getResources().getString(R.string.need_permission));
                            builder.setPositiveButton(getResources().getString(R.string.go_setting), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startAppSettings();
                                }
                            });
                            builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.create().show();
                        }else {
                            PermissionGen.with(MainActivity.this)
                                    .addRequestCode(100)
                                    .permissions(
                                            android.Manifest.permission.CAMERA,
                                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    .request();
                        }
                    }else{
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                    }
                }
            }
        });
        setPermissionDialog();
        if(StatActivity.isToast){
            StatActivity.isToast = false;
        }
        getAppKeyHash();
    }
    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void okPermission(){
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.ok_permission), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, PostActivity.class));
    }
    @PermissionFail(requestCode = 100)
    public void failPermission() {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_permission), Toast.LENGTH_SHORT).show();
    }
    //권한 요청
    private void setPermissionDialog(){
        try {
            SharedPreferences mPref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
            Boolean bfirst = mPref.getBoolean("isFirst", false);
            if (!bfirst) {
                SharedPreferences.Editor editor = mPref.edit();
                editor.putBoolean("isFirst", true).apply();
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                    startActivity(new Intent(MainActivity.this, PermissionActivity.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return super.onOptionsItemSelected(item) || actionBarDrawerToggle.onOptionsItemSelected(item);
    }

    private List<Recent> getRecentList(Context context){
        List<Recent> ret = null;
        RecentDBHelper recentDBHelper = new RecentDBHelper(context);
        try {
            recentDBHelper.open();
            ret = recentDBHelper.getList();
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
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
        Intent intent = getIntent();
        if(intent != null) {
            int key = intent.getIntExtra("tagSearch", 1);
            GoodsApplication.getInstance().setKey(key);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        deinitDataList();
        HomeFragment.isSelect = 1;
        HomeFragment.changeView = 0;
    }

    private void deinitDataList(){
        HomeFragment.pageNo = 1;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().remove("dataList").apply();
    }
    @Override
    protected void onStop(){
        super.onStop();
        if(authStateListener != null){
            auth.removeAuthStateListener(authStateListener);
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    private void setRecentAdapter(){
        List<Recent> recentDrawerMenu = getRecentList(MainActivity.this);
        if(recentDrawerMenu == null){
            RecentAdapter recentAdapter = new RecentAdapter(getSupportActionBar().getThemedContext(), null);
            drawerListView.setAdapter(recentAdapter);
        }else {
            RecentAdapter recentAdapter = new RecentAdapter(getSupportActionBar().getThemedContext(), recentDrawerMenu);
            drawerListView.setAdapter(recentAdapter);
            recentAdapter.notifyDataSetChanged();
        }
    }
    private void startAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }
    private void Logout(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getResources().getString(R.string.confirm_logout));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                auth.signOut();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key:", "*****"+something+"*****");
            }
        } catch (Exception e){
            Log.e("name not found", e.toString());
        }
    }

}
