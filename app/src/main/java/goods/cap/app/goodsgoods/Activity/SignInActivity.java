package goods.cap.app.goodsgoods.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goods.cap.app.goodsgoods.MainActivity;
import goods.cap.app.goodsgoods.R;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.loginEmail)EditText emailEdit;
    @BindView(R.id.logInPassword)EditText pwEdit;
    @BindView(R.id.btnLogIn)Button signBtn;
    @BindView(R.id.btnLogInNewAccount)TextView newAccount;
    @BindView(R.id.btnGoogleSignIn)SignInButton googleBtn;
    @BindView(R.id.btnForgotAccount)TextView forgotAccount;
    private static final String logger = SignInActivity.class.getSimpleName();
    private static int GOOGLE_SIGN_IN = 5;
    private static int checkLimit;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference dbRef;
    private GoogleApiClient client;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        dbRef =  FirebaseDatabase.getInstance().getReference().child("users");
        progressDialog = new ProgressDialog(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        client = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.sign_error), Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    SignInActivity.this.finish();
                }
            }
        };

    }

    @OnClick(R.id.btnForgotAccount)
    public void forgotAccount(){
        Intent intent = new Intent(SignInActivity.this, ForgotActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnLogIn)
    public void login(){
        String email = emailEdit.getText().toString().trim();
        String pw = pwEdit.getText().toString().trim();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pw)){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_sign_data), Toast.LENGTH_SHORT).show();
        }else{
            setProgressDialog();
            auth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    try {
                        if (task.isSuccessful()) {
                            checkUserExist();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_sign_data), Toast.LENGTH_SHORT).show();
                    }
                    if(progressDialog.isShowing())progressDialog.cancel();
                }
            });
        }
    }

    private void checkUserExist(){
        try {
            final String uid = auth.getCurrentUser().getUid();
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(uid)){
                        if(progressDialog.isShowing())progressDialog.cancel();
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        SignInActivity.this.finish();
                    }else{
                        ++checkLimit;
                        if(checkLimit >= 5){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_match_over), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, ForgotActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_match), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.sign_error), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.sign_error),Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnLogInNewAccount)
    public void signUp(){
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }

    @Override
    protected void onStart(){
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    //< Google Sign Button Click >

    @OnClick(R.id.btnGoogleSignIn)
    public void googleLogin(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(client);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setProgressDialog();
        if(requestCode == GOOGLE_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                //Toast.makeText(SignInActivity.this,getResources().getString(R.string.google_signin),Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.sign_error), Toast.LENGTH_SHORT).show();
                Log.w(logger,"Google failed:"+ result +"," + result.getStatus() + "," + result.getSignInAccount());
            }
        }
        if(progressDialog.isShowing())progressDialog.cancel();
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, getResources().getString(R.string.sign_error), Toast.LENGTH_SHORT).show();
                        } else {
                            DatabaseReference userDbRef = dbRef.child(auth.getCurrentUser().getUid());
                            userDbRef.child("email").setValue(acct.getEmail());
                            userDbRef.child("name").setValue(acct.getDisplayName());
                            userDbRef.child("profile_image").setValue(acct.getPhotoUrl().toString());
                        }

                    }
                });
        progressDialog.cancel();
    }


    public void setProgressDialog(){
        progressDialog.setTitle(getResources().getString(R.string.data_refresh_title));
        progressDialog.setMessage(getResources().getString(R.string.data_refresh));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
