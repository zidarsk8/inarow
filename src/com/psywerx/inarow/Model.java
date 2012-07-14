package com.psywerx.inarow;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import android.opengl.GLES20;

public class Model {
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;

    private String mVertexShaderCode =
    // This matrix member variable provides a hook to manipulate
    // the coordinates of the objects that use this vertex shader
    "uniform mat4 uMVPMatrix;" +

    "attribute vec4 vPosition;" + "void main() {" +
    // the matrix must be included as a modifier of gl_Position
            "  gl_Position = vPosition * uMVPMatrix;" + "}";

    private String mFragmentShaderCode = "precision mediump float;" + "uniform vec4 vColor;"
            + "void main() {" + "  gl_FragColor = vColor;" + "}";

    private final FloatBuffer mVertexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    private final int mFacesCount;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per
                                                            // vertex

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    private IntBuffer drawListBuffer;

    public Model(ArrayList<Float> vertices, ArrayList<Float> normals, ArrayList<Integer> faces) {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
        // (number of coordinate values * 4 bytes per float)
                vertices.size() * 4);
        
        L.d(faces.toString() + " FFFF");

        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        mVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        mVertexBuffer.put(toPrimitiveFloat(vertices));
        // set the buffer to read the first coordinate
        mVertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 2 bytes per short)
                faces.size() * 4);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asIntBuffer();
        drawListBuffer.put(toPrimitiveInteger(faces));
        drawListBuffer.position(0);
        mFacesCount = faces.size();
        // prepare shaders and OpenGL programInitModel

        int vertexShader = MyRenderer.loadShader(GLES20.GL_VERTEX_SHADER, mVertexShaderCode);
        int fragmentShader = MyRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShaderCode);

        mProgram = GLES20.glCreateProgram(); // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader); // add the vertex shader
                                                       // to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment
                                                         // shader to program
        GLES20.glLinkProgram(mProgram); // create OpenGL program executables

    }

    private short[] toPrimitiveShort(ArrayList<Short> arr) {
        short[] primitive = new short[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            primitive[i] = (short) arr.get(i);
        }
        return primitive;
    }
    
    private int[] toPrimitiveInteger(ArrayList<Integer> arr) {
        int[] primitive = new int[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            primitive[i] = (int) arr.get(i);
        }
        return primitive;
    }
    
    private float[] toPrimitiveFloat(ArrayList<Float> arr) {
        float[] primitive = new float[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            primitive[i] = (float) arr.get(i);
        }
        return primitive;
    }
    
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,
                vertexStride, mVertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mFacesCount, GLES20.GL_UNSIGNED_INT,
                drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
