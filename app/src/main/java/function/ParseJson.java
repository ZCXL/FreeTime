package function;

import org.json.JSONException;

import java.util.ArrayList;

import bean.BaseObject;
/**
 * Created by zhuchao on 7/13/15.
 */
public interface ParseJson {
    /**
     * You must implement this function to parse string to base objects.
     * @param c
     * @return
     */
     ArrayList<BaseObject> getObjects(String c) throws JSONException;
}
