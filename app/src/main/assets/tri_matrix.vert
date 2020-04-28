attribute vec3 vPosition;//顶点坐标
uniform mat4 uMVPMatrix; //总变换矩阵
void main() {
    gl_Position = uMVPMatrix*vec4(vPosition,1);
}