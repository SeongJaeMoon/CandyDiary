package goods.cap.app.goodsgoods.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.desai.vatsal.mydynamiccalendar.EventModel;
import com.desai.vatsal.mydynamiccalendar.GetEventListListener;
import com.desai.vatsal.mydynamiccalendar.MyDynamicCalendar;
import com.desai.vatsal.mydynamiccalendar.OnDateClickListener;
import com.desai.vatsal.mydynamiccalendar.OnEventClickListener;
import com.desai.vatsal.mydynamiccalendar.OnWeekDayViewClickListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.Helper.StatDBHelper;
import goods.cap.app.goodsgoods.Model.Statistic;
import goods.cap.app.goodsgoods.R;

public class StatActivity extends AppCompatActivity {
    @BindView(R.id.myCalendar)MyDynamicCalendar myCalendar;
    @BindView(R.id.my_toolbar)Toolbar toolbar;
    private String logger = StatActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private StatDBHelper statDBHelper;
    private List<Statistic> statisticList;
    private SimpleDateFormat sdfDayMonthYear = new SimpleDateFormat("dd-MM-yyyy", Locale.KOREA);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        ButterKnife.bind(this);

        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        statDBHelper = new StatDBHelper(this);
        try{
            statDBHelper.open();
            statisticList = statDBHelper.getALLStatistic();
            if(statisticList != null && statisticList.size() > 0){
                showProgressDialog();
                for(Statistic s : statisticList){
                    String with = "";
                    if(s.getBrkfast() != null){
                        if(s.getWhobrkfast() != null){
                            with = s.getWhobrkfast();
                        }
                        myCalendar.addEvent(s.getDate(), s.getBrkstrtime(), s.getBrkendtime(), String.format("%s(%s님과 함께)",s.getBrkfast(), with));
                    }
                    if(s.getLunch() != null){
                        if(s.getWholunch() != null){
                            with = s.getWhobrkfast();
                        }
                        myCalendar.addEvent(s.getDate(), s.getLchstrtime(), s.getLchendtime(), String.format("%s(%s님과 함께)", s.getLunch(), with));
                    }
                    if(s.getDinner() != null){
                        if(s.getWhodinner() != null){
                            with = s.getWhodinner();
                        }
                        myCalendar.addEvent(s.getDate(), s.getDinstrtime(), s.getDinendtime(), String.format("%s(%s님과 함께)", s.getDinner(), with));
                    }
                    if(s.getSnack() != null){
                        if(s.getWhosnack() != null){
                            with = s.getWhosnack();
                        }
                        myCalendar.addEvent(s.getDate(), s.getSnkstrtime(), s.getSnkendtime(), String.format("%s(%s님과 함께)", s.getSnack(), with));
                    }
                    if(s.getDessert() != null){
                        if(s.getWhodessert() != null){
                            with = s.getWhodessert();
                        }
                        myCalendar.addEvent(s.getDate(), s.getDesstrtime(), s.getDesendtime(), String.format("%s(%s님과 함께)", s.getDessert(), with));
                    }
                }
            }
            hideProgressDialog();
            statDBHelper.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        myCalendar.showMonthViewWithBelowEvents();
        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(Date date) {
                Log.w(logger, String.valueOf(date));
                setChooseDialog(sdfDayMonthYear.format(date));
            }
            @Override
            public void onLongClick(Date date) {
                Log.w(logger, String.valueOf(date));
                if(statisticList != null && statisticList.size() > 0){
                    for(Statistic statistic : statisticList){
                        if(statistic.getDate().equals(sdfDayMonthYear.format(date))){
                            Intent intent = new Intent(StatActivity.this, StatDtlActivity.class);
                            Gson gson = new Gson();
                            intent.putExtra("statistic", gson.toJson(statistic));
                            startActivity(intent);
                        }
                    }
                }
            }
        });

        //myCalendar.addEvent("5-10-2016", "8:00", "8:15", "Today Event 1");
        //myCalendar.updateEvent(0, "5-10-2016", "8:00", "8:15", "Today Event 111111");
        //myCalendar.deleteEvent(2);
        //myCalendar.deleteAllEvent();

        myCalendar.getEventList(new GetEventListListener() {
            @Override
            public void eventList(ArrayList<EventModel> eventList) {
                Log.e("tag", "eventList.size():-" + eventList.size());
                for (int i = 0; i < eventList.size(); i++) {
                    Log.e("tag", "eventList.getStrName:-" + eventList.get(i).getStrName());
                }
            }
        });
        setMyCalendarLayout();
    }

    private void setMyCalendarLayout(){
        myCalendar.setHeaderBackgroundColor("#eeeeee");
        myCalendar.setHeaderTextColor("#000000");
        myCalendar.setBelowMonthEventTextColor("#000000");
        myCalendar.setBelowMonthEventDividerColor("#000000");
        myCalendar.setHolidayCellBackgroundColor("#eeeeee");
        myCalendar.setHolidayCellTextColor(R.color.black);
        myCalendar.setCalendarBackgroundColor("#eeeeee");
        myCalendar.setNextPreviousIndicatorColor("#000000");
        myCalendar.setWeekDayLayoutBackgroundColor("#eeeeee");
        myCalendar.isSaturdayOff(true, R.color.white, R.color.red);
        myCalendar.isSundayOff(true, R.color.white, R.color.red);
        myCalendar.setHolidayCellTextColor("#FF4500");
        myCalendar.setExtraDatesOfMonthBackgroundColor("#C0C0C0");
        myCalendar.setExtraDatesOfMonthTextColor("#000000");
        myCalendar.setDatesOfMonthBackgroundColor(R.drawable.event_view);
        myCalendar.setDatesOfMonthBackgroundColor("#eeeeee");
        myCalendar.setDatesOfMonthTextColor("#000000");
        myCalendar.setCurrentDateBackgroundColor("#eeeeee");
        myCalendar.setCurrentDateTextColor("#87CEFA");
        myCalendar.setEventCellBackgroundColor(R.color.colorPrimaryDark);
        myCalendar.setEventCellTextColor(R.color.colorPrimary);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stat, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            StatActivity.this.finish();
            return true;
        }else {
            switch (item.getItemId()) {
                case R.id.action_month:
                    this.showMonthViewWithBelowEvents();
                    return true;
                case R.id.action_week:
                    this.showWeekView();
                    return true;
                case R.id.action_day:
                    this.showDayView();
                    return true;
                case R.id.action_today:
                    myCalendar.goToCurrentDate();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMonthViewWithBelowEvents() {
        myCalendar.showMonthViewWithBelowEvents();
        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(Date date) {
                Log.e("date", String.valueOf(date));
            }

            @Override
            public void onLongClick(Date date) {
                Log.e("date", String.valueOf(date));
            }
        });

    }
    private void showWeekView() {
        myCalendar.showWeekView();
        myCalendar.setOnEventClickListener(new OnEventClickListener() {
            @Override
            public void onClick() {
                Log.e("showWeekView","from setOnEventClickListener onClick");
            }
            @Override
            public void onLongClick() {
                Log.e("showWeekView","from setOnEventClickListener onLongClick");
            }
        });
        myCalendar.setOnWeekDayViewClickListener(new OnWeekDayViewClickListener() {
            @Override
            public void onClick(String date, String time) {
                Log.e("showWeekView", "from setOnWeekDayViewClickListener onClick");
                Log.e("tag", "date:-" + date + " time:-" + time);
            }
            @Override
            public void onLongClick(String date, String time) {
                Log.e("showWeekView", "from setOnWeekDayViewClickListener onLongClick");
                Log.e("tag", "date:-" + date + " time:-" + time);
            }
        });
    }
    private void showDayView() {
        myCalendar.showDayView();
        myCalendar.setOnEventClickListener(new OnEventClickListener() {
            @Override
            public void onClick() {
                Log.e("showDayView", "from setOnEventClickListener onClick");

            }

            @Override
            public void onLongClick() {
                Log.e("showDayView", "from setOnEventClickListener onLongClick");

            }
        });
        myCalendar.setOnWeekDayViewClickListener(new OnWeekDayViewClickListener() {
            @Override
            public void onClick(String date, String time) {
                Log.e("showDayView", "from setOnWeekDayViewClickListener onClick");
                Log.e("tag", "date:-" + date + " time:-" + time);
            }

            @Override
            public void onLongClick(String date, String time) {
                Log.e("showDayView", "from setOnWeekDayViewClickListener onLongClick");
                Log.e("tag", "date:-" + date + " time:-" + time);
            }
        });

    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(StatActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.data_refresh));
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }
    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void setChooseDialog(final String date){
        final AlertDialog.Builder builder = new AlertDialog.Builder(StatActivity.this);
        builder.setTitle(getResources().getString(R.string.stat_title));
        builder.setItems(new CharSequence[]{
                getResources().getString(R.string.morining),
                getResources().getString(R.string.lunch),
                getResources().getString(R.string.dinner),
                getResources().getString(R.string.snack),
                getResources().getString(R.string.dessert),
                getResources().getString(R.string.close)}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0://아침
                        goStatInput(getResources().getString(R.string.morining), date);
                        break;
                    case 1://점심
                        goStatInput(getResources().getString(R.string.lunch), date);
                        break;
                    case 2://저녁
                        goStatInput(getResources().getString(R.string.dinner), date);
                        break;
                    case 3://간식
                        goStatInput(getResources().getString(R.string.snack), date);
                        break;
                    case 4://후식
                        goStatInput(getResources().getString(R.string.dessert), date);
                        break;
                    case 5:
                        dialog.cancel();
                        break;
                }
            }
        });
        builder.create().show();
    }
    private void goStatInput(String key, String date){
        Intent intent = new Intent(StatActivity.this, StatInputActivity.class);
        intent.putExtra("key", key);
        intent.putExtra("date", date);
        startActivity(intent);
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
        if(statDBHelper != null){
            statDBHelper.close();
        }
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
}
