package goods.cap.app.goodsgoods.Fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import goods.cap.app.goodsgoods.R;

/* main 화면 상단 Grid view, created by supermoon. */

public class MainFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        return view;
    }

}
