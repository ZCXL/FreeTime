package bean;

/**
 * Created by zhuchao on 7/13/15.
 */
public abstract class BaseObjects {
    /**
     * Get the total of current objects.
     * @return
     */
    public abstract int getCount();

    /**
     * Get the item where is at index,and you must check the bundary.
     * @param index
     * @return
     */
    public abstract BaseObject getItem(int index);
}
