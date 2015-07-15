package view_rewrite;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nineoldandroids.view.ViewHelper;
import com.zhuchao.freetime.R;

import listener.OnDistanceChangeListener;
import listener.OnUnpressedListener;
import utils.Utils;

/**
 * Created by zhuchao on 7/14/15.
 */
public class SlideDown extends CustomerView implements OnDistanceChangeListener{
    //Parameters that will be used to set interface value.
    private int lineLength;
    private int lineWidth;
    private int lineColor= Color.parseColor("#ffffff");//set default rgb value for line
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

    private OnUnpressedListener unpressedListener;

    public SlideDown(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(event.getY()>imageView.getY()&&event.getY()<imageView.getY()+imageView.getHeight()){
                    pressed=true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(pressed){
                    if(imageView.getListener()!=null)
                        imageView.getListener().onChangeOver(yCurrent-yInit);
                    pressed=false;
                }
                yCurrent=yInit;
                yLast=yInit;
                break;
            case MotionEvent.ACTION_MOVE:
                if(event.getY()<=getHeight()&&event.getY()>0){
                    if(pressed){
                        yLast=yCurrent;
                        yCurrent=event.getY();
                        float distance=yCurrent-yLast;
                        if(imageView.getListener()!=null)
                            imageView.getListener().onDistanceChanged(distance);
                    }
                }
                break;
        }
        return true;
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
        imageView.setListener(this);
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
    public OnUnpressedListener getUnpressedListener() {
        return unpressedListener;
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

    public void setUnpressedListener(OnUnpressedListener unpressedListener) {
        this.unpressedListener = unpressedListener;
    }

    /**
     * place the button at right position
     */
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
        }else if(imageView.getY()>yInit){
            paint.setColor(lineColor);
            paint.setStrokeWidth(Utils.dpToPx(2.33f, getResources()));
            canvas.drawLine(getWidth() / 2, 0, getWidth()/2, lineLength, paint);

            float division=yCurrent-yInit;

            if(division>0.0f){
                canvas.drawLine(getWidth()/2,0,getWidth()/2,lineLength+division,paint);
            }else{
                paint.setColor(getResources().getColor(android.R.color.transparent));
                canvas.drawLine(getWidth()/2,yCurrent,getWidth()/2,yInit,paint);
            }
        }else{
            paint.setColor(getResources().getColor(android.R.color.transparent));
            paint.setStrokeWidth(Utils.dpToPx(2.33f, getResources()));
            canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, lineLength, paint);

            paint.setColor(lineColor);
            canvas.drawLine(getWidth() / 2, 0, getWidth()/2, yCurrent+Utils.dpToPx(9,getResources()), paint);
        }
        invalidate();
    }
    @Override
    public void onDistanceChanged(float changedValue) {
        ViewHelper.setY(imageView,imageView.getY()+changedValue);
    }

    @Override
    public void onChangeOver(float distance) {
        float distanceTemp=distance;
        while(Math.abs(distanceTemp)>1f){
            distanceTemp=-distanceTemp/2;
            ViewHelper.setY(imageView,yInit+distanceTemp);
        }
        ViewHelper.setY(imageView,yInit);
        if(distance>min_distance){
            if(unpressedListener!=null)
                unpressedListener.onUnpressed(true);
        }else{
            if(unpressedListener!=null)
                unpressedListener.onUnpressed(false);
        }
    }

    public class FlatImage extends ImageView{
        //Listener for updating image's position.
        private OnDistanceChangeListener listener;
        public FlatImage(Context context) {
            super(context);
        }

        public OnDistanceChangeListener getListener() {
            return listener;
        }

        public void setListener(OnDistanceChangeListener listener) {
            this.listener = listener;
        }
    }
}
