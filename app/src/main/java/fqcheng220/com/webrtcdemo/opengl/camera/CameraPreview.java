package fqcheng220.com.webrtcdemo.opengl.camera;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import fqcheng220.com.webrtcdemo.opengl.utils.ByteBufferUtils;
import fqcheng220.com.webrtcdemo.opengl.utils.GLCustomUTils;
import fqcheng220.com.webrtcdemo.opengl.IDrawDemo;
import fqcheng220.com.webrtcdemo.opengl.utils.TextureUtils;
import java.io.IOException;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 * @date 2019/12/11 13:27
 */
public class CameraPreview implements IDrawDemo {

  private final String TAG = getClass().getSimpleName();
  protected static final String VERTEX_SHADER = ""
      + "uniform mat4 u_Matrix;\n"
      + "attribute vec4 a_Position;\n"
      +
      // 纹理坐标：2个分量，S和T坐标
      "attribute vec2 a_TexCoord;\n"
      + "varying vec2 v_TexCoord;\n"
      + "void main()\n"
      + "{\n"
      + "    v_TexCoord = a_TexCoord;\n"
      + "    gl_Position = u_Matrix * a_Position;\n"
      + "}";
  private static final String FRAGMENT_SHADER = ""
      + "#extension GL_OES_EGL_image_external : require\n"
      + "precision mediump float;\n"
      + "varying vec2 v_TexCoord;\n"
      + "uniform samplerExternalOES u_TextureUnit;\n"
      + "void main()\n"
      + "{\n"
      + "    gl_FragColor = texture2D(u_TextureUnit, v_TexCoord);\n"
      + "}";
  private static final float[] POSITION = new float[] {
      0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f
  };

  private static final float[] TEXTURE_COORDINATE = new float[] {
      1f, 0f, 1f, 1f, 0f, 1f, 0f, 0f
  };
  private static final float[] PROJECTION = new float[] {
      1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0
  };

  private static final float[] PROJECTION_CONST = new float[] {
      1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1
  };
  private static final boolean sEnableProjection = true;

  private int mTextureId;
  private SurfaceTexture mSurfaceTexture;
  private float[] mTexMatrix = new float[4 * 4];
  private boolean mSurfaceUpdated = false;

  protected int mProgramId;

  private int mVPosition;
  private int mUMatrix;
  private int mVTextureCoordinate;
  private int mUTextureUnit;

  private FloatBuffer mFB_VertexCoordinate;
  private FloatBuffer mFB_TexCoordinate;

  private int mWidth;
  private int mHeight;

  private Activity mActivity;
  public CameraPreview(Activity activity){
    mActivity = activity;
  }

  /**
   * 先生成textureid 绑定到{@link GLES11Ext#GL_TEXTURE_EXTERNAL_OES} 而不是{@link GLES20#GL_TEXTURE_2D}
   * 并且设置相关纹理参数 如{@link GLES20#GL_TEXTURE_MAG_FILTER}
   *
   * 着色器源码中
   * 添加扩展声明#extension GL_OES_EGL_image_external : require
   * 把sampler2D类型换成samplerExternalOES
   *
   * GLContext上下文线程中执行render
   */
  @Override public void init() {
    mTextureId = TextureUtils.createTextureOES();
    mSurfaceTexture = new SurfaceTexture(mTextureId);
    mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
      @Override public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        mSurfaceUpdated = true;
      }
    });
    Camera camera = CameraUtils.cameraOpen(0,mActivity,320,480,30);
    try {
      camera.setPreviewTexture(mSurfaceTexture);
    } catch (IOException e) {
      e.printStackTrace();
    }
    camera.startPreview();

    mProgramId = GLCustomUTils.createProgram(VERTEX_SHADER, FRAGMENT_SHADER);

    mVPosition = GLES20.glGetAttribLocation(mProgramId, "a_Position");
    mUMatrix = GLES20.glGetUniformLocation(mProgramId, "u_Matrix");
    mVTextureCoordinate = GLES20.glGetAttribLocation(mProgramId, "a_TexCoord");
    mUTextureUnit = GLES20.glGetUniformLocation(mProgramId, "u_TextureUnit");

    /**
     * 实际显示效果来看 preview逆时针旋转了90度 所以需要把顶点坐标或者纹理坐标顺时针旋转90度即可
     */
    mFB_VertexCoordinate = ByteBufferUtils.getFloatBuffer(RotationUtils.rotate(270,POSITION,false));
    mFB_TexCoordinate =  ByteBufferUtils.getFloatBuffer(TEXTURE_COORDINATE);
  }

  @Override public void setDimension(int width, int height) {
    mWidth = width;
    mHeight = height;
  }

  @Override public void draw() {
    if (mSurfaceUpdated) {
      mSurfaceTexture.updateTexImage();
      mSurfaceTexture.getTransformMatrix(mTexMatrix);
      Log.d(TAG, "draw mTexMatrix=" + printMatrix(mTexMatrix));

      GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT);

      GLES20.glUseProgram(mProgramId);

      GLES20.glEnableVertexAttribArray(mVPosition);
      GLES20.glVertexAttribPointer(mVPosition, 2, GLES20.GL_FLOAT, true, 0, mFB_VertexCoordinate);

      GLES20.glEnableVertexAttribArray(mVTextureCoordinate);
      GLES20.glVertexAttribPointer(mVTextureCoordinate, 2, GLES20.GL_FLOAT, true, 0,mFB_TexCoordinate);

      // 设置当前活动的纹理单元为纹理单元0
      GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
      // 绑定textureId到纹理目标GL_TEXTURE_2D 有多种纹理目标
      GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureId);
      // 将纹理单元传递片段着色器的u_TextureUnit GLSL中操作的sampler2D是glint型 可以赋值为纹理单元
      GLES20.glUniform1i(mUTextureUnit, 0);

      enableProjection(mWidth,mHeight);
      //GLES20.glUniformMatrix4fv(mUMatrix, 1, false, mTexMatrix, 0);
      //
      //注意：此处必须为GLES20.GL_TRIANGLE_FAN 详见GLES20.GL_TRIANGLE_FAN GLES20.GL_TRIANGLE_STRIP GLES20.GL_TRIANGLES的区别
      GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);

      GLES20.glDisableVertexAttribArray(mVPosition);
      GLES20.glDisableVertexAttribArray(mVTextureCoordinate);
      GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
    }
  }

  /**
   * 正交投影
   */
  private void enableProjection(int width, int height) {
    if (sEnableProjection) {
      float ratio = 1.0f;
      if (width > height) {
        ratio = (width * 1.0f) / height;
      } else {
        ratio = (height * 1.0f) / width;
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
      GLES20.glUniformMatrix4fv(mUMatrix, 1, false, PROJECTION, 0);
    } else {
      GLES20.glUniformMatrix4fv(mUMatrix, 1, false, PROJECTION_CONST, 0);
    }
  }

  private String printMatrix(float[] matrix) {
    StringBuilder stringBuilder = new StringBuilder();
    for (float item : matrix) {
      stringBuilder.append(item + ",");
    }
    return stringBuilder.toString();
  }
}
