package function;

import android.content.Context;

import java.util.ArrayList;

import bean.BaseObject;

/**
 * Created by zhuchao on 7/12/15.
 */
public interface ParseXml {
    void Save(Context context,ArrayList<BaseObject>objects);
    ArrayList<BaseObject> Open(Context context);
    void Delete(Context context);
}
