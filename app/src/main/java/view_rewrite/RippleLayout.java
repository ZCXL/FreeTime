package view_rewrite;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zhuchao.freetime.R;

/**
 * Created by zhuchao on 7/16/15.
 */
public class RippleLayout extends CustomerView {
    //onClickListener
    private OnClickListener listener;
    //pressed or not
    private boolean pressed=false;
    //the color of ripple layout
    private int color;
    //the speed of ripple layout
    private int speed;
    //x of starting animation
    private float x;
    //y of starting animation
    private float y;
    //margin
    private float yTop,yBottom,xLeft,xRight;
    //radius
    private float radius;

    private boolean isOver=true;

    public RippleLayout(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
        setAttributes(attributeSet);
    }
    @Override
    void setAttributes(AttributeSet attributes) {
        TypedArray array=getContext().obtainStyledAttributes(attributes, R.styleable.rippleLayout);
        //get color
        color=array.getColor(R.styleable.rippleLayout_color,0);
        //get speed
        speed=array.getInt(R.styleable.rippleLayout_speed,1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x=event.getX();
                y=event.getY();
                xLeft=x;
                xRight=getWidth()-x;
                yTop=y;
                yBottom=getHeight()-y;
                pressed = true;
                isOver=false;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                pressed=false;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return true;
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(!isOver){
            Paint paint=new Paint();
            paint.setAntiAlias(true);
            paint.setColor(makePressColor());
            canvas.drawCircle(x,y,radius,paint);
            if(radius<Math.max(getWidth()-x,x)){
                radius+=speed;
            }else{
                radius=1;
                isOver=true;
                xLeft=0f;
                xRight=0f;
                yBottom=0f;
                yTop=0f;
                if(listener!=null)
                    listener.onClick(this);
            }
            invalidate();
        }
    }
    public OnClickListener getListener() {
        return listener;
    }

    public int getColor() {
        return color;
    }

    public int getSpeed() {
        return speed;
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    private int makePressColor(){
        int r=(color>>16) & 0xFF;
        int g=(color>>8) & 0xFF;
        int b=(color>>0) & 0xFF;
        r = (r - 30 < 0) ? 0 : r - 30;
        g = (g - 30 < 0) ? 0 : g - 30;
        b = (b - 30 < 0) ? 0 : b - 30;
        return Color.rgb(r, g, b);
    }
}
