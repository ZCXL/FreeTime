package bean;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import function.ParseJson;
import function.SaveAndOpenMovies;

/**
 * Created by zhuchao on 7/13/15.
 */
public class Movies extends BaseObjects implements ParseJson{
    private SaveAndOpenMovies saveAndOpenMovies;
    private ArrayList<BaseObject>movies;
    public Movies(){
        super();
    }
    public Movies(Context context){
        super();
        saveAndOpenMovies=new SaveAndOpenMovies();
        movies=saveAndOpenMovies.Open(context);
    }

    public Movies(String c){
        super();
        movies=getObjects(c);
    }
    /**
     * get movies' counts
     * @return int
     * Created by LMZ on 7/13/15
     */
    @Override
    public int getCount() {
        if(movies==null)
            return -1;
        else
            return movies.size();
    }

    /**
     * get instant movie's item
     * @param index
     * @return
     * Created by LMZ on 7/13/15
     */
    @Override
    public BaseObject getItem(int index) {
        if(movies==null)
            return null;
        else
            return movies.get(index);
    }

    @Override
    public ArrayList<BaseObject> getObjects(String c) {
        ArrayList<BaseObject>list=new ArrayList<BaseObject>();
        try {
            JSONObject object=new JSONObject(c);
            JSONArray array=object.getJSONArray("M");
            for(int i=0;i<array.length();i++){
                JSONObject jsonObject=array.getJSONObject(i);
                Movie movie=new Movie();
                movie.setMovieName(jsonObject.getString("moviename"));
                movie.setMovieId(jsonObject.getString("movieId"));
                movie.setFileUrl(jsonObject.getString("movieurl"));
                movie.setPlayUrl(jsonObject.getString("playurl"));
                movie.setTime(jsonObject.getString("movietime"));
                movie.setImageUrl(jsonObject.getString("moviepictureurl"));
                movie.setDescription(jsonObject.getString("movieinstruction"));
                movie.setViewNumber(jsonObject.getString("viewnumber"));
                movie.setCommentNumber(jsonObject.getString("commentnumber"));
                list.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }
}
