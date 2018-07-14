package goods.cap.app.goodsgoods.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.Helper.StatDBHelper;
import goods.cap.app.goodsgoods.Model.Statistic;
import goods.cap.app.goodsgoods.R;

public class StatModifyActivity extends AppCompatActivity {
    @BindView(R.id.my_toolbar)Toolbar toolbar;
    @BindView(R.id.list_modify)ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> viewList;
    private StatDBHelper statDBHelper;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_modify);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if(intent != null){
            date = intent.getStringExtra("date");
            statDBHelper = new StatDBHelper(this);
            try{
                statDBHelper.open();
                final List<Statistic>list = statDBHelper.getALLStatistic(date);
                if(list != null && list.size() > 0){
                    viewList = new ArrayList<>();
                    arrayAdapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, viewList);
                    listView.setAdapter(arrayAdapter);
                    for(Statistic s : list){
                        String with = s.getWhodiet();
                        String stat = "";
                        if(with!=null && !with.equals("")){
                            stat  = String.format(Locale.KOREA, "%s(%s님과 함께) %n%s(%s:%s)", s.getNames(), with, s.getDietdate(), s.getStrtime(), s.getEndtime());
                        }else{
                            stat  = String.format(Locale.KOREA, "%s%n%s(%s:%s)", s.getNames(), s.getDietdate(), s.getStrtime(), s.getEndtime());
                        }
                        viewList.add(stat);
                        arrayAdapter.notifyDataSetChanged();
                    }
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            clickEvent(list.get(position), position);
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }
    private void clickEvent(final Statistic s, final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(StatModifyActivity.this);
        builder.setTitle(getResources().getString(R.string.stat_title));
        builder.setItems(new CharSequence[]{
                getResources().getString(R.string.stat_modify),
                getResources().getString(R.string.stat_delete),
                getResources().getString(R.string.close)}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0://수정
                        String statistic = new Gson().toJson(s);
                        startActivity(new Intent(StatModifyActivity.this,
                                StatInputActivity.class).putExtra("key", "modify").putExtra("stat", statistic));
                        StatModifyActivity.this.finish();
                        break;
                    case 1://삭제
                        try{
                            long id1 = statDBHelper.removeEvent1(s.getId());
                            long id2 = statDBHelper.removeEvent2(s.getId());
                            Log.i("Modify", "id1: " + id1 + ", id2: " + id2);
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.delete_error),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        viewList.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                        break;
                    case 2://닫기
                        dialog.cancel();
                        break;
                }
            }
        });
        builder.create().show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(StatModifyActivity.this, StatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(StatModifyActivity.this, StatActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
