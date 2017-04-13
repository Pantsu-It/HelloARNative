/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package cn.easyar.samples.helloar.view;



import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ResizeLayout extends LinearLayout{  
    private OnResizeListener mListener; 
     
    public interface OnResizeListener { 
        void OnResize(int w, int h, int oldw, int oldh); 
        void OnPreLayout(boolean changed, int l, int t, int r, int b);
        void OnPreMeasure(int widthMeasureSpec, int heightMeasureSpec);
    } 
     
    public void setOnResizeListener(OnResizeListener l) { 
        mListener = l; 
    } 
     
    public ResizeLayout(Context context, AttributeSet attrs) { 
        super(context, attrs); 
    } 
    @Override 
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) 
    {
    	if (mListener!=null) {
			mListener.OnPreMeasure(widthMeasureSpec, heightMeasureSpec);
		}
    	
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    @Override  
    protected void onLayout (boolean changed, int l, int t, int r, int b) {
    	if (mListener!=null) {
			mListener.OnPreLayout(changed, l, t, r, b);
		}
    	super.onLayout(changed, l, t, r, b);
    	
    }
    @Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {     
        super.onSizeChanged(w, h, oldw, oldh); 
//         System.out.println("w["+w+"]"+"h["+h+"]"+"oldw["+oldw+"]"+"oldh["+oldh+"]");
        if (mListener != null) { 
            mListener.OnResize(w, h, oldw, oldh); 
        } 
    } 
}