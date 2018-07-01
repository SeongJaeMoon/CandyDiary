package goods.cap.app.goodsgoods.Activity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import goods.cap.app.goodsgoods.R;

public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        this.setFinishOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_permission);

        View views = getWindow().getDecorView();
        views.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

        TextView notice = (TextView)findViewById(R.id.noticeText);
        TextView privacy = (TextView)findViewById(R.id.privacyText);
        Button okBtn = (Button)findViewById(R.id.okBtn);

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이용 약관 보기 연결
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //개인정보 처리 방침 연결
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //동의 -> Permission Granted
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.ok_permission), Toast.LENGTH_SHORT).show();
                PermissionActivity.this.finish();
            }
        });
    }
}
