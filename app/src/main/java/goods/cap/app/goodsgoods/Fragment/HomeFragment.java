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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import goods.cap.app.goodsgoods.API.Config;
import goods.cap.app.goodsgoods.API.MainHttp;
import goods.cap.app.goodsgoods.Activity.DetailItemActivity;
import goods.cap.app.goodsgoods.Adapter.GirdViewAdapter;
import goods.cap.app.goodsgoods.Helper.DietHelper;
import goods.cap.app.goodsgoods.Model.Diet.Diet;
import goods.cap.app.goodsgoods.Model.Diet.DietResponseModel;
import goods.cap.app.goodsgoods.Model.Recipe.Recipe;
import goods.cap.app.goodsgoods.Util.MultiSwipeRefreshLayout;
import goods.cap.app.goodsgoods.R;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

/* HomeFragment, created by supermoon. */

public class HomeFragment extends Fragment implements MultiSwipeRefreshLayout.OnRefreshListener
        , AdapterView.OnItemClickListener {

    private static final String logger = HomeFragment.class.getSimpleName();
    public static final String ARG_PAGE = "ARG_PAGE";
    private TextView mainText;
    private GridViewWithHeaderAndFooter gridView;
    private List<Diet> dietList;
    private MultiSwipeRefreshLayout swipeRefreshLayout;
    private GirdViewAdapter girdViewAdapter;
    private TextView footer;
    private static final int limitCount = 103;
    private boolean isLimitCount = false;
    private SharedPreferences sharedPreferences;
    private static int pageNo = 1;
    private boolean lastitemVisibleFlag;

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
                    addData();
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
        initData();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initData() {
        MainHttp mainHttp = new MainHttp(getActivity(), getResources().getString(R.string.data_refresh_title),
                getResources().getString(R.string.data_refresh), Config.API_KEY2);
        mainHttp.setPageNo(1);
        mainHttp.getDiet(new DietHelper() {
            @Override
            public void success(DietResponseModel response) {
                dietList = response.getBody().getItems().getDietList();
                if (dietList == null) {
                    Log.w(logger, "dietList is null");
                } else {
                    girdViewAdapter = new GirdViewAdapter(getActivity(), dietList, R.layout.grid_single);
                    gridView.setAdapter(girdViewAdapter);
                    mainText.setText(getResources().getString(R.string.main_title2));
                }
            }

            @Override
            public void failure(String message) {
                Log.w(logger, message);
                mainText.setText(getResources().getString(R.string.data_error));
            }
        });
    }

    private void addData() {
        final Activity activity = getActivity();
        Log.w(logger, "activity : " + activity);
        MainHttp mainHttp = new MainHttp(activity, getResources().getString(R.string.data_refresh_title),
                getResources().getString(R.string.data_refresh), Config.API_KEY2);
        if (limitCount > this.dietList.size()) {
            Log.w(logger, "dietList size : " + dietList.size());
            mainHttp.setPageNo(pageNo + 1);
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
        /*else{
            Log.w(logger, "recipeList size : " + dietList.size());
            //mainHttp.setStartIndex(recipeList.size());
            //mainHttp.setEndIndex(recipeList.size() + 6);
            mainHttp.getDiet(new DietHelper() {
                @Override
                public void success(DietResponseModel response) {
                    List<Diet> templist = response.getBody().getItems().getDietList();
                    if(templist == null){
                        Log.w(logger, "templist is null");
                    }else {
                        dietList.addAll(templist);
                        girdViewAdapter.notifyDataSetChanged();
                        footer.setText(getResources().getString(R.string.data_limit));
                        isLimitCount = true;
                    }
                }
                @Override
                public void failure(String message) {
                    mainText.setText(getResources().getString(R.string.data_error));
                }
            });
        }*/
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), DetailItemActivity.class);
        Gson gson = new Gson();
        Diet temp = dietList.get(position);
        temp.setFilePath(Config.getAbUrl(dietList.get(position).getRtnImageDc(), dietList.get(0).getRtnStreFileNm()));
        String diet = gson.toJson(temp);
        intent.putExtra("diet", diet);
        getActivity().startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.w(logger, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w(logger, "onResume");
        Gson gson = new Gson();
        String json = sharedPreferences.getString("dataList", null);
        if (json == null) {
            Log.w(logger, "initData" + "," + pageNo);
            initData();
        } else {
            Diet[] diets = gson.fromJson(json, Diet[].class);
            dietList = new ArrayList<Diet>(Arrays.asList(diets));
            girdViewAdapter = new GirdViewAdapter(getActivity(), dietList, R.layout.grid_single);
            gridView.setAdapter(girdViewAdapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w(logger, "onPause");
        if (dietList != null && dietList.size() > 0) {
            Gson gson = new Gson();
            String recipe = gson.toJson(dietList);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("dataList", recipe);
            editor.apply();
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
