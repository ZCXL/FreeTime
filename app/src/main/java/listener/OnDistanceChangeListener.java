package listener;

/**
 * distance change listener used to change the length of line and position of button.
 * @author zhuchao 2015/7/14
 */
public interface OnDistanceChangeListener{
    /**
     * change image button position
     * @param changedValue
     */
    void onDistanceChanged(float changedValue);

    /**
     * when the change is over,backup the position.
     * @param distance
     */
    void onChangeOver(float distance);
}