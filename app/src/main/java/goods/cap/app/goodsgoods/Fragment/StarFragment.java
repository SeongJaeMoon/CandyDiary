package goods.cap.app.goodsgoods.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import goods.cap.app.goodsgoods.Activity.DetailItemActivity;
import goods.cap.app.goodsgoods.Activity.DetailTherapyActivity;
import goods.cap.app.goodsgoods.Adapter.GirdViewAdapter;
import goods.cap.app.goodsgoods.Helper.StarDBHelper;
import goods.cap.app.goodsgoods.Model.Diet.Diet;
import goods.cap.app.goodsgoods.Model.Recent;
import goods.cap.app.goodsgoods.Model.Therapy.Therapy;
import goods.cap.app.goodsgoods.R;
import goods.cap.app.goodsgoods.Util.MultiSwipeRefreshLayout;

public class StarFragment extends Fragment implements MultiSwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener{

    private static final String logger = StarFragment.class.getSimpleName();
    public static final String ARG_PAGE = "ARG_PAGE";
    private List<Object> allList = new ArrayList<Object>();
    private MultiSwipeRefreshLayout swipeRefreshLayout;
    private GirdViewAdapter gridViewAdapter;
    private TextView mainText;
    private ListView gridView;

    public static StarFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        StarFragment fragment = new StarFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_star, container, false);
        swipeRefreshLayout = (MultiSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        gridView = (ListView) view.findViewById(R.id.gridMain);
        mainText = (TextView) view.findViewById(R.id.mainTitle);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setSwipeableChildren(R.id.gridMain);
        gridView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            for (String key : savedInstanceState.keySet()) {
                Log.i(logger, "key : " + key);
            }
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();
        initData();
    }

    private void initData(){
        List<Recent> ret = getStarList();
        if(ret == null){
            mainText.setText(getResources().getString(R.string.no_star_data));
        }else{
            Gson gson = new Gson();
            String stars = gson.toJson(ret);
            Recent[] temp = gson.fromJson(stars, Recent[].class);
            allList = new ArrayList<Object>(Arrays.asList(temp));
            gridViewAdapter = new GirdViewAdapter(getActivity(), allList, R.layout.grid_single);
            gridView.setAdapter(gridViewAdapter);
            mainText.setText(getResources().getString(R.string.star_data));
        }
    }

    private List<Recent> getStarList(){
        List<Recent> ret = null;
        StarDBHelper starDBHelper = new StarDBHelper(getActivity());
        try{
            starDBHelper.open();
            ret = starDBHelper.getStarList();
        }catch (Exception e){
            Log.w(logger, e.getMessage());
        }finally {
            starDBHelper.close();
        }
        return ret;
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

    @Override
    public void onRefresh() {
        initData();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(allList.get(position) instanceof Recent) {
            Recent temp = (Recent) allList.get(position);
            Gson gson = new Gson();
            String recent = "";
            int flag = temp.getFlag();
            if (flag == 0) {
                Diet diet = new Diet(temp.getCtnno(), temp.getImgUrl(), temp.getSummary(), temp.getCntnt());
                Intent intent = new Intent(getActivity(), DetailItemActivity.class);
                recent = gson.toJson(diet);
                intent.putExtra("diet", recent);
                startActivity(intent);
            } else {
                Therapy therapy = new Therapy(temp.getCtnno(), temp.getImgUrl(), temp.getSummary(), temp.getCntnt());
                Intent intent = new Intent(getActivity(), DetailTherapyActivity.class);
                recent = gson.toJson(therapy);
                intent.putExtra("therapy", recent);
                startActivity(intent);
            }
        }
    }

    /*@Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.noti_star_delete));
        builder.setPositiveButton(getResources().getString(R.string.delete_star_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StarDBHelper starDBHelper = new StarDBHelper(getActivity());
                Recent recent = (Recent)allList.get(position);
                try{
                    starDBHelper.open();
                    starDBHelper.removeList(recent.getCtnno());
                    Toast.makeText(getActivity(), getResources().getString(R.string.star_delete), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Log.w(logger, e.getMessage());
                    Toast.makeText(getActivity(), getResources().getString(R.string.star_delete_error),Toast.LENGTH_SHORT).show();
                }finally {
                    starDBHelper.close();
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
        return false;
    }*/
}
