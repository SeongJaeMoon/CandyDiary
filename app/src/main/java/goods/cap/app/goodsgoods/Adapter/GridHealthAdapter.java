package goods.cap.app.goodsgoods.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import goods.cap.app.goodsgoods.API.Config;
import goods.cap.app.goodsgoods.Model.Health.Health;
import goods.cap.app.goodsgoods.R;

public class GridHealthAdapter extends ArrayAdapter{

    private Context context; //연결할 화면 context
    private int resourceId; //이미지 연결용 R.id
    private LayoutInflater inflater;
    private List<Health>data;

    public GridHealthAdapter(@NonNull Context context, List<Health>data, int resourceId) {
        super(context, resourceId, data);
        this.resourceId = resourceId;
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        TextView title;
        TextView subTitle;
    }
    @Override
    public int getCount() { return data.size(); }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            row = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) row.findViewById(R.id.pd_nm);
            holder.subTitle = (TextView) row.findViewById(R.id.dis_pos);
            row.setTag(holder);
        } else {
            //Recycler View
            holder = (ViewHolder) row.getTag();
        }

        holder.title.setText(data.get(position).getPRDLST_NM());
        holder.subTitle.setText(data.get(position).getDISPOS());

        return row;
    }
}
