package com.test.myopengldemo;

import android.content.Context;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL;

/**
 * Created by yao on 2020 2020/5/14 at 19:27
 *
 */
public class TextureRectangle {
    private static final String TAG = TextureRectangle.class.getSimpleName();


    private Context mContext;
    private int mProgram;
    private int mPositionHandle;//位置句柄
    private int mColorHandle;//颜色句柄
    private int muMVPMatrixHandle;//顶点变换矩阵句柄
    //顶点缓冲
    private FloatBuffer vertexBuffer;
    //纹理缓冲
    private FloatBuffer mTextureCooBuffer;
    //索引缓冲
    private ShortBuffer idxBuffer;
    //数组中每个顶点的 坐标数
    static final int COORDS_PER_VERTEX = 3;
    //跨度
    private final int vertexStride = COORDS_PER_VERTEX*4;

    //数组总每个顶点坐标数
    static final int c = 1;
    //一个模拟的五边形：
    //        -0.5f, 0.5f, 0.0f, // p0    //2象限
    //        -1.0f, 0.0f, 0.0f, // p1    //负x轴
    //        -0.5f, -0.5f, 0.0f, // p2   //3象限
    //        0.5f, -0.5f, 0.0f, //p3     //4象限
    //        1.0f, 0.0f, 0.0f, //p4      //正x轴
    //        0.5f, 0.5f, 0.0f, //p5      //1象限
    //

    //顶点数组
    static float sCoo[]={//逆时针三角形
        -c,c,0.0f,//point0//2象限
        -c,-c,0.0f,//point1//3象限
        c,-c,0.0f//point2//4象限
    };


    ////test
    //private final float[] textureCoo = {
    //    0.0f,1.0f,
    //    1.0f,1.0f,
    //    1.0f,0.0f
    //};
    //// TODO: 2020/5/18 逆时针顺序???纹理坐标//镜像了？？？？
    private final float[] textureCoo = {
        0.0f,0.0f,
        0.0f,1.0f,
        1.0f,1.0f
    };
    //
    ////逆时针顺序???纹理坐标-2
    //private final float[] textureCoo = {
    //    0.0f,1.0f,
    //    1.0f,1.0f,
    //    1.0f,0.0f
    //};

    ////逆时针顺序???纹理坐标-3
    //private final float[] textureCoo = {
    //    1.0f,1.0f,
    //    1.0f,0.0f,
    //    0.0f,0.0f
    //};

    //纹理数组中，每个顶点坐标数，2个
    static final int TEXTURE_PER_VERTEX = 2;
    private final int vertexTextureStride = TEXTURE_PER_VERTEX*4;

    //// TODO: 2020/5/14 绘制用的索引，，详见glDrawElements方法（https://glumes.com/post/opengl/opengl-tutorial-gldrawelements-method/）
    private short[] idx = {
        1,2,3
    };


    public TextureRectangle(Context context){
        this.mContext = context;
        //初始化顶点字节缓冲数据
        //1.缓冲顶点数据
        vertexBuffer = GLUtils.getFloatBuffer(sCoo);
        mTextureCooBuffer = GLUtils.getFloatBuffer(textureCoo);
        idxBuffer = GLUtils.getShortBuffer(idx);

        //初始化OpenGL ES程序
        initProgram();

    }

    private void initProgram() {
        /************渲染器程序************/

        //1.顶点着色
        int vertexShader = GLUtils.loadShaderAssets(MainActivity.instant,
            GLES20.GL_VERTEX_SHADER,
            "rect_texture.vert"//顶点着色 GLSL(GL Shader Language) 代码
        );
        //int vertexShader = GLRenderer.loadShader(
        //    GLES20.GL_VERTEX_SHADER,//顶点着色
        //    vertexShaderCode//顶点着色 GLSL(GL Shader Language) 代码
        //);
        //注意 类型是：GL_VERTEX_SHADER

        //2.片元着色
        int fragmentShader = GLUtils.loadShaderAssets(MainActivity.instant,
            GLES20.GL_FRAGMENT_SHADER,
            "rect_texture.frag"
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
        mColorHandle = GLES20.glGetAttribLocation(mProgram,"vCoordinate");//获取片元着色器的vCoordinate成员的句柄
        ////获取程序中 总变换矩阵uMVPMatrix成员句柄
        ////注意此处是u
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,"uMVPMatrix");
    }

    /************绘制************/
    public void draw(float[] mvpMatrix, int textureId){
        GLES20.glUseProgram(mProgram);//将程序添加到OpenGL ES环境中
        //for 顶点着色器
        GLES20.glEnableVertexAttribArray(mPositionHandle);//启动三角形 顶点的句柄
        //for 顶点颜色
        GLES20.glEnableVertexAttribArray(mColorHandle);//启用三角形顶点颜色句柄

        //for 变换矩阵
        //启用三角形顶点颜色句柄
        //for 顶点矩阵变换
        GLES20.glUniformMatrix4fv(
            muMVPMatrixHandle,
            1,
            false,
            mvpMatrix,
            0);



        //准备三角顶点坐标数据 6个参数
        GLES20.glVertexAttribPointer(
            mPositionHandle,//int 索引
            COORDS_PER_VERTEX,//int 大小
            GLES20.GL_FLOAT,//int 类型
            false,//boolean 是否标准化
            vertexStride,//int 跨度
            vertexBuffer//java.nio.Buffer 缓冲
        );


        //就是把颜色句柄换成了贴图句柄 再绘制的时候接受一个贴图的id
        //https://juejin.im/post/5c39af866fb9a049ea392c6e#heading-16
        //准备三角形顶点颜色数据--->换为纹理数据相关
        // GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);两行代码贴图
        //其余的基本一致，下面最重要的是这个贴图id如何获取
        GLES20.glVertexAttribPointer(
            mColorHandle,
            TEXTURE_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            vertexTextureStride,
            mTextureCooBuffer
        );



        //顶点颜色设置变换后，不用再设置颜色
        ////for 片元着色器
        //mColorHandle = GLES20.glGetUniformLocation(mProgram,"vColor");//获取片元着色器的vColor成员的句柄
        //GLES20.glUniform4fv(mColorHandle,1,color,0);//设置三角形颜色

        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);



        //for 绘制
        //使用glDrawElements按照 规格和索引数组绘制
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,idx.length,GLES20.GL_UNSIGNED_SHORT,idxBuffer);//绘制三角形

        //禁用index指定的通用顶点属性数组。
        // 默认情况下，禁用所有客户端功能，包括所有通用顶点属性数组。
        // 如果启用，将访问通用顶点属性数组中的值，
        // 并在调用顶点数组命令（如glDrawArrays或glDrawElements）时用于呈现
        GLES20.glDisableVertexAttribArray(mPositionHandle);//禁用顶点数组
    }


}
