package fqcheng220.com.webrtcdemo.opengl.utils;

import android.opengl.GLES20;
import android.util.Log;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 * @date 2019/12/5 15:32
 */
public class GLCustomUTils {
  private static final String TAG = GLCustomUTils.class.getSimpleName();

  public static int createProgram(String vertextSource,String fragmentSource) {
    int vertexShaderId = createShader(GLES20.GL_VERTEX_SHADER, vertextSource);
    int fragmentShaderId = createShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);

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

  private static int createShader(int shaderType, String shaderSourceCode) {
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
}
