//for 片元着色 glsl code
//片元着色器--code加载后，结合java层color[]-->glUniform4fv(...)--->vColor运行
precision mediump float;//声明片元精度
uniform vec4 vColor;//声明片元颜色-vec4的变量
void main(){
    gl_FragColor = vColor;//gl_FragColor是glsl内定名称
}