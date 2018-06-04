package goods.cap.app.goodsgoods.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import goods.cap.app.goodsgoods.API.Config;
import goods.cap.app.goodsgoods.API.MainHttp;
import goods.cap.app.goodsgoods.Activity.DetailItemActivity;
import goods.cap.app.goodsgoods.Adapter.GirdViewAdapter;
import goods.cap.app.goodsgoods.Adapter.GridHealthAdapter;
import goods.cap.app.goodsgoods.GoodsApplication;
import goods.cap.app.goodsgoods.Helper.DietHelper;
import goods.cap.app.goodsgoods.Helper.HealthHelper;
import goods.cap.app.goodsgoods.Model.Diet.Diet;
import goods.cap.app.goodsgoods.Model.Diet.DietResponseModel;
import goods.cap.app.goodsgoods.Model.Health.Health;
import goods.cap.app.goodsgoods.Model.Health.HealthResponseModel;
import goods.cap.app.goodsgoods.Util.MultiSwipeRefreshLayout;
import goods.cap.app.goodsgoods.R;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

/* HomeFragment, created by supermoon. */

public class HomeFragment extends Fragment implements MultiSwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private static final String logger = HomeFragment.class.getSimpleName();
    public static final String ARG_PAGE = "ARG_PAGE";
    private TextView mainText;
    private GridViewWithHeaderAndFooter gridView;
    private List<Diet> dietList;
    private List<Health> healthList;
    private MultiSwipeRefreshLayout swipeRefreshLayout;
    private GirdViewAdapter girdViewAdapter;
    private GridHealthAdapter gridHealthAdapter;
    private int limitCount = Config.limitCount[1];
    private SharedPreferences sharedPreferences;
    private boolean lastitemVisibleFlag;
    private int isSelect = 1; //현재 선택된 선택 요청 페이지
    public static int pageNo = 1;
    public static int changeView;
    //"수험생을 위한 식단", "美를 위한 다이어트 식단", "가정을 위한 식단", "특별한 날 이벤트 식단", "기분이 좋아지는 식단"
    public static HomeFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        Log.i(logger, "page : " + args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.w(logger, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(logger, "onCreate");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (getArguments() != null) {
            changeView = getArguments().getInt(ARG_PAGE);
            Log.w(logger, "data => " + changeView);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w(logger, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = (MultiSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        gridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.gridMain);
        mainText = (TextView) view.findViewById(R.id.mainTitle);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setSwipeableChildren(R.id.gridMain);
        gridView.setOnItemClickListener(this);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    if(changeView != 11) {
                        if (limitCount > dietList.size()) {
                            addData(isSelect);
                        } else {
                            Log.w(logger, "dietList size : " + dietList.size());
                        }
                    }else{
                        if (limitCount > healthList.size()) {
                            addData2();
                        } else {
                            Log.w(logger, "heaList size : " + healthList.size());
                        }
                    }
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem)
                // + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
                lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);

            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            for (String key : savedInstanceState.keySet()) {
                Log.i(logger, "key : " + key);
            }
        }
        super.onActivityCreated(savedInstanceState);
        // 뷰에 데이터를 넣는 작업 등을 추가
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.w(logger, "onViewCreated");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onRefresh() {
        if(changeView != 11){
            initData(isSelect);
        }else{
            initData2();
        }
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initData(int key) {
        MainHttp mainHttp = new MainHttp(getActivity(), getResources().getString(R.string.data_refresh_title), getResources().getString(R.string.data_refresh), Config.API_KEY2);
        mainHttp.setPageNo(1);
        mainHttp.setFlag(key);
        mainHttp.getDiet(new DietHelper() {
            @Override
            public void success(DietResponseModel response) {
                dietList = response.getBody().getItems().getDietList();
                if (dietList == null) {
                    Log.w(logger, "dietList is null");
                    mainText.setText(getResources().getString(R.string.data_error));
                } else {
                    girdViewAdapter = new GirdViewAdapter(getActivity(), dietList, R.layout.grid_single);
                    gridView.setAdapter(girdViewAdapter);
                }
            }
            @Override
            public void failure(String message) {
                Log.w(logger, message);
                mainText.setText(getResources().getString(R.string.data_error));
            }
        });
    }

    private void initData2(){
        MainHttp mainHttp = new MainHttp(getActivity(), getResources().getString(R.string.data_refresh_title), getResources().getString(R.string.data_refresh), Config.API_KEY3);
        // paging 1~23500
        mainHttp.setPageNo(1);
        mainHttp.getHealth(new HealthHelper() {
            @Override
            public void success(HealthResponseModel response) {
                healthList = response.getBody().getHealthList();
                if(healthList == null){
                    Log.w(logger, "healthList is null");
                    mainText.setText(getResources().getString(R.string.data_error));
                }else{
                    gridHealthAdapter = new GridHealthAdapter(getActivity(), healthList, R.layout.grid_single2);
                    gridView.setAdapter(gridHealthAdapter);
                }
            }

            @Override
            public void failure(String message) {
                mainText.setText(getResources().getString(R.string.data_error));
            }
        });
    }

    private void addData(int key) {
        MainHttp mainHttp = new MainHttp(getActivity(), getResources().getString(R.string.data_refresh_title), getResources().getString(R.string.data_refresh), Config.API_KEY2);
        mainHttp.setPageNo(++pageNo);
        Log.i(logger, "addData :" + pageNo);
        mainHttp.setFlag(key);
        mainHttp.getDiet(new DietHelper() {
            @Override
            public void success(DietResponseModel response) {
                List<Diet> templist = response.getBody().getItems().getDietList();
                if (templist == null) {
                    Log.w(logger, "templist is null");
                } else {
                    dietList.addAll(templist);
                    girdViewAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void failure(String message) {
                mainText.setText(getResources().getString(R.string.data_error));
            }
        });
    }

    private void addData2(){
        MainHttp mainHttp = new MainHttp(getActivity(), getResources().getString(R.string.data_refresh_title), getResources().getString(R.string.data_refresh), Config.API_KEY3);
        // paging 1~23500
        mainHttp.setPageNo(++pageNo);
        Log.i(logger, "addData :" + pageNo);
        mainHttp.getHealth(new HealthHelper() {
            @Override
            public void success(HealthResponseModel response) {
                List<Health> templist = response.getBody().getHealthList();
                if(templist == null){
                    Log.w(logger, "healthList is null");
                    mainText.setText(getResources().getString(R.string.data_error));
                }else{
                    healthList.addAll(templist);
                    gridHealthAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void failure(String message) {
                mainText.setText(getResources().getString(R.string.data_error));
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(changeView != 11) {
            Intent intent = new Intent(getActivity(), DetailItemActivity.class);
            Gson gson = new Gson();
            Diet temp = dietList.get(position);
            temp.setFilePath(Config.getAbUrl(dietList.get(position).getRtnImageDc(), dietList.get(position).getRtnStreFileNm()));
            String diet = gson.toJson(temp);
            intent.putExtra("diet", diet);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.w(logger, "onStart");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.w(logger, "onResume");
        if (changeView != 11){
            GoodsApplication goodsApplication = (GoodsApplication)getActivity().getApplicationContext();
            int key = 1;
            if(goodsApplication != null) {
                Log.i(logger, "key => " + goodsApplication.getKey());
                key = goodsApplication.getKey();
            }
            if (key < 5) {
                isSelect = Config.dietCode[key]; //dietCode
                limitCount = Config.limitCount[key];//dietLimit
                mainText.setText(Config.tabList[key]);//dietContent
                Log.i(logger, "code =>" + isSelect);
            }
            //데이터를 가지고 있는지 확인.
            Gson gson = new Gson();
            String json = sharedPreferences.getString("dataList", null);
            //데이터 초기화 or 기존 데이터 불러오기
            if (json == null) {
                initData(isSelect);
            } else {
                Diet[] diets = gson.fromJson(json, Diet[].class);
                dietList = new ArrayList<Diet>(Arrays.asList(diets));
                girdViewAdapter = new GirdViewAdapter(getActivity(), dietList, R.layout.grid_single);
                gridView.setAdapter(girdViewAdapter);
            }
        }else{
            Gson gson = new Gson();
            String json = sharedPreferences.getString("dataList", null);
            limitCount = 23500;
            //데이터 초기화 or 기존 데이터 불러오기
            if (json == null) {
                initData2();
            } else {
                Health[] health = gson.fromJson(json, Health[].class);
                healthList = new ArrayList<Health>(Arrays.asList(health));
                gridHealthAdapter = new GridHealthAdapter(getActivity(), healthList, R.layout.grid_single);
                gridView.setAdapter(girdViewAdapter);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w(logger, "onPause");
        if(changeView!=11) {
            if (dietList != null && dietList.size() > 0) {
                Gson gson = new Gson();
                String recipe = gson.toJson(dietList);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("dataList", recipe);
                editor.apply();
            }
        }else{
            if(healthList != null && healthList.size() > 0){
                Gson gson = new Gson();
                String health = gson.toJson(healthList);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("dataList", health);
                editor.apply();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w(logger, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.w(logger, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(logger, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.w(logger, "onDetach");
    }

    private static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
