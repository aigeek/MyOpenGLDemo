package com.test.myopengldemo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by yao on 2020 2020/4/21 at 16:28
 * 渲染类
 */
public class GLRenderer implements GLSurfaceView.Renderer {
    TextureRectangle mRectangle;
    private Context mContext;
    //Model View Projection Matrix--模型视图投影矩阵
    private static float[] mMVPMatrix = new float[16];
    //投影矩阵 mProjectionMatrix
    private static final float[] mProjectionMatrix = new float[16];
    //视图矩阵 mViewMatrix
    private static final float[] mViewMatrix = new float[16];
    //变换矩阵
    private float[] mOpMatrix = new float[16];

    public GLRenderer(Context context) {
        this.mContext = context;
    }

    private int textureId;
    @Override public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f,0.0f,0.0f,1.0f);//rgba
        //初始化纹理
        textureId = GLUtils.loadTexture(mContext, R.mipmap.mian_a);
        mRectangle = new TextureRectangle(mContext);
        //相机视图
        // 模版代码：https://developer.android.com/guide/topics/graphics/opengl.html#coordinate-mapping
        //修改eyeZ可以调整三角形距离屏幕的远近
        //注意 -5是从z轴从下向上 反方向看，看到的二维图像会左右镜像
        Matrix.setLookAtM(mViewMatrix,0,0,0,5,0f,0f,0f,0f,1.0f,0.0f);
    }

    @Override public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);//GL视口
        float ratio = (float) width/height;
        Matrix.frustumM(mProjectionMatrix,0,-ratio,ratio,-1,1,3,7);
    }

    @Override public void onDrawFrame(GL10 gl) {
        //清除颜色缓存和深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        //结合 相机和投影视图
        Matrix.multiplyMM(mMVPMatrix,0,mProjectionMatrix,0,mViewMatrix,0);

        //绘制时使用纹理
        mRectangle.draw(mMVPMatrix,textureId);
    }

}
