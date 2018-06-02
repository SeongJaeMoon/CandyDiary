package goods.cap.app.goodsgoods.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import goods.cap.app.goodsgoods.API.Config;
import goods.cap.app.goodsgoods.Model.Diet.Diet;

import goods.cap.app.goodsgoods.R;

/* 메인 화면 GridView 연결 컨트롤 Adapter, created by supermoon. */

public class GirdViewAdapter extends ArrayAdapter{

    private static final String logger = GirdViewAdapter.class.getSimpleName();
    private Context context; //연결할 화면 context
    private int resourceId; //이미지 연결용 R.id
    private LayoutInflater inflater;
    private List<Diet>data;
    //private List<Recipe> data;
    //final GirdViewAdapter adapter = this;

    static class ViewHolder {
        TextView text;
        ImageView image;
    }
    //그리드 뷰 어뎁터 생성자
    public GirdViewAdapter(Context context, List<Diet>data, int resourceId){
        //슈퍼클래스 생성자 호출 필요.
        super(context, resourceId, data);
        this.resourceId = resourceId;
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
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
            holder.image = (ImageView) row.findViewById(R.id.grid_image);
            holder.text = (TextView) row.findViewById(R.id.imgTitle);
            row.setTag(holder);
        } else {
            //Recycler View
            holder = (ViewHolder) row.getTag();
        }

        holder.text.setText(data.get(position).getFdNm());

        String oldPath = data.get(position).getRtnImageDc();
        String newPath = data.get(position).getRtnStreFileNm();

        RequestOptions ro = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .format(DecodeFormat.PREFER_ARGB_8888);

        Glide.with(context)
                .setDefaultRequestOptions(ro)
                .load(Config.getAbUrl(oldPath, newPath))
                .into(holder.image);

        return row;
    }

    private void setView(){

    }
}
