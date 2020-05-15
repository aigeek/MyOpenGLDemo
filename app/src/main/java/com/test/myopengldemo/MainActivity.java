package com.test.myopengldemo;

import android.content.Context;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


//使用GLSurfaceView显示黑屏的简单 Android 应用
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static Context instant;
    private GLSurfaceView mGLSurfaceView;

    private class MyGLSurfaceView extends GLSurfaceView {
        private final MyGLRenderer renderer;

        public MyGLSurfaceView(Context context) {
            super(context);
            //1.Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);
            //2.Set the Renderer for drawing on the GLSurfaceView
            //renderer = new MyGLRenderer();
            renderer = new MyGLRenderer();
            //3.setRenderer-->内部初始化render线程,new GLThread()
            setRenderer(renderer);
            // Render the view only when there is a change in the drawing data
            //渲染模式设置为仅在绘制数据发生变化时绘制视图：
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }

    }

    private class MyGLRenderer implements GLSurfaceView.Renderer{

        //调用一次以设置视图的 OpenGL ES 环境。
        // Set the background frame color
        @Override public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(1.0f,0.0f,0.0f,1.0f);
        }

        //每次重新绘制视图时调用。
        @Override public void onDrawFrame(GL10 gl) {
            // Redraw background color
            ////清除颜色缓存和深度缓存
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);

        }

        //当视图的几何图形发生变化（例如当设备的屏幕方向发生变化）时调用
        @Override public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0,0,width,height);
        }

    }


    private class GLView extends GLSurfaceView{

        private GLRenderer mRenderer;
        public GLView(Context context) {
            this(context,null);
        }

        public GLView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            init();
        }

        private void init() {
            setEGLContextClientVersion(2);//设置OpenGL ES2.0 context
            mRenderer = new GLRenderer(MainActivity.this);
            setRenderer(mRenderer);//设置渲染器
        }
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instant = this.getApplicationContext();
        //mGLSurfaceView = new MyGLSurfaceView(this);
        //setContentView(mGLSurfaceView);
        //
        //String version = GLES10.glGetString(GL10.GL_VERSION);
        //Log.w(TAG, "Version: " + version );

        setContentView(new GLView(this));
    }
}
