package fqcheng220.com.webrtcdemo.opengl.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import fqcheng220.com.webrtcdemo.R;
import fqcheng220.com.webrtcdemo.opengl.utils.ByteBufferUtils;
import fqcheng220.com.webrtcdemo.opengl.utils.GLCustomUTils;
import fqcheng220.com.webrtcdemo.opengl.IDrawDemo;
import fqcheng220.com.webrtcdemo.opengl.utils.TextureUtils;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description:
 * 纹理：在OpenGL中简单理解就是一张图片
 * 纹理Id：纹理的直接引用
 * 纹理单元：纹理的操作容器，有GL_TEXTURE0、GL_TEXTURE1、GL_TEXTURE2等，纹理单元的数量是有限的，最多16个。所以在最多只能同时操作16个纹理。在切换使用纹理单元的时候，使用glActiveTexture方法。
 * 纹理目标：一个纹理单元中包含了多个类型的纹理目标，有GL_TEXTURE_1D、GL_TEXTURE_2D、CUBE_MAP等。本章中，将纹理ID绑定到纹理单元0的GL_TEXTURE_2D纹理目标上，之后对纹理目标的操作都是对纹理Id对应的数据进行操作。
 *
 * 作者：Benhero
 * 链接：https://www.jianshu.com/p/3659f4649f98
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 * @date 2019/12/5 15:30
 */
public class TextureDraw implements IDrawDemo {
  private final String TAG = getClass().getSimpleName();
  protected static final String VERTEX_SHADER = "" +
      "uniform mat4 u_Matrix;\n" +
      "attribute vec4 a_Position;\n" +
      // 纹理坐标：2个分量，S和T坐标
      "attribute vec2 a_TexCoord;\n" +
      "varying vec2 v_TexCoord;\n" +
      "void main()\n" +
      "{\n" +
      "    v_TexCoord = a_TexCoord;\n" +
      "    gl_Position = u_Matrix * a_Position;\n" +
      "}";
  //private static final String FRAGMENT_SHADER = "" +
  //    "precision mediump float;\n" +
  //    "varying vec2 v_TexCoord;\n" +
  //    "uniform sampler2D u_TextureUnit;\n" +
  //    "void main()\n" +
  //    "{\n" +
  //    "    gl_FragColor = texture2D(u_TextureUnit, v_TexCoord);\n" +
  //    "}";
  private static final String FRAGMENT_SHADER = "" +
      "precision mediump float;\n" +
      "varying vec2 v_TexCoord;\n" +
      "uniform vec2 u_TexCoord;\n" +
      "uniform sampler2D u_TextureUnit;\n" +
      "void main()\n" +
      "{\n" +
      "    gl_FragColor = texture2D(u_TextureUnit, v_TexCoord);\n" +
      "}";


  // 数据中有多少个顶点，管线就调用多少次顶点着色器
  public static final String NO_FILTER_VERTEX_SHADER = "" +
      "attribute vec4 position;\n" + // 顶点着色器的顶点坐标,由外部程序传入
      "attribute vec4 inputTextureCoordinate;\n" + // 传入的纹理坐标
      " \n" +
      "varying vec2 textureCoordinate;\n" +
      " \n" +
      "void main()\n" +
      "{\n" +
      "    gl_Position = position;\n" +
      "    textureCoordinate = inputTextureCoordinate.xy;\n" + // 最终顶点位置
      "}";

  // 光栅化后产生了多少个片段，就会插值计算出多少个varying变量，同时渲染管线就会调用多少次片段着色器
  public static final String NO_FILTER_FRAGMENT_SHADER = "" +
      "varying highp vec2 textureCoordinate;\n" + // 最终顶点位置，上面顶点着色器的varying变量会传递到这里
      " \n" +
      "uniform sampler2D inputImageTexture;\n" + // 外部传入的图片纹理 即代表整张图片的数据
      " \n" +
      "void main()\n" +
      "{\n" +
      "     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n" +  // 调用函数 进行纹理贴图
      "}";

  private static final float[] POSITION = new float[]{
      0.5f,0.5f,
      0.5f,-0.5f,
      -0.5f,-0.5f,
      -0.5f,0.5f
  };

  private static final float[] TEXTURE_COORDINATE = new float[]{
      1f,0f,
      1f,1f,
      0f,1f,
      0f,0f
  };

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
  private static final boolean sEnableProjection = true;

  protected int mProgramId;
  private int mTextureId;

  private int mVPosition;
  private int mUMatrix;
  private int mVTextureCoordinate;
  private int mUTextureCoordinate;
  private int mUTextureUnit;

  private int mWidth;
  private int mHeight;

  private Context mCtx;
  private String mSourceVertexShader;
  private String mSourceFragmentShader;

  private int mDrawCount = 0;

  public TextureDraw(Context context){
    this(context,VERTEX_SHADER,FRAGMENT_SHADER);
  }

  public TextureDraw(Context context,String sourceVertexShader,String sourceFragmentShader){
    mCtx = context;
    mSourceFragmentShader = sourceFragmentShader;
    mSourceVertexShader = sourceVertexShader;
  }

  @Override public void init() {
    GLES20.glClearColor(0, 0, 0, 1);
    GLES20.glDisable(GLES20.GL_DEPTH_TEST); // 当我们需要绘制透明图片时，就需要关闭它

    mProgramId = GLCustomUTils.createProgram(mSourceVertexShader,mSourceFragmentShader);
    mTextureId = loadBitmapToTexture(mCtx);

    mVPosition = GLES20.glGetAttribLocation(mProgramId, "a_Position");
    mUMatrix = GLES20.glGetUniformLocation(mProgramId, "u_Matrix");
    mVTextureCoordinate = GLES20.glGetAttribLocation(mProgramId, "a_TexCoord");
    mUTextureCoordinate = GLES20.glGetUniformLocation(mProgramId, "u_TexCoord");
    mUTextureUnit = GLES20.glGetUniformLocation(mProgramId,"u_TextureUnit");
  }

  @Override public void setDimension(int width, int height) {
    mWidth=width;
    mHeight=height;
  }

  @Override public void draw() {
    Log.d(TAG, "draw " + (++mDrawCount));

    mTextureId = loadBitmapToTexture(mCtx);

    GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT);

    GLES20.glUseProgram(mProgramId);

    GLES20.glEnableVertexAttribArray(mVPosition);
    GLES20.glVertexAttribPointer(mVPosition,2,GLES20.GL_FLOAT,true,0, ByteBufferUtils.getFloatBuffer(POSITION));

    GLES20.glEnableVertexAttribArray(mVTextureCoordinate);
    GLES20.glVertexAttribPointer(mVTextureCoordinate,2,GLES20.GL_FLOAT,true,0,ByteBufferUtils.getFloatBuffer(TEXTURE_COORDINATE));

    //测试无效
    //在片元着色器中使用uniform限定符修饰的变量替换varying限定符修饰的变量
    //varying限定符修饰的变量是vertex和fragment shader之间做数据传递用的，并且OpenGL ES会对其进行线性插值 很重要！！！
    //https://blog.csdn.net/lb377463323/article/details/65939279
    //https://upload-images.jianshu.io/upload_images/1909670-1f4ad6bfe79c0fa2.jpg?imageMogr2/auto-orient/strip|imageView2/2/format/webp
    GLES20.glUniform2fv(mUTextureCoordinate,2,ByteBufferUtils.getFloatBuffer(TEXTURE_COORDINATE));

    // 设置当前活动的纹理单元为纹理单元0
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    // 绑定textureId到纹理目标GL_TEXTURE_2D 有多种纹理目标
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,mTextureId);
    // 将纹理单元传递片段着色器的u_TextureUnit GLSL中操作的sampler2D是glint型 可以赋值为纹理单元
    GLES20.glUniform1i(mUTextureUnit,0);

    enableProjection(mWidth,mHeight);

    //注意：此处必须为GLES20.GL_TRIANGLE_FAN 详见GLES20.GL_TRIANGLE_FAN GLES20.GL_TRIANGLE_STRIP GLES20.GL_TRIANGLES的区别
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,4);

    GLES20.glDisableVertexAttribArray(mVPosition);
    GLES20.glDisableVertexAttribArray(mVTextureCoordinate);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
  }

  private int loadBitmapToTexture(Context context) {
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inScaled = false;
    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.test, options);
    if (bitmap == null) {
      Log.e(TAG, "bitmap==null");
      return 0;
    }
    android.graphics.Matrix matrix = new android.graphics.Matrix();
    matrix.postTranslate(0.5f,0.5f);
    bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
    return TextureUtils.loadBitmapToTextureV2(bitmap,false,mTextureId);
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
