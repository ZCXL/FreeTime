package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhuchao.freetime.R;

import java.util.ArrayList;

import bean.Comment;
import bean.CommentBack;
import bean.CommentBacks;
import bean.Movie;
import utils.ImageLoader;
import utils.Utils;
import view_rewrite.EditWindow;
import view_rewrite.RoundImageView;

/**
 * Created by zhuchao on 7/22/15.
 */
public class CommentBackAdapter extends BaseAdapter implements EditWindow.OnCommentListener{

    private ArrayList<Comment>comments;

    private Context context;

    private Movie movie;

    private ImageLoader imageLoader;

    private EditWindow editWindow;

    public CommentBackAdapter(Context context,ArrayList<Comment>comments,Movie movie){
        this.comments=comments;
        this.context=context;
        this.movie=movie;
        imageLoader=new ImageLoader(context);
        editWindow=new EditWindow(context);
        editWindow.setOnCommentListener(this);
    }
    @Override
    public int getCount() {
        return comments.size()+2;
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
    public View getView(final int position,View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if(position==0){
            convertView=LayoutInflater.from(context).inflate(R.layout.comment_recent,parent,false);
            return  convertView;
        }
        if(position==3||(position==1&&comments.size()==0)||(position==2&&comments.size()==1)){
            convertView=LayoutInflater.from(context).inflate(R.layout.comment_all, parent, false);
            return  convertView;
        }
        Comment comment=null;
        if(position>0&&position<3){
            comment=comments.get(position-1);
        }else if(position>3){
            comment=comments.get(position-2);
        }
        if(convertView==null||convertView.getTag()==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.comment_back_item,parent,false);

            holder=new ViewHolder();
            holder.head=(RoundImageView)convertView.findViewById(R.id.mine_head_image);
            holder.comment_text=(TextView)convertView.findViewById(R.id.comment_text);
            holder.comment_time=(TextView)convertView.findViewById(R.id.comment_time);
            holder.user_name=(TextView)convertView.findViewById(R.id.user_name);
            holder.back_container=(ListView)convertView.findViewById(R.id.comment_back_container);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.head.setTag(comment.getHead_url());
        imageLoader.DisplayImage(comment.getHead_url(), holder.head);

        holder.user_name.setText(comment.getNick_name());
        holder.comment_time.setText(comment.getTime());
        holder.comment_text.setText(comment.getComment());

        final String commented_number=comment.getNumber();
        final String comment_time=comment.getTime();
        final String movieId=movie.getMovieId();
        holder.comment_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWindow.setParameters(commented_number,comment_time,movieId,position);
                editWindow.show();
            }
        });
        final CommentBacks backs=comment.getCommentBacks();
        ArrayList<CommentBack>commentBackArrayList=new ArrayList<CommentBack>();
        for(int i=0;i<backs.getCount();i++){
           commentBackArrayList.add((CommentBack)backs.getItem(i));
        }
        CommentBackItemAdapter adapter=new CommentBackItemAdapter(commentBackArrayList,context);
        holder.back_container.setAdapter(adapter);
        Utils.setListViewHeightBasedOnChildren(holder.back_container);
        holder.back_container.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                CommentBack commentBack=(CommentBack)backs.getItem(p);
                editWindow.setParameters(commentBack.getComment_number(),comment_time,movieId,position);
                editWindow.show();
            }
        });
        return convertView;
    }

    @Override
    public void onSuccess(CommentBack back, int position) {
        comments.get(position).getCommentBacks().addCommentBacks(back);
        notifyDataSetChanged();
    }

    @Override
    public void onError() {

    }


    static class ViewHolder{
        RoundImageView head;
        TextView comment_text;
        TextView user_name;
        TextView comment_time;
        ListView back_container;
    }
}
