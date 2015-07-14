package bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import function.ParseJson;

/**
 * Created by zhuchao on 7/13/15.
 */
public class Versions extends BaseObjects implements ParseJson {
    private static String TAG="VersionProcessJson";
    private ArrayList<BaseObject>versions;
    public Versions(String c){
        versions=getObjects(c);
    }

    @Override
    public ArrayList<BaseObject> getObjects(String c) {
        /**
         * deal with String c for version
         * Created by LMZ on 7/13/15
         * modified by LMZ on 7/14/15
         */
        ArrayList<BaseObject> list = new ArrayList<BaseObject>();
        Version version;
        try {
            JSONArray jsonArray = new JSONArray(c);
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                version = new Version();
                version.setVersionId(jsonObject.getString("versionId"));
                version.setVersionDescription(jsonObject.getString("versiondescription"));
                version.setVersionUrl(jsonObject.getString("versionurl"));
                version.setAvailable(jsonObject.getBoolean("available"));
                list.add(version);
            }
            return list;
        } catch (JSONException e) {
            Log.d(TAG, e.toString()+"version of json error");
            return  null;
        }
    }
    /**
     * get versions' counts
     * @return int
     * Created by LMZ on 7/13/15
     */
    @Override
    public int getCount() {
        if (versions == null)
            return -1;
        else
            return versions.size();
    }
    /**
     * get instant version's item
     * @param index
     * @return
     * Created by LMZ on 7/13/15
     */
    @Override
    public BaseObject getItem(int index) {
        if (versions == null)
            return null;
        else
            return versions.get(index);
    }
}
