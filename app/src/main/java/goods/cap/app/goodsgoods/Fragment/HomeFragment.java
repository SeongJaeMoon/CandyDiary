package goods.cap.app.goodsgoods.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import java.util.List;
import goods.cap.app.goodsgoods.API.MainHttp;
import goods.cap.app.goodsgoods.Adapter.GirdViewAdapter;
import goods.cap.app.goodsgoods.Helper.GroceryHelper;
import goods.cap.app.goodsgoods.Helper.RecipeHelper;
import goods.cap.app.goodsgoods.MainActivity;
import goods.cap.app.goodsgoods.Model.Grocery;
import goods.cap.app.goodsgoods.Model.GroceryResponseModel;
import goods.cap.app.goodsgoods.Model.Recipe;
import goods.cap.app.goodsgoods.Model.RecipeResponseModel;
import goods.cap.app.goodsgoods.Util.MultiSwipeRefreshLayout;
import goods.cap.app.goodsgoods.R;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class HomeFragment extends Fragment implements MultiSwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener{

    private static final String logger = HomeFragment.class.getSimpleName();
    public static final String ARG_PAGE = "ARG_PAGE";
    private TextView mainText;
    private GridViewWithHeaderAndFooter gridView;
    private List<Recipe> recipeList;
    private List<Grocery> groceryList;
    private MultiSwipeRefreshLayout swipeRefreshLayout;
    private boolean isLoaded;
    private static final String API_KEY = "d43e3638a6db0b8a56a6fce44d37a02949b592cfbcf66f0eb5ef58aba9fb980f";
    private GirdViewAdapter girdViewAdapter;
    private TextView footer;
    private final static int limitCount = 537;
    private boolean isLimitCount = false;

    public static HomeFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        Log.i(logger, "page : "+ args);
        return fragment;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.w(logger, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(logger, "onCreate");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.w(logger, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = (MultiSwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        gridView = (GridViewWithHeaderAndFooter)view.findViewById(R.id.gridMain);
        mainText = (TextView)view.findViewById(R.id.mainTitle);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setSwipeableChildren(R.id.gridMain);
        gridView.setOnItemClickListener(this);

        //layoutInflater = LayoutInflater.from(getActivity().inflate(R.layout_data_footer, null));
        View temp = inflater.inflate(R.layout.data_footer, null);
        temp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(logger, "more data");
                        if(!isLimitCount) addData();
                        break;
                }
                return false;
            }
        });
        footer = (TextView)view.findViewById(R.id.moreData);
        gridView.addFooterView(temp);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            for(String key : savedInstanceState.keySet()) {
                Log.i(logger, "key : " + key);
            }
        }
        super.onActivityCreated(savedInstanceState);
        // 뷰에 데이터를 넣는 작업 등을 추가
        initData();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.w(logger, "onViewCreated");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isLoaded = isVisibleToUser;
        if(isResumed()) {
            if (isLoaded) {
                Log.w(logger, "isVisible");
                initData();
            } else {
                Log.w(logger, "isNotVisible");
            }
        }
    }

    @Override
    public void onRefresh() {
        initData();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initData(){
        final Activity activity = getActivity();
        Log.w(logger, "activity : " + activity);
        MainHttp mainHttp = new MainHttp(activity, getResources().getString(R.string.data_refresh_title),
                getResources().getString(R.string.data_refresh), API_KEY);
        mainHttp.setStartIndex(1);
        mainHttp.setEndIndex(10);
        mainHttp.getRecipe(new RecipeHelper() {
            @Override
            public void success(RecipeResponseModel response) {
                recipeList = response.getQuery().getRow();
                if(recipeList == null){
                    Log.w(logger, "recipeList is null");
                }else {
                    girdViewAdapter = new GirdViewAdapter(activity, recipeList, R.layout.grid_single);
                    gridView.setAdapter(girdViewAdapter);
                    mainText.setText(getResources().getString(R.string.main_title));
                }
            }
            @Override
            public void failure(String message) {
                mainText.setText(getResources().getString(R.string.data_error));
            }
        });
    }

    private void addData(){
        final Activity activity = getActivity();
        Log.w(logger, "activity : " + activity);
        MainHttp mainHttp = new MainHttp(activity, getResources().getString(R.string.data_refresh_title),
                getResources().getString(R.string.data_refresh), API_KEY);
        if(limitCount > this.recipeList.size() && this.recipeList.size() < 530){
            Log.w(logger, "recipeList size : " + recipeList.size());
            mainHttp.setStartIndex(recipeList.size());
            mainHttp.setEndIndex(recipeList.size() + 9);
            mainHttp.getRecipe(new RecipeHelper() {
                @Override
                public void success(RecipeResponseModel response) {
                    List<Recipe> templist = response.getQuery().getRow();
                    if(templist == null){
                        Log.w(logger, "templist is null");
                    }else {
                        recipeList.addAll(templist);
                        girdViewAdapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void failure(String message) {
                    mainText.setText(getResources().getString(R.string.data_error));
                }
            });
        }else{
            Log.w(logger, "recipeList size : " + recipeList.size());
            mainHttp.setStartIndex(recipeList.size());
            mainHttp.setEndIndex(recipeList.size() + 6);
            mainHttp.getRecipe(new RecipeHelper() {
                @Override
                public void success(RecipeResponseModel response) {
                    List<Recipe> templist = response.getQuery().getRow();
                    if(templist == null){
                        Log.w(logger, "templist is null");
                    }else {
                        recipeList.addAll(templist);
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
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onStart(){
        super.onStart();
        Log.w(logger, "onStart");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.w(logger, "onResume");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.w(logger, "onPause");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.w(logger, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.w(logger, "onDestroyView");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.w(logger, "onDestroy");
    }

    @Override
    public void onDetach(){
        super.onDetach();
        Log.w(logger, "onDetach");
    }

    private static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
