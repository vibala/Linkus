package com.start_up.dev.apilinkus.Tool;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Huong on 22/01/2017.
 */

public class LockableScrollView extends ScrollView {


    // true if we can scroll (not locked)
    // false if we cannot scroll (locked)
    private boolean mScrollable = true;

    public LockableScrollView(Context context) {
        super(context);
    }

    public LockableScrollView(Context context, boolean mScrollable) {
        super(context);
        this.mScrollable = mScrollable;
    }

    public LockableScrollView(Context context, AttributeSet attrs, boolean mScrollable) {
        super(context, attrs);
        this.mScrollable = mScrollable;
    }

    public LockableScrollView(Context context, AttributeSet attrs, int defStyleAttr, boolean mScrollable) {
        super(context, attrs, defStyleAttr);
        this.mScrollable = mScrollable;
    }
/*
//Constructor that is called when inflating a view from XML.
View(Context context, AttributeSet attrs)

//Perform inflation from XML and apply a class-specific base style.
View(Context context, AttributeSet attrs, int defStyle)
*/
    public LockableScrollView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }
    public LockableScrollView(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    public void setScrollingEnabled(boolean enabled) {
        mScrollable = enabled;
    }

    public boolean isScrollable() {
        return mScrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // if we can scroll pass the event to the superclass
                if (mScrollable) return super.onTouchEvent(ev);
                // only continue to handle the touch event if scrolling enabled
                return mScrollable; // mScrollable is always false at this point
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        if (!mScrollable) return false;
        else return super.onInterceptTouchEvent(ev);
    }

}