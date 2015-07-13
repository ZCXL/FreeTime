package bean;

import java.util.ArrayList;

import function.ParseJson;

/**
 * Created by zhuchao on 7/13/15.
 */
public class Versions extends BaseObjects implements ParseJson {
    private ArrayList<BaseObject>versions;
    public Versions(String c){
        versions=getObjects(c);
    }
    @Override
    public ArrayList<BaseObject> getObjects(String c) {
        return null;
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
