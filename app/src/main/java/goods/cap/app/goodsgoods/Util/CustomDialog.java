package goods.cap.app.goodsgoods.Util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import goods.cap.app.goodsgoods.R;

public class CustomDialog extends Dialog{
    private static final String logger = CustomDialog.class.getSimpleName();
    private PieChart mChart;
    private String[] calorieInfo;
    private List<String> dietKey = new ArrayList<String>();
    private List<String> dietVal = new ArrayList<String>();
    private List<String> dietUnit = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.custom_dialog);

        mChart = findViewById(R.id.chart);
        mChart.setUsePercentValues(true);

        initChart(this.calorieInfo);

        Button closeButton = (Button) findViewById(R.id.close_dialog);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.this.dismiss();
            }
        });
    }

    public CustomDialog(Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
    }

    public CustomDialog(Context context , String[] calorieInfo) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.calorieInfo = calorieInfo;
    }

    private void initChart(String[] dietInfo){

        ArrayList<Integer> colors = new ArrayList<Integer>();
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        int len = dietInfo.length;
        //ex, 열량 690 Kcal,당질 96 g,단백질 42 g,지질 15 g
        for(String s : dietInfo){
            Log.i(logger, "dt : " + s);
            dietKey.add(getHangul(s));
            dietVal.add(getNumber(s));
            dietUnit.add(getUnit(s));

        }

        Random random = new Random();
        for (int i = 1; i < len; ++i){
            entries.add(new PieEntry(Float.parseFloat(dietVal.get(i)), dietKey.get(i)));
            colors.add(Color.rgb(random.nextInt(255) + 1,random.nextInt(255) + 1, random.nextInt(255) + 1));
        }
        PieDataSet pieDataSet = new PieDataSet(entries, "Result");
        pieDataSet.setValueTextSize(14);
        pieDataSet.setValueTextColor(getContext().getResources().getColor(R.color.white));
        mChart.setCenterText(dietKey.get(0) + dietVal.get(0) + dietUnit.get(0));
        mChart.setCenterTextSize(20);
        mChart.setCenterTextColor(getContext().getResources().getColor(R.color.accent));
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(getContext().getResources().getColor(R.color.white));
        pieDataSet.setColors(colors);
        PieData data = new PieData(pieDataSet);
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);
        mChart.setData(data);
        mChart.highlightValues(null);
    }

    private String getHangul(String str) {
        return str.replaceAll("[^\uAC00-\uD7AF\u1100-\u11FF\u3130-\u318F]", "");
    }

    private String getUnit(String str){
        String temp = "";
        String unit = "";
        for( int i = 0; i < str.length(); i++ ) {
            temp = str.substring(i, i + 1);
            if(isEng(temp.charAt(0)+"")){
                unit += temp;
            }
        }
        return unit;
    }

    private String getNumber(String str){
        String temp = "";
        String num = "";
        for( int i = 0; i < str.length(); i++ ) {
            temp = str.substring(i, i + 1);
            if(isNum(temp.charAt(0)+"")){
                num += temp;
            }
        }
        return num;
    }

    private boolean isNum(String str) { return Pattern.matches("^[0-9]*$", str); }

    private boolean isEng(String str) { return Pattern.matches("^[a-zA-Z]*$", str); }
}

