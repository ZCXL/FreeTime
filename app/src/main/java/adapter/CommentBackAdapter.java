package adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuchao.freetime.R;

import java.util.ArrayList;

import bean.Comment;
import bean.CommentBack;
import bean.CommentBacks;
import bean.Movie;
import fragment.MineFragment;
import function.LoginNotification;
import function.NetworkFunction;
import utils.ImageLoader;
import utils.Utils;
import view_rewrite.LoadingDialog;
import view_rewrite.RoundImageView;

/**
 * Created by zhuchao on 7/22/15.
 */
public class CommentBackAdapter extends BaseAdapter implements View.OnClickListener{

    private ArrayList<Comment>comments;

    private Context context;

    private Movie movie;

    private ImageLoader imageLoader;

    private LoadingDialog loadingDialog;

    private CommentBack back;

    private ListView listView;

    private int position;

    private LinearLayout linear_layout_edit;

    private EditText editText;

    private Button send;

    private String commentednumber;

    private String commenttime;

    public void setLinear_layout_edit(LinearLayout edit){
        this.linear_layout_edit=edit;
        editText=(EditText)edit.findViewById(R.id.comment_back_edit);
        send=(Button)edit.findViewById(R.id.comment_back_send);
        send.setOnClickListener(this);
    }
    public void setListView(ListView listView){
        this.listView=listView;
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.d("tell me why",String.valueOf(position));
                    if(position>0&&position<3){
                        comments.get(position-1).getCommentBacks().addCommentBacks(back);
                    }else if(position>3){
                        comments.get(position-2).getCommentBacks().addCommentBacks(back);
                    }
                    CommentBackAdapter.this.notifyDataSetChanged();
                    loadingDialog.stopProgressDialog();
                    break;
                case 1:
                    loadingDialog.stopProgressDialog();
                    break;
            }
        }
    };
    public CommentBackAdapter(Context context,ArrayList<Comment>comments,Movie movie){
        this.comments=comments;
        this.context=context;
        this.movie=movie;
        imageLoader=new ImageLoader(context);
        loadingDialog=new LoadingDialog(context);
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
        holder.comment_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentBackAdapter.this.commentednumber=commented_number;
                CommentBackAdapter.this.commenttime=comment_time;
                CommentBackAdapter.this.position = position;
                if(linear_layout_edit.getVisibility()==View.GONE) {
                    linear_layout_edit.setVisibility(View.VISIBLE);
                    editText.requestFocus();
                }else {
                    linear_layout_edit.setVisibility(View.GONE);
                }
            }
        });
        CommentBacks backs=comment.getCommentBacks();
        ArrayList<CommentBack>commentBackArrayList=new ArrayList<CommentBack>();
        for(int i=0;i<backs.getCount();i++){
           commentBackArrayList.add((CommentBack)backs.getItem(i));
        }
        CommentBackItemAdapter adapter=new CommentBackItemAdapter(commentBackArrayList,context);
        holder.back_container.setAdapter(adapter);
        Utils.setListViewHeightBasedOnChildren(holder.back_container);
        return convertView;
    }


    static class ViewHolder{
        RoundImageView head;
        TextView comment_text;
        TextView user_name;
        TextView comment_time;
        ListView back_container;
    }

    @Override
    public void onClick(View v) {
        if (MineFragment.isLogin) {
            loadingDialog.startProgressDialog();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String con = editText.getText().toString();
                    if (con != null && !con.equals("")) {
                        String keys[] = {"movieid", "number", "comment", "commentnumber", "commentednumber", "commenttime"};
                        String parameters[] = {movie.getMovieId(),commentednumber, con, MineFragment.userInfo.getNumber(), commentednumber, commenttime};
                        String result = NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/comment_back.php", keys, parameters);
                        if (result != null && !result.contains("error")) {
                            back = new CommentBack(result);
                            Log.d("tell me why", back.getComment());
                            mHandler.sendEmptyMessage(0);
                        } else {
                            mHandler.sendEmptyMessage(1);
                        }
                    } else {
                        Toast.makeText(context, "Comment can't be null", Toast.LENGTH_SHORT).show();
                    }
                }
            }).start();
        } else {
            LoginNotification.loginNotification(context);
        }
    }
}
