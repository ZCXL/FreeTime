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
    private static String TAG="ProcessJson";
    private ArrayList<BaseObject>versions;
    public Versions(String c){
        versions=getObjects(c);
    }
    @Override
    public ArrayList<BaseObject> getObjects(String c) {
        /**
         * deal with String c for version
         * Created by LMZ on 7/13/15
         */
        ArrayList<BaseObject> list = new ArrayList<BaseObject>();
        Version version;
        try {
            JSONObject object = new JSONObject(c);
            JSONArray jsonArray = object.getJSONArray("V");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                version = new Version();
                version.setVersionDescription(jsonObject.getString("versionDescription"));
                version.setVersionId(jsonObject.getString("versionId"));
                version.setVersionUrl(jsonObject.getString("versionUrl"));
                list.add(version);
            }
            return list;
        } catch (JSONException e) {
            Log.d(TAG, e.toString() + "version of json fault");
            return null;
        }
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public BaseObject getItem(int index) {
        return null;
    }
}
