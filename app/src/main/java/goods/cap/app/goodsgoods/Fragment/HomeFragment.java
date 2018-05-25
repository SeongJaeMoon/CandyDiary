package goods.cap.app.goodsgoods.Fragment;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
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
import goods.cap.app.goodsgoods.R;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private static final String logger = HomeFragment.class.getSimpleName();
    public static final String ARG_PAGE = "ARG_PAGE";
    private TextView mainText;
    private GridView gridView;
    private List<Recipe> recipeList;
    private List<Grocery> groceryList;
    private SwipeRefreshLayout swipeRefreshLayout;


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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        gridView = (GridView)view.findViewById(R.id.gridMain);
        mainText = (TextView)view.findViewById(R.id.mainTitle);
        swipeRefreshLayout.setOnRefreshListener(this);
        gridView.setOnItemClickListener(this);

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
    public void onRefresh() {
        initData();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initData(){
        final Activity activity = getActivity();
        Log.w(logger, "activity : " + activity);
        MainHttp mainHttp = new MainHttp(activity, getResources().getString(R.string.data_refresh_title), getResources().getString(R.string.data_refresh),"");

        //        mainHttp.getGrocery(new GroceryHelper() {
//            @Override
//            public void success(GroceryResponseModel response) {
//
//            }
//
//            @Override
//            public void failure(String message) {
//
//            }
//        });

        mainHttp.getRecipe(new RecipeHelper() {
            @Override
            public void success(RecipeResponseModel response) {
                recipeList = response.getQuery().getRow();
                if(recipeList == null){
                    Log.w(logger, "recipeList is null");
                }else {
                    GirdViewAdapter adapter = new GirdViewAdapter(activity, recipeList, R.layout.grid_single);
                    gridView.setAdapter(adapter);
                    mainText.setText(getResources().getString(R.string.main_title));
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

    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }

    private static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
