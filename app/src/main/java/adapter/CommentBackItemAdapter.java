package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuchao.freetime.R;

import java.util.ArrayList;

import bean.CommentBack;

/**
 * Created by zhuchao on 7/23/15.
 */
public class CommentBackItemAdapter extends BaseAdapter {
    private ArrayList<CommentBack>commentBacks;
    private Context context;

    public CommentBackItemAdapter(ArrayList<CommentBack>commentBacks,Context context){
        this.commentBacks=commentBacks;
        this.context=context;
    }
    @Override
    public int getCount() {
        return commentBacks.size();
    }

    @Override
    public Object getItem(int position) {
        return commentBacks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        CommentBack commentBack=commentBacks.get(position);
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.comment_back,parent,false);

            holder=new ViewHolder();
            holder.comment_name=(TextView)convertView.findViewById(R.id.commentname);
            holder.commented_name=(TextView)convertView.findViewById(R.id.commentedname);
            holder.comment=(TextView)convertView.findViewById(R.id.comment_reply_text);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.comment_name.setText(commentBack.getComment_name());
        holder.commented_name.setText(commentBack.getCommented_name());
        holder.comment.setText(commentBack.getComment());

        return convertView;
    }
    static class ViewHolder{
        TextView comment_name;
        TextView commented_name;
        TextView comment;
    }
}
