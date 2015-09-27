package net.gostun.ouch.moneypack;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

public abstract class AbstractCash extends ImageView {

	public AbstractCash(Context context, Drawable drawable) {
		super(context);
		this.setImageDrawable(drawable);

		this.setClickable(true);
		setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
		setAdjustViewBounds(true);

		setPivotX(0);
		setPivotY(0);
		setTranslationX(200);
	}

/*	public void setPosition(float x, float y) {
		setTranslationX(x);
		setTranslationY(y);
	}*/

	private float beginTX, beginTY, beginX, beginY;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getActionMasked();

		switch (action) {
			case (MotionEvent.ACTION_DOWN):
				beginX = event.getRawX();
				beginY = event.getRawY();
				beginTX = getTranslationX();
				beginTY = getTranslationY();
				return true;
			case (MotionEvent.ACTION_MOVE):
				setTranslationX(event.getRawX() - beginX + beginTX);
				setTranslationY(event.getRawY() - beginY + beginTY);
				return true;
			case (MotionEvent.ACTION_UP):
				//Log.i("ouch", "Translation - Original: ["+beginTX+", "+beginTY+"] New: ["+getTranslationX()+", "+getTranslationY()+"]");
				//Log.i("ouch", "TouchEvents - Original: ["+beginX+", "+beginY+"] New: ["+event.getRawX()+", "+event.getRawY()+"]");
				return true;
			case (MotionEvent.ACTION_CANCEL):
				return true;
			case (MotionEvent.ACTION_OUTSIDE):
				return true;
			default:
				return super.onTouchEvent(event);
		}
	}

}
