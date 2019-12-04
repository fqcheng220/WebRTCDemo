package fqcheng220.com.webrtcdemo.opengl.demo1;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
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
 * @date 2019/12/4 16:09
 */
public class Demo1GLSurfaceViewRender implements GLSurfaceView.Renderer {
  private final String TAG = getClass().getSimpleName();

  private int mProgramId;
  private int mVPosition;
  private int mUColor;

  @Override public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
    mProgramId = createProgram();
    mVPosition = GLES20.glGetAttribLocation(mProgramId, "vPosition");
    mUColor = GLES20.glGetUniformLocation(mProgramId, "uColor");
  }

  @Override public void onSurfaceChanged(GL10 gl10, int width, int height) {
    // 设置绘图的窗口(可以理解成在画布上划出一块区域来画图)
    GLES20.glViewport(0, 0, width, height);
  }

  @Override public void onDrawFrame(GL10 gl10) {
    FloatBuffer floatBufferVertex = getVertexBuffer();
    // 清屏
    GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
    // 使用某套shader程序 注意必须设置 否则不显示
    GLES20.glUseProgram(mProgramId);
    // 允许顶点位置数据数组 注意必须设置 否则不显示
    GLES20.glEnableVertexAttribArray(mVPosition);
    //注意参数normalized为true或者false 貌似不受影响
    GLES20.glVertexAttribPointer(mVPosition, 2, GLES20.GL_FLOAT, true, 0, floatBufferVertex);
    GLES20.glUniform4f(mUColor, 1.0f, 0f, 1.0f, 1.0f);

    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 3);
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

  private static final String VERTEX_SOURCE = "attribute vec2 vPosition;            \n" // 顶点位置属性vPosition
      + "void main(){                         \n" + "   gl_Position = vec4(vPosition,0,1);\n" // 确定顶点位置
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
}
