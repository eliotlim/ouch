package net.gostun.ouch;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class FlickActivity extends Activity {

	private GLSurfaceView flickGLView;

	/**
	 * Called when the activity is first created.
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		/**
		 * Create a GLSurfaceView instance and set it
		 * as the contentView for this Activity.
		 */
		flickGLView = new FlickGLSurfaceView(this);
		setContentView(flickGLView);
	}

	/**
	 * An extended GLSurfaceView that allows you to
	 * capture touch events.
	 */
	class FlickGLSurfaceView extends GLSurfaceView {
		private final FlickGLRenderer flickGLRenderer;

		public FlickGLSurfaceView(Context context){
			super(context);

			// Create an OpenGL ES 2.0 context
			setEGLContextClientVersion(2);

			// Create and set the GLRenderer for drawing on the GLSurfaceView
			flickGLRenderer = new FlickGLRenderer();
			setRenderer(flickGLRenderer);
		}
	}
}
