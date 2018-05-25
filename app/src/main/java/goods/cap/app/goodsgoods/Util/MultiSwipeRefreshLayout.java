package goods.cap.app.goodsgoods.Util;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

public class MultiSwipeRefreshLayout extends SwipeRefreshLayout{

    private View[] mSwipeableChildren;

    public MultiSwipeRefreshLayout(Context context) {
        super(context);
    }

    public MultiSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSwipeableChildren(final int... ids) {
        assert ids != null;
        mSwipeableChildren = new View[ids.length];
        for (int i = 0; i < ids.length; i++) {
            mSwipeableChildren[i] = findViewById(ids[i]);
        }
    }

    @Override
    public boolean canChildScrollUp() {
        if (mSwipeableChildren != null && mSwipeableChildren.length > 0) {
            for (View view : mSwipeableChildren) {
                if (view != null && view.isShown() && !canViewScrollUp(view)) {
                    return false;
                }
            }
        }
        return true;
    }
    private static boolean canViewScrollUp(View view) {
        if (view instanceof AbsListView) {
            final AbsListView listView = (AbsListView) view;
            return listView.getChildCount() > 0 &&
                    (listView.getFirstVisiblePosition() > 0
                            || listView.getChildAt(0).getTop() < listView.getPaddingTop());
        }
        else {
            return view.getScrollY() > 0;
        }
    }
}
