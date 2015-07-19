package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuchao.freetime.R;

import java.util.ArrayList;

import bean.Movie;
import function.ImageLoaderTask;
import function.ImageProcess;

/**
 * Created by zhuchao on 7/16/15.
 */
public class TopHotItemAdapter extends BaseAdapter {
    private ArrayList<Movie>movies;
    private Context context;
    public TopHotItemAdapter(ArrayList<Movie>movies,Context context){
        this.movies=movies;
        this.context=context;
    }
    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Movie movie=movies.get(position);
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.top_hot_item,parent,false);

            holder=new ViewHolder();
            holder.movie_image=(ImageView)convertView.findViewById(R.id.top_hot_item_image);
            holder.movie_description=(TextView)convertView.findViewById(R.id.top_hot_item_description);
            holder.movie_sights=(TextView)convertView.findViewById(R.id.top_hot_item_sight_text);
            holder.movie_comment_number=(TextView)convertView.findViewById(R.id.top_hot_item_comment_text);
            holder.movie_time=(TextView)convertView.findViewById(R.id.top_hot_item_time_text);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        new ImageLoaderTask(holder.movie_image,context, ImageProcess.FileType_Image.MovieImage).execute(movie.getImageUrl());

        holder.movie_sights.setText(movie.getViewNumber());
        holder.movie_comment_number.setText(movie.getCommentNumber());
        holder.movie_time.setText(movie.getTime());
        if(movie.getMovieName().length()>=20){
            holder.movie_description.setText(movie.getMovieName().substring(0,20)+"...");
        }else{
            holder.movie_description.setText(movie.getMovieName());
        }
        return convertView;
    }

    static  class ViewHolder{
        ImageView movie_image;
        TextView movie_description;
        TextView movie_time;
        TextView movie_sights;
        TextView movie_comment_number;
    }
}
