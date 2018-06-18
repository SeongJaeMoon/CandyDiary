package goods.cap.app.goodsgoods.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.R;

/* keyword search 화면, created by supermoon. */

public class UserProfileActivity extends AppCompatActivity {
    @BindView(R.id.mainImage)ImageView mainImage;
    @BindView(R.id.imageButton)ImageButton postImage;
    @BindView(R.id.imageButton2)ImageButton commentImage;
    @BindView(R.id.textView)TextView nameText;
    @BindView(R.id.textView2)TextView emailText;
    @BindView(R.id.textView3)TextView postsText;
    @BindView(R.id.textView4)TextView commentsText;
    @BindView(R.id.textView5)TextView statisticText;
    @BindView(R.id.my_toolbar)Toolbar toolbar;
    private static final String logger = UserProfileActivity.class.getSimpleName();
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private StorageReference stRef;
    private static final int PICK_IMAGE = 2;
    private Uri resultUri;
    private String uid;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pickImageIntent.setType("image/*");
                startActivityForResult(pickImageIntent, PICK_IMAGE);
            }
        });

        uid = auth.getCurrentUser().getUid();
        stRef = FirebaseStorage.getInstance().getReference();
        dbRef = FirebaseDatabase.getInstance().getReference().child("users");
        //DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("posts");
        DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("comments");
        commentRef.keepSynced(true);

        dbRef.keepSynced(true);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = auth.getCurrentUser();
                String name = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);
                String email = dataSnapshot.child(user.getUid()).child("email").getValue(String.class);
                String pimage = dataSnapshot.child(user.getUid()).child("profile_image").getValue(String.class);

                nameText.setText(name);
                emailText.setText(email);

                RequestOptions ro = new RequestOptions()
                        .placeholder(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.empty_user))
                        .error(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.empty_user));

                Glide.with(UserProfileActivity.this)
                        .setDefaultRequestOptions(ro)
                        .load(pimage)
                        .into(mainImage);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                RequestOptions ro = new RequestOptions()
                        .placeholder(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.empty_user))
                        .error(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.empty_user));

                Glide.with(UserProfileActivity.this)
                        .setDefaultRequestOptions(ro)
                        .load(resultUri)
                        .into(mainImage);

                progressDialog.setTitle(getResources().getString(R.string.upload_image_title));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                final StorageReference storageReference = stRef.child("profile_images").child(uid);
                storageReference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        try {
                            if (progressDialog.isShowing()) progressDialog.cancel();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    dbRef.child(uid).child("profile_image").setValue(uri.toString());
                                }
                            });
                        }catch (Exception e){
                            if (progressDialog.isShowing()) progressDialog.cancel();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.update_fail), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                        Toast.makeText(UserProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage((int) progress + getResources().getString(R.string.progress_image));
                    }
                });
            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.w(logger, error.toString());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.img_upload_error), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
