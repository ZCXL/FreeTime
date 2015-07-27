package adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuchao.freetime.R;

import java.util.ArrayList;

import bean.Movie;
import function.Network;
import function.NetworkFunction;
import utils.ImageLoader;
import view_rewrite.SwipeListView;

/**
 * Created by zhuchao on 7/21/15.
 */
public class CollectionAdapter extends BaseAdapter {
    private ImageLoader imageLoader;
    private ArrayList<Movie>movies;
    private Context context;
    private int position;

    private SwipeListView listView;

    public SwipeListView getListView() {
        return listView;
    }

    public void setListView(SwipeListView listView) {
        this.listView = listView;
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    movies.remove(position);
                    CollectionAdapter.this.notifyDataSetChanged();
                    listView.hiddenRight(listView.mCurrentItemView);
                    break;
            }
        }
    };
    public CollectionAdapter(ArrayList<Movie>movies,Context context){
        this.movies=movies;
        this.context=context;
        imageLoader=new ImageLoader(context);
    }
    @Override
    public int getCount() {
        if(movies==null)
            return -1;
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        if(movies==null)
            return null;
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Movie movie=movies.get(position);
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.collection_item,parent,false);
            holder=new ViewHolder();
            holder.video_image=(ImageView)convertView.findViewById(R.id.mine_collect_video_image);
            holder.video_time=(TextView)convertView.findViewById(R.id.mine_collect_video_time);
            holder.video_name=(TextView)convertView.findViewById(R.id.mine_collect_video_name);
            holder.video_delete=(RelativeLayout)convertView.findViewById(R.id.item_right);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.video_name.setText(movie.getMovieName());
        holder.video_time.setText(movie.getTime());
        holder.video_image.setTag(movie.getImageUrl());

        final String id=movie.getMovieId();
        holder.video_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Network.checkNetWorkState(context)){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String keys[]={"movieid","number","available"};
                            String parameters[]=new String[]{id,"241938F47DE2A7CEAB664C99E5A63F28","1"};
                            String result= NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/collect.php", keys, parameters);
                            if(result!=null&&!result.contains("error")){
                                CollectionAdapter.this.position=position;
                                mHandler.sendEmptyMessage(0);
                            }
                        }
                    }).start();
                }
            }
        });
        imageLoader.DisplayImage(movie.getImageUrl(),holder.video_image);
        return convertView;
    }
    static class ViewHolder{
        ImageView video_image;
        TextView video_time;
        TextView video_name;
        RelativeLayout video_delete;
    }
}
