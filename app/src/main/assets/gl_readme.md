

GLSL(OpenGL Shader Language)

  - 1.一种面相过程的高级语言
  - 2.基于C/C++的语法(子集)及流程控制
  - 3.完美支持向量和矩阵的操作
  - 4.通过类型限定符来管理输入与输出

文件的格式(自定义)
  - .vert - 顶点着色器
  - .tesc - 曲面细分控制着色器
  - .tese - 曲面细分评估着色器
  - .geom - 几何着色器
  - .frag - 片元着色器
  - .comp - 计算着色器


原生数据类型
  - 标量:一维的数值操作
    - float   浮点型
    - bool    布尔型
    - int     整型
    - |--- 支持 8进制(0开头)  16进制(0x开头)
  - 向量:储存及操作 颜色、位置、纹理坐标等
    - vec2    二维向量型-浮点型
    - vec3    三维向量型-浮点型
    - vec4    四维向量型-浮点型

    - ivec2    二维向量型-整型
    - ivec3    三维向量型-整型
    - ivec4    四维向量型-整型

    - bvec2    二维向量型-布尔型
    - bvec3    三维向量型-布尔型
    - bvec4    四维向量型-布尔型
  - 矩阵:根据矩阵的运算进行变换操作
    - mat2    2X2矩阵-浮点型
    - mat3    3X3矩阵-浮点型
    - mat4    4X4矩阵-浮点型
  - 采样器
    - sampler2D   二维纹理
    - sampler3D   三维纹理
    - samplerCube 立方贴图纹理
  - 结构体:例如
  ```c
    struct ball{
        vec3 color;
        vec3 position;
    }
  ```
  - 数组
    - vec3 pos[]; //声明不定大小的三维向量数组
    - vec3 pos[6];//声明6个三维向量数组
  - 限定符
    - attribute 顶点的变量,如顶点位置，颜色
    - uniform
    - varying 用于从定点着色器传递到片元作色器的变量
    - const
    - precision 精度
     |--- lowp
     |--- mediump
     |--- highp