//for 片元着色 glsl code
//定义四维向量给gl_Position
attribute vec4 vPosition;
void main(){
    gl_Position = vPosition;
}