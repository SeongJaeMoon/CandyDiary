package goods.cap.app.goodsgoods.Activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goods.cap.app.goodsgoods.MainActivity;
import goods.cap.app.goodsgoods.R;
import me.gujun.android.taggroup.TagGroup;

public class PostActivity extends AppCompatActivity implements TagGroup.OnTagClickListener{

    @BindView(R.id.my_toolbar)Toolbar toolbar;
    @BindView(R.id.add_img1)ImageView addImg1;
    @BindView(R.id.add_img2)ImageView addImg2;
    @BindView(R.id.add_img3)ImageView addImg3;
    @BindView(R.id.add_img4)ImageView addImg4;
    @BindView(R.id.add_img5)ImageView addImg5;
    @BindView(R.id.etTitle)EditText etTitle;
    @BindView(R.id.etDesc)EditText etDesc;
    @BindView(R.id.tag_group)TagGroup tagGroup;
    //@BindView(R.id.btn_add)TextView picAdd;
    //@BindView(R.id.btnPost)Button posting;

    private static final String logger = PostActivity.class.getSimpleName();
    private DatabaseReference dbRef;
    private StorageReference stRef;
    private ProgressDialog progressDialog;
    private static final int PICK_CAMERA = 1, PICK_IMAGE = 2;
    private Uri resultUri;
    private ArrayList<Uri> imgList = new ArrayList<Uri>();
    private String uid;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss", Locale.KOREA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle(getResources().getString(R.string.post_toolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        uid = auth.getCurrentUser().getUid();
        stRef = FirebaseStorage.getInstance().getReference();
        dbRef = FirebaseDatabase.getInstance().getReference().child("posts");

        tagGroup.setTags(getResources().getString(R.string.post_hashtag),
                getResources().getString(R.string.post_hashtag),
                getResources().getString(R.string.post_hashtag),
                getResources().getString(R.string.post_hashtag),
                getResources().getString(R.string.post_hashtag));

        tagGroup.setOnTagClickListener(this);
    }

    @OnClick(R.id.btn_add)
    void click(){
        if(imgList.size() == 5){
           Toast.makeText(getApplicationContext(), getResources().getString(R.string.choose_pic), Toast.LENGTH_SHORT).show();
        }else{
            final AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
            builder.setTitle(getResources().getString(R.string.choose_cam_title));
            builder.setItems(new CharSequence[]{getResources().getString(R.string.choose_cam1),
                    getResources().getString(R.string.choose_cam2),
                    getResources().getString(R.string.close)}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            picGallery();
                            break;
                        case 1:
                            picTake();
                            break;
                        case 2:
                            dialog.cancel();
                            break;
                    }
                }
            });
            builder.create().show();
        }
    }
    //갤러리에서 선택
    private void picGallery(){
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(pickImageIntent, getResources().getString(R.string.choose_pic)), PICK_IMAGE);
    }
    //사진 촬영
    private void picTake(){

        Intent pickcamIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String uri = "goods_" + sdf.format(new Date(System.currentTimeMillis())) + ".jpg";
        resultUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), uri));
        pickcamIntent.putExtra(MediaStore.EXTRA_OUTPUT, resultUri);
        startActivityForResult(pickcamIntent, PICK_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE){
            if(resultCode != RESULT_OK){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.choose_error), Toast.LENGTH_SHORT).show();
                return;
            }
            if(data.getClipData() == null){
                imgList.add(data.getData());
            }else{
                ClipData clipData = data.getClipData();
                int len = clipData.getItemCount();
                if(len > 5){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.choose_pic),Toast.LENGTH_SHORT).show();
                    return;
                }else if(len == 1){
                    imgList.add(clipData.getItemAt(0).getUri());
                }else if(len > 1 && len < 5){
                    for(int i = 0; i < len; ++i){
                        imgList.add(clipData.getItemAt(i).getUri());
                    }
                }
            }
            int size = imgList.size();
            for(int i = 0; i < size; ++i){
                if(i == 0)addImg1.setImageURI(imgList.get(0));
                else if(i == 1)addImg2.setImageURI(imgList.get(1));
                else if(i == 2)addImg3.setImageURI(imgList.get(2));
                else if(i == 3)addImg4.setImageURI(imgList.get(3));
                else if(i == 4)addImg5.setImageURI(imgList.get(4));
            }
        }
        if (requestCode == PICK_CAMERA){
            if (resultCode != RESULT_OK){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.choose_error), Toast.LENGTH_SHORT).show();
                return;
            }
            //String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/goods/" + sdf.format(new Date(System.currentTimeMillis())) + ".jpg";
            int size = imgList.size();
            imgList.set(size - 1, resultUri);
            switch (size){
                case 1:addImg2.setImageURI(resultUri);
                    break;
                case 2:addImg3.setImageURI(resultUri);
                    break;
                case 3:addImg4.setImageURI(resultUri);
                    break;
                case 4:addImg5.setImageURI(resultUri);
                    break;
            }
        }
    }

    @OnClick(R.id.btnPost)
    void postingClick(){
        String title = etTitle.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        String[] tags = tagGroup.getTags();
        if(TextUtils.isEmpty(title)){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.need_title), Toast.LENGTH_SHORT).show();
        }else{
            final DatabaseReference pushRef = dbRef.push();
            //DB 저장
            pushRef.child("title").setValue(title);
            pushRef.child("desc").setValue(desc);
            pushRef.child("date").setValue(sdf.format(new Date(System.currentTimeMillis())));
            pushRef.child("uid").setValue(uid);
            Map<String, Object> tagUpdates = new HashMap<>();
            //HashTag 저장
            int tagLen = tags.length;
            if(tagLen != 0){
                for(int i = 0; i < tagLen; ++i){
                    if(!tags[i].equals("") && !tags[i].equals(getResources().getString(R.string.post_hashtag))){
                        tagUpdates.put(String.valueOf(i), tags[i]);
                    }
                }
                pushRef.child("tags").updateChildren(tagUpdates);
            }
            final int size = imgList.size();
            if(size > 0){
                try {
                    final Map<String, Object> picUpdates = new HashMap<>();
                    for (int i = 0; i < size; ++i) {
                        progressDialog.setTitle(getResources().getString(R.string.upload_post_title));
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        final StorageReference dataRef = stRef.child("goods_posts").child(imgList.get(i).getLastPathSegment());
                        dataRef.putFile(imgList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                try {
                                    if(progressDialog.isShowing()) progressDialog.cancel();
                                    dataRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            picUpdates.put(pushRef.getKey(), uri.toString());
                                            pushRef.updateChildren(picUpdates);
                                        }
                                    });
                                } catch (Exception e) {
                                    if(progressDialog.isShowing()) progressDialog.cancel();
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_post_fail), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if(progressDialog.isShowing()) progressDialog.cancel();
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_post_fail), Toast.LENGTH_SHORT).show();
                                Log.w("stFailure => ", e.getMessage());
                                e.printStackTrace();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage((int) progress + getResources().getString(R.string.progress_image));
                            }
                        });
                    }
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_post_success), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_post_fail), Toast.LENGTH_SHORT).show();
                    Log.w("stError => ", e.getMessage());
                    e.printStackTrace();
                }
            }
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
    protected void onDestroy() { super.onDestroy(); }
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
    public void onBackPressed() { super.onBackPressed(); this.finish();}

    @Override
    public void onTagClick(String tag) {

    }
}
