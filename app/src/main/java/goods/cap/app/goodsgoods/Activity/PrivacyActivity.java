package goods.cap.app.goodsgoods.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import goods.cap.app.goodsgoods.R;

public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        Intent intent = getIntent();
        if(intent != null) {
            try {
                TextView mainTitle = (TextView) findViewById(R.id.mainTitle);
                TextView mainText = (TextView) findViewById(R.id.mainText);
                //privacy
                if (intent.getIntExtra("privacy", 0) == 10) {
                    mainTitle.setText(getResources().getString(R.string.pref_terms));
                    mainText.setText(getResources().getString(R.string.tems_of_user).replace(" ", "\u00A0"));
                } else {
                    //open source
                    mainTitle.setText(getResources().getString(R.string.pref_opensource).replace(" ", "\u00A0"));
                    mainText.setText(getResources().getString(R.string.open_source_license).replace(" ", "\u00A0"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }
    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
