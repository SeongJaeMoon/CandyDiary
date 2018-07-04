package goods.cap.app.goodsgoods.Adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import goods.cap.app.goodsgoods.Model.Firebase.Stars;
import goods.cap.app.goodsgoods.R;
import me.gujun.android.taggroup.TagGroup;

public class StarPostAdapter extends RecyclerView.Adapter<StarPostAdapter.StarsViewHolder> {

    private List<Stars>stars;
    private Context context;
    private ItemListener mListener;

    public StarPostAdapter(){
        this.stars = new ArrayList<>();
    }

    public StarPostAdapter(Context context){
        this.stars = new ArrayList<>();
        this.context = context;
    }

    public StarPostAdapter(List<Stars>stars, Context context, ItemListener listener){
        this.stars = stars;
        this.context = context;
        this.mListener = listener;
    }

    public void setListener(ItemListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public StarsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StarsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StarsViewHolder holder, int position) {
        holder.setStars(stars.get(position), context);
    }

    @Override
    public int getItemCount() {
        return stars.size();
    }

    public void addAll(Stars newStars) {
        stars.add(newStars);
        notifyDataSetChanged();
    }

    public String getLastItemId() {
        return stars.get(stars.size() - 1).getFkey();
    }

    public void clear(){
        stars.clear();
        notifyDataSetChanged();
    }

    public class StarsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CircleImageView userImg;
        public TextView user;
        public TextView title;
        private Stars stars;
        private TagGroup tagGroup;

        public StarsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            userImg = (CircleImageView) itemView.findViewById(R.id.user_img);
            title = (TextView) itemView.findViewById(R.id.bottom_title);
            user = (TextView) itemView.findViewById(R.id.bottom_user);
            tagGroup = (TagGroup)itemView.findViewById(R.id.bottom_tag);
        }

        public void setStars(Stars star, final Context context){
            this.stars = star;
            final RequestOptions ro = new RequestOptions()
                    .placeholder(ContextCompat.getDrawable(context, R.mipmap.empty_user))
                    .error(ContextCompat.getDrawable(context, R.mipmap.empty_user));

            userImg.post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(context).setDefaultRequestOptions(ro).load(stars.getImg()).into(userImg);
                    user.setText(stars.getUser());
                }
            });
            title.setText(stars.getTitle());
            if(stars.getTags() != null) tagGroup.setTags(stars.getTags());
        }
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(stars);
            }
        }
    }
    public interface ItemListener {
        void onItemClick(Stars stars);
    }
}
