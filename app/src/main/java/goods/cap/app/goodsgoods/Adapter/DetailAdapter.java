package goods.cap.app.goodsgoods.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.net.URL;
import java.util.List;
import java.util.Locale;

import goods.cap.app.goodsgoods.Model.Food;
import goods.cap.app.goodsgoods.R;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder>{

    private static final String logger = DetailAdapter.class.getSimpleName();
    private List<Food> data;
    private Context context; //연결할 화면 context

    static class ViewHolder extends RecyclerView.ViewHolder {

        //private ImageView stepImg;
        //private TextView stepNum;
        private TextView stepText;
        //private TextView stepTip;

        public ViewHolder(View v) {
            super(v);
            //stepImg = (ImageView)v.findViewById(R.id.step_image);
            //stepNum = (TextView)v.findViewById(R.id.stepNum);
            stepText = (TextView)v.findViewById(R.id.stepText);
            //stepTip = (TextView)v.findViewById(R.id.stepTip);
        }
    }

    public DetailAdapter(Context context, List<Food> data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public DetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cardview, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        RequestOptions ro = new RequestOptions()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .override(256, 140)
//                .fitCenter();
//
//        Glide.with(context)
//                .setDefaultRequestOptions(ro)
//                .load(data.get(position).getStepImgURL())
//                .into(holder.stepImg);
        StringBuilder sb = new StringBuilder();
//        holder.stepNum.setText(String.format(Locale.KOREA, "%d", data.get(position).getCookingNo()));
        for(Food f : data){
            sb.append(String.format(Locale.KOREA, "(%d). %s\n", f.getCookingNo(),f.getCookingDC()));
        }
//        holder.stepText.setText(data.get(position).getCookingDC());
//        holder.stepTip.setText(data.get(position).getStepTIP());
        holder.stepText.setText(sb.toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
