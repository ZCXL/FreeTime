package bean;

import java.io.Serializable;

/**
 * Created by zhuchao on 7/13/15.
 */
public class Movie extends BaseObject implements Serializable {
    private static final long serialVersionUID=-7620435178023928254L;
    private String description;
    private long filesize;
    private String time;
    private String imageUrl;
    private String fileUrl;

    public String getDescription() {
        return description;
    }

    public long getFilesize() {
        return filesize;
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

    public void setTime(String time) {
        this.time = time;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
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
}
