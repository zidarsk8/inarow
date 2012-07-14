package com.psywerx.inarow;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;

public class MainActivity extends Activity {

    private GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
        
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
}

class MyGLSurfaceView extends GLSurfaceView {

    private MyRenderer mRenderer;
    private ScaleGestureDetector mScaleDetector;
    private float mPrevX;
    private float mPrevY;
    private double theta;
    private double pi;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyRenderer(context);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        mScaleDetector = new ScaleGestureDetector(context, new OnScaleGestureListener() {

            public void onScaleEnd(ScaleGestureDetector detector) {
            }

            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            public boolean onScale(ScaleGestureDetector detector) {
                mRenderer.dz += detector.getCurrentSpan() - detector.getPreviousSpan();
                mRenderer.dz = Math.max(0, mRenderer.dz);
                return true;
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(e.getPointerCount()>1){
        
            mScaleDetector.onTouchEvent(e);
            return true;
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN: 
                mPrevX = e.getX();
                mPrevY = e.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                
                pi += (e.getX() - mPrevX)*0.01d;
                theta += (e.getY() - mPrevY)*0.1d;
                mRenderer.dx = 5f * (float) (Math.sin(pi)*Math.cos(theta));
                mRenderer.dy = 5f * (float) (Math.cos(pi)*Math.sin(theta));
                mPrevX = e.getX();
                mPrevY = e.getY();
                break;

            case MotionEvent.ACTION_UP:   
                // finger leaves the screen
                break;
        }
        return true;
    }

}
