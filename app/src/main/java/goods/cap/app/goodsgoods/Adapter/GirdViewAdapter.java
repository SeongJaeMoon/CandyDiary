package goods.cap.app.goodsgoods.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import goods.cap.app.goodsgoods.R;

/* 메인 화면 GridView 연결 컨트롤 Adapter, created by supermoon. */

public class GirdViewAdapter extends ArrayAdapter{

    private Context context; //연결할 화면 context
    private int resourceId; //이미지 연결용 R.id
    private LayoutInflater inflater;
    private List<String> data;

    static class ViewHolder {
        ImageView image;
    }
    //그리드 뷰 어뎁터 생성자
    public GirdViewAdapter(Context context, List<String>data, int resourceId){
        //슈퍼클래스 생성자 호출 필요.
        super(context, resourceId, data);
        this.resourceId = resourceId;
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            row = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.grid_image);
            
            row.setTag(holder);
        } else {
            //Recycler View
            holder = (ViewHolder) row.getTag();
        }
        Glide.with(context)
                .load(data.get(position))
                .into(holder.image);

        return row;
    }
}
