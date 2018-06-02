package goods.cap.app.goodsgoods.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import goods.cap.app.goodsgoods.Model.Diet.DietDtl;
import goods.cap.app.goodsgoods.R;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder>{

    private static final String logger = DetailAdapter.class.getSimpleName();
    private List<DietDtl> data;
    private Context context; //연결할 화면 context

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView stepImg;
        private TextView stepNum;
        private TextView stepText;
        private TextView stepTip;
        private ImageView stepArrow;

        public ViewHolder(View v) {
            super(v);
            stepImg = (ImageView)v.findViewById(R.id.step_image);
            stepNum = (TextView)v.findViewById(R.id.step_num);
            stepText = (TextView)v.findViewById(R.id.step_text);
            stepTip = (TextView)v.findViewById(R.id.step_tip);
            stepArrow = (ImageView)v.findViewById(R.id.arrow_img);
        }
    }

    public DetailAdapter(Context context, List<DietDtl> data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public DetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_cardview, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailAdapter.ViewHolder holder, int position) {

        RequestOptions ro = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter();

        Glide.with(context)
                .setDefaultRequestOptions(ro)
                .load(data.get(position).getRtnImageDc())
                .into(holder.stepImg);
        if(position == data.size() - 1){
            holder.stepArrow.setVisibility(View.GONE);
        }
        holder.stepText.setText(data.get(position).getMatrlInfo());
        holder.stepNum.setText(data.get(position).getCntntsSj());
        holder.stepTip.setText(data.get(position).getCkngMthInfo());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
