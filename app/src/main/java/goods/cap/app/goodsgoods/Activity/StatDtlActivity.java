package goods.cap.app.goodsgoods.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.Model.Diet.Diet;
import goods.cap.app.goodsgoods.Model.Statistic;
import goods.cap.app.goodsgoods.R;
import goods.cap.app.goodsgoods.Util.PostImageLoader;
import goods.cap.app.goodsgoods.Util.PostMainSlider;
import ss.com.bannerslider.Slider;

public class StatDtlActivity extends AppCompatActivity {
    @BindView(R.id.chart) RadarChart mChart;
    @BindView(R.id.wallpaper)Slider slider;
    @BindView(R.id.close_dialog)Button button;
    @BindView(R.id.total_kal)TextView kalView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_dtl);
        ButterKnife.bind(this);

        Slider.init(new PostImageLoader(this));

        mChart.setBackgroundColor(Color.rgb(60, 65, 82));
        mChart.getDescription().setEnabled(false);
        mChart.setWebLineWidth(1f);
        mChart.setWebColor(Color.LTGRAY);
        mChart.setWebLineWidthInner(1f);
        mChart.setWebColorInner(Color.LTGRAY);
        mChart.setWebAlpha(100);
        Intent intent = getIntent();
        if(intent != null){
            Gson gson = new Gson();
            final String jsonData = intent.getStringExtra("statistic");
            final Statistic statistic = gson.fromJson(jsonData, Statistic.class);

            String imgB = statistic.getImgbrkfast();
            String imgL = statistic.getImglunch();
            String imgD = statistic.getImgdinner();
            String imgS = statistic.getImgsnack();
            String imgDe = statistic.getImgdinner();

            List<String>imgList = new ArrayList<>();
            if(imgB != null) imgList.add(imgB);
            if(imgL != null) imgList.add(imgL);
            if(imgD != null) imgList.add(imgD);
            if(imgS != null) imgList.add(imgS);
            if (imgDe != null) imgList.add(imgDe);

            final PostMainSlider postMainSlider = new PostMainSlider(imgList);
            slider.postDelayed(new Runnable() {
                @Override
                public void run() {
                    slider.setAdapter(postMainSlider);
                    slider.setSelectedSlide(0);
                    slider.setInterval(5000);
                }
            }, 1500);

            String brk = getNumber(statistic.getBrkfast());
            String lch = getNumber(statistic.getLunch());
            String din = getNumber(statistic.getDinner());
            String snk = getNumber(statistic.getSnack());
            String des = getNumber(statistic.getDessert());

            double totalKal = 0;

            ArrayList<RadarEntry> entries = new ArrayList<RadarEntry>();
            if(brk != null) {
                totalKal += Float.parseFloat(brk);
                entries.add(new RadarEntry(Float.parseFloat(brk)));
            }
            if(lch != null) {
                totalKal += Float.parseFloat(lch);
                entries.add(new RadarEntry(Float.parseFloat(lch)));
            }
            if(din != null) {
                totalKal += Float.parseFloat(din);
                entries.add(new RadarEntry(Float.parseFloat(din)));
            }
            if(snk != null) {
                totalKal += Float.parseFloat(snk);
                entries.add(new RadarEntry(Float.parseFloat(snk)));
            }
            if (des != null) {
                totalKal += Float.parseFloat(des);
                entries.add(new RadarEntry(Float.parseFloat(des)));
            }

            kalView.setText(String.format(Locale.KOREA, "총 섭취 칼로리:%fkal", totalKal));

            XAxis xAxis = mChart.getXAxis();
            xAxis.setTextSize(9f);
            xAxis.setYOffset(0f);
            xAxis.setXOffset(0f);
            xAxis.setValueFormatter(new IAxisValueFormatter() {

                private String[] mActivities = new String[]{getResources().getString(R.string.morining),
                        getResources().getString(R.string.lunch),
                        getResources().getString(R.string.dinner),
                        getResources().getString(R.string.snack),
                        getResources().getString(R.string.dessert)};

                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return mActivities[(int) value % mActivities.length];
                }
            });

            xAxis.setTextColor(Color.WHITE);

            YAxis yAxis = mChart.getYAxis();
            yAxis.setLabelCount(5, false);
            yAxis.setTextSize(9f);
            yAxis.setAxisMinimum(0f);
            yAxis.setAxisMaximum(80f);
            yAxis.setDrawLabels(false);

            Legend l = mChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(5f);
            l.setTextColor(Color.WHITE);

            RadarDataSet set = new RadarDataSet(entries, "");
            set.setColor(Color.rgb(103, 110, 129));
            set.setFillColor(Color.rgb(103, 110, 129));
            set.setDrawFilled(true);
            set.setFillAlpha(180);
            set.setLineWidth(2f);
            set.setDrawHighlightCircleEnabled(true);
            set.setDrawHighlightIndicators(false);

            ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
            sets.add(set);

            RadarData data = new RadarData(sets);
            data.setValueTextSize(8f);
            data.setDrawValues(false);
            data.setValueTextColor(Color.WHITE);

            mChart.setData(data);
            mChart.invalidate();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatDtlActivity.this.finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onStop(){
        super.onStop();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private String getNumber(String str){
        if(str == null){
            return null;
        }
        String temp = "";
        StringBuilder num = new StringBuilder();
        for( int i = 0; i < str.length(); i++ ) {
            temp = str.substring(i, i + 1);
            if(isNum(temp.charAt(0)+"")){
                num.append(temp);
            }
        }
        return num.toString();
    }

    private boolean isNum(String str) { return Pattern.matches("^[0-9]*$", str); }
}
