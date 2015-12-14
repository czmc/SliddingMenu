package me.czmc.sliddingmenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

import me.czmc.sliddingmenu.R;

/**
 * Created by czmz on 15/12/13.
 */
public class SliddingView extends HorizontalScrollView{
    private int screenWidth;
    private int rightPadding;
    private int menuWidth;
    private int lastScrollX = 0;

    private LinearLayout menuWrapper;
    private ViewGroup menu;
    private ViewGroup content;

    private boolean isOnce = true;
    private boolean isOpen;

    public SliddingView(Context context) {
        this(context, null);
    }

    public SliddingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SliddingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //得到自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SliddingView,defStyleAttr,0);
        rightPadding = ta.getDimensionPixelSize(R.styleable.SliddingView_padding_right, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,context.getResources().getDisplayMetrics()));
        ta.recycle();

        //得到屏幕尺寸
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;

    }

    /**
     * 测量菜单区和内容区的宽度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(isOnce) {
            menuWrapper = (LinearLayout)getChildAt(0);
            menu = (ViewGroup)menuWrapper.getChildAt(0);
            content = (ViewGroup)menuWrapper.getChildAt(1);

            menuWidth =menu.getLayoutParams().width=screenWidth-rightPadding;
            content.getLayoutParams().width = screenWidth;
            menuWrapper.getLayoutParams().width=menuWidth+ screenWidth;

            isOnce = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            this.scrollTo(menuWidth,0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastScrollX = getScrollX();
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                int moveX = lastScrollX - scrollX;
                if(!isOpen) {
                    if (moveX < 100) {
                        this.smoothScrollTo(screenWidth, 0);
                        isOpen = false;
                    } else {
                        this.smoothScrollTo(0, 0);
                        isOpen = true;
                    }
                }else{
                    if (moveX < -100) {
                        this.smoothScrollTo(screenWidth, 0);
                        isOpen = false;
                    } else {
                        this.smoothScrollTo(0, 0);
                        isOpen = true;
                    }
                }
                return true;//不加上的话，smoothScrollTo无法正常进行。
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 打开菜单栏
     */
    public void openMenu(){
        if(isOpen) return ;
        smoothScrollTo(0,0);
        isOpen=true;
    }

    /**
     * 关闭菜单栏
     */
    public void closeMenu(){
        if(!isOpen) return ;
        smoothScrollTo(menuWidth,0);
        isOpen = false;
    }

    /**
     * 切换菜单栏
     */
    public void toggleMenu(){
        if(isOpen){
            closeMenu();
        }
        else {
            openMenu();
        }
    }

    /**
     * 滚动动画设置
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        //1.0~0之间，易于动画设置
        float scale = l*1.0f/menuWidth;
        float rightScale = 0.7f+0.3f*scale;
        float leftScale = 1-0.7f*scale;
        float leftAlpha = 0.6f+0.4f*(1.0f-scale);

        //菜单栏动画设置
        // 低版本适配
        ViewHelper.setTranslationX(menu,menuWidth * scale * 0.4f);
        ViewHelper.setScaleX(menu,leftScale);
        ViewHelper.setScaleY(menu,leftScale);
        ViewHelper.setAlpha(menu,leftAlpha);

//        menu.setTranslationX(menuWidth * scale * 0.4f);
//        menu.setScaleX(leftScale);
//        menu.setScaleY(leftScale);
//        menu.setAlpha(leftAlpha);

        //主界面动画设置
        // 低版本适配
        ViewHelper.setPivotX(content,0);
        ViewHelper.setPivotY(content,content.getHeight()/2);
        ViewHelper.setScaleX(content,rightScale);
        ViewHelper.setScaleY(content,rightScale);

//        content.setPivotX(0);
//        content.setPivotY(content.getHeight()/2);
//        content.setScaleX(rightScale);
//        content.setScaleY(rightScale);

        super.onScrollChanged(l, t, oldl, oldt);
    }
}
