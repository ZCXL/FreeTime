package bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhuchao on 7/12/15.
 */
public class Version extends BaseObject implements Parcelable{
    private String versionId;
    private String versionDescription;
    private String versionUrl;
    private boolean available;
    public Version(){
        super(TYPE.VERSION);
    }
    public String getVersionId() {
        return versionId;
    }

    public String getVersionDescription() {
        return versionDescription;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public void setVersionDescription(String versionDescription) {
        this.versionDescription = versionDescription;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(versionId);
        dest.writeString(versionDescription);
        dest.writeString(versionUrl);
        //dest.writeBooleanArray(new boolean[]{available});
    }
    public static final Parcelable.Creator<Version>CREATOR=new Creator<Version>() {
        @Override
        public Version createFromParcel(Parcel source) {
            return new Version(source);
        }

        @Override
        public Version[] newArray(int size) {
            return new Version[size];
        }
    };
    private Version(Parcel in){
        super(TYPE.VERSION);
        versionId=in.readString();
        versionDescription=in.readString();
        versionUrl=in.readString();
//        available=in.re;
    }
}
