package bean;

import android.content.Context;

import java.util.ArrayList;

import function.SaveAndOpenMovies;

/**
 * Created by zhuchao on 7/13/15.
 */
public class Movies extends BaseObjects {
    private SaveAndOpenMovies saveAndOpenMovies;
    private ArrayList<BaseObject>movies;
    public Movies(){
        super();
    }
    public Movies(Context context){
        super();
        saveAndOpenMovies=new SaveAndOpenMovies();
        movies=saveAndOpenMovies.Open(context);
    }
    /**
     * get movies' counts
     * @return int
     * Created by LMZ on 7/13/15
     */
    @Override
    public int getCount() {
        if(movies==null)
            return -1;
        else
            return movies.size();
    }

    /**
     * get instant movie's item
     * @param index
     * @return
     * Created by LMZ on 7/13/15
     */
    @Override
    public BaseObject getItem(int index) {
        if(movies==null)
            return null;
        else
            return movies.get(index);
    }
}
