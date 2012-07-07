package com.psywerx.inarow;

import android.opengl.Matrix;

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

        // Create a rotation for the triangle
        // long time = SystemClock.uptimeMillis() % 4000L;
        // float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);
        float[] mTranslationMatrix = new float[16];
        
        //Matrix.translateM(translate, 0, dx, dy, 0);
        // Combine the rotation matrix with the projection and camera view
//        Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);

        Matrix.scaleM(mMVPMatrix, 0, 1-dx*0.001f, 1-dx*0.001f, 1);
        mSquare.draw(mMVPMatrix);
        mTriangle.draw(mMVPMatrix);

    }

}
