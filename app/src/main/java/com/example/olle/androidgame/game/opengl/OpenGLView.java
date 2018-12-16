package com.example.olle.androidgame.game.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Sara on 2017-11-28.
 */

class OpenGLView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public OpenGLView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }
}
