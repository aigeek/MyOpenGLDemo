package com.test.myopengldemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL;

/**
 * Created by yao on 2020 2020/4/22 at 12:50
 */
public class GLUtils {

    /**
     * float数组缓冲
     * @param sCoo-float[]
     * @return
     */
    public static FloatBuffer getFloatBuffer(float[] sCoo) {
        FloatBuffer vertexBuffer;
        /************float数组缓冲************/
        //点数量*4 = xx bytes//每个浮点数4个字节32位
        ByteBuffer bb = ByteBuffer.allocateDirect(sCoo.length*4);
        bb.order(ByteOrder.nativeOrder());//使用本机硬件的字节顺序
        vertexBuffer = bb.asFloatBuffer();//从字节缓冲区创建浮点缓冲区
        vertexBuffer.put(sCoo);//将坐标添加到浮点缓冲区
        vertexBuffer.position(0);//设置缓冲区以读取第一个坐标
        return vertexBuffer;
    }
    /**
     * short数组缓冲
     * @param sCoo-float[]
     * @return
     */
    public static ShortBuffer getShortBuffer(short[] sCoo) {
        ShortBuffer vertexBuffer;
        /************float数组缓冲************/
        //点数量*2 = xx bytes//每个short 2个字节 16位
        ByteBuffer bb = ByteBuffer.allocateDirect(sCoo.length*2);
        bb.order(ByteOrder.nativeOrder());//使用本机硬件的字节顺序
        vertexBuffer = bb.asShortBuffer();//从字节缓冲区创建浮点缓冲区
        vertexBuffer.put(sCoo);//将坐标添加到浮点缓冲区
        vertexBuffer.position(0);//设置缓冲区以读取第一个坐标
        return vertexBuffer;
    }

    /**
     * 才脚本中加载shader内容
     * @param context
     * @param type
     * @param name
     * @return
     */
    public static int loadShaderAssets(Context context,int type,String name){
        String result = null;
        try {
            InputStream in  = context.getAssets().open(name);
            int ch = 0;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while ((ch = in.read())!=-1){
                out.write(ch);
            }
            byte[] buff = out.toByteArray();
            out.close();
            in.close();
            result = new String(buff,"UTF-8");
            result = result.replaceAll("\\r\\n","\n");

        }catch (Exception e){
            e.printStackTrace();
        }
        return loadShader(type,result);
    }

    /**
     * 加载着色器
     * @param type 着色器类型
     *              顶点着色 {@link GLES20.GL_VERTEX_SHADER}
     *              片元着色 {@link GLES20.GL_FRAGMENT_SHADER}
     * @param shaderCode 着色代码
     * @return 着色器
     */
    private static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);//创建着色器
        if (shader == 0){//加载失败直接返回
            return 0;
        }
        GLES20.glShaderSource(shader,shaderCode);//加载着色器代码
        GLES20.glCompileShader(shader);//编译
        return checkCompile(type,shader);
    }

    /**
     * 检查shader代码是否编译成功
     * @param type 着色器类型
     * @param shader 着色器
     * @return 着色器
     */
    private static int checkCompile(int type, int shader) {
        int[] compiled = new int[1];//存放编译成功的shader数量的数组
        GLES20.glGetShaderiv(shader,GLES20.GL_COMPILE_STATUS,compiled,0);
        if (compiled[0] == 0){//弱编译失败则显示错误日志并删除着色器
            Log.e("ES20_COMPILE_ERROR","Could not compile shader "+type+":"+GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);//删除此shader
            shader = 0;
        }
        return shader;
    }

    public static int loadTexture(Context mContext, int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),resId);
        return loadTexture(mContext,bitmap);
    }

    public static int loadTexture(Context mContext, Bitmap bitmap) {
        //生成纹理id
        int[] textures = new int[1];
        //产生的纹理id的数量，数组，偏移量
        GLES20.glGenTextures(1,textures,0);
        int textureId = textures[0];
        // TODO: 2020/5/14  ???
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
        //实际加载纹理--android sdk
        //纹理类型，纹理的层次，纹理图像，纹理边框尺寸
        android.opengl.GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);
        bitmap.recycle();
        return textureId;
    }
}
