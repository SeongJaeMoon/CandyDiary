package goods.cap.app.goodsgoods.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;

import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.R;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView splashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
    }
}
