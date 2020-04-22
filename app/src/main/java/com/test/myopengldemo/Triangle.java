package com.test.myopengldemo;

import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL;

/**
 * Created by yao on 2020 2020/4/19 at 23:24
 * 配合GLRenderer类绘制三角形
 */
public class Triangle {


    /****************for顶点着色***************/

    //顶点缓冲
    private FloatBuffer vertexBuffer;
    private final  int vertexCount = sCoo.length/COORDS_PER_VERTEX;//顶点个数
    //跨度
    private final int vertexStride = COORDS_PER_VERTEX*4;
    //3个顶点的三纬坐标
    static float sCoo[]={//逆时针
        0.0f,0.0f,0.0f,//顶部
        -1.0f,-1.0f,0.0f,//左下
        1.0f,-1.0f,0.0f//右下
    };

    //颜色缓冲
    private FloatBuffer mVerColorBuffer;
    private int mVertexColorHandle;//顶点颜色句柄
    static final int COLOR_PER_VERTEX = 4;//向量纬度 rgba
    private final int vertexColorStride = COLOR_PER_VERTEX * 4;//4*4 =16;
    float verClors[] = new float[]{
        1f,1f,0.0f,1.0f,//黄色
        0.05882353f, 0.09411765f, 0.9372549f, 1.0f,//蓝
        0.19607843f, 1.0f, 0.02745098f, 1.0f//绿
    };




    private final String vertexShaderCode=//顶点着色代码-GPU
    "attribute vec4 vPosition;"//vec4 四维向量
        + "void main() {"
        + " gl_Position = vPosition;"
        + "}";
    private final String fragmentShaderCode = //片元着色代码-GPU
    "precision mediump float;"
        + "uniform vec4 vColor;"
        + "void main(){"
        + " gl_FragColor = vColor;"
        + "}";
    private final int mProgram;
    private int mPositionHandle;//位置句柄
    private int mColorHandle;//颜色句柄
    private int muMVPMatrixHandle;//顶点变换矩阵句柄
    //数组中每个顶点的 坐标数
    static final int COORDS_PER_VERTEX = 3;


    //颜色 rgba
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    public Triangle(){

        /************顶点缓冲************/
        //初始化顶点字节缓冲区 3*4 = 12 bytes//每个浮点数4个字节32位
        ByteBuffer bb = ByteBuffer.allocateDirect(sCoo.length*4);
        bb.order(ByteOrder.nativeOrder());//使用本机硬件的字节顺序
        vertexBuffer = bb.asFloatBuffer();//从字节缓冲区创建浮点缓冲区
        vertexBuffer.put(sCoo);//将坐标添加到浮点缓冲区
        vertexBuffer.position(0);//设置缓冲区以读取第一个坐标

        //初始化顶点字节颜色缓冲区 4*4 = 16 bytes//每个浮点数4个字节32位
        ByteBuffer bb_color = ByteBuffer.allocateDirect(verClors.length*4);
        bb_color.order(ByteOrder.nativeOrder());//使用本机硬件的字节顺序
        mVerColorBuffer = bb_color.asFloatBuffer();//从字节缓冲区创建浮点缓冲区
        mVerColorBuffer.put(verClors);//将坐标添加到浮点缓冲区
        mVerColorBuffer.position(0);//设置缓冲区以读取第一个坐标


        /************渲染器程序************/
        int vertexShader = GLUtils.loadShaderAssets(MainActivity.instant,
            GLES20.GL_VERTEX_SHADER,//顶点着色
            "tri_color_change.vert"//顶点着色 GLSL(GL Shader Language) 代码
        );
        //int vertexShader = GLRenderer.loadShader(
        //    GLES20.GL_VERTEX_SHADER,//顶点着色
        //    vertexShaderCode//顶点着色 GLSL(GL Shader Language) 代码
        //);
        //注意 类型是：GL_VERTEX_SHADER
        int fragmentShader = GLUtils.loadShaderAssets(MainActivity.instant,
            GLES20.GL_FRAGMENT_SHADER,//顶点着色
            "tri_color_change.frag"//顶点着色 GLSL(GL Shader Language) 代码
        );
        //int fragmentShader = GLRenderer.loadShader(
        //    GLES20.GL_FRAGMENT_SHADER,//片元着色
        //    fragmentShaderCode//片元着色 GLSL 代码
        //);
        mProgram = GLES20.glCreateProgram();//创建空的OpenGL ES程序
        GLES20.glAttachShader(mProgram,vertexShader);//加入顶点着色器
        GLES20.glAttachShader(mProgram,fragmentShader);//加入片元着色器
        GLES20.glLinkProgram(mProgram);//创建可执行的OpenGL ES项目

        mPositionHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");//获取顶点着色器的vPosition成员的句柄
        mVertexColorHandle = GLES20.glGetAttribLocation(mProgram,"aColor");
        ////获取程序中 总变换矩阵uMVPMatrix成员句柄
        ////注意此处是u
        //muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,"uMVPMatrix");



    }


    /************绘制************/
    public void draw(){
        GLES20.glUseProgram(mProgram);//将程序添加到OpenGL ES环境中
        //for 顶点着色器
        GLES20.glEnableVertexAttribArray(mPositionHandle);//启动三角形 顶点的句柄
        //for 顶点颜色
        //启用三角形顶点颜色句柄
        GLES20.glEnableVertexAttribArray(mVertexColorHandle);

        ////for 变换矩阵
        ////启用三角形顶点颜色句柄
        ////for 顶点矩阵变换
        //GLES20.glUniformMatrix4fv(
        //    muMVPMatrixHandle,
        //    1,
        //    false,
        //    new float[16],
        //    0);



        //准备三角坐标数据 6个参数
        GLES20.glVertexAttribPointer(
            mPositionHandle,//int 索引
            COORDS_PER_VERTEX,//int 大小
            GLES20.GL_FLOAT,//int 类型
            false,//boolean 是否标准化
            vertexStride,//int 跨度
            vertexBuffer//java.nio.Buffer 缓冲
        );
        //准备三角形顶点颜色数据
        GLES20.glVertexAttribPointer(
            mVertexColorHandle,
            COLOR_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            vertexColorStride,
            mVerColorBuffer
        );



        //顶点颜色设置变换后，不用再设置颜色
        ////for 片元着色器
        //mColorHandle = GLES20.glGetUniformLocation(mProgram,"vColor");//获取片元着色器的vColor成员的句柄
        //GLES20.glUniform4fv(mColorHandle,1,color,0);//设置三角形颜色


        //for 绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount);//绘制三角形
        GLES20.glDisableVertexAttribArray(mPositionHandle);//禁用顶点数组
    }




}
