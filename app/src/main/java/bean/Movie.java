package bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhuchao on 7/13/15.
 */
public class Movie extends BaseObject implements Parcelable{
    private String description;
    private long fileSize;
    private String time;
    private String imageUrl;
    private String fileUrl;
    private String movieId;
    private String playUrl;
    private String movieName;
    private String viewNumber;
    private String commentNumber;

    public String getMovieId() {
        return movieId;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getCommentNumber() {
        return commentNumber;
    }

    public String getViewNumber() {
        return viewNumber;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setViewNumber(String viewNumber) {
        this.viewNumber = viewNumber;
    }

    public void setCommentNumber(String commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getDescription() {

        return description;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getTime() {
        return time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setDescription(String description){this.description = description;}

    public void setTime(String time) {
        this.time = time;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Movie() {
        super(TYPE.MOVIE);
    }

    //implement parcel
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeLong(fileSize);
        dest.writeString(time);
        dest.writeString(imageUrl);
        dest.writeString(fileUrl);
        dest.writeString(movieId);
        dest.writeString(playUrl);
        dest.writeString(movieName);
        dest.writeString(viewNumber);
        dest.writeString(commentNumber);
    }
    public static  final Parcelable.Creator<Movie>CREATOR=new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private Movie(Parcel in){
        super(TYPE.MOVIE);
        description=in.readString();
        fileSize=in.readLong();
        time=in.readString();
        imageUrl=in.readString();
        fileUrl=in.readString();
        movieId=in.readString();
        playUrl=in.readString();
        movieName=in.readString();
        viewNumber=in.readString();
        commentNumber=in.readString();
    }
}
