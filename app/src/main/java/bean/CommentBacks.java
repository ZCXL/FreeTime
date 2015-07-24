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
public class CommentBacks extends BaseObjects implements ParseJson{
    private ArrayList<BaseObject>commentBacks;
    public CommentBacks(JSONArray array){
        commentBacks=getObjects(array);
    }
    @Override
    public int getCount() {
        if(commentBacks==null)
            return -1;
        return commentBacks.size();
    }

    @Override
    public BaseObject getItem(int index) {
        if(commentBacks==null)
            return null;
        return commentBacks.get(index);
    }
    public void addCommentBacks(CommentBack back){
        Log.d("tell me why",back.getComment());
        commentBacks.add(back);
    }
    @Override
    public ArrayList<BaseObject> getObjects(String c) {
        return null;
    }
    public ArrayList<BaseObject> getObjects(JSONArray array){
        ArrayList<BaseObject>commentBacks=new ArrayList<BaseObject>();
        try {
            for(int i=0;i<array.length();i++){
                CommentBack back=new CommentBack();
                JSONObject temp=array.getJSONObject(i);
                back.setComment(temp.getString("comment"));
                back.setTime(temp.getString("time"));
                back.setComment_number(temp.getString("commentnumber"));
                back.setComment_name(temp.getString("commentname"));
                back.setCommented_name(temp.getString("commentedname"));
                back.setCommented_number(temp.getString("commentednumber"));
                commentBacks.add(back);
            }
            return commentBacks;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
