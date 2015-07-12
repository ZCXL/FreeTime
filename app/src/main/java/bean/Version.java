package bean;

import java.io.Serializable;

/**
 * Created by zhuchao on 7/12/15.
 */
public class Version extends BaseObject implements Serializable {
    private String versionId;
    private String versionDescription;
    private String versionUrl;
    private boolean available;
    private static final long serialVersionUID=-7620435178023928254L;
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
}
