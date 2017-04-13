/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */

package cn.easyar.samples.helloar.view;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.easyar.samples.helloar.R;

/**
 * @author lid 设计思想 标题栏内部分3个容器，左侧 右侧 中间，可以分别向其中添加action控件
 */
// @TargetApi(Build.VERSION_CODES.HONEYCOMB)
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CommonTitleView extends LinearLayout {
	Context mctx;
	ResizeLayout mLeftGroupLayout;
	LinearLayout mRightGroupLayout;
	ResizeLayout mmiddleGroupLayout;
	ResizeLayout mmiddleGroupLayoutForAdd;
	TextView mCenterTextView;
	ImageButton mCenterHelpEntryIB;
	ProgressBar progressBarState;
	DisplayMetrics mdmDisplayMetrics;
	int mActionBtnWidth;
	Resources mResources;
	Handler mHandler;
	int mMiddleWidth;

    /*add by zhonggy*/
    private ImageView mLeftIcon;
    private ImageView mRightIcon;

	private View mMoreBtn;//add by wubb

    public CommonTitleView(Context context) {
        super(context);
        init(context, null);
    }

	public CommonTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public CommonTitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		mHandler = new Handler();
		mctx = context;
		View layout = LayoutInflater.from(context).inflate(R.layout.title_common, null);
		addView(layout);
		setBackgroundResource(R.drawable.new_action_bar_bg);
		mCenterTextView = (TextView) layout.findViewById(R.id.txtCenter);
		mCenterHelpEntryIB = (ImageButton) layout.findViewById(R.id.centerHelpEntryIB);
		progressBarState = (ProgressBar) layout.findViewById(R.id.progressBarState);
		mLeftGroupLayout = (ResizeLayout) layout.findViewById(R.id.title_leftgroup);
		mLeftGroupLayout.setOnResizeListener(new ResizeLayout.OnResizeListener() {

			@Override
			public void OnResize(int w, int h, int oldw, int oldh) {
				// TODO Auto-generated method stub
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						updateMiddleWidth();
					}
				});
			}

			@Override
			public void OnPreLayout(boolean changed, int l, int t, int r, int b) {
				// TODO Auto-generated method stub
				// FCLog.d("OnPreLayout", 1);
			}

			@Override
			public void OnPreMeasure(int widthMeasureSpec, int heightMeasureSpec) {
				// layoutBottom(mIsBottomVisiable);
				int mode = MeasureSpec.getMode(widthMeasureSpec);
				int w = MeasureSpec.getSize(widthMeasureSpec);
				int h = MeasureSpec.getSize(heightMeasureSpec);
				// FCLog.d("OnPreMeasure", 1);
			}
		});

		mRightGroupLayout = (LinearLayout) layout.findViewById(R.id.title_rightgroup);
		mmiddleGroupLayout = (ResizeLayout) layout.findViewById(R.id.title_middlegroup);
		mmiddleGroupLayoutForAdd = (ResizeLayout) layout.findViewById(R.id.title_middlegroupForAdd);
		// mmiddleGroupLayoutForAdd.setOnResizeListener(new OnResizeListener() {
		//
		// @Override
		// public void OnResize(int w, int h, int oldw, int oldh) {
		// // TODO Auto-generated method stub
		// mHandler.post(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// updateMiddleTextMaxWidth();
		// }
		// });
		// }
		// });
		mmiddleGroupLayout.setOnResizeListener(new ResizeLayout.OnResizeListener() {

			@Override
			public void OnResize(int w, int h, int oldw, int oldh) {
				// TODO Auto-generated method stub
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						updateMiddleTextMaxWidth();
					}
				});
			}

			@Override
			public void OnPreLayout(boolean changed, int l, int t, int r, int b) {
				// TODO Auto-generated method stub
				// FCLog.d("OnPreLayout", 1);
			}

			@Override
			public void OnPreMeasure(int widthMeasureSpec, int heightMeasureSpec) {
				// layoutBottom(mIsBottomVisiable);
				int mode = MeasureSpec.getMode(widthMeasureSpec);
				int w = MeasureSpec.getSize(widthMeasureSpec);
				int h = MeasureSpec.getSize(heightMeasureSpec);
				// FCLog.d("OnPreMeasure", 1);
			}
		});
		mdmDisplayMetrics = context.getResources().getDisplayMetrics();
		mActionBtnWidth = (int) (40 * mdmDisplayMetrics.density + 0.5);
		mResources = context.getResources();
	}
    public void addCustomMiddleView(View v){
    	mmiddleGroupLayout.addView(v);
    	mmiddleGroupLayoutForAdd.setVisibility(View.GONE);
    }
	/**
	 * 获取 title中间的文本控件
	 */
	public TextView getCenterTxtView() {
		return mCenterTextView;
	}

	/**
	 * 获取 title中间文本控件左侧的菊花控件
	 */
	public ProgressBar getProgressBar() {
		return progressBarState;
	}

	/**
	 * 获取 title中间文本控件的文本
	 */
	public void setMiddleText(String txt) {
		mCenterTextView.setText(txt);
	}

	/**
	 *
	 * @param visible
	 * @param lis
	 * @return 中间帮助系统入口的view
	 */
	public ImageButton setMiddleHelpVisible(boolean visible,OnClickListener lis){
		if(visible){
			mCenterHelpEntryIB.setVisibility(View.VISIBLE);
			mCenterHelpEntryIB.setOnClickListener(lis);
		}else{
			mCenterHelpEntryIB.setOnClickListener(null);
			mCenterHelpEntryIB.setVisibility(View.GONE);
		}
		return  mCenterHelpEntryIB;
	}
	public void setTitle(String txt) {
		mCenterTextView.setText(txt);
	}

	void updateMiddleWidth() {
		int leftwidth = mLeftGroupLayout.getWidth();
		int w = (mdmDisplayMetrics.widthPixels / 2 - (leftwidth + (int) (1 * mdmDisplayMetrics.density))) * 2;
		setMiddleWidth(w);
	}

	// public void setMiddleTextMaxWidth(int wdp){
	// mCenterTextView.setMaxWidth((int)(wdp*mdmDisplayMetrics.density));
	// }
	public void updateMiddleTextMaxWidth() {
		int count = mmiddleGroupLayoutForAdd.getChildCount();
		int reduceW = 0;
		for (int i = 0; i < count; i++) {
			View v = mmiddleGroupLayoutForAdd.getChildAt(i);
			if (v.getId() != R.id.txtCenter && v.getVisibility() != View.GONE) {
				int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
				int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
				v.measure(w, h);

				reduceW += v.getMeasuredWidth();
			}

		}
		if (reduceW > 0 && mMiddleWidth > 0) {

			mCenterTextView.setMaxWidth(mMiddleWidth - reduceW - (int) (5 * mdmDisplayMetrics.density));
		}
	}

	/**
	 * 获取 title中间文本控件的文本
	 */
	void setMiddleWidth(int wpx) {
		ViewGroup.LayoutParams params2 = mmiddleGroupLayout.getLayoutParams();
		params2.width = wpx;
		mMiddleWidth = wpx;
		mmiddleGroupLayout.setLayoutParams(params2);
	}

	/**
	 * 追加title中间控件组中的控件，比如“全部”右侧的小三角
	 */
	public TextViewForResize addMiddleTextView(int leftmargin, int width, int height, int bg_resid, OnClickListener lis) {
		LayoutParams params = new LayoutParams((int) (width * mdmDisplayMetrics.density), (int) (height * mdmDisplayMetrics.density));
		params.gravity = Gravity.CENTER_VERTICAL;
		final TextViewForResize btn = createTextView(leftmargin, bg_resid, 0, params, lis);
		ViewTreeObserver vto = btn.getViewTreeObserver();
		vto.addOnPreDrawListener(new OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				// TODO Auto-generated method stub
				if (!btn.getIsMeasured()) {

					updateMiddleTextMaxWidth();
					btn.setIsMeasured(true);
				} else {

				}

				return true;
			}
		});
		mmiddleGroupLayoutForAdd.addView(btn);
		return btn;
	}

	/**
	 * 追加title中间控件组中的控件，比如“全部”右侧的小三角
	 */
	public TextViewForResize addMiddleTextView(int leftmargin, int width, int height, int bg_resid, int topMargin, OnClickListener lis) {
		LayoutParams params = new LayoutParams((int) (width * mdmDisplayMetrics.density), (int) (height * mdmDisplayMetrics.density));
		params.gravity = Gravity.CENTER_VERTICAL;
		params.topMargin = (int) (topMargin * mdmDisplayMetrics.density);
		final TextViewForResize btn = createTextView(leftmargin, bg_resid, 0, params, lis);
		ViewTreeObserver vto = btn.getViewTreeObserver();
		vto.addOnPreDrawListener(new OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				// TODO Auto-generated method stub
				if (!btn.getIsMeasured()) {

					updateMiddleTextMaxWidth();
					btn.setIsMeasured(true);
				} else {

				}

				return true;
			}
		});
		mmiddleGroupLayoutForAdd.addView(btn);
		return btn;
	}

	/**
	 * 追加title中间控件组中的控件，比如“全部”右侧的小三角
	 */
	public TextViewForResize addMiddleTextView(int leftmargin, int rightmargin, int width, int height, int bg_resid, int topMargin, OnClickListener lis) {
		LayoutParams params = new LayoutParams((int) (width * mdmDisplayMetrics.density), (int) (height * mdmDisplayMetrics.density));
		params.gravity = Gravity.CENTER_VERTICAL;
		params.topMargin = (int) (topMargin * mdmDisplayMetrics.density);
		params.leftMargin = (int) (leftmargin * mdmDisplayMetrics.density);
		params.rightMargin = (int) (rightmargin * mdmDisplayMetrics.density);
		final TextViewForResize btn = createTextView(leftmargin, bg_resid, 0, params, lis);
		ViewTreeObserver vto = btn.getViewTreeObserver();
		vto.addOnPreDrawListener(new OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				// TODO Auto-generated method stub
				if (!btn.getIsMeasured()) {

					updateMiddleTextMaxWidth();
					btn.setIsMeasured(true);
				} else {

				}

				return true;
			}
		});
		mmiddleGroupLayoutForAdd.addView(btn);
		return btn;
	}

	public TextView addMiddleTextView(int leftmargin) {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_VERTICAL;
		final TextViewForResize btn = createTextView(leftmargin, 0, 0, params, null);
		// btn.setSizeChangedListener(new ISizeChangedListener() {
		//
		// @Override
		// public void onSizeChanged() {
		// // TODO Auto-generated method stub
		// updateMiddleTextMaxWidth();
		// }
		// });
		ViewTreeObserver vto = btn.getViewTreeObserver();
		vto.addOnPreDrawListener(new OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				// TODO Auto-generated method stub
				if (!btn.getIsMeasured()) {

					updateMiddleTextMaxWidth();
					btn.setIsMeasured(true);
				} else {

				}

				return true;
			}
		});
		// btn.setBackgroundColor(Color.BLUE);
		mmiddleGroupLayoutForAdd.addView(btn);
		return btn;
	}

	/**
	 * 获取中间控件组
	 */
	public View getMiddleLayout() {
		return mmiddleGroupLayout;
	}

	/**
	 * 获取右按钮组
	 *
	 * @return
	 */
	public ViewGroup getRightLayout() {
		return mRightGroupLayout;
	}

	/**
	 * 获取左按钮组
	 *
	 * @return
	 */
	public ViewGroup getLeftLayout() {
		return mLeftGroupLayout;
	}

	/**
	 * 追加左侧文字按钮控件，比如“返回”按钮
	 */
	public View addLeftAction(String txt, int lefticon, OnClickListener lis) {
		return addLeftAction(txt,R.drawable.btn_actionbar_top_bg,lefticon,lis);
	}

	/**
	 * 追加左侧文字按钮控件的重载方法，当标题和默认标题颜色不一致时使用，例如登录界面的橙色样式
	 */
	public View addLeftAction(String txt,int bg_resid, int lefticon, OnClickListener lis) {
		TextView btn = createTextView(0, bg_resid, lefticon, null, lis);
		setLeftActionStyle(btn);
		btn.setText( txt);
		mLeftGroupLayout.addView(btn);
		return btn;
	}

	/**
	 * 追加左侧文字按钮控件，比如“取消”按钮
	 */
	public View addLeftAction(String txt, OnClickListener lis) {
		TextView btn = createTextView(0, R.drawable.btn_actionbar_top_bg, 0, null, lis);
		setLeftActionStyle(btn);
		btn.setText(txt);
		mLeftGroupLayout.addView(btn);
		return btn;
	}

    public View addLeftBackAction(OnClickListener lis) {
        // TextView btn = createTextView(0, R.drawable.btn_actionbar_bg,
        // R.drawable.return_before_new_normal, null, lis);
        // setLeftActionStyle(btn);
        // mLeftGroupLayout.addView(btn);
        mLeftIcon = createImgView(R.drawable.return_before_new_normal, lis);
        mLeftGroupLayout.addView(mLeftIcon);
        return mLeftIcon;
    }

	/**
	 * 更新左侧文字按钮控件上的文本，比如 对话详情页，左上角显示“企信(5)”
	 */
	public void updateLeftActionText(String txt) {

		for (int i = 0; i < mLeftGroupLayout.getChildCount(); i++) {
			View v = mLeftGroupLayout.getChildAt(i);
			if (v instanceof TextView) {
				((TextView) v).setText(" " + txt);
				break;
			}
		}
	}

    /**
     * 追加左侧图标按钮控件，比如 发纷享 按钮
     */
    public ImageView addLeftAction(int icon, OnClickListener lis) {
        mLeftIcon = createImgView(icon, lis);
        mLeftGroupLayout.addView(mLeftIcon);
        return mLeftIcon;

	}

    /**
     * 追加左侧图标按钮控件，比如 发纷享 按钮 //无宽度
     */
    public ImageView addLeftActionEx(int icon, int width, OnClickListener lis) {
        mLeftIcon = createImgViewEx(icon, width, lis);
        mLeftGroupLayout.addView(mLeftIcon);
        return mLeftIcon;

    }

    /**
     * 设置字体颜色
     *
     * @param color add by zhonggy
     */
    public void setTextColor(int color) {
        mCenterTextView.setTextColor(color);
        setGroupTextColor(mLeftGroupLayout, color);
        setGroupTextColor(mRightGroupLayout, color);
    }

    /**
     * 设置左边和右边字体颜色
     *
     * @param viewGroup
     * @param color
     */
    public void setGroupTextColor(ViewGroup viewGroup, int color) {
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = viewGroup.getChildAt(i);
            if (childView instanceof TextView) {
                TextView textView = (TextView) childView;
                textView.setTextColor(color);
            }
        }
    }

//	/**
//	 * 追加旧版本风格的返回按钮控件，
//	 */
//	public ImageView addOldLeftAction(int icon, View.OnClickListener lis) {
//		ImageView iv = createOldLeftImgView(icon, lis);
//		mLeftGroupLayout.addView(iv);
//		return iv;
//
//	}

    /**
     * 追加右侧侧图标按钮控件，比如 对话详情页的右上角 群组详情 按钮
     */
    public View addRightAction(int icon, OnClickListener lis) {
        mRightIcon = createImgView(icon, lis);
        mRightGroupLayout.addView(mRightIcon, 0);
        return mRightIcon;
    }

	/**
	 * 追加右侧侧文本按钮控件，比如 修改群组名称页 右上角 的 确定 按钮
	 */
	public TextView addRightAction(String txt, OnClickListener lis) {
		TextView tv = createTextView(0, R.drawable.btn_actionbar_top_bg, 0, null, lis);
		setLeftActionStyle(tv);
		tv.setText(txt);
		mRightGroupLayout.addView(tv, 0);
		return tv;
	}

	/**
	 * 追加右侧自定义控件
	 */
	public void addRightAction(View v, OnClickListener lis) {
		LayoutParams params = new LayoutParams(mActionBtnWidth, LayoutParams.MATCH_PARENT);

		v.setLayoutParams(params);
		v.setOnClickListener(lis);
		mRightGroupLayout.addView(v, 0);
	}

	/**
	 * 设置title bar more 按钮
	 * add by wubb
	 * @param moreBtn
     */
	public void setMoreBtn(View moreBtn){
		mMoreBtn=moreBtn;
	}

	/**
	 * 获取more按钮引用
	 * add by wubb
	 * @return
     */
	public View getMoreBtn(){
		return mMoreBtn;
	}

	/**
	 * 移除more按钮
	 * add by wubb
	 */
	public void removeMoreBtn(){
		if(mMoreBtn!=null){
			mRightGroupLayout.removeView(mMoreBtn);
			mMoreBtn=null;
		}
	}

	/**
     * 删除所有左侧控件 by anjx 0825
     */
    public void removeAllLeftActions(){
        mLeftGroupLayout.removeAllViews();
		mMoreBtn=null;
    }


    /**
     * 删除所有右侧控件 by anjx 0825
     */
    public void removeAllRightActions() {
        mRightGroupLayout.removeAllViews();
    }

    public ImageView getLeftIcon() {
        return mLeftIcon;
    }

    public ImageView getRightIcon() {
        return mRightIcon;
    }

	ImageView createImgView(int icon, OnClickListener lis) {
		ImageView iv = new ImageView(mctx);
		LayoutParams params = new LayoutParams(mActionBtnWidth, LayoutParams.MATCH_PARENT);

		iv.setLayoutParams(params);
		iv.setImageResource(icon);
		iv.setScaleType(ScaleType.CENTER);
		iv.setBackgroundResource(R.drawable.btn_actionbar_top_bg);
		if (lis == null) {
			iv.setClickable(false);
		} else {

			iv.setOnClickListener(lis);
		}
		return iv;
	}

	// 追加左边无宽度图片
	ImageView createImgViewEx(int icon, int width, OnClickListener lis) {
		ImageView iv = new ImageView(mctx);
		LayoutParams params = new LayoutParams(width, LayoutParams.MATCH_PARENT);

		iv.setLayoutParams(params);
		iv.setImageResource(icon);
		iv.setScaleType(ScaleType.CENTER);
		iv.setBackgroundResource(R.drawable.btn_actionbar_top_bg);
		if (lis == null) {
			iv.setClickable(false);
		} else {

			iv.setOnClickListener(lis);
		}
		return iv;
	}

//	ImageView createOldLeftImgView(int icon, View.OnClickListener lis) {
//		ImageView iv = new ImageView(mctx);
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
//		params.leftMargin = StringUtils.dip2px(1);
//		iv.setLayoutParams(params);
//		iv.setImageResource(icon);
//		iv.setScaleType(ScaleType.CENTER);
//		iv.setBackgroundResource(R.drawable.btn_actionbar_bg);
//		if (lis == null) {
//			iv.setClickable(false);
//		} else {
//
//			iv.setOnClickListener(lis);
//		}
//		return iv;
//	}

	void setLeftActionStyle(TextView tv) {
		tv.setPadding((int) (8 * mdmDisplayMetrics.density + 0.5), 0, (int) (8 * mdmDisplayMetrics.density + 0.5), 0);
	}

	static public void setActionButtonStyle(TextView btn, Context ctx) {
		btn.setBackgroundResource(R.drawable.btn_actionbar_top_bg);
		btn.setTextColor(ctx.getResources().getColor(R.color.title_text_backcolor));
		btn.setTextSize(16);
		btn.setTextColor(ctx.getResources().getColor(R.color.title_text_color));
		float den = ctx.getResources().getDisplayMetrics().density;
		btn.setPadding((int) (8 * den), 0, (int) (8 * den), 0);
	}

	TextViewForResize createTextView(int leftmargin, int bg_resid, int lefticon, LayoutParams params, OnClickListener lis) {
		TextViewForResize btn = new TextViewForResize(mctx);
		if (params == null) {
			params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

		} else {

		}
		if (leftmargin > 0) {

			params.setMargins((int) (leftmargin * mdmDisplayMetrics.density), 0, 0, 0);
		}
		btn.setLayoutParams(params);
		btn.setGravity(Gravity.CENTER_VERTICAL);
		// btn.setPadding((int)(8*mdmDisplayMetrics.density+0.5), 0,
		// (int)(8*mdmDisplayMetrics.density+0.5), 0);
		btn.setBackgroundResource(bg_resid);
		btn.setTextColor(mResources.getColor(R.color.title_text_color));
		btn.setTextSize(16);
//		btn.setTextColor(getContext().getResources().getColor(R.color.title_text_color));
		btn.setSingleLine(true);
		if (lefticon == 0) {

		} else {

			btn.setCompoundDrawablesWithIntrinsicBounds(lefticon, 0, 0, 0);
		}

		if (lis == null) {
			btn.setClickable(false);
		} else {

			btn.setOnClickListener(lis);
		}
		return btn;
	}

}
