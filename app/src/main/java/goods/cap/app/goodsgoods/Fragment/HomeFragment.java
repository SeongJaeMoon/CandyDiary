package goods.cap.app.goodsgoods.Fragment;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import goods.cap.app.goodsgoods.API.Config;
import goods.cap.app.goodsgoods.API.MainHttp;
import goods.cap.app.goodsgoods.Activity.DetailItemActivity;
import goods.cap.app.goodsgoods.Activity.DetailTherapyActivity;
import goods.cap.app.goodsgoods.Adapter.GridViewAdapter;
import goods.cap.app.goodsgoods.GoodsApplication;
import goods.cap.app.goodsgoods.Helper.DietHelper;
import goods.cap.app.goodsgoods.Helper.TherapyHelper;
import goods.cap.app.goodsgoods.Model.Diet.Diet;
import goods.cap.app.goodsgoods.Model.Diet.DietResponseModel;
import goods.cap.app.goodsgoods.Model.Therapy.Therapy;
import goods.cap.app.goodsgoods.Model.Therapy.TherapyResponseModel;
import goods.cap.app.goodsgoods.Util.MultiSwipeRefreshLayout;
import goods.cap.app.goodsgoods.R;

/* HomeFragment, created by supermoon. */

public class HomeFragment extends Fragment implements MultiSwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private static final String logger = HomeFragment.class.getSimpleName();
    public static final String ARG_PAGE = "ARG_PAGE";
    private TextView mainText;
    private ListView gridView;
    private List<Object> allList = new ArrayList<Object>();
    private MultiSwipeRefreshLayout swipeRefreshLayout;
    private GridViewAdapter gridViewAdapter;
    private SharedPreferences sharedPreferences;
    private boolean lastitemVisibleFlag;
    public static int limitCount = Config.limitCount[1]; //limitCount
    public static int isSelect = Config.dietCode[1]; //현재 선택된 선택 요청 페이지
    public static int pageNo = 1; //API 페이징 처리 변수
    public static int changeView; //View 변경 처리 변수
    public static int key = 1;
    private static String mainTitle;

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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = (MultiSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        gridView = (ListView) view.findViewById(R.id.gridMain);
        mainText = (TextView) view.findViewById(R.id.mainTitle);
        Button sortDiet = (Button) view.findViewById(R.id.sort_diet);
        Button sortTherapy = (Button) view.findViewById(R.id.sort_therapy);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setSwipeableChildren(R.id.gridMain);
        gridView.setOnItemClickListener(this);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    try {
                        if(changeView != 11) {
                            if(limitCount > allList.size()) {
                                addData(isSelect);
                            }
                        }else{
                            if(limitCount > allList.size()) {
                                addData();
                            }
                        }
                    }catch (Exception e){
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        });
        sortDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HomeFragment.changeView != 11){
                    Toast.makeText(getActivity(), getResources().getString(R.string.already_show1),Toast.LENGTH_SHORT).show();
                }else {
                    changeView = 10;
                    limitCount = Config.limitCount[key];
                    initData(isSelect);
                }
            }
        });
        sortTherapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HomeFragment.changeView == 11){
                    Toast.makeText(getActivity(), getResources().getString(R.string.already_show2), Toast.LENGTH_SHORT).show();
                }else {
                    changeView = 11;
                    limitCount = 173;
                    initData();
                }
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
            initData();
        }
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initData(int key) {
        final MainHttp mainHttp = new MainHttp(getActivity(), getResources().getString(R.string.data_refresh_title), getResources().getString(R.string.data_refresh), Config.API_KEY2);
        mainHttp.setPageNo(1);
        mainHttp.setFlag(key);
        mainHttp.getDiet(new DietHelper() {
            @Override
            public void success(DietResponseModel response) {
                final List<Diet> diets = response.getBody().getItems().getDietList();
                //데이터가 null => Network Error
                if (diets == null) {
                    mainText.setText(getResources().getString(R.string.data_error));
                } else {
                    if(gridView.getAdapter() != null){
                        gridViewAdapter.refreshDiet(diets);
                    } else{
                        allList.addAll(diets);
                        gridViewAdapter = new GridViewAdapter(getActivity(), allList, R.layout.grid_single);
                        gridView.setAdapter(gridViewAdapter);
                    }
                    mainText.setText(mainTitle);
                }
            }
            @Override
            public void failure(String message) {
                Log.w(logger, message);
                mainText.setText(getResources().getString(R.string.data_error));
            }
        });
    }

    private void initData(){
        MainHttp mainHttp = new MainHttp(getActivity(), getResources().getString(R.string.data_refresh_title), getResources().getString(R.string.data_refresh), Config.API_KEY2);
        //paging 1~23500
        mainHttp.setPageNo(1);
        mainHttp.getTherapy(new TherapyHelper() {
            @Override
            public void success(TherapyResponseModel response) {
                final List<Therapy> therapies = response.getBody().getItems().getTherapyList();
                // 데이터가 null => Network Error
                if(therapies  == null){
                    mainText.setText(getResources().getString(R.string.data_error));
                }else {
                    gridViewAdapter.refreshTherapy(therapies);
                }
                mainText.setText(getResources().getString(R.string.main_therapy));
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
        mainHttp.setFlag(key);
        mainHttp.getDiet(new DietHelper() {
            @Override
            public void success(DietResponseModel response) {
                List<Diet> templist = response.getBody().getItems().getDietList();
                if (templist == null) {
                    mainText.setText(getResources().getString(R.string.data_error));
                } else {
                    allList.addAll(templist);
                    gridViewAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void failure(String message) {
                mainText.setText(getResources().getString(R.string.data_error));
            }
        });
    }

    private void addData(){
        MainHttp mainHttp = new MainHttp(getActivity(), getResources().getString(R.string.data_refresh_title), getResources().getString(R.string.data_refresh), Config.API_KEY2);
        //paging 1~23500
        mainHttp.setPageNo(++pageNo);
        mainHttp.getTherapy(new TherapyHelper() {
            @Override
            public void success(TherapyResponseModel response) {
                Log.i(logger, response.toString());
                List<Therapy>templist = response.getBody().getItems().getTherapyList();
                if(templist == null){
                    mainText.setText(getResources().getString(R.string.data_error));
                }else{
                    allList.addAll(templist);
                    gridViewAdapter.notifyDataSetChanged();
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
        if(allList.get(position) instanceof Diet){
            Intent intent = new Intent(getActivity(), DetailItemActivity.class);
            Gson gson = new Gson();
            Diet temp = (Diet)allList.get(position);
            temp.setFilePath(Config.getAbUrl(temp.getRtnImageDc(), temp.getRtnStreFileNm()));
            String diet = gson.toJson(temp);
            intent.putExtra("diet", diet);
            getActivity().startActivity(intent);
        }else if(allList.get(position) instanceof  Therapy){
            Intent intent = new Intent(getActivity(), DetailTherapyActivity.class);
            Gson gson = new Gson();
            Therapy temp = (Therapy)allList.get(position);
            String therapy = gson.toJson(temp);
            intent.putExtra("therapy", therapy);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.w(logger, "onStart");
        setChangeView();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void setChangeView(){
        //기존의 데이터가 있는지 확인
        Gson gson = new Gson();
        String json = sharedPreferences.getString("dataList", null);
        //Search 값 확인
        GoodsApplication goodsApplication = GoodsApplication.getInstance();
        //기본 선택 키
        //기존의 데이터가 없다면 (최초 실행 or 검색 or 식단 <-> 약초)
        if(json == null){
            //null 체크
            if (goodsApplication != null) {
                Log.i(logger, "change diet key => " + goodsApplication.getKey());
                key = goodsApplication.getKey();
            }
            //key 검사
            if (key < 5) {
                isSelect = Config.dietCode[key]; //dietCode
                limitCount = Config.limitCount[key];//dietLimit
                mainTitle = Config.tabList[key]; //dietTitle
            }
            //선택 값 특정
            initData(isSelect);
        }else{
            if (changeView != 11){
                Diet[] diets = gson.fromJson(json, Diet[].class);
                allList = new ArrayList<Object>(Arrays.asList(diets));
                gridViewAdapter = new GridViewAdapter(getActivity(), allList, R.layout.grid_single);
                gridView.setAdapter(gridViewAdapter);
                mainText.setText(Config.tabList[key]); //dietTitle
            }else{
                Therapy[] therapies = gson.fromJson(json, Therapy[].class);
                allList = new ArrayList<Object>(Arrays.asList(therapies));
                gridViewAdapter = new GridViewAdapter(getActivity(), allList, R.layout.grid_single);
                gridView.setAdapter(gridViewAdapter);
                mainText.setText(getResources().getString(R.string.main_therapy));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (allList != null && allList.size() > 0) {
            Gson gson = new Gson();
            String list = gson.toJson(allList);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("dataList", list);
            editor.apply();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
