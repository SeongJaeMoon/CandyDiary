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
import android.widget.Toast;

import com.desai.vatsal.mydynamiccalendar.EventModel;
import com.desai.vatsal.mydynamiccalendar.GetEventListListener;
import com.desai.vatsal.mydynamiccalendar.MyDynamicCalendar;
import com.desai.vatsal.mydynamiccalendar.OnDateClickListener;

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
    private final static String logger = StatActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private StatDBHelper statDBHelper;
    private List<Statistic> statisticList;
    private SimpleDateFormat sdfDayMonthYear = new SimpleDateFormat("dd-MM-yyyy", Locale.KOREA);
    public static boolean isToast = false;
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
                    Log.e(logger, s.toString());
                    String with = s.getWhodiet();
                    if(with != null && !with.equals("")){
                        myCalendar.addEvent(s.getDietdate(), s.getStrtime(), s.getEndtime(), String.format("%s(%s님과 함께)", s.getNames(), s.getWhodiet()));
                    }else{
                        myCalendar.addEvent(s.getDietdate(), s.getStrtime(), s.getEndtime(), String.format("%s", s.getNames()));
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
                try {
                    statDBHelper.open();
                    if(statDBHelper.getALLStatistic(sdfDayMonthYear.format(date)) != null) {
                        Intent intent = new Intent(StatActivity.this, StatDtlActivity.class);
                        intent.putExtra("date", sdfDayMonthYear.format(date));
                        startActivity(intent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    statDBHelper.close();
                }
            }
        });

        //myCalendar.addEvent("5-07-2018", "8:00", "8:15", "Today Event 1");
        //myCalendar.updateEvent(0, "5-10-2016", "8:00", "8:15", "Today Event 111111");
        //myCalendar.deleteEvent(2);
        //myCalendar.deleteAllEvent();

        myCalendar.getEventList(new GetEventListListener() {
            @Override
            public void eventList(ArrayList<EventModel> eventList) {
                Log.e("tag", "eventList.size(): " + eventList.size());
                for (int i = 0; i < eventList.size(); i++) {
                    Log.e("tag", "eventList.getStrName: " + eventList.get(i).getStrName());
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
        StatActivity.this.finish();
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
    private void showDayView() {
        myCalendar.showAgendaView();
        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(Date date) {
                Log.e("showAgendaView","from setOnDateClickListener onClick"+date);
            }

            @Override
            public void onLongClick(Date date) {
                Log.e("showAgendaView","from setOnDateClickListener onClick"+date);
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
                getResources().getString(R.string.stat_input),
                getResources().getString(R.string.stat_modify),
                getResources().getString(R.string.close)}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0://입력
                        goStatInput(0, date);
                        break;
                    case 1://수정
                        goStatInput(1, date);
                        break;
                    case 2://닫기
                        dialog.cancel();
                        break;
                }
            }
        });
        builder.create().show();
    }
    private void goStatInput(int flag, String date){
        try {
            if (flag == 0) {
                Intent intent = new Intent(StatActivity.this, StatInputActivity.class);
                intent.putExtra("key", "init");
                intent.putExtra("date", date);
                startActivity(intent);
            } else {
                statDBHelper.open();
                if(statDBHelper.getALLStatistic(date) != null) {
                    Intent intent = new Intent(StatActivity.this, StatModifyActivity.class);
                    intent.putExtra("date", date);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_data_date), Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//        finally {
//            if(statDBHelper!=null){
//                statDBHelper.close();
//            }
//        }
    }
    @Override
    protected void onStart() {
        Log.i(logger, "start");
        super.onStart();
    }
    @Override
    protected void onResume(){
        Log.i(logger, "resume");
        super.onResume();
        if(!isToast) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.long_date_hint), Toast.LENGTH_LONG).show();
            isToast = true;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(logger, "destroy");
        if(myCalendar != null){
            Log.e(logger, "destroy all");
            myCalendar.deleteAllEvent();
        }
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.i(logger, "stop");
        if(myCalendar != null){
            Log.e(logger, "stop all");
            myCalendar.deleteAllEvent();
        }
    }
    @Override
    protected void onPause(){
        Log.i(logger, "pause");
        super.onPause();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(logger, "restart");
        if(statisticList == null) {
            try {
                statDBHelper = new StatDBHelper(this);
                statDBHelper.open();
                statisticList = statDBHelper.getALLStatistic();
                if (statisticList != null && statisticList.size() > 0) {
                    showProgressDialog();
                    for (Statistic s : statisticList) {
                        Log.e(logger, s.toString());
                        String with = s.getWhodiet();
                        if (with != null && !with.equals("")) {
                            myCalendar.addEvent(s.getDietdate(), s.getStrtime(), s.getEndtime(), String.format("%s(%s님과 함께)", s.getNames(), s.getWhodiet()));
                        } else {
                            myCalendar.addEvent(s.getDietdate(), s.getStrtime(), s.getEndtime(), String.format("%s", s.getNames()));
                        }
                    }
                }
                hideProgressDialog();
                statDBHelper.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
