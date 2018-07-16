package goods.cap.app.goodsgoods.Util;

import android.content.Context;
import android.net.Uri;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.io.File;

import goods.cap.app.goodsgoods.R;
import ss.com.bannerslider.ImageLoadingService;

public class PostImageLoader implements ImageLoadingService {
    private Context context;

    public PostImageLoader(Context context){
        this.context = context;
    }
    @Override
    public void loadImage(final String url, final ImageView imageView) {
        try {
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.get()
                            .load(url)
                            .placeholder(R.drawable.loading)
                            .error(R.drawable.none)
                            .resize(imageView.getMeasuredWidth(), imageView.getMeasuredHeight())
                            .onlyScaleDown()
                            .centerCrop()
                            .tag(context)
                            .into(imageView);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void loadImage(int resource, ImageView imageView) {
        try {
            Picasso.get()
                    .load(resource)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.none)
                    .fit()
                    .tag(context)
                    .into(imageView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
        try {
            Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.none)
                    .fit()
                    .tag(context)
                    .into(imageView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
