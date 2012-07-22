package com.psywerx.inarow;

import android.content.Context;

public class Game {
    
    // Declare as volatile because we are updating it from another thread
    public volatile int mState = 0;
    public volatile float mAngle;
    
    private Model mModel;
    
    public Game(Context context){
        //mTriangle = new Square();
        mModel = ModelLoader.getModel(context, "piller");
    }

    public void draw(float[] mMVPMatrix, float[] mVMatrix) {

        // Create a rotation for the triangle
        // long time = SystemClock.uptimeMillis() % 4000L;
        // float angle = 0.090f * ((int) time);
        
        //Matrix.translateM(translate, 0, dx, dy, 0);
        // Combine the rotation matrix with the projection and camera view
//        Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);
        
//        mSquare.draw(mMVPMatrix);
        //mTriangle.draw(mMVPMatrix);
        mModel.draw(mMVPMatrix, mVMatrix);

    }

}
