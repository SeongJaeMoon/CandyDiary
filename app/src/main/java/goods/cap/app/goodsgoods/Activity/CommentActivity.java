package goods.cap.app.goodsgoods.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.Adapter.CommentAdapter;
import goods.cap.app.goodsgoods.Model.Firebase.Comment;
import goods.cap.app.goodsgoods.R;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.list_comment)ListView commentList;
    @BindView(R.id.edit_comment)EditText commentEdit;
    @BindView(R.id.btn_send)Button commetnBtn;
    @BindView(R.id.data_miss)TextView datamiss;
    @BindView(R.id.my_toolbar)Toolbar toolbar;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm aa", Locale.KOREA);
    private static final String logger = CommentActivity.class.getSimpleName();
    private CommentAdapter commentAdapter;
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private ChildEventListener eventListener;
    private String cntntNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            cntntNo = intent.getStringExtra("cntntno");
            String fdnm = intent.getStringExtra("fdnm");
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            toolbar.setTitle(fdnm);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            commetnBtn.setOnClickListener(this);
            commentAdapter = new CommentAdapter(CommentActivity.this, 0);
            commentList.setAdapter(commentAdapter);
            commentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    return false;
                }
            });
            auth = FirebaseAuth.getInstance();
            datamiss.setText(getResources().getString(R.string.data_miss));
            initFirebaseDatabase();
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
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbRef.removeEventListener(eventListener);
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
        this.finish();
    }

    @Override
    public void onClick(View v) {
        final String cmt = commentEdit.getText().toString();
        commentEdit.setText("");
        if(!TextUtils.isEmpty(cmt)){
            final String uid = auth.getCurrentUser().getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Comment comment = new Comment();
                    comment.setComment(cmt);
                    comment.setName(dataSnapshot.child("name").getValue(String.class));
                    comment.setPimage(dataSnapshot.child("profile_image").getValue(String.class));
                    comment.setRegDate(sdf.format(new Date(System.currentTimeMillis())));
                    dbRef.push().setValue(comment);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            Toast.makeText(getBaseContext(), getResources().getString(R.string.edit_comment), Toast.LENGTH_SHORT).show();
        }
    }

    private void initFirebaseDatabase() {
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("comments").child(cntntNo);
        dbRef.keepSynced(true);
            eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    comment.setfKey(dataSnapshot.getKey());
                    commentAdapter.add(comment);
                    commentList.smoothScrollToPosition(commentAdapter.getCount());
                    if(datamiss.getVisibility() == View.VISIBLE) datamiss.setVisibility(View.GONE);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s){ }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
                Toast.makeText(getBaseContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
                Log.i(logger, "error => " + databaseError);
            }
        };
        dbRef.addChildEventListener(eventListener);
    }
    private void deletePopup(){

    }
}
