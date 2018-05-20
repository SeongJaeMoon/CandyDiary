package goods.cap.app.goodsgoods.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import goods.cap.app.goodsgoods.R;

/* Fragment viewe 반환, created by supermoon. */

public class PageFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }
        FrameLayout fl = new FrameLayout(getActivity());
        View view = null;
        if (mPage == 1) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
        }else if(mPage == 2){
            view = inflater.inflate(R.layout.fragment_star, container, false);
        }else if(mPage == 3) {
            view = inflater.inflate(R.layout.fragment_com, container, false);
        }
        fl.addView(view);
        return fl;
    }

//    @Override
//    public void onAttach(Context context){
//        super.onAttach(context);
//    }
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//    }
}
