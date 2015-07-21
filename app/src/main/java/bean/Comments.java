package bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import function.ParseJson;

/**
 * Created by zhuchao on 7/20/15.
 */
public class Comments extends BaseObjects implements ParseJson{
    ArrayList<BaseObject>comments;
    public Comments(String c){
        comments=getObjects(c);
    }
    @Override
    public int getCount() {
        if(comments==null)
            return -1;
        return comments.size();
    }

    @Override
    public BaseObject getItem(int index) {
        if(comments==null)
            return null;
        return comments.get(index);
    }

    @Override
    public ArrayList<BaseObject> getObjects(String c) {
        ArrayList<BaseObject>comments=new ArrayList<BaseObject>();
        try {
            JSONObject object=new JSONObject(c);
            JSONArray array=object.getJSONArray("CS");
            for(int i=0;i<array.length();i++){
                Comment comment=new Comment();
                JSONObject temp=array.getJSONObject(i);
                comment.setMovieId(temp.getString("movieid"));
                comment.setNumber(temp.getString("number"));
                comment.setComment(temp.getString("comment"));
                comment.setNick_name(temp.getString("nickname"));
                comment.setHead_url(temp.getString("headurl"));
                comment.setTime(temp.getString("time"));
                comment.setBacks(temp.getJSONArray("commentbacks"));
                comments.add(comment);
                Log.d("result",comment.getTime());
            }
            return comments;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
