package goods.cap.app.goodsgoods.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goods.cap.app.goodsgoods.Helper.StatDBHelper;
import goods.cap.app.goodsgoods.Model.Firebase.Calorie;
import goods.cap.app.goodsgoods.R;

public class StatDtlActivity extends AppCompatActivity {
    @BindView(R.id.chart) RadarChart mChart;
    @BindView(R.id.close_dialog)Button button;
    @BindView(R.id.total_kal)TextView kalView;
    @BindView(R.id.image_dialog)Button imgButton;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_dtl);
        ButterKnife.bind(this);

        mChart.setBackgroundColor(Color.rgb(60, 65, 82));
        mChart.getDescription().setEnabled(false);
        mChart.setWebLineWidth(1f);
        mChart.setWebColor(Color.LTGRAY);
        mChart.setWebLineWidthInner(1f);
        mChart.setWebColorInner(Color.LTGRAY);
        mChart.setWebAlpha(100);

        Intent intent = getIntent();
        if(intent != null) {
            date = intent.getStringExtra("date");
            StatDBHelper statDBHelper = new StatDBHelper(this);
            statDBHelper.open();
            List<Calorie> calories = statDBHelper.getAllCalorie(date);
            if (calories != null) {
                try {
                    double totalKal = 0;

                    ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();

                    for (Calorie calorie : calories) {
                        ArrayList<RadarEntry> entries = new ArrayList<RadarEntry>();
                        Log.e("cals", "cal: " + calorie.toString());
                        if(calorie.getCho_mg() == null){
                            entries.add(new RadarEntry(0));
                        }else{
                            if(isNum(calorie.getCho_mg())){
                                entries.add(new RadarEntry(Float.parseFloat(calorie.getCho_mg().toString())));
                            }else{
                                entries.add(new RadarEntry(0));
                            }
                        }
                        if(calorie.getDan_g() == null){
                            entries.add(new RadarEntry(0));
                        }else {
                            if(isNum(calorie.getDan_g())){
                                entries.add(new RadarEntry(Float.parseFloat(calorie.getDan_g().toString())));
                            }else{
                                entries.add(new RadarEntry(0));
                            }
                        }
                        if(calorie.getDang_g() == null){
                            entries.add(new RadarEntry(0));
                        }else {
                            if(isNum(calorie.getDang_g())){
                                entries.add(new RadarEntry(Float.parseFloat(calorie.getDang_g().toString())));
                            }else{
                                entries.add(new RadarEntry(0));
                            }
                        }
                        if(calorie.getJi_g() == null){
                            entries.add(new RadarEntry(0));
                        }else {
                            if(isNum(calorie.getJi_g())){
                                entries.add(new RadarEntry(Float.parseFloat(calorie.getJi_g().toString())));
                            }else{
                                entries.add(new RadarEntry(0));
                            }
                        }
                        if(calorie.getNa_mg() == null){
                            entries.add(new RadarEntry(0));
                        }else {
                            if(isNum(calorie.getNa_mg())){
                                entries.add(new RadarEntry(Float.parseFloat(calorie.getNa_mg().toString())));
                            }else{
                                entries.add(new RadarEntry(0));
                            }
                        }
                        if(calorie.getPho_g() == null){
                            entries.add(new RadarEntry(0));
                        }else {
                            if(isNum(calorie.getPho_g())){
                                entries.add(new RadarEntry(Float.parseFloat(calorie.getPho_g().toString())));
                            }else{
                                entries.add(new RadarEntry(0));
                            }
                        }
                        if(calorie.getTan_g() == null){
                            entries.add(new RadarEntry(0));
                        }else {
                            if(isNum(calorie.getTan_g())){
                                entries.add(new RadarEntry(Float.parseFloat(calorie.getTan_g().toString())));
                            }else{
                                entries.add(new RadarEntry(0));
                            }
                        }
                        if(calorie.getTrans_g() == null){
                            entries.add(new RadarEntry(0));
                        }else {
                            if(isNum(calorie.getTrans_g())){
                                entries.add(new RadarEntry(Float.parseFloat(calorie.getTrans_g().toString())));
                            }else{
                                entries.add(new RadarEntry(0));
                            }
                        }
                        totalKal += calorie.getKal();
                        sets.add(setRadarData(entries, String.format("종류:(%s)", calorie.getCateorgy())));
                    }

                    kalView.setText(String.format(Locale.KOREA, "총 섭취 칼로리:%.2fkal(위 차트는 비율을 나타냅니다.)", totalKal));

                    XAxis xAxis = mChart.getXAxis();
                    xAxis.setTextSize(14f);
                    xAxis.setYOffset(0f);
                    xAxis.setXOffset(0f);

                    xAxis.setValueFormatter(new IAxisValueFormatter() {
                        private String[] mActivities = new String[]{getResources().getString(R.string.cho_mg),
                                getResources().getString(R.string.dan_g),
                                getResources().getString(R.string.dang_g),
                                getResources().getString(R.string.ji_g),
                                getResources().getString(R.string.na_mg),
                                getResources().getString(R.string.pho_g),
                                getResources().getString(R.string.tan_g),
                                getResources().getString(R.string.trans_g)
                        };
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            return mActivities[(int) value % mActivities.length];
                        }
                    });

                    xAxis.setTextColor(Color.WHITE);

                    YAxis yAxis = mChart.getYAxis();
                    yAxis.setLabelCount(8, false);
                    yAxis.setTextSize(10f);
                    yAxis.setAxisMinimum(0f);
                    yAxis.setAxisMaximum(80f);
                    yAxis.setDrawLabels(false);

                    Legend l = mChart.getLegend();
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
                    l.setDrawInside(false);
                    l.setXEntrySpace(3f);
                    l.setYEntrySpace(3f);
                    l.setTextColor(Color.WHITE);

                    RadarData data = new RadarData(sets);
                    data.setValueTextSize(10f);
                    data.setDrawValues(false);
                    data.setValueTextColor(Color.WHITE);

                    mChart.setData(data);
                    mChart.invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    statDBHelper.close();
                }
            }
        }else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatDtlActivity.this.finish();
            }
        });
    }

    private RadarDataSet setRadarData(ArrayList<RadarEntry> list, String label){
        RadarDataSet set1 = new RadarDataSet(list, label);
        Random random = new Random();
        set1.setColor(Color.rgb(random.nextInt(255) + 1,random.nextInt(255) + 1, random.nextInt(255) + 1));
        set1.setFillColor(Color.rgb(random.nextInt(255) + 1,random.nextInt(255) + 1, random.nextInt(255) + 1));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);
        return set1;
    }

    private boolean isNum(Object value){
        if(value instanceof String) {
            String s = (String)value;
            return Pattern.matches("^[0-9\\.]*$", s);
        }else{
            return false;
        }
    }
    @OnClick(R.id.image_dialog)
    public void clickButton(){
        startActivity(new Intent(StatDtlActivity.this, StatImageActivity.class).putExtra("date", date));
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(StatDtlActivity.this, StatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
