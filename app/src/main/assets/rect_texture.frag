//片元代码
precision mediump float;//中等精度
varying vec2 aCoordinate;//贴图坐标系
uniform sampler2D vTexture;//贴图： sampler2D 二维纹理
void main() {
    //texture2D glsl内置函数：https://wxdut.com/15271715795286.html
    //使用texture坐标aCoordinate来查找当前绑定到采样器的texture
    gl_FragColor = texture2D(vTexture,aCoordinate);
}
