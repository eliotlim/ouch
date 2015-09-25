package net.gostun.ouch;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import net.gostun.ouch.moneypak.AbstractNote;
import net.gostun.ouch.moneypak.ExampleNote;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FlickGLRenderer implements GLSurfaceView.Renderer {

	private AbstractNote note;

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background frame colour
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		// Initialize a (rectangular) note
		note = new ExampleNote();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Resize viewport
		GLES20.glViewport(0, 0, width, height);

		/**
		 * Perspective correction for viewport ratio
		 * This Projection Matrix is applied to object coordinates
		 * in the onDrawFrame() method.
		 */
		float ratio = (float) width / height;
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// Redraw background colour
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		/**
		 * These calls emulate a camera by performing view transformation
		 */
		// Set the camera position (View matrix)
		Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		// Calculate the projection and view transformation
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

		/**
		 * Draw Objects
		 */
		note.draw(mMVPMatrix);
	}

	public static int loadShader(int type, String shaderCode){
		/**
		 * Create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		 * or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		 */
		int shader = GLES20.glCreateShader(type);

		// Add the source code to the shader and compile it.
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

	// mMVPMatrix is an abbreviation for "Model View Projection Matrix"
	private final float[] mMVPMatrix = new float[16];
	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];

}
