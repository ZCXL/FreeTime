package view_rewrite;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhuchao.freetime.R;

import utils.Utils;

/**
 * Created by zhuchao on 7/15/15.
 */
public class RippleImage extends CustomerView {
    //ripple's color
    private int rippleColor;
    //image button's width
    private int imageWidth;
    //image button's height
    private int imageHeight;
    //image's source
    private int imageSource;
    //x coordinate of ripple circle
    private float x;
    //y coordinate of ripple circle
    private float y;
    //speed of ripple
    private int speed=3;
    //is pressed
    private boolean pressed;
    //is over
    private boolean isOver=true;
    //radius of ripple
    private float radius;
    //listener
    private OnClickListener onClickListener;
    private ImageView imageView;
    public RippleImage (Context context, AttributeSet attrs){
        super(context,attrs);
        setAttributes(attrs);
    }
    @Override
    void setAttributes(AttributeSet attributes) {
        //Set parent style
        setBackgroundResource(android.R.color.transparent);


        TypedArray array=getContext().obtainStyledAttributes(attributes, R.styleable.rippleImageView);

        //ripple's color
        rippleColor=array.getColor(R.styleable.rippleImageView_ripple_color, -1);
        //ripple's width
        imageWidth=array.getDimensionPixelSize(R.styleable.rippleImageView_image_width, 0);
        //ripple's height
        imageHeight=array.getDimensionPixelSize(R.styleable.rippleImageView_image_height, 0);
        //ripple's speed
        speed=array.getInt(R.styleable.rippleImageView_ripple_speed,0);
        //image's source
        imageSource=attributes.getAttributeResourceValue(CUSTOMREXML,"image",-1);

        setLayoutParams(new ViewGroup.LayoutParams(Utils.dpToPx(imageHeight+20,getResources()),Utils.dpToPx(imageWidth+20,getResources())));
        //add imageView
        imageView=new ImageView(getContext());
        imageView.setImageResource(imageSource);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(Utils.dpToPx(imageWidth,getResources()),Utils.dpToPx(imageHeight,getResources()));
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        imageView.setLayoutParams(params);
        addView(imageView);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(event.getX()>imageView.getX()&&event.getX()<imageView.getX()+imageView.getWidth()&&event.getY()>imageView.getY()&&event.getY()<imageView.getY()+imageView.getHeight()) {
                    radius=imageView.getWidth()/2;
                    x=getHeight()/2;
                    y=getWidth()/2;
                    pressed = true;
                    isOver=false;
                    invalidate();
                }
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
            if(radius<getHeight()/2){
                radius+=speed;
            }else{
                radius=1;
                isOver=true;
                if(onClickListener!=null)
                    onClickListener.onClick(this);
            }
            invalidate();
        }
    }
    public int getImageSource() {
        return imageSource;
    }

    public int getRippleColor() {
        return rippleColor;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }
    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public int getSpeed() {
        return speed;
    }
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public void setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private int makePressColor(){
        int r=(rippleColor>>16) & 0xFF;
        int g=(rippleColor>>8) & 0xFF;
        int b=(rippleColor>>0) & 0xFF;
        r = (r - 30 < 0) ? 0 : r - 30;
        g = (g - 30 < 0) ? 0 : g - 30;
        b = (b - 30 < 0) ? 0 : b - 30;
        return Color.rgb(r, g, b);
    }
}
