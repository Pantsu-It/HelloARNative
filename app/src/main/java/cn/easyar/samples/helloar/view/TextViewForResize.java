/*
 * Copyright 2014 Hieu Rocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.easyar.samples.helloar.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com).
 */
public class TextViewForResize extends TextView {
	public interface ISizeChangedListener{
		public void onSizeChanged();
	}
	ISizeChangedListener msizelis;
	boolean misMeasured;
    public TextViewForResize(Context context) {
        super(context);

    } 

    public TextViewForResize(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TextViewForResize(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }
    
    private void init(AttributeSet attrs) {
        
    }
    public void setSizeChangedListener( ISizeChangedListener iea ){
    	msizelis=iea;
    }
    
    public void setIsMeasured(boolean im){
    	misMeasured=im;
    }
    public boolean getIsMeasured(){
    	return misMeasured;
    }
    
    @Override 
    protected void onTextChanged (CharSequence text, int start, int lengthBefore, int lengthAfter){
    	misMeasured=false;
    	super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

//    @Override 
//    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){
//    	int oldw=getMeasuredWidth();
//    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    	int neww=getMeasuredWidth();
//    	if (oldw!=neww) {
//    		if (msizelis != null) { 
//            	msizelis.onSizeChanged(); 
//            } 
//		}
//    }
    @Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {     
        super.onSizeChanged(w, h, oldw, oldh); 
//         System.out.println("w["+w+"]"+"h["+h+"]"+"oldw["+oldw+"]"+"oldh["+oldh+"]");
        if (msizelis != null) { 
        	msizelis.onSizeChanged(); 
        } 
    } 	
    
}
