package com.test.myopengldemo;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by yao on 2020 2020/4/21 at 16:28
 * 渲染类
 */
public class GLRenderer implements GLSurfaceView.Renderer {


    Triangle mTriangle;









    /************着色器************/
    /**
     * 加载着色器
     * @param type 顶点着色 {@link GLES20.GL_VERTEX_SHADER}
     *             片元着色 {@link GLES20.GL_FRAGMENT_SHADER}
     * @param shaderCode 着色代码
     * @return 着色器
     */
    public static int loadShader(int type,String shaderCode){
        int shader = GLES20.glCreateShader(type);//创建着色器
        GLES20.glShaderSource(shader,shaderCode);//添加着色器代码
        GLES20.glCompileShader(shader);//编译
        return shader;
    }


    /************相机视图************/
    //相机视图
    private final float[] mViewMatrix = new float[16];
    @Override public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f,0.0f,0.0f,1.0f);//rgba
        mTriangle = new Triangle();
        //相机视图
        // 模版代码：https://developer.android.com/guide/topics/graphics/opengl.html#coordinate-mapping
        Matrix.setLookAtM(mViewMatrix,0,0,0,-3,0f,0f,0f,0f,1.0f,0.0f);

    }
    /************投影视图************/
    //通过投影矩阵mProjectionMatrix在onSurfaceChanged中计算投影变化数据;
    // 通过Matrix.frustumM()方法将GLSurfaceView填充到投影变换矩阵中;
    // 投影视图需要和相机视图在onDrawFrame中结合才能显示出来。
    //投影矩阵-->投影视图（即 透视投影，摄像机的平截头体（frustum））
    private final float[] mProjectionMatrix = new float[16];
    @Override public void onSurfaceChanged(GL10 gl, int width, int height) {
        //投影视图
        // 模版代码：
        // https://developer.android.com/guide/topics/graphics/opengl.html#coordinate-mapping
        GLES20.glViewport(0,0,width,height);//GL视口
        float ratio = (float) width/height;
        Matrix.frustumM(mProjectionMatrix,0,-ratio,ratio,-1,1,3,7);
    }


    /************应用 相机和投影视图************/
    //最终视图--Model View Projection Matrix
    private final float[] mMVPMatrix = new float[16];
    @Override public void onDrawFrame(GL10 gl) {
        //清除颜色缓存和深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT| GLES20.GL_DEPTH_BUFFER_BIT);
        //结合 相机和投影视图
        Matrix.multiplyMM(mMVPMatrix,0,mProjectionMatrix,0,mViewMatrix,0);
        //应用 相机和投影转换--->、在draw中处理

        mTriangle.draw(mMVPMatrix);
    }
}
