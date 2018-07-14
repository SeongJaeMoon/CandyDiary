package goods.cap.app.goodsgoods.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.Adapter.StarPostAdapter;
import goods.cap.app.goodsgoods.Model.Firebase.Stars;
import goods.cap.app.goodsgoods.R;

public class TagActivity extends AppCompatActivity {
    @BindView(R.id.rvSearch)RecyclerView rvSearch;
    @BindView(R.id.my_toolbar)Toolbar toolbar;
    @BindView(R.id.mainTitle)TextView textView;
    private DatabaseReference userRef;
    private DatabaseReference dbRef;
    private String logger = TagActivity.class.getSimpleName();
    private int itemSize = 0;
    private ProgressDialog progressDialog;
    private StarPostAdapter starPostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        ButterKnife.bind(this);
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.keepSynced(true);
        userRef.keepSynced(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        rvSearch.setLayoutManager(layoutManager);
        starPostAdapter = new StarPostAdapter(getApplicationContext());
        starPostAdapter.setListener(new StarPostAdapter.ItemListener() {
            @Override
            public void onItemClick(Stars stars) {
                final Intent intent = new Intent(TagActivity.this, PostDtlActivity.class);
                if(stars.getUser() != null){
                    intent.putExtra("userName", stars.getUser());
                }else{
                    intent.putExtra("userName", "");
                }
                intent.putExtra("postKey", stars.getFkey());
                startActivity(intent);
                TagActivity.this.finish();
            }
        });
        rvSearch.setAdapter(starPostAdapter);

        //ComFragment 포스팅 tag 클릭시 넘어 오는 intent 값
        Intent intent = getIntent();
        String tag = intent.getStringExtra("tag");
        if(tag != null){
            Log.w(logger, "tag => " + tag);
            initSearchTag(tag);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void initSearchTag(final String searchKey) {
        showProgressDialog();

        final Query query = dbRef.child("posts").orderByChild("date");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    List<String> keyList = new ArrayList<String>();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot datas : dataSnapshot.getChildren()) {
                            Map<String, Object> tagMap = (HashMap<String, Object>) datas.child("tags").getValue();
                            if (tagMap != null) {
                                for (Object o : tagMap.values()) {
                                    String val = (String) o;
                                    if (val.equals(searchKey)) {
                                        Log.w(logger, "equal val => " + datas.getKey());
                                        keyList.add(datas.getKey());
                                    }
                                }
                            }
                        }
                        for(String key : keyList){
                            if(key != null){
                                final Stars stars = new Stars();
                                stars.setFkey(key);
                                dbRef.child("posts").child(key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            String uid = dataSnapshot.child("uid").getValue(String.class);
                                            userRef.child(uid).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    String userName = dataSnapshot.child("name").getValue(String.class);
                                                    String userProfileImage = dataSnapshot.child("profile_image").getValue(String.class);
                                                    stars.setUser(userName);
                                                    stars.setImg(userProfileImage);
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) { }
                                            });
                                            String title = dataSnapshot.child("title").getValue(String.class);
                                            Map<String, Object> tagMap = (HashMap<String, Object>)dataSnapshot.child("tags").getValue();
                                            List<String>tagList;
                                            if(tagMap != null){
                                                tagList = new ArrayList<String>(tagMap.size());
                                                for(Object o : tagMap.values()){
                                                    tagList.add(o.toString());
                                                }
                                                stars.setTags(tagList);
                                            }
                                            stars.setTitle(String.format("%s %s", "제목:", title));
                                            starPostAdapter.addAll(stars);
                                            textView.setText(String.format(Locale.KOREA, "총 %d%s", ++itemSize, getResources().getString(R.string.tag_title)));
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                                });
                            }
                        }
                    }
                    hideProgressDialog();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressDialog();
            }
        });
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(TagActivity.this);
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

    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onStop(){
        super.onStop();
        starPostAdapter.clear();
    }
}
