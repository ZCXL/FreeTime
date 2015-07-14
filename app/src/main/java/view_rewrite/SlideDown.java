package view_rewrite;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;
import com.zhuchao.freetime.R;

import listener.OnDistanceChangeListener;
import utils.Utils;

/**
 * Created by zhuchao on 7/14/15.
 */
public class SlideDown extends CustomerView implements OnDistanceChangeListener{
    //Parameters that will be used to set interface value.
    private int lineLength;
    private int lineWidth;
    private int lineColor= Color.parseColor("#ffffff");
    private int max_distance=100;
    private int min_distance=50;

    private boolean pressed;
    private boolean isPlaced=false;
    private Bitmap bitmap;
    //The last position of image button.
    private float yLast;

    //The current position of image button.
    private float yCurrent;

    //The starting position of image button.
    private float yInit;

    private FlatImage imageView;
    private int image_button_source;

    private static int TO_UP=1;
    private static int TO_DOWN=2;
    private OnDistanceChangeListener listener;
    public SlideDown(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);
    }

    @Override
    public void invalidate(){
        super.invalidate();
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
        TypedArray array=getContext().obtainStyledAttributes(attributes, R.styleable.CustomerAttributes);
        //get line color
        lineColor=array.getColor(R.styleable.CustomerAttributes_line_color, -1);
        //int lineColorTemp=attributes.getAttributeResourceValue(CUSTOMREXML,"line_color",-1);
        /*if(lineColorTemp!=-1){
            lineColor=getResources().getColor(lineColorTemp);
        }else{
            lineColorTemp=attributes.getAttributeIntValue(CUSTOMREXML,"line_color",-1);
            if(lineColorTemp!=-1)
                lineColor=lineColorTemp;
        }*/

        //get line length
        //lineLength=attributes.getAttributeIntValue(CUSTOMREXML, "line_length", 0);
        lineLength=array.getDimensionPixelSize(R.styleable.CustomerAttributes_line_length, 0);
        //Toast.makeText(getContext(),lineLength,Toast.LENGTH_LONG).show();

        //get line width
        //lineWidth=attributes.getAttributeIntValue(CUSTOMREXML, "line_width", 0);
        lineWidth=array.getDimensionPixelSize(R.styleable.CustomerAttributes_line_width, 0);

        //get max slide distance
        //max_distance=attributes.getAttributeResourceValue(CUSTOMREXML,"max_distance",0);
        max_distance=array.getDimensionPixelSize(R.styleable.CustomerAttributes_max_distance, 0);

        //get min slide distance
        //min_distance=attributes.getAttributeResourceValue(CUSTOMREXML,"min_distance",0);
        min_distance=array.getDimensionPixelSize(R.styleable.CustomerAttributes_min_distance, 0);

        //set image of button
        image_button_source=attributes.getAttributeResourceValue(CUSTOMREXML,"source_button",-1);
        imageView=new FlatImage(getContext());
        imageView.setImageResource(image_button_source);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(Utils.dpToPx(51f,getResources()),Utils.dpToPx(51f,getResources()));
        params.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        imageView.setLayoutParams(params);
        addView(imageView);

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
    private void placeButton(){
        ViewHelper.setX(imageView,getWidth()/2-imageView.getWidth()/2);
        ViewHelper.setY(imageView,lineLength-Utils.dpToPx(9,getResources()));
        yInit=imageView.getY();
        yCurrent=yInit;
        yLast=yInit;
        isPlaced=true;
    }

    @Override
    protected  void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(!isPlaced)
            placeButton();
        Paint paint=new Paint();
        if(imageView.getY()==yInit) {
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas temp = new Canvas(bitmap);
            paint.setColor(lineColor);
            paint.setStrokeWidth(Utils.dpToPx(2.33f, getResources()));
            temp.drawLine(getWidth() / 2, 0, getWidth()/2, lineLength, paint);
            canvas.drawBitmap(bitmap, 0, 0, new Paint());
        }else{
            paint.setColor(lineColor);
            paint.setStrokeWidth(Utils.dpToPx(2.33f, getResources()));
            canvas.drawLine(getWidth() / 2, 0, getWidth()/2, lineLength, paint);

            float division=yCurrent-yInit;

            if(division>0.0f){
                canvas.drawLine(getWidth()/2,0,getWidth()/2,lineLength+division,paint);
            }else{
                paint.setColor(getResources().getColor(android.R.color.transparent));
                canvas.drawLine(getWidth()/2,yCurrent,getWidth()/2,division,paint);
            }
        }
        invalidate();
    }
    @Override
    public void onDistanceChanged(float changedValue) {
        if(changedValue>0.0f){
            ViewHelper.setY(imageView,yInit+changedValue);
        }else{
            ViewHelper.setY(imageView,yInit-changedValue);
        }
    }

    @Override
    public void onChangeOver(int flag, float distance) {

    }

    public class FlatImage extends ImageView{
        public FlatImage(Context context) {
            super(context);
        }
        @Override
        public boolean onTouchEvent(MotionEvent event){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    yCurrent=event.getY();
                    yLast=yCurrent;
                    pressed=true;
                    break;
                case MotionEvent.ACTION_UP:
                    yCurrent=event.getY();
                    float distance=yCurrent-yInit;
                    if(distance>0){
                        if(listener!=null)
                            listener.onChangeOver(1,distance);
                    }else{
                        if(listener!=null)
                            listener.onChangeOver(2,distance);
                    }
                    pressed=false;
                    yCurrent=yInit;
                    yLast=yInit;
                    break;
                case MotionEvent.ACTION_MOVE:
                    yCurrent=event.getY();
                    float distance1=yCurrent-yLast;
                    yLast=yCurrent;
                    if(listener!=null)
                        listener.onDistanceChanged(distance1);
                    break;
            }
            return true;
        }
    }
}
