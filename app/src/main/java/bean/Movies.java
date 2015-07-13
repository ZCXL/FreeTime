package bean;

import java.util.ArrayList;

/**
 * Created by zhuchao on 7/13/15.
 */
public class Movies extends BaseObjects {
    private ArrayList<BaseObject>movies;
    public Movies(){
        super();
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
