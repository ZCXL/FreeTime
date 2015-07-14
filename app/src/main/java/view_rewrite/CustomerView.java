package view_rewrite;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by zhuchao on 7/14/15.
 */
public abstract class CustomerView extends RelativeLayout {
    final static String ANDROIDXML="http://schemas.android.com/apk/res/android";
    final static String CUSTOMREXML="http://schemas.android.com/apk/res-auto";
    public CustomerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    abstract void setAttributes(AttributeSet attributes);
}
