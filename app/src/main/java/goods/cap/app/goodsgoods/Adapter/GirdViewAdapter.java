package goods.cap.app.goodsgoods.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import goods.cap.app.goodsgoods.Activity.DetailItemActivity;
import goods.cap.app.goodsgoods.Model.Grocery;
import goods.cap.app.goodsgoods.Model.Recipe;

import goods.cap.app.goodsgoods.R;

/* 메인 화면 GridView 연결 컨트롤 Adapter, created by supermoon. */

public class GirdViewAdapter extends ArrayAdapter{

    private static final String logger = GirdViewAdapter.class.getSimpleName();
    private Context context; //연결할 화면 context
    private int resourceId; //이미지 연결용 R.id
    private LayoutInflater inflater;
    private List<Recipe> data;
    final GirdViewAdapter adapter = this;

    static class ViewHolder {
        TextView text;
        ImageView image;
        Button button;
    }
    //그리드 뷰 어뎁터 생성자
    public GirdViewAdapter(Context context, List<Recipe>data, int resourceId){
        //슈퍼클래스 생성자 호출 필요.
        super(context, resourceId, data);
        this.resourceId = resourceId;
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
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
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            row = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.grid_image);
            holder.text = (TextView) row.findViewById(R.id.imgTitle);
            holder.button = (Button) row.findViewById(R.id.btnImgDetail);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailItemActivity.class);
                    Gson gson = new Gson();
                    String recipe = gson.toJson(data.get(position));
                    intent.putExtra("recipe", recipe);
                    context.startActivity(intent);
                    Log.w(logger, "recipe : " + recipe);
                }
            });
            holder.text.setText(this.data.get(position).getRecipe_nm_ko());
            row.setTag(holder);
        } else {
            //Recycler View
            holder = (ViewHolder) row.getTag();
        }
        Glide.with(context)
                .load(data.get(position).getImg_url())
                .into(holder.image);
        return row;
    }
}
