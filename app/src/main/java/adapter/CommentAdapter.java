package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuchao.freetime.R;

import java.util.ArrayList;

import bean.Comment;
import utils.ImageLoader;
import view_rewrite.RoundImageView;

/**
 * Created by zhuchao on 7/20/15.
 */
public class CommentAdapter extends BaseAdapter {

    private ImageLoader imageLoader;
    private ArrayList<Comment> comments;
    private Activity context;
    public CommentAdapter(ArrayList<Comment>comments,Activity context){
        this.comments=comments;
        this.context=context;
        imageLoader=new ImageLoader(context);
    }
    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Comment comment=comments.get(position);
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false);

            holder=new ViewHolder();
            holder.head=(RoundImageView)convertView.findViewById(R.id.mine_head_image);
            holder.user_name=(TextView)convertView.findViewById(R.id.user_name);
            holder.comment=(TextView)convertView.findViewById(R.id.comment_text);
            holder.time=(TextView)convertView.findViewById(R.id.comment_time);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        //holder.head.setTag(comment.);
        //holder.user_name=
        return convertView;
    }
    static class ViewHolder{
        RoundImageView head;
        TextView user_name;
        TextView comment;
        TextView time;
    }
}
