package bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import function.ParseJson;

/**
 * Created by zhuchao on 7/20/15.
 */
public class Comment extends BaseObject implements ParseJson {
    private String movieId;
    private String number;
    private String comment;
    private String time;

    private String nick_name;
    private String head_url;

    private CommentBacks backs;
    public Comment(){
        super(TYPE.COMMENT);
    }
    public Comment(String c){
        super(TYPE.COMMENT);
        getObjects(c);
    }
    public String getNick_name() {
        return nick_name;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }
    public void setBacks(JSONArray array){
        backs=new CommentBacks(array);
    }
    public String getMovieId() {
        return movieId;
    }

    public CommentBacks getCommentBacks(){
        return backs;
    }
    public String getTime() {
        return time;
    }

    public String getComment() {
        return comment;
    }

    public String getNumber() {
        return number;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public ArrayList<BaseObject> getObjects(String c) {
        try {
            JSONObject object=new JSONObject(c);
            JSONObject comm=object.getJSONObject("C");
            this.setMovieId(comm.getString("movieid"));
            this.setNumber(comm.getString("number"));
            this.setComment(comm.getString("comment"));
            this.setTime(comm.getString("time"));
            this.setHead_url(comm.getString("headurl"));
            this.setNick_name(comm.getString("nickname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
