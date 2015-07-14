package view_rewrite;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by zhuchao on 7/14/15.
 */
public class SlideDown extends CustomerView {
    //Parameters that will be used to set interface value.
    private int lineLength;
    private int lineWidth;
    private int lineColor= Color.parseColor("#ffffff");
    private int max_distance=100;
    private int min_distance=50;

    private boolean pressed;
    private int yLast;
    private int yCurrent;
    private FlatImage imageView;
    private int image_button_source;
    public SlideDown(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);
    }

    @Override
    void setAttributes(AttributeSet attributes) {
        setBackgroundResource(android.R.color.transparent);


        //get background value from attributes,and set it as background
        int backgroundColor=attributes.getAttributeResourceValue(ANDROIDXML,"background",-1);
        if(backgroundColor!=-1){
            setBackgroundColor(getResources().getColor(backgroundColor));
        }else{
            int background=attributes.getAttributeIntValue(ANDROIDXML,"background",-1);
            if(background!=-1)
                setBackgroundColor(background);
        }


        //get line color
        int lineColorTemp=attributes.getAttributeResourceValue(CUSTOMREXML,"line_color",-1);
        if(lineColorTemp!=-1){
            lineColor=getResources().getColor(lineColorTemp);
        }else{
            lineColorTemp=attributes.getAttributeIntValue(CUSTOMREXML,"line_color",-1);
            if(lineColorTemp!=-1)
                lineColor=lineColorTemp;
        }

        //get line length
        lineLength=attributes.getAttributeIntValue(CUSTOMREXML, "line_length", 0);
        //get line width
        lineWidth=attributes.getAttributeIntValue(CUSTOMREXML, "line_width", 0);
        //get max slide distance
        max_distance=attributes.getAttributeResourceValue(CUSTOMREXML,"max_distance",0);
        //get min slide distance
        min_distance=attributes.getAttributeResourceValue(CUSTOMREXML,"min_distance",0);

        //set image of button
        image_button_source=attributes.getAttributeResourceValue(CUSTOMREXML,"",-1);
        imageView=new FlatImage(getContext());
        imageView.setImageResource(image_button_source);

    }

    public void setMax_distance(int max_distance) {
        this.max_distance = max_distance;
    }

    public void setMin_distance(int min_distance) {
        this.min_distance = min_distance;
    }

    public int getMin_distance() {
        return min_distance;
    }

    public int getMax_distance() {
        return max_distance;
    }

    public int getLineLength() {
        return lineLength;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineLength(int lineLength) {
        this.lineLength = lineLength;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }
    public class FlatImage extends ImageView{
        public FlatImage(Context context) {
            super(context);
        }
        @Override
        public boolean onTouchEvent(MotionEvent event){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
            }
            return true;
        }
    }

    /**
     * distance change listener used to change the length of line and position of button.
     * @author zhuchao 2015/7/14
     */
    public interface OnDistanceChangeListener{
        void onDistanceChanged(int changedValue);
    }
}
