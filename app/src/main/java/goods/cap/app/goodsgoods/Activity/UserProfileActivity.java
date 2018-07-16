package goods.cap.app.goodsgoods.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import goods.cap.app.goodsgoods.Adapter.StarPostAdapter;
import goods.cap.app.goodsgoods.Model.Firebase.Post;
import goods.cap.app.goodsgoods.Model.Firebase.Stars;
import goods.cap.app.goodsgoods.R;
import goods.cap.app.goodsgoods.Util.PostImageLoader;
import goods.cap.app.goodsgoods.Util.PostMainSlider;
import me.gujun.android.taggroup.TagGroup;
import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideClickListener;

/* keyword search 화면, created by supermoon. */

public class UserProfileActivity extends AppCompatActivity {
    @BindView(R.id.mainImage)ImageView mainImage;
    @BindView(R.id.imageButton)ImageButton postImage;
    @BindView(R.id.imageButton2)ImageButton commentImage;
    @BindView(R.id.textView)TextView nameText;
    @BindView(R.id.textView2)TextView emailText;
    @BindView(R.id.textView3)TextView starsText;//즐겨찾기 목록
    @BindView(R.id.textView4)TextView postsText;//포스팅 목록
//    @BindView(R.id.textView5)TextView statisticText;
    @BindView(R.id.my_toolbar)Toolbar toolbar;
    @BindView(R.id.postTitle)TextView postTitle;
    @BindView(R.id.post_list)RecyclerView postList;
    @BindView(R.id.secession)TextView secession; //회원탈퇴
    private static final String logger = UserProfileActivity.class.getSimpleName();
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private StorageReference stRef;
    private static final int PICK_IMAGE = 2;
    private Uri resultUri;
    private String uid, userName, userImg;
    private ProgressDialog progressDialog;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm aa", Locale.KOREA);
    private FirebaseRecyclerAdapter<Post, UserProfileActivity.PostViewHolder>firebaseRecyclerAdapter;
    private boolean likeProcess = false;
    private BottomSheetDialog bottomSheetDialog;
    private List<Stars>starsList;
    private DatabaseReference postRef;
    private DatabaseReference starRef;
    private DatabaseReference likeRef;
//    private String token;
//    private AuthCredential credential;
    private FirebaseAuth.AuthStateListener authListener;

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

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(UserProfileActivity.this, SignInActivity.class));
                    finish();
                }
            }
        };

        uid = auth.getCurrentUser().getUid();
        stRef = FirebaseStorage.getInstance().getReference();
        dbRef = FirebaseDatabase.getInstance().getReference().child("users");
        postRef = FirebaseDatabase.getInstance().getReference().child("posts");
        starRef = FirebaseDatabase.getInstance().getReference().child("stars");
        likeRef = FirebaseDatabase.getInstance().getReference().child("likes");
        likeRef.keepSynced(true);
        postRef.keepSynced(true);
        starRef.keepSynced(true);
        dbRef.keepSynced(true);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = auth.getCurrentUser();
                final String name = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);
                final String email = dataSnapshot.child(user.getUid()).child("email").getValue(String.class);
                final String pimage = dataSnapshot.child(user.getUid()).child("profile_image").getValue(String.class);

                userName = name;
                userImg = pimage;
                nameText.setText(name);
                emailText.setText(email);

                final RequestOptions ro = new RequestOptions()
                        .placeholder(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.empty_user))
                        .error(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.empty_user));

                mainImage.post(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(UserProfileActivity.this)
                                .setDefaultRequestOptions(ro)
                                .load(pimage)
                                .into(mainImage);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        Query postsQuery = postRef.orderByChild("uid").equalTo(uid);
        postsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long postsCount = dataSnapshot.getChildrenCount();
                postsText.setText(String.format(Locale.KOREA, "%s%d", getResources().getString(R.string.my_post), postsCount));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        Query starsQuery = starRef.orderByKey();
        starsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long starsCount = 0;
                List<String>keyList = new ArrayList<String>();
                try {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if (data.exists()) {
                            Map<String, Object> keyMap = (HashMap<String, Object>)data.getValue();
                            for (String s : keyMap.keySet()) {
                                if (s.equals(uid)) {
                                    String key = data.getKey();
                                    if(key != null){
                                        keyList.add(key);
                                    }
                                    starsCount += 1;
                                }
                            }
                        }
                    }
                    starsList = new ArrayList<>(keyList.size());
                    for(String key : keyList){
                        if(key != null){
                            final Stars stars = new Stars();
                            stars.setFkey(key);
                            postRef.child(key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        String uid = dataSnapshot.child("uid").getValue(String.class);
                                        dbRef.child(uid).addValueEventListener(new ValueEventListener() {
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
                                        starsList.add(stars);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) { }
                            });
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
               starsText.setText(String.format(Locale.KOREA,"%s%d", getResources().getString(R.string.star_post), starsCount));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        postList.setLayoutManager(layoutManager);
        starsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(starsList.size() != 0){
                    bottomDialog();
                }
            }
        });
        Slider.init(new PostImageLoader(this));
        //식단 관리 클릭
//        statisticText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(UserProfileActivity.this, StatActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//            }
//        });
    }
    @OnClick(R.id.textView)
    void nameChange(){
        AlertDialog.Builder ad = new AlertDialog.Builder(UserProfileActivity.this);
        ad.setTitle(getResources().getString(R.string.noti_name));
        final EditText et = new EditText(UserProfileActivity.this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        ad.setView(et);
        ad.setPositiveButton(getResources().getString(R.string.success), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = et.getText().toString();
                if(TextUtils.isEmpty(value) || value.length() < 2){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.too_short),Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference userDbRef = dbRef.child(uid);
                    userDbRef.child("name").setValue(value);
                }
                dialog.dismiss();
            }
        });
        ad.setNegativeButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
    }
    @OnClick(R.id.secession)
    void secessionUser() {
        if (auth != null) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserProfileActivity.this);
            alertDialog.setTitle(getResources().getString(R.string.secession_confirm));
            alertDialog.setMessage(getResources().getString(R.string.secession_delete));
            alertDialog.setNegativeButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // get Token
                    if(auth.getCurrentUser() != null) {
                        final FirebaseUser mFirebaseUser = auth.getCurrentUser();
//                        mFirebaseUser.getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<GetTokenResult> task) {
//                                if (task.isSuccessful()) {
//                                    Log.e(logger, "getToken Task is successful");
//                                    token = task.getResult().getToken();
//                                }
//                            }
//                        });
//                        if (mFirebaseUser.getProviders() != null && mFirebaseUser.getProviders().get(0).equals("google.com")) {
//                            credential = GoogleAuthProvider.getCredential(token, null);
//                        }
                        try {
                            mFirebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    final String uid = mFirebaseUser.getUid();
                                    // 사용자 삭제
                                    mFirebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            try {
                                                if (task.isSuccessful()) {
                                                    // Database 삭제
                                                    Log.e(logger, "Uid" + uid);
                                                    final DatabaseReference comRef = FirebaseDatabase.getInstance().getReference().child("comments");
                                                    comRef.orderByKey().addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                                                    if (datas.getKey() != null) {
                                                                        if (datas.getKey().equals(uid)) {
                                                                            comRef.child(uid).removeValue();
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        }
                                                    });
                                                    postRef.orderByKey().addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                                                    if (datas.getKey() != null) {
                                                                        if (datas.getKey().equals(uid)) {
                                                                            postRef.child(uid).removeValue();
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        }
                                                    });
                                                    starRef.orderByKey().addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                                                    if (datas.getKey() != null) {
                                                                        if (datas.getKey().equals(uid)) {
                                                                            starRef.child(uid).removeValue();
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        }
                                                    });
                                                    likeRef.orderByKey().addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                                                    if (datas.getKey() != null) {
                                                                        if (datas.getKey().equals(uid)) {
                                                                            likeRef.child(uid).removeValue();
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        }
                                                    });
                                                    dbRef.child(uid).removeValue();
//                                                dbRef.child(uid).child("email").removeValue();
//                                                dbRef.child(uid).child("name").removeValue();
//                                                dbRef.child(uid).child("profile_image").removeValue();
//                                                Map<String, Object> recordUpdate = new HashMap<>();
//                                                recordUpdate.put(uid, null);
//                                                dbRef.updateChildren(recordUpdate);
                                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.secession_delete_ok), Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(getApplicationContext(), SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                    UserProfileActivity.this.finish();
                                                }else{
                                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.secession_delete_ok), Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(getApplicationContext(), SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                    UserProfileActivity.this.finish();
                                                }
                                            } catch (Exception e) {
                                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            });
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        //users, comments, likes, posts, stars
                    }else{
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alertDialog.create().show();
        }
    }
//    @OnClick(R.id.textView2)
//    void emailChange(){
//       AlertDialog.Builder ad = new AlertDialog.Builder(UserProfileActivity.this);
//       ad.setTitle(getResources().getString(R.string.noti_email));
//       final EditText et = new EditText(UserProfileActivity.this);
//       et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//       ad.setView(et);
//       ad.setPositiveButton(getResources().getString(R.string.success), new DialogInterface.OnClickListener() {
//           @Override
//           public void onClick(DialogInterface dialog, int which) {
//               String value = et.getText().toString();
//               if(TextUtils.isEmpty(value) || !isValidEmail(value)){
//                   Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_validation),Toast.LENGTH_SHORT).show();
//               }else{
//                   DatabaseReference userDbRef = dbRef.child(uid);
//                   userDbRef.child("email").setValue(value);
//               }
//               dialog.dismiss();
//           }
//       });
//       ad.setNegativeButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
//           @Override
//           public void onClick(DialogInterface dialog, int which) {
//               dialog.dismiss();
//           }
//       });
//       ad.show();
//   }
//    private boolean isValidEmail(String email) {
//        boolean err = false;
//        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
//        Pattern p = Pattern.compile(regex);
//        Matcher m = p.matcher(email);
//        if(m.matches()) {
//            err = true;
//        }
//        return err;
//    }

    @OnClick(R.id.pwChange)
    void pwChange(){
        AlertDialog.Builder ad = new AlertDialog.Builder(UserProfileActivity.this);
        ad.setTitle(getResources().getString(R.string.noti_pw));
        final EditText et = new EditText(UserProfileActivity.this);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        ad.setView(et);
        ad.setPositiveButton(getResources().getString(R.string.success), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = et.getText().toString();
                if(TextUtils.isEmpty(value) || value.length() < 6) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.pw_short), Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(emailText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.resetPwEmail), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.resetPwEmailFail), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                dialog.dismiss();
            }
        });
        ad.setNegativeButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
    }
    @Override
    protected void onStart(){
        super.onStart();
        initFirebase();
        firebaseRecyclerAdapter.startListening();
        auth.addAuthStateListener(authListener);

    }
    @Override
    protected void onStop(){
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
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

    private void initFirebase() {
        Query postsQuery = postRef.orderByChild("uid").equalTo(uid);

        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postsQuery, Post.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, UserProfileActivity.PostViewHolder>(options) {
            @NonNull
            @Override
            public UserProfileActivity.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_cardview, parent, false);
                return new UserProfileActivity.PostViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final UserProfileActivity.PostViewHolder holder, final int position, @NonNull final Post model) {
                final String postKey = getRef(position).getKey();
                holder.setContext(getApplicationContext());
                DatabaseReference dbRef = postRef.child(postKey);
                dbRef.keepSynced(true);
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            Map<String, Object> imgMap = model.getImages();
                            Map<String, Object> tagMap = model.getTags();
                            String name = "";
                            if(imgMap != null){
                                List<String> tempList = new ArrayList<String>(imgMap.size());
                                for(Object o : imgMap.values()){
                                    tempList.add(o.toString());
                                }
                                PostMainSlider postMainSlider = new PostMainSlider(tempList);
                                if (holder.userNameFeeds.getText() != null) {
                                    name = holder.userNameFeeds.getText().toString();
                                }
                                holder.setPostImages(postMainSlider, name, postKey);
                            }else{
                                holder.setPostImages(null, name, postKey);
                            }
                            if(tagMap != null){
                                List<String>tempList = new ArrayList<String>(tagMap.size());
                                for(Object o : tagMap.values()){
                                    tempList.add(o.toString());
                                }
                                holder.setTags(tempList);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
                holder.setUserImage(getApplicationContext(), userImg);
                holder.setUserName(userName);
                holder.setTitle(model.getTitle());
                holder.setPostDate(model.getDate());
                holder.setLike(postKey);
                holder.likeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        likeProcess = true;
                        likeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (likeProcess) {
                                    if (dataSnapshot.child(postKey).hasChild(auth.getCurrentUser().getUid())) {
                                        holder.likeBtn.setImageResource(R.drawable.like_grey);
                                        likeRef.child(postKey).child(auth.getCurrentUser().getUid()).removeValue();
                                    } else {
                                        holder.likeBtn.setImageResource(R.drawable.like_orange);
                                        likeRef.child(postKey).child(auth.getCurrentUser().getUid()).setValue(sdf.format(new Date(System.currentTimeMillis())));
                                    }
                                    likeProcess = false;
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                holder.userNameFeeds.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                holder.userImgFeeds.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        };
        postList.setAdapter(firebaseRecyclerAdapter);
    }

    private void bottomDialog(){
        bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new StarPostAdapter(starsList, getApplicationContext(), new StarPostAdapter.ItemListener() {
            @Override
            public void onItemClick(Stars stars) {
                if (bottomSheetDialog != null) {
                    bottomSheetDialog.dismiss();
                }
                final Intent intent = new Intent(UserProfileActivity.this, PostDtlActivity.class);
                if(stars.getUser() != null){
                    intent.putExtra("userName", stars.getUser());
                }else{
                    intent.putExtra("userName", "");
                }
                intent.putExtra("postKey", stars.getFkey());
                startActivity(intent);
            }
        }));
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialog = null;
            }
        });
    }

    private static class PostViewHolder extends RecyclerView.ViewHolder{
        private View view;
        public CircleImageView userImgFeeds;
        public TextView userNameFeeds;
        private TextView userDateFeeds;
        public ImageView likeBtn;
        private TextView likeCount;
        private DatabaseReference likeRef;
        private FirebaseAuth auth;
        private Context context;

        private PostViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            likeRef = FirebaseDatabase.getInstance().getReference().child("likes");
            likeRef.keepSynced(true);
            auth = FirebaseAuth.getInstance();
            userImgFeeds = (CircleImageView)view.findViewById(R.id.userImgFeeds);
            userNameFeeds = (TextView)view.findViewById(R.id.userNameFeeds);
            userDateFeeds = (TextView)view.findViewById(R.id.userDateFeeds);
            likeBtn = (ImageView)view.findViewById(R.id.likeBtn);
            likeCount = (TextView)view.findViewById(R.id.likeCount);
        }
        public void setContext(Context context){
            this.context = context;
        }
        public void setTitle(String title) {
            TextView tvTitle = (TextView) view.findViewById(R.id.title);
            tvTitle.setText(title);
        }
        public void setPostImages(final PostMainSlider postAdapter, final String name, final String postKey) {
            final Slider slider = view.findViewById(R.id.banner_slider);
            if (postAdapter == null) {
                slider.setVisibility(View.GONE);
            } else {
                slider.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        slider.setAdapter(postAdapter);
                        slider.setSelectedSlide(0);
                        slider.setOnSlideClickListener(new OnSlideClickListener() {
                            @Override
                            public void onSlideClick(int position) {
                                Intent intent = new Intent(context.getApplicationContext(), PostDtlActivity.class);
                                if (name != null) {
                                    intent.putExtra("userName", name);
                                }
                                intent.putExtra("postKey", postKey);
                                context.startActivity(intent);
                            }
                        });
                    }
                }, 1000);
            }
        }
        public void setTags(List<String> tags){
            TagGroup tagGroup = (TagGroup)view.findViewById(R.id.tag_group);
            tagGroup.setTags(tags);
        }
        public void setUserImage(Context context, String url){
            RequestOptions ro = new RequestOptions()
                    .placeholder(ContextCompat.getDrawable(context, R.mipmap.empty_user))
                    .error(ContextCompat.getDrawable(context, R.mipmap.empty_user));
            Glide.with(context)
                    .setDefaultRequestOptions(ro)
                    .load(url)
                    .into(userImgFeeds);
        }
        public void setUserName(String userName){
            userNameFeeds.setText(userName);
        }
        public void setPostDate(String date){
            userDateFeeds.setText(date);
        }
        public void setLike(final String postKey){
            likeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(postKey).hasChild(auth.getCurrentUser().getUid())) {
                        likeBtn.setImageResource(R.drawable.like_orange);
                        likeCount.setTextColor(Color.rgb(252, 176, 48));
                    } else {
                        likeCount.setTextColor(Color.rgb(127, 127, 127));
                        likeBtn.setImageResource(R.drawable.like_grey);
                    }
                    long likes = dataSnapshot.child(postKey).getChildrenCount();
                    if(likes == 0){
                        likeCount.setVisibility(View.GONE);
                    }else{
                        likeCount.setVisibility(View.VISIBLE);
                        likeCount.setText(String.valueOf(likes));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(logger, databaseError.getMessage());
                }
            });
        }
    }

}
