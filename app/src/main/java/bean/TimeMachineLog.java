package bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import function.ParseJson;

/**
 * Created by zhuchao on 7/25/15.
 */
public class TimeMachineLog implements ParseJson {
    private String viewNumber;
    private String commentNumber;
    private String collectionNumber;
    public TimeMachineLog(String c){
        getObjects(c);
    }
    @Override
    public ArrayList<BaseObject> getObjects(String c) {
        try {
            JSONObject object=new JSONObject(c);
            JSONObject jsonObject=object.getJSONObject("TM");
            setViewNumber(jsonObject.getString("viewnumber"));
            setCommentNumber(jsonObject.getString("commentnumber"));
            setCollectionNumber(jsonObject.getString("collectionnumber"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCollectionNumber() {
        return collectionNumber;
    }

    public void setCollectionNumber(String collectionNumber) {
        this.collectionNumber = collectionNumber;
    }

    public String getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(String commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getViewNumber() {
        return viewNumber;
    }

    public void setViewNumber(String viewNumber) {
        this.viewNumber = viewNumber;
    }
}
