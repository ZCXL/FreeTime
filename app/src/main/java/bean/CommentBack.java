package bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhuchao on 7/20/15.
 */
public class CommentBack extends BaseObject implements Parcelable{
    private String comment;
    private String time;
    private String comment_number;
    private String comment_time;

    public String getComment() {
        return comment;
    }

    public String getComment_number() {
        return comment_number;
    }

    public String getComment_time() {
        return comment_time;
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

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public CommentBack() {
        super(TYPE.COMMENTBACK);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comment);
        dest.writeString(time);
        dest.writeString(comment_number);
        dest.writeString(comment_time);
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
        comment_time=in.readString();
    }
}
