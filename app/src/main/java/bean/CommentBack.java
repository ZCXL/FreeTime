package bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import function.ParseJson;

/**
 * Created by zhuchao on 7/20/15.
 */
public class CommentBack extends BaseObject implements Parcelable, ParseJson{
    private String comment;
    private String time;
    private String comment_number;
    private String comment_name;
    private String commented_name;
    private String commented_number;

    public String getCommented_name() {
        return commented_name;
    }

    public void setCommented_name(String commented_name) {
        this.commented_name = commented_name;
    }

    public String getCommented_number() {
        return commented_number;
    }

    public void setCommented_number(String commented_number) {
        this.commented_number = commented_number;
    }

    public String getComment() {
        return comment;
    }

    public String getComment_number() {
        return comment_number;
    }

    public String getTime() {
        return time;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setComment_number(String comment_number) {
        this.comment_number = comment_number;
    }

    public CommentBack() {
        super(TYPE.COMMENTBACK);
    }
    public CommentBack(String c){
        super(TYPE.COMMENTBACK);
        getObjects(c);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public String getComment_name() {
        return comment_name;
    }

    public void setComment_name(String comment_name) {
        this.comment_name = comment_name;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comment);
        dest.writeString(time);
        dest.writeString(comment_number);
        dest.writeString(comment_name);
    }
    public static final Parcelable.Creator<CommentBack>CREATOR=new Creator<CommentBack>() {
        @Override
        public CommentBack createFromParcel(Parcel source) {
            return new CommentBack(source);
        }

        @Override
        public CommentBack[] newArray(int size) {
            return new CommentBack[size];
        }
    };
    private CommentBack(Parcel in){
        super(TYPE.COMMENTBACK);
        comment=in.readString();
        time=in.readString();
        comment_number=in.readString();
        comment_name =in.readString();
    }

    @Override
    public ArrayList<BaseObject> getObjects(String c) {
        try {
            JSONObject object=new JSONObject(c);
            JSONObject comment=object.getJSONObject("C");
            setComment_number(comment.getString("commentnumber"));
            setComment(comment.getString("comment"));
            setComment_name(comment.getString("commentname"));
            setCommented_number(comment.getString("commentednumber"));
            setCommented_name(comment.getString("commentedname"));
            setTime(comment.getString("time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
