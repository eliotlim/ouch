package net.gostun.ouch.moneypack;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

public abstract class AbstractCash extends ImageView {

	public AbstractCash(Context context, Drawable drawable) {
		super(context);
		// Set up the Drawable to be shown within the ImageView
		this.setImageDrawable(drawable);

		// Enable interaction with the ImageView
		this.setClickable(true);

		// Wrap the ImageView bounds to the dimensions of the Drawable
		setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
		setAdjustViewBounds(true);

		// Set parameters for pivot and drawing
		setPivotX(0);
		setPivotY(0);
		setTranslationX(200);
	}

/*	public void setPosition(float x, float y) {
		setTranslationX(x);
		setTranslationY(y);
	}*/

	/**
	 * State-holding variables for the move action implementation
	 */
	private float beginTX, beginTY, beginX, beginY;

	/**
	 * Handle user interaction and performs necessary updates
	 * @param event
	 * @return
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getActionMasked();

		switch (action) {
			case (MotionEvent.ACTION_DOWN):
				// Cache the initial values
				beginX = event.getRawX();
				beginY = event.getRawY();
				beginTX = getTranslationX();
				beginTY = getTranslationY();
				return true;
			case (MotionEvent.ACTION_MOVE):
				// Calculate the new position
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
