package net.gostun.ouch.moneypak;

import android.opengl.GLES20;
import net.gostun.ouch.FlickGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public abstract class AbstractNote {
	private FloatBuffer vertexBuffer;
	private ShortBuffer drawListBuffer;

	private final int flickGLProgram;

	private int mPositionHandle;
	private int mColourHandle;

	private final int vertexCount = (squareCoords.length+6)/COORDS_PER_VERTEX; // Since two coordinates are reused
	private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

	static final float[] colour = {0.8f, 0.0f, 0.8f, 1.0f};
	// Number of coordinates per vertex in this array
	static final int COORDS_PER_VERTEX = 3;
	static float squareCoords[] = {
			-0.5f, 0.5f, 0.0f,  // top left
			-0.5f, -0.5f, 0.0f, // bottom left
			0.5f, -0.5f, 0.0f,  // bottom right
			0.5f, 0.5f, 0.0f    // top right
	};
	private short drawOrder[] = {0, 1, 2, 0, 2, 3};

	//Used to access and set the view transformation
	private int mMVPMatrixHandle;

	/**
	 * GLSL shader definitions
	 * Vertex Shader - renders vertices
	 * Fragment Shader - renders faces of a shape with colours/textures
	 * Program - an OpenGL ES object that contains the shaders you are using
	 *
	 * The matrix member variable provides a hook to manipulate the coordinates of the objects that use this vertex shader.
	 * The matrix must be included as a modifier of gl_Position. Note that the uMVPMatrix factor *must be first* (not-commutative)
	 */
	private final String vertexShaderCode =
			"uniform mat4 uMVPMatrix;" +
					"attribute vec4 vPosition;" +
					"void main() {" +
					"   gl_Position = uMVPMatrix * vPosition;" +
					"}";
	private final String fragmentShaderCode =
			"precision mediump float;" +
					"uniform vec4 vColor;" +
					"void main() {" +
					"   gl_FragColor = vColor;" +
					"}";

	public AbstractNote() {
		/**
		 * Initialize vertex byte buffer for shape coordinates
		 * (# of coordinate values * 4 bytes per float)
		 */
		ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
		bb.order(ByteOrder.nativeOrder());

		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(squareCoords);
		vertexBuffer.position(0);

		/**
		 * Initialize byte buffer for the draw list
		 * (# of coordinates * 2 bytes per short)
		 */
		ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
		dlb.order(ByteOrder.nativeOrder());

		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(drawOrder);
		drawListBuffer.position(0);

		/**
		 * Load the shaders for this object.
		 */
		int vertexShader = FlickGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = FlickGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
		// Create an empty OpenGL ES program
		flickGLProgram = GLES20.glCreateProgram();
		// Add the vertexShader and fragmentShader to program
		GLES20.glAttachShader(flickGLProgram, vertexShader);
		GLES20.glAttachShader(flickGLProgram, fragmentShader);
		// Creates OpenGL ES program executables
		GLES20.glLinkProgram(flickGLProgram);
	}

	public void draw(float[] mvpMatrix) {
		// Add program to OpenGL ES environment
		GLES20.glUseProgram(flickGLProgram);

		// Get Handle to vertexShader's vPosition member
		mPositionHandle = GLES20.glGetAttribLocation(flickGLProgram, "vPosition");
		// Enable a handle to the vertices
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		// Prepare the triangle coordinate data
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
		// Get handle to fragmentShader's vColour member
		mColourHandle = GLES20.glGetUniformLocation(flickGLProgram, "vColor");
		// Set colour for drawing the shape
		GLES20.glUniform4fv(mColourHandle, 1, colour, 0);

		// Get handle to shape's transformation matrix
		mMVPMatrixHandle = GLES20.glGetUniformLocation(flickGLProgram, "uMVPMatrix");
		// Pass the projection and view transformation to the shader
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

		// Draw the shape
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
}
