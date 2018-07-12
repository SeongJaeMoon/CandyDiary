package goods.cap.app.goodsgoods.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import goods.cap.app.goodsgoods.R;

public class PostImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image);

        try {
            Intent intent = getIntent();
            if (intent != null) {
                String imageUri = intent.getStringExtra("image_uri");

                ImageView ivImage = (ImageView) findViewById(R.id.detail_img);

                RequestOptions ro = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .format(DecodeFormat.PREFER_ARGB_8888);

                Glide.with(this)
                        .setDefaultRequestOptions(ro)
                        .load(imageUri)
                        .into(ivImage);
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_error), Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
