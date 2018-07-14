package goods.cap.app.goodsgoods.Activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goods.cap.app.goodsgoods.R;

public class PostActivity extends AppCompatActivity{
    @BindView(R.id.my_toolbar)Toolbar toolbar;
    @BindView(R.id.add_img1)ImageView addImg1;
    @BindView(R.id.add_img2)ImageView addImg2;
    @BindView(R.id.add_img3)ImageView addImg3;
    @BindView(R.id.add_img4)ImageView addImg4;
    @BindView(R.id.add_img5)ImageView addImg5;
    @BindView(R.id.etTitle)EditText etTitle;
    @BindView(R.id.etDesc)EditText etDesc;

    private static final String logger = PostActivity.class.getSimpleName();
    private DatabaseReference dbRef;
    private StorageReference stRef;
    private ProgressDialog progressDialog;
    private static final int PICK_CAMERA = 1, PICK_IMAGE = 2;
    private Uri resultUri;
    private String imagePath;
    private ArrayList<Uri> imgList = new ArrayList<Uri>();
    private String uid;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA);
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.KOREA);
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
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
            Log.w(logger, ex.getMessage());
            ex.printStackTrace();
        }
        if(photoFile!=null) {
            resultUri = FileProvider.getUriForFile(getApplicationContext(), "goods.cap.app.goodsgoods", photoFile);
            pickcamIntent.putExtra(MediaStore.EXTRA_OUTPUT, resultUri);
            startActivityForResult(pickcamIntent, PICK_CAMERA);
        }
    }

    @OnClick(R.id.add_img1)
    void clickOne(){
        try {
            if (imgList != null) {
                if(imgList.size() > 0 && imgList.get(0)!=null){
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PostActivity.this);
                    alertDialog.setMessage(getResources().getString(R.string.retry_photo));
                    alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(addImg1 != null){
                                addImg1.setImageResource(R.drawable.ic_action_add);
                                imgList.set(0, null);
                            }

                        }
                    });
                    alertDialog.create().show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @OnClick(R.id.add_img2)
    void clickTwo(){
        try {
            if (imgList != null) {
                if(imgList.size() > 1 && imgList.get(1)!=null){
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PostActivity.this);
                    alertDialog.setMessage(getResources().getString(R.string.retry_photo));
                    alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(addImg2 != null){
                                addImg2.setImageResource(R.drawable.ic_action_add);
                                imgList.set(1, null);
                            }

                        }
                    });
                    alertDialog.create().show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @OnClick(R.id.add_img3)
    void clickThree(){
        try {
            if (imgList != null) {
                if(imgList.size() > 2 && imgList.get(2)!=null){
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PostActivity.this);
                    alertDialog.setMessage(getResources().getString(R.string.retry_photo));
                    alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(addImg3 != null){
                                addImg3.setImageResource(R.drawable.ic_action_add);
                                imgList.set(2, null);
                            }

                        }
                    });
                    alertDialog.create().show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @OnClick(R.id.add_img4)
    void clickFour(){
        try {
            if (imgList != null) {
                if(imgList.size() > 3 && imgList.get(3)!=null){
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PostActivity.this);
                    alertDialog.setMessage(getResources().getString(R.string.retry_photo));
                    alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(addImg4 != null){
                                addImg4.setImageResource(R.drawable.ic_action_add);
                                imgList.set(3, null);
                            }

                        }
                    });
                    alertDialog.create().show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @OnClick(R.id.add_img5)
    void clickFive(){
        try {
            if (imgList != null) {
                if(imgList.size() > 4 && imgList.get(4)!=null){
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PostActivity.this);
                    alertDialog.setMessage(getResources().getString(R.string.retry_photo));
                    alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(addImg5!= null){
                                addImg5.setImageResource(R.drawable.ic_action_add);
                                imgList.set(4, null);
                            }

                        }
                    });
                    alertDialog.create().show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
            if(size == 5){
                addImg1.setImageURI(imgList.get(size - 1));
            }else {
                for (int i = 0; i < size; ++i) {
                    if (i == 0) addImg1.setImageURI(imgList.get(0));
                    else if (i == 1) addImg2.setImageURI(imgList.get(1));
                    else if (i == 2) addImg3.setImageURI(imgList.get(2));
                    else if (i == 3) addImg4.setImageURI(imgList.get(3));
                    else if (i == 4) addImg5.setImageURI(imgList.get(4));
                }
            }
        }
        if (requestCode == PICK_CAMERA){
            if (resultCode != RESULT_OK){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.choose_error), Toast.LENGTH_SHORT).show();
                return;
            }
            int size = imgList.size();
            switch (size){
                case 0: //rotationPic(resultUri.toString(), addImg1);
                    addImg1.setImageURI(resultUri);
                    break;
                case 1: //rotationPic(resultUri.toString(), addImg2);
                    addImg2.setImageURI(resultUri);
                    break;
                case 2: //rotationPic(resultUri.toString(), addImg3);
                    addImg3.setImageURI(resultUri);
                    break;
                case 3: //rotationPic(resultUri.toString(), addImg4);
                    addImg4.setImageURI(resultUri);
                    break;
                case 4: //rotationPic(resultUri.toString(), addImg5);
                    addImg5.setImageURI(resultUri);
                    break;
            }
            imgList.add(resultUri);
        }
    }

    private void rotationPic(String imageFilePath, ImageView imageView){
        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imageFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation;
        int exifDegree;
        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        } else {
            exifDegree = 0;
        }
        imageView.setImageBitmap(rotate(bitmap, exifDegree));
    }

    private File createImageFile() throws IOException {
        String imageFileName = "goods_" + sdf.format(new Date(System.currentTimeMillis())) + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imagePath = image.getAbsolutePath();
        return image;
    }
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
    private void postingClick(){
        String title = etTitle.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        Pattern p = Pattern.compile("\\#([0-9a-zA-Z가-힣]*)");
        Matcher m = p.matcher(desc);
        List<String> tags = new ArrayList<>();
        while (m.find()){
            tags.add(m.group());
        }
        if(TextUtils.isEmpty(title)){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.need_title), Toast.LENGTH_SHORT).show();
        }else if(imgList == null || imgList.size() == 0){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.need_photo), Toast.LENGTH_SHORT).show();
        }else{
            final DatabaseReference pushRef = dbRef.push();
            //DB 저장
            pushRef.child("title").setValue(title);
            pushRef.child("desc").setValue(desc);
            pushRef.child("date").setValue(sdf2.format(new Date(System.currentTimeMillis())));
            pushRef.child("uid").setValue(uid);
            Map<String, Object> tagUpdates = new HashMap<String, Object>();
            //HashTag 저장
            int tagLen = tags.size();
            if(tagLen != 0){
                for(String tag : tags){
                    if(!tag.equals("") && !tag.equals(getResources().getString(R.string.post_hashtag))){
                        tagUpdates.put(UUID.randomUUID().toString(), tag);
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
                                            picUpdates.put(UUID.randomUUID().toString(), uri.toString());
                                            pushRef.child("images").updateChildren(picUpdates);
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

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_post_fail), Toast.LENGTH_SHORT).show();
                    Log.w("stError => ", e.getMessage());
                    e.printStackTrace();
                }
            }
            if(progressDialog.isShowing()) progressDialog.cancel();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_post_success), Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.action_add){
            postingClick();
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
}
