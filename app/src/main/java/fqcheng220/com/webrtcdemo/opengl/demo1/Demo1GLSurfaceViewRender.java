package fqcheng220.com.webrtcdemo.opengl.demo1;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 *
参数	类型	作用	案例图形
GL_POINTS	点	绘制独立的点	A、B、C、D、E、F
GL_LINES	线段	每2个点构成一条线段	AB、CD、EF
GL_LINE_LOOP	线段	按顺序将所有的点连接起来，包括首位相连	AB、BC、CD、DE、EF、FA
GL_LINE_STRIP	线段	按顺序将所有的点连接起来，不包括首位相连	AB、BC、CD、ED、EF
GL_TRIANGLES	三角形	每3个点构成一个三角形	ABC、DEF
GL_TRIANGLE_STRIP	三角形	相邻3个点构成一个三角形,不包括首位两个点	ABC、BCD、CDE、DEF
GL_TRIANGLE_FAN	三角形	第一个点和之后所有相邻的2个点构成一个三角形

作者：Benhero
链接：https://www.jianshu.com/p/eb11a8346cf6
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 * @date 2019/12/4 16:09
 */
public class Demo1GLSurfaceViewRender implements GLSurfaceView.Renderer {
  private final String TAG = getClass().getSimpleName();

  public void setmDrawDemo(IDrawDemo mDrawDemo) {
    this.mDrawDemo = mDrawDemo;
  }

  private IDrawDemo mDrawDemo;

  private int mProgramId;
  private int mVPosition;
  private int mUMatrix;
  private int mUColor;

  private int mWidth;
  private int mHeight;

  private static final boolean sEnableProjection = false;

  private static final String VERTEX_SOURCE = "attribute vec2 vPosition;            \n" // 顶点位置属性vPosition
      +"uniform mat4 u_Matrix;            \n"//如果不使用mat4（4X4矩阵） 屏幕实际显示会拉伸
      + "void main(){                         \n" + "   gl_Position = u_Matrix * vec4(vPosition,0,1);\n" // 确定顶点位置
      + "}";
  private static final String FRAGMENT_SOURCE = "precision mediump float;         \n" // 声明float类型的精度为中等(精度越高越耗资源)
      + "uniform vec4 uColor;             \n" // uniform的属性uColor
      + "void main(){                     \n" + "   gl_FragColor = uColor;        \n" // 给此片元的填充色
      + "}";

  private static final String VERTEX_SOURCE_TEXTURE = "attribute vec2 vPosition;            \n" // 顶点位置属性vPosition
      + "void main(){                         \n" + "   gl_Position = vec4(vPosition,0,1);\n" // 确定顶点位置
      + "}";
  private static final String FRAGMENT_SOURCE_TEXTURE = "precision mediump float;         \n" // 声明float类型的精度为中等(精度越高越耗资源)
      + "uniform vec4 uColor;             \n" // uniform的属性uColor
      + "void main(){                     \n" + "   gl_FragColor = uColor;        \n" // 给此片元的填充色
      + "}";
  private static final float[] PROJECTION = new float[]
      {
          1, 0, 0, 0,
          1, 0, 0, 0,
          1, 0, 0, 0,
          1, 0, 0, 0
      };

  private static final float[] PROJECTION_CONST = new float[]
      {
          1, 0, 0, 0,
          0, 1, 0, 0,
          0, 0, 1, 0,
          0, 0, 0, 1
      };


  @Override public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
    //mProgramId = createProgram();
    ////mProgramId = createProgramPolygon();
    //mVPosition = GLES20.glGetAttribLocation(mProgramId, "vPosition");
    //mUMatrix = GLES20.glGetUniformLocation(mProgramId, "u_Matrix");
    //mUColor = GLES20.glGetUniformLocation(mProgramId, "uColor");

    if(mDrawDemo != null){
      mDrawDemo.init();
    }
  }

  @Override public void onSurfaceChanged(GL10 gl10, int width, int height) {
    // 设置绘图的窗口(可以理解成在画布上划出一块区域来画图)
    GLES20.glViewport(0, 0, width, height);
    //mWidth=width;
    //mHeight=height;

    if(mDrawDemo != null){
      mDrawDemo.setDimension(width,height);
    }
  }

  @Override public void onDrawFrame(GL10 gl10) {
    //drawTriangle();
    //drawPolygon(6,0.5f);
    if(mDrawDemo != null){
      mDrawDemo.draw();
    }
  }

  private void drawTriangle() {
    // 清屏
    GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

    // 使用某套shader程序 注意必须设置 否则不显示
    GLES20.glUseProgram(mProgramId);

    // 允许顶点位置数据数组 注意必须设置 否则不显示
    GLES20.glEnableVertexAttribArray(mVPosition);
    //注意参数normalized为true或者false 貌似不受影响
    GLES20.glVertexAttribPointer(mVPosition, 2, GLES20.GL_FLOAT, true, 0, getVertexBuffer());

    GLES20.glUniform4f(mUColor, 1.0f, 0f, 1.0f, 1.0f);

    enableProjection(mWidth,mHeight);

    //注意：第三个参数为顶点数量
    //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 3);
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 3);
  }

  private FloatBuffer getVertexBuffer() {
    float[] vertex = new float[] {
        0f, 0.5f, 0.5f, 0f, -0.5f, 0f
    };
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertex.length * 4);
    byteBuffer.order(ByteOrder.nativeOrder());//设置字节顺序 注意必须设置 否则不显示
    FloatBuffer ret = byteBuffer.asFloatBuffer();
    ret.put(vertex);//向缓冲区中放入顶点坐标数据 注意必须设置 否则不显示
    ret.position(0);//设置缓冲区起始位置 注意必须设置 否则不显示
    return ret;
  }

  private int createProgram() {
    int vertexShaderId = createShader(GLES20.GL_VERTEX_SHADER, VERTEX_SOURCE);
    int fragmentShaderId = createShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SOURCE);

    int programId = GLES20.glCreateProgram();
    if (programId != 0) {
      GLES20.glAttachShader(programId, vertexShaderId);
      GLES20.glAttachShader(programId, fragmentShaderId);
      GLES20.glLinkProgram(programId);
      int[] linkStatus = new int[1];
      GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0);
      if (linkStatus[0] != GLES20.GL_TRUE) {
        Log.e(TAG, "glGetProgramiv fail" + GLES20.glGetProgramInfoLog(programId));
        GLES20.glDeleteProgram(programId);
        programId = 0;
      }
    }
    return programId;
  }

  private int createShader(int shaderType, String shaderSourceCode) {
    int shaderId = GLES20.glCreateShader(shaderType);
    if (shaderId != 0) {
      GLES20.glShaderSource(shaderId, shaderSourceCode);
      GLES20.glCompileShader(shaderId);
      int[] shaderStatus = new int[1];
      //注意：不是GLES20.GL_SHADER_COMPILER
      // 是GLES20.GL_COMPILE_STATUS
      GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, shaderStatus, 0);
      if (shaderStatus[0] != GLES20.GL_TRUE) {
        Log.e(TAG, "glGetShaderiv fail " + GLES20.glGetShaderInfoLog(shaderId));
        GLES20.glDeleteShader(shaderId);
        shaderId = 0;
      }
    }
    return shaderId;
  }

  ;
  /**
   * 绘制多边形Polygon
   */
  private int createProgramPolygon() {
    int vertexShaderId = createShader(GLES20.GL_VERTEX_SHADER, VERTEX_SOURCE);
    int fragmentShaderId = createShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SOURCE);

    int programId = GLES20.glCreateProgram();
    if (programId != 0) {
      GLES20.glAttachShader(programId, vertexShaderId);
      GLES20.glAttachShader(programId, fragmentShaderId);
      GLES20.glLinkProgram(programId);
      int[] linkStatus = new int[1];
      GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0);
      if (linkStatus[0] != GLES20.GL_TRUE) {
        Log.e(TAG, "glGetProgramiv fail" + GLES20.glGetProgramInfoLog(programId));
        GLES20.glDeleteProgram(programId);
        programId = 0;
      }
    }
    return programId;
  }

  /**
   * 绘制多边形Polygon
   * @param polygonCount 边数
   * @param radius 半径
   */
  private void drawPolygon(int polygonCount, float radius) {
    // 清屏
    GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

    float[] pointData = new float[(polygonCount + 2) * 2];
    pointData[0] = 0f;
    pointData[1] = 0f;
    //弧度
    float radian = (float) (2 * Math.PI / polygonCount);
    for (int i = 0; i < polygonCount; i++) {
      pointData[2 * i + 2] = (float) (radius * Math.cos(i * radian));
      pointData[2 * i + 2 + 1] = (float) (radius * Math.sin(i * radian));
    }
    pointData[2 * polygonCount + 2] = (float) (radius * Math.cos(0));
    pointData[2 * polygonCount + 3] = (float) (radius * Math.sin(0));

    GLES20.glUseProgram(mProgramId);

    GLES20.glEnableVertexAttribArray(mVPosition);
    GLES20.glVertexAttribPointer(mVPosition,2,GLES20.GL_FLOAT,false,0,ByteBufferUtils.getFloatBuffer(pointData));

    GLES20.glUniform4f(mUColor,1.0f,0f,0f,1.0f);

    enableProjection(mWidth,mHeight);

    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,polygonCount + 2);
  }

  /**
   *正交投影
   * @param width
   * @param height
   */
  private void enableProjection(int width,int height){
    if(sEnableProjection){
      float ratio = 1.0f;
      if(width>height){
        ratio = (width*1.0f)/height;
      }else{
        ratio = (height*1.0f)/width;
      }
      // 1. 矩阵数组
      // 2. 结果矩阵起始的偏移量
      // 3. left：x的最小值
      // 4. right：x的最大值
      // 5. bottom：y的最小值
      // 6. top：y的最大值
      // 7. near：z的最小值
      // 8. far：z的最大值
      if (width > height) {
        Matrix.orthoM(PROJECTION, 0, -ratio, ratio, -1.0f, 1.0f, -1.0f, 1.0f);
      } else {
        Matrix.orthoM(PROJECTION, 0, -1.0f, 1.0f, -ratio, ratio, -1.0f, 1.0f);
      }
      GLES20.glUniformMatrix4fv(mUMatrix,1,false,PROJECTION,0);
    }else{
      GLES20.glUniformMatrix4fv(mUMatrix,1,false,PROJECTION_CONST,0);
    }
  }
}
