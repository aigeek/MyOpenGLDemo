package com.test.myopengldemo;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.microedition.khronos.opengles.GL;

/**
 * Created by yao on 2020 2020/4/22 at 12:50
 */
public class GLUtils {
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
}
