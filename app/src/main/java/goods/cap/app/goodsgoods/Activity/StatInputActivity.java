package goods.cap.app.goodsgoods.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import goods.cap.app.goodsgoods.Helper.StatDBHelper;
import goods.cap.app.goodsgoods.Model.Statistic;
import goods.cap.app.goodsgoods.Model.Therapy.Therapy;
import goods.cap.app.goodsgoods.R;

public class StatInputActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    @BindView(R.id.imgEdit)ImageView imgEdit;
    @BindView(R.id.withEdit)EditText withEdit;
    @BindView(R.id.dietEdit)TextView dietEdit;
    @BindView(R.id.startEdit)TextView startTime;
    @BindView(R.id.endEdit)TextView endTime;
    @BindView(R.id.my_toolbar)Toolbar toolbar;
    private static final String logger = StatInputActivity.class.getSimpleName();
    private static final int SEARCH_DIET = 1;
    private static final int PICK_IMAGE = 2;
    private Uri imgUri;
    private TimePickerDialog tpd;
    private Calendar now = Calendar.getInstance();
    private int timeFlag = 0;
    private StatDBHelper statDBHelper;
    private String key, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_input);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        if(intent != null){
            statDBHelper = new StatDBHelper(this);
            try {
                //식사 종류, 날짜
                key = intent.getStringExtra("key");
                date = intent.getStringExtra("date");
                toolbar.setTitle(key);
                toolbar.setTitleTextColor(getResources().getColor(R.color.white));
                statDBHelper.open();
                //존재함
                Statistic list = statDBHelper.getDateStatistic(date);
                if(list != null){
                    setData(key, list);
                }else{
                    long id = statDBHelper.initDatas(date);
                    Log.e(logger, "id=> " + id);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                statDBHelper.close();
            }
        }else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
            StatInputActivity.this.finish();
        }
    }
    private void setData(String key, Statistic statistic){
        final RequestOptions ro = new RequestOptions()
                .placeholder(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_add))
                .error(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_add));

        if(key.equals(getResources().getString(R.string.morining))){
            final String uri = statistic.getImgbrkfast();
            if(uri != null){
                imgEdit.post(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(StatInputActivity.this)
                                .setDefaultRequestOptions(ro)
                                .load(uri)
                                .into(imgEdit);
                    }
                });
            }
            String with = statistic.getWhobrkfast();
            if(with != null) withEdit.setText(with);
            dietEdit.setText(statistic.getBrkfast());
            startTime.setText(statistic.getBrkstrtime());
            endTime.setText(statistic.getBrkendtime());
        }else if(key.equals(getResources().getString(R.string.lunch))){
            final String uri = statistic.getImglunch();
            if(uri != null){
                imgEdit.post(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(StatInputActivity.this)
                                .setDefaultRequestOptions(ro)
                                .load(uri)
                                .into(imgEdit);
                    }
                });
            }
            String with = statistic.getWholunch();
            if(with != null) withEdit.setText(with);
            dietEdit.setText(statistic.getLunch());
            startTime.setText(statistic.getLchstrtime());
            endTime.setText(statistic.getLchendtime());
        }else if(key.equals(getResources().getString(R.string.dinner))){
            final String uri = statistic.getImgdinner();
            if(uri != null){
                imgEdit.post(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(StatInputActivity.this)
                                .setDefaultRequestOptions(ro)
                                .load(uri)
                                .into(imgEdit);
                    }
                });
            }
            String with = statistic.getWhodinner();
            if(with != null) withEdit.setText(with);
            dietEdit.setText(statistic.getDinner());
            startTime.setText(statistic.getDinstrtime());
            endTime.setText(statistic.getDinendtime());
        }else if(key.equals(getResources().getString(R.string.snack))){
            final String uri = statistic.getImgsnack();
            if(uri != null){
                imgEdit.post(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(StatInputActivity.this)
                                .setDefaultRequestOptions(ro)
                                .load(uri)
                                .into(imgEdit);
                    }
                });
            }
            String with = statistic.getWhosnack();
            if(with != null) withEdit.setText(with);
            dietEdit.setText(statistic.getSnack());
            startTime.setText(statistic.getSnkstrtime());
            endTime.setText(statistic.getSnkendtime());
        }else if(key.equals(getResources().getString(R.string.dessert))){
            final String uri = statistic.getImgdessert();
            if(uri != null){
                imgEdit.post(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(StatInputActivity.this)
                                .setDefaultRequestOptions(ro)
                                .load(uri)
                                .into(imgEdit);
                    }
                });
            }
            String with = statistic.getWhodessert();
            if(with != null) withEdit.setText(with);
            dietEdit.setText(statistic.getDessert());
            startTime.setText(statistic.getDesstrtime());
            endTime.setText(statistic.getDesendtime());
        }
    }
    @OnClick(R.id.imgEdit)
    public void imgClick(){
        Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickImageIntent.setType("image/*");
        startActivityForResult(pickImageIntent, PICK_IMAGE);
    }
    @OnClick(R.id.dietEdit)
    public void dietClick(){
        startActivityForResult(new Intent(StatInputActivity.this, StatSearchActivity.class), SEARCH_DIET);
    }
    @OnClick(R.id.startEdit)
    public void starClick(){
        timeFlag = 0;
        timePick();
    }
    @OnClick(R.id.endEdit)
    public void endClick(){
        timeFlag = 1;
        timePick();
    }
    private void timePick(){
        try {
            if (tpd == null) {
                tpd = TimePickerDialog.newInstance(
                        StatInputActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );
            } else {
                tpd.initialize(
                        StatInputActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        now.get(Calendar.SECOND),
                        true
                );
            }
            tpd.show(getFragmentManager(), "Timepickerdialog");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode == SEARCH_DIET){
                dietEdit.setText(data.getStringExtra("search"));
            }
            if (requestCode == PICK_IMAGE) {
                Uri imageUri = data.getData();
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imgUri = result.getUri();
                final RequestOptions ro = new RequestOptions()
                        .placeholder(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_add))
                        .error(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_add));
                imgEdit.post(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(StatInputActivity.this)
                                .setDefaultRequestOptions(ro)
                                .load(imgUri)
                                .into(imgEdit);
                    }
                });
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(logger, "onSaveInstanceState");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            StatInputActivity.this.finish();
            return true;
        }
        if (item.getItemId() == R.id.action_add){
            addData();
        }
        return super.onOptionsItemSelected(item);
    }
    //DB저장
    private void setDietDB(){
        try {
            statDBHelper.open();
            String with = withEdit.getText().toString();
            if(!TextUtils.isEmpty(with)){
                statDBHelper.insertWith(key, with, date);
            }
            if (imgUri != null) {
                statDBHelper.insertImg(key, imgUri.toString(), date);
            }
            statDBHelper.insertDiet(key, dietEdit.getText().toString(), date);
            statDBHelper.insertStartEndTime(key, startTime.getText().toString(), endTime.getText().toString(), date);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            statDBHelper.close();
        }
    }
    private void addData(){
        if(date == null || key == null){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_data_error), Toast.LENGTH_SHORT).show();
        }else{
            if(TextUtils.isEmpty(dietEdit.getText().toString())){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_data_diet), Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(startTime.getText().toString()) || TextUtils.isEmpty(endTime.getText().toString())){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_data_time), Toast.LENGTH_SHORT).show();
            }else{
                final AlertDialog.Builder builder = new AlertDialog.Builder(StatInputActivity.this);
                builder.setMessage(getResources().getString(R.string.input_diet));
                builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setDietDB();
                        Intent intent = new Intent(StatInputActivity.this, StatActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        StatInputActivity.this.finish();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume(){
        super.onResume();
        try {
            TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("TimepickerDialog");
            if (tpd != null) tpd.setOnTimeSetListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }
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
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay; //시간
        String minuteString = minute < 10 ? "0" + minute : "" + minute; //분
        String time = String.format("%s:%s", hourString, minuteString);
        if(timeFlag == 0){
            startTime.setText(time);
        }else {
            endTime.setText(time);
        }
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
