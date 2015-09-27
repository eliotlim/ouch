package net.gostun.ouch.moneypack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

public abstract class AbstractCash extends ImageView {

	public AbstractCash(Context context, Drawable drawable) {
		super(context);
		// Set up the Drawable to be shown within the ImageView
		this.setImageDrawable(drawable);

		// Wrap the ImageView bounds to the dimensions of the Drawable
		setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
		setAdjustViewBounds(true);

		// Set parameters for pivot and drawing
		setPivotX(0);
		setPivotY(0);
		setTranslationX(200);

		/**
		 * Configure GestureDetector with a FlickGestureListener and
		 * setup an OnTouchListener to pump events to the FlickGestureListener
		 */
		final GestureDetector gestureDetector = new GestureDetector(new FlickGestureListener());
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Let the view handle the event first
				onTouchEvent(event);
				// Perform flick gesture detection
				gestureDetector.onTouchEvent(event);
				return true;
			}
		});
	}

	/**
	 * Handle user interaction and performs necessary updates
	 *
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
				lastTime = event.getEventTime();
				beginTX = getTranslationX();
				beginTY = getTranslationY();
				pumpPosition(event);

				// Cancel all existing animations
				if (animX != null && animX.isRunning()) {
					animX.cancel();
					animX = null;
				}
				if (animY != null && animY.isRunning()) {
					animY.cancel();
					animY = null;
				}
				return true;
			case (MotionEvent.ACTION_MOVE):
				// Calculate the new position
				setTranslationX(event.getRawX() - beginX + beginTX);
				setTranslationY(event.getRawY() - beginY + beginTY);
				pumpPosition(event);
				return true;
			case (MotionEvent.ACTION_UP):
				return true;
			case (MotionEvent.ACTION_CANCEL):
				return true;
			case (MotionEvent.ACTION_OUTSIDE):
				return true;
			default:
				return super.onTouchEvent(event);
		}
	}

	/**
	 * State-holding variables for the move action implementation
	 */
	private float beginTX, beginTY, beginX, beginY, currX, currY, lastX, lastY;
	private long currTime, lastTime;
	protected ObjectAnimator animX, animY;

	/**
	 * Used to provide values for correcting velocity in flick calculation
	 *
	 * @param event
	 */
	private void pumpPosition(MotionEvent event) {
		lastX = currX;
		lastY = currY;
		currX = event.getRawX();
		currY = event.getRawY();
		lastTime = currTime;
		currTime = event.getEventTime();
	}

	/**
	 * Parameters for configuring Flick behaviour
	 */
	private static final int FLICK_MIN_DISTANCE = 120;
	private static final int FLICK_THRESHOLD_VEOCITY = 180;
	private static final int MULT_FACTOR = 250;
	private static final int DECCELERATE_TIME = 250;
	private static final float DECCELERATION_FORCE = 200;

	private class FlickGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			// Apparently the provided values of velocityX and Y are inaccurate .-.
			velocityX = MULT_FACTOR * (e2.getRawX() - lastX) / (e2.getEventTime() - lastTime);
			velocityY = MULT_FACTOR * (e2.getRawY() - lastY) / (e2.getEventTime() - lastTime);

			// If either velocity (x or y) AND either distance (x or y) exceed threshold values
			if ((Math.abs(velocityX) > FLICK_THRESHOLD_VEOCITY || Math.abs(velocityY) > FLICK_THRESHOLD_VEOCITY) &&
					(Math.abs(e2.getRawX() - e1.getRawX()) > FLICK_MIN_DISTANCE || (Math.abs(e2.getRawY() - e1.getRawY()) > FLICK_MIN_DISTANCE))) {
				// Perform flick
				float targetX = getTranslationX() + velocityX * DECCELERATE_TIME / DECCELERATION_FORCE,
						targetY = getTranslationY() + velocityY * DECCELERATE_TIME / DECCELERATION_FORCE;
				// Log.i("ouch", "Start Position: " + getTranslationX() + ", " + getTranslationY() + " Target Position: " + velocityX + ", " + velocityY);

				// Create ObjectAnimators for updating translation
				animX = ObjectAnimator.ofFloat(AbstractCash.this, "TranslationX", getTranslationX(), targetX);
				animY = ObjectAnimator.ofFloat(AbstractCash.this, "TranslationY", getTranslationY(), targetY);

				// Set the Interpolation mode to feel more realistic
				animX.setInterpolator(new AccelerateDecelerateInterpolator());
				animY.setInterpolator(new AccelerateDecelerateInterpolator());
				// TODO: Set Keyframes
				animY.setDuration(DECCELERATE_TIME);
				animX.setDuration(DECCELERATE_TIME);

				// Setup animation listeners to detect the view going off-screen
				animX.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);

						// Find bounding rectangle of parent
						Rect rect = new Rect();
						FrameLayout frameLayout = (FrameLayout) AbstractCash.this.getParent();
						frameLayout.getHitRect(rect);
						// Intersect bounding rectangle of parent and self
						if (!AbstractCash.this.getLocalVisibleRect(rect)) {
							Log.i("ouch", "flicked off-screen");
							// TODO: Notify FlickActivity of flick

						}
					}
				});

				// Start the animation
				animX.start();
				animY.start();
			}

			return false;
		}
	}

}
