package com.test.myopengldemo;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by yao on 2020 2020/4/21 at 16:28
 * 渲染类
 */
public class GLRenderer implements GLSurfaceView.Renderer {
    Triangle mTriangle;

    @Override public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f,0.0f,0.0f,1.0f);//rgba
        mTriangle = new Triangle();
    }

    @Override public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);//GL视口
    }

    @Override public void onDrawFrame(GL10 gl) {
        //清除颜色缓存和深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT| GLES20.GL_DEPTH_BUFFER_BIT);
        mTriangle.draw();
    }

}
