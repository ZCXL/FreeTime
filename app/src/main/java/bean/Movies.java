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
    /**
     * get movies counts
     * @return
     * Created by LMZ on 7/13/15
     */
    @Override
    public int getCount() {
        if(movies==null)
            return -1;
        else
            return movies.size();
    }

    @Override
    public BaseObject getItem(int index) {
        if(movies==null)
            return null;
        else
            return movies.get(index);
    }
}
