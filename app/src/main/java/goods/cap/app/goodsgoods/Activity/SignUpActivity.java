package goods.cap.app.goodsgoods.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goods.cap.app.goodsgoods.R;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.signUpName)EditText nameEdit;
    @BindView(R.id.signUpEmail)EditText emailEdit;
    @BindView(R.id.signUpPW)EditText pwEdit;
    @BindView(R.id.signUpPW2)EditText pwEdit2;
    @BindView(R.id.btnSignUp)Button signBtn;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference().child("users");

        progressDialog = new ProgressDialog(this);
    }

    @OnClick(R.id.btnSignUp)
    public void signUp() {
        final String name = nameEdit.getText().toString().trim();
        final String email = emailEdit.getText().toString().trim();
        final String pw = pwEdit.getText().toString().trim();
        final String pw2 = pwEdit2.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pw) || TextUtils.isEmpty(pw2)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_input_satisfy), Toast.LENGTH_SHORT).show();
        } else if (!pw.equals(pw2)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_match_pw), Toast.LENGTH_SHORT).show();
        } else {
            setProgressDialog();
            auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    try {
                        String uid = auth.getCurrentUser().getUid();
                        DatabaseReference userDbRef = dbRef.child(uid);
                        if (userDbRef.child("email").equals(email)) {
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.exist_email),Toast.LENGTH_LONG).show();
                            if (progressDialog.isShowing())progressDialog.cancel();
                        }else{
                            userDbRef.child("name").setValue(name);
                            userDbRef.child("email").setValue(email);
                            userDbRef.child("uid").setValue(uid);
                            if (progressDialog.isShowing())progressDialog.cancel();
                            Intent intent = new Intent(SignUpActivity.this, UserProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.sign_error), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void setProgressDialog(){
        progressDialog.setTitle(getResources().getString(R.string.data_refresh_title));
        progressDialog.setMessage(getResources().getString(R.string.data_refresh));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
