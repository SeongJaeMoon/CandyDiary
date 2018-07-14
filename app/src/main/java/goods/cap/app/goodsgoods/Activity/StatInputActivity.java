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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goods.cap.app.goodsgoods.Helper.StatDBHelper;
import goods.cap.app.goodsgoods.Model.Firebase.Calorie;
import goods.cap.app.goodsgoods.Model.Statistic;
import goods.cap.app.goodsgoods.R;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

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
    private Calorie calorie;
    private String message;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_input);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if(intent != null){
            try {
                statDBHelper = new StatDBHelper(this);
                //종류, 날짜
                key = intent.getStringExtra("key");
                date = intent.getStringExtra("date");
                Log.e(logger, "key1: " + key);
                if(key.equals("modify")) {
                    String modifyData = intent.getStringExtra("stat");
                    final Statistic statistic = new Gson().fromJson(modifyData, Statistic.class);
                    if(statistic != null) {
                        Log.e(logger, "jsonString: " + statistic.toString());
                        message = getResources().getString(R.string.update_diet);
                        setData(statistic);
                    }else{
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    message = getResources().getString(R.string.input_diet);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
            StatInputActivity.this.finish();
        }
    }
    private void setData(final Statistic statistic){
        id = statistic.getId();
        Log.e(logger, "id: " + id);
        date = statistic.getDietdate();
        Log.e(logger, "modify date: " + date);
        toolbar.setTitle(String.format(Locale.KOREA, "%s(%s)","수정", date));
        imgEdit.post(new Runnable() {
            @Override
            public void run() {
                final RequestOptions ro = new RequestOptions()
                        .placeholder(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_add))
                        .error(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_add));

                Glide.with(getApplicationContext())
                        .setDefaultRequestOptions(ro)
                        .load(statistic.getImgdiet())
                        .into(imgEdit);
            }
        });
        withEdit.setText(statistic.getWhodiet());
        dietEdit.setText(statistic.getNames());
        startTime.setText(statistic.getStrtime());
        endTime.setText(statistic.getEndtime());
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
        try {
            timeFlag = 0;
            timePick();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @OnClick(R.id.endEdit)
    public void endClick(){
        try {
            timeFlag = 1;
            timePick();
        }catch (Exception e){
            e.printStackTrace();
        }
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
                String jsonData = data.getStringExtra("calorie");
                calorie = new Gson().fromJson(jsonData, Calorie.class);
                if(calorie == null){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.diet_data_error),Toast.LENGTH_SHORT).show();
                }else {
                    calorie.setDate(date);
                }
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
        Intent intent = new Intent(StatInputActivity.this, StatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(StatInputActivity.this, StatActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_add){
            addData();
        }
        return super.onOptionsItemSelected(item);
    }
    //DB저장
    private void modifyDB(){
        try {
            statDBHelper.open();
            String with = withEdit.getText().toString();
            String uri = "";
            if (TextUtils.isEmpty(with)) {
                with = "";
                Log.e(logger, "with: " + with);
            }
            if (imgUri != null) {
                uri = imgUri.toString();
                Log.e(logger, "imgUri: " + uri);
            }
            Statistic statistic = new Statistic();
            statistic.setId(id);
            statistic.setDietdate(date);
            statistic.setNames(dietEdit.getText().toString());
            statistic.setStrtime(startTime.getText().toString());
            statistic.setEndtime(endTime.getText().toString());
            statistic.setImgdiet(uri);
            statistic.setWhodiet(with);
            Log.e(logger, "statistic id: " + statistic.getId() + ", statistic date: " + date);
            long id = statDBHelper.updateData(statistic);
            Log.e(logger, "update id1: " + id);
            if(calorie != null){
                long id2 = statDBHelper.updateCalorie(calorie);
                Log.e(logger, "update id2: " + id2);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            statDBHelper.close();
        }
    }
    private void initDB(){
        try {
            statDBHelper.open();
            String with = withEdit.getText().toString();
            String uri = "";
            if (TextUtils.isEmpty(with)) {
                with = "";
                Log.e(logger, "with: " + with);
            }
            if (imgUri != null) {
                uri = imgUri.toString();
                Log.e(logger, "imgUri: " + uri);
            }
            Statistic statistic = new Statistic();
            statistic.setNames(dietEdit.getText().toString());
            statistic.setStrtime(startTime.getText().toString());
            statistic.setEndtime(endTime.getText().toString());
            statistic.setImgdiet(uri);
            statistic.setWhodiet(with);
            statistic.setDietdate(date);
            long id = statDBHelper.initDatas(statistic);
            long id2 = statDBHelper.initCalorie(calorie);
            Log.e(logger, "init id: " + id +", init id2: " + id2);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_data_error), Toast.LENGTH_SHORT).show();
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
                builder.setMessage(message);
                builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(key.equals("init")){
                            initDB();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pulldown_release), Toast.LENGTH_SHORT).show();
                        }else if (key.equals("modify")){
                            modifyDB();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pulldown_release), Toast.LENGTH_SHORT).show();
                        }
                        Log.e(logger, "key2: " + key);
                        Intent intent = new Intent(StatInputActivity.this, StatActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
    public void onPointerCaptureChanged(boolean hasCapture) {}
}
