package goods.cap.app.goodsgoods.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.Adapter.GridViewAdapter;
import goods.cap.app.goodsgoods.Helper.StatDBHelper;
import goods.cap.app.goodsgoods.Model.Recent;
import goods.cap.app.goodsgoods.Model.Statistic;
import goods.cap.app.goodsgoods.R;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class StatImageActivity extends AppCompatActivity {
    @BindView(R.id.rvStat)ListView rvStat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_image);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        if (intent != null) {
            try {
                String date = intent.getStringExtra("date");
                StatDBHelper statDBHelper = new StatDBHelper(this);
                statDBHelper.open();
                List<Statistic> statistics = statDBHelper.getALLStatistic(date);
                if (statistics == null || statistics.size() == 0) {
                   Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
                    this.finish();
                } else {
                    List<Object> imgList = new ArrayList<>();
                    for (Statistic s : statistics) {
                        String url = s.getImgdiet();
                        Recent recent = new Recent();
                        if (url != null && !url.isEmpty()) {
                            Log.e("imageActivity", "url: " + url);
                            recent.setImgUrl(url);
                            recent.setCntnt(String.format("%s %s(%s:%s)", s.getNames(), s.getDietdate(), s.getStrtime(), s.getEndtime()));
                            imgList.add(recent);
                        }
                    }
                    if (imgList.size() == 0) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_data_date), Toast.LENGTH_SHORT).show();
                        this.finish();
                    } else {
                        GridViewAdapter gridViewAdapter = new GridViewAdapter(getApplicationContext(), imgList, R.layout.stat_image_box);
                        rvStat.setAdapter(gridViewAdapter);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StatImageActivity.this.finish();
    }
}
