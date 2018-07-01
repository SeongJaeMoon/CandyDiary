package goods.cap.app.goodsgoods.Util;

import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class PostMainSlider extends SliderAdapter {

    private List<String>data;

    public PostMainSlider(List<String>data){
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        int len = data.size();
        if(len == 1) imageSlideViewHolder.bindImageSlide(data.get(0));
        else if(len == 2) {
            switch (position){
                case 0:imageSlideViewHolder.bindImageSlide(data.get(0));
                    break;
                case 1:imageSlideViewHolder.bindImageSlide(data.get(1));
                    break;
            }
        } else if(len == 3) {
            switch (position){
                case 0:imageSlideViewHolder.bindImageSlide(data.get(0));
                    break;
                case 1:imageSlideViewHolder.bindImageSlide(data.get(1));
                    break;
                case 2:imageSlideViewHolder.bindImageSlide(data.get(2));
                    break;
            }
        } else if(len == 4) {
            switch (position){
                case 0:imageSlideViewHolder.bindImageSlide(data.get(0));
                    break;
                case 1:imageSlideViewHolder.bindImageSlide(data.get(1));
                    break;
                case 2:imageSlideViewHolder.bindImageSlide(data.get(2));
                    break;
                case 3:imageSlideViewHolder.bindImageSlide(data.get(3));
                    break;
            }
        } else if (len == 5) {
            switch (position){
                case 0:imageSlideViewHolder.bindImageSlide(data.get(0));
                    break;
                case 1:imageSlideViewHolder.bindImageSlide(data.get(1));
                    break;
                case 2:imageSlideViewHolder.bindImageSlide(data.get(2));
                    break;
                case 3:imageSlideViewHolder.bindImageSlide(data.get(3));
                    break;
                case 4: imageSlideViewHolder.bindImageSlide(data.get(4));
                    break;
            }
        }
    }
}
