package net.codeforeurope.amsterdam.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SquareRelativeLayout extends RelativeLayout {

	public SquareRelativeLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SquareRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new RelativeLayout.LayoutParams(getContext(), attrs);
    }

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {

		}
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(this.getMeasuredWidth(), this.getMeasuredWidth());
		super.onMeasure(MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(),
				MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
				this.getMeasuredWidth(), MeasureSpec.EXACTLY));
		

	}
}
