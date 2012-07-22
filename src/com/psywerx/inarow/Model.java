package com.psywerx.inarow;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.psywerx.utils.L;
import com.psywerx.utils.RawResourceReader;
import com.psywerx.utils.ShaderHelper;

public class Model {
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;

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

    private int mNormalHandle;

    private FloatBuffer mNormalBuffer;

    private int mLightPosHandle;

    private int mMVMatrixHandle;

    private float[] mModelMatrix;

    public Model(Context context, ArrayList<Float> vertices, ArrayList<Float> normals, ArrayList<Integer> faces) {
        
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
        
        
     // initialize byte buffer for the draw list
        bb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 2 bytes per short)
                normals.size() * 4);
        bb.order(ByteOrder.nativeOrder());
        mNormalBuffer = bb.asFloatBuffer();
        mNormalBuffer.put(toPrimitiveFloat(normals));
        mNormalBuffer.position(0);

        int vertexShader = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, RawResourceReader.readFile(context, R.raw.vertexshader));
        int fragmentShader = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, RawResourceReader.readFile(context, R.raw.fragmentshader));

        mProgram = ShaderHelper.createAndLinkProgram(vertexShader, fragmentShader, 
                                                     new String[]{ "a_Position", "a_Color", "a_Normal" });

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
    
    public void draw(float[] mvpMatrix, float[] mVMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);
        

        // get handle to vertex shader's vPosition member
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVMatrix"); 
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "a_Color");
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "a_Normal");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,
                vertexStride, mVertexBuffer);

        // Prepare the normals data
        GLES20.glVertexAttribPointer(mNormalHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,
                vertexStride, mNormalBuffer);

        
        // Pass in the modelview matrix.

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mFacesCount, GLES20.GL_UNSIGNED_INT,
                drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
