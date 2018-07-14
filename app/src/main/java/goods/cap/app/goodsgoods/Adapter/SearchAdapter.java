package goods.cap.app.goodsgoods.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import goods.cap.app.goodsgoods.R;
import goods.cap.app.goodsgoods.Util.Search;
import me.gujun.android.taggroup.TagGroup;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<Search> search;
    private Context context;
    private ItemListener mListener;

    public SearchAdapter(){
        this.search = new ArrayList<>();
    }

    public SearchAdapter(Context context){
        this.context = context;
        this.search = new ArrayList<>();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_single2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.setSearch(search.get(position), context);
    }

    @Override
    public int getItemCount() {
        return search.size();
    }

    public void addAll(Search newUsers) {
        search.add(newUsers);
        notifyDataSetChanged();
    }
    public void clear(){
        search.clear();
        notifyDataSetChanged();
    }

    public String getLastItemId() {
        return search.get(search.size() - 1).getFkey();
    }

    public void setListener(ItemListener listener) {
        this.mListener = listener;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView mainText;
        private TagGroup tagGroup;
        private Search search;

        public SearchViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.searchImage);
            mainText = itemView.findViewById(R.id.searchTitle);
            tagGroup = itemView.findViewById(R.id.searchTag);
        }

        public void setSearch(final Search search, final Context context) {
            this.search = search;

            final RequestOptions ro = new RequestOptions()
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.none2))
                    .error(ContextCompat.getDrawable(context, R.drawable.none));

            imageView.post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(context).setDefaultRequestOptions(ro).load(search.getImage()).into(imageView);
                    if(search.getMainText() != null) {
                        mainText.setText(search.getMainText());
                    }

                }
            });
            if (search.getTags() != null) tagGroup.setTags(search.getTags());
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(search);
            }
        }
    }
    public interface ItemListener {
        void onItemClick(Search search);
    }
}
