package goods.cap.app.goodsgoods.Util;

import android.content.Context;
import android.net.Uri;

import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class PostMainSlider extends SliderAdapter {

    private Context context;
    private List<String>data;

    public PostMainSlider(Context context, List<String>data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        int len = data.size();
        for (int i = 0; i < len; ++i) {
            imageSlideViewHolder.bindImageSlide(data.get(i));
        }
    }
}
