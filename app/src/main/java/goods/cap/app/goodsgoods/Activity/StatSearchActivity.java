package goods.cap.app.goodsgoods.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.Model.Firebase.Calorie;
import goods.cap.app.goodsgoods.R;

public class StatSearchActivity extends AppCompatActivity {

    @BindView(R.id.search)SearchView searchView;
    @BindView(R.id.list_view)ListView listView;
    private ProgressDialog progressDialog;
    private List<String> searchList = new ArrayList<String>();
    private List<Calorie>calorieList = new ArrayList<Calorie>();
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference calRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        this.setFinishOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_stat_search);
        ButterKnife.bind(this);

        View views = getWindow().getDecorView();
        views.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

        calRef = FirebaseDatabase.getInstance().getReference().child("calories");
        calRef.keepSynced(true);

        setSearchView();
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, searchList);
        listView.setAdapter(arrayAdapter);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searhing(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchList.clear();
                arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(RESULT_OK, new Intent().putExtra("search", searchList.get(position)).putExtra("calorie", new Gson().toJson(calorieList.get(position))));
                StatSearchActivity.this.finish();
            }
        });
    }

    private void searhing(final String text){
        showProgressDialog();
        calRef.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        String name = datas.child("name").getValue(String.class);
                        if(name != null) {
                            try {
                                if (name.contains(text)) {
                                    Calorie calorie = new Calorie();
                                    calorie.setCateorgy(datas.child("category").getValue(String.class));
                                    calorie.setCho_mg(datas.child("chol_mg").getValue());
                                    calorie.setDan_g(datas.child("dan_g").getValue());
                                    calorie.setDang_g(datas.child("dang_g").getValue());
                                    calorie.setJi_g(datas.child("ji_g").getValue());
                                    calorie.setNa_mg(datas.child("na_mg").getValue());
                                    calorie.setPho_g(datas.child("pho_mg").getValue());
                                    calorie.setTan_g(datas.child("tan_g").getValue());
                                    calorie.setTrans_g(datas.child("trans_g").getValue());
                                    double kal = datas.child("kal").getValue(Double.class);
                                    calorie.setKal(kal);
                                    calorieList.add(calorie);
                                    String result = String.format(Locale.KOREA, "%s(%.2fkal)", name, kal);
                                    searchList.add(result);
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
             hideProgressDialog();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressDialog();
            }
        });
    }
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(StatSearchActivity.this);
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

    private void setSearchView(){
        searchView.setActivated(true);
        searchView.setQueryHint(getResources().getString(R.string.hint_search));
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
