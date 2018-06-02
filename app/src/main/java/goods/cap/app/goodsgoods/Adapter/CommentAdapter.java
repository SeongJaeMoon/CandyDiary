package goods.cap.app.goodsgoods.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;
import goods.cap.app.goodsgoods.Model.Firebase.Comment;
import goods.cap.app.goodsgoods.R;

public class CommentAdapter extends ArrayAdapter<Comment> {

    private static final String logger = CommentAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater mInflater;


    public CommentAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.comment_box, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Comment comment = getItem(position);

        vh.commenter.setText(comment.getName());
        vh.comment.setText(comment.getComment());
        vh.commentDate.setText(comment.getRegDate());
        RequestOptions ro = new RequestOptions()
                .placeholder(ContextCompat.getDrawable(context, R.mipmap.empty_user))
                .error(ContextCompat.getDrawable(context, R.mipmap.empty_user));

        Glide.with(context)
                .setDefaultRequestOptions(ro)
                .load(comment.getPimage())
                .into(vh.imageView);

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final CircleImageView imageView;
        public final TextView commenter;
        public final TextView comment;
        public final TextView commentDate;

        private ViewHolder(RelativeLayout rootView, CircleImageView imageView, TextView commenter, TextView comment, TextView commentDate) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.commenter = commenter;
            this.comment = comment;
            this.commentDate = commentDate;
        }

        private static ViewHolder create(RelativeLayout rootView) {
            CircleImageView circleImageView = (CircleImageView)rootView.findViewById(R.id.commenter_img);
            TextView commenter = (TextView)rootView.findViewById(R.id.commenter);
            TextView comment = (TextView)rootView.findViewById(R.id.comment);
            TextView commentDate = (TextView)rootView.findViewById(R.id.comment_date);
            return new ViewHolder(rootView, circleImageView, commenter, comment, commentDate);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
