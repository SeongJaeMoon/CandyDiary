package goods.cap.app.goodsgoods.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import goods.cap.app.goodsgoods.Fragment.PageFragment;
import goods.cap.app.goodsgoods.R;

/* Fragment 컨트롤 Adapter, created by supermoon. */

public class MainAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    private final int PAGE_COUNT = 3;
    private int tabIcons[] = {R.drawable.ic_home_white, R.drawable.ic_favorite_white, R.drawable.ic_people_white};

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
    }

    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View v, Object o) {
        return v == ((View) o);
    }
}
