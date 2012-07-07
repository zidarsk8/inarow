package com.psywerx.inarow;

import java.util.Arrays;

import android.opengl.Matrix;
import android.util.Log;

public class Game {
    
    // Declare as volatile because we are updating it from another thread
    public volatile int mState = 0;
    public volatile float mAngle;
    public volatile float dx = 0;
    public volatile float dy = 0;
    
    private Square mSquare;
    private Triangle mTriangle;
    
    public Game(){
        mTriangle = new Triangle();
        mSquare = new Square();
    }

    public void draw(float[] mMVPMatrix, float[] mRotationMatrix) {

        mSquare.draw(mMVPMatrix);
        // Create a rotation for the triangle
        // long time = SystemClock.uptimeMillis() % 4000L;
        // float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);
        float[] translate = new float[16];
        
        // Combine the rotation matrix with the projection and camera view
        Matrix.translateM(mMVPMatrix, 0, dx/10f, dy/10f, 0);
        mTriangle.draw(mMVPMatrix);

    }

}
