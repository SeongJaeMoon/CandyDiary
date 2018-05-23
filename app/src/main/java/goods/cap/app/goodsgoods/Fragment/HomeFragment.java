package goods.cap.app.goodsgoods.Fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import goods.cap.app.goodsgoods.Adapter.GirdViewAdapter;
import goods.cap.app.goodsgoods.MainActivity;
import goods.cap.app.goodsgoods.Model.Recipe;
import goods.cap.app.goodsgoods.R;

public class HomeFragment extends Fragment {

    private static final String logger = HomeFragment.class.getSimpleName();
    public static final String ARG_PAGE = "ARG_PAGE";
    private TextView mainText;
    private GridView gridView;
    private List<Recipe> datalist;

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
        gridView = (GridView)container.findViewById(R.id.grid);
        
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
        // 뷰에 데이터를 넣는 작업 등을 할 추가
        GirdViewAdapter adapter = new GirdViewAdapter(getActivity(), datalist, R.layout.grid_single);
        gridView.setAdapter(adapter);
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
