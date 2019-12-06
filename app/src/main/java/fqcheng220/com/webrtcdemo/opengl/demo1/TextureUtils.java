package fqcheng220.com.webrtcdemo.opengl.demo1;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 * @date 2019/12/6 11:58
 */
public class TextureUtils {
  private static final String TAG = TextureUtils.class.getSimpleName();

  public static int loadBitmapToTexture(Bitmap bitmap,boolean bRecycle) {
    int[] textureIds = new int[2];
    GLES20.glGenTextures(1, textureIds, 0);
    if (textureIds[0] == 0) {
      Log.e(TAG, "glGenTextures error");
      return 0;
    }
    //2. 将纹理绑定到OpenGL对象上
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);
    // 3. 设置纹理过滤参数:解决纹理缩放过程中的锯齿问题。若不设置，则会导致纹理为黑色
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
    // 4. 通过OpenGL对象读取Bitmap数据，并且绑定到纹理对象上，之后就可以回收Bitmap对象
    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    // Note: Following code may cause an error to be reported in the
    // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
    // Failed to generate texture mipmap levels (error=3)
    // No OpenGL error will be encountered (glGetError() will return
    // 0). If this happens, just squash the source image to be
    // square. It will look the same because of texture coordinates,
    // and mipmap generation will work.
    // 5. 生成Mip位图
    GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
    //
    if(bRecycle){
      bitmap.recycle();
    }
    // 7. 将纹理从OpenGL对象上解绑
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    return textureIds[0];
  }

  public static int loadBitmapToTextureV2(Bitmap bitmap,boolean bRecycle) {
    return loadBitmapToTextureV2(bitmap,bRecycle,0);
  }

  /**
   * 可以被高频调用
   * 例如开启{@link GLSurfaceView#RENDERMODE_CONTINUOUSLY}模式
   * {@link GLSurfaceView.Renderer#onDrawFrame(GL10)}中
   * 用{@link GLUtils#texSubImage2D(int, int, int, int, Bitmap)}替代{@link GLUtils#texImage2D(int, int, int, Bitmap, int)}
   * 测试验证高频调用{@link GLUtils#texImage2D(int, int, int, Bitmap, int)}会崩溃  但是无日志输出！！ Profile查看内存一直在暴涨 不会释放 直至崩溃
   *
   * 疑问点：bitmap不回收也不会造成内存涨上去的现象？？？
   * @param bitmap
   * @param bRecycle
   * @param usedTexId 可重复使用的纹理id
   * @return
   */
  public static int loadBitmapToTextureV2(Bitmap bitmap,boolean bRecycle,int usedTexId) {
    int[] textureIds = new int[2];
    if(usedTexId == 0){
      GLES20.glGenTextures(1, textureIds, 0);
      if (textureIds[0] == 0) {
        Log.e(TAG, "glGenTextures error");
        return 0;
      }
      //2. 将纹理绑定到OpenGL对象上
      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);
      // 3. 设置纹理过滤参数:解决纹理缩放过程中的锯齿问题。若不设置，则会导致纹理为黑色
      GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
      GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
      //纹理也有坐标系，称UV坐标，或者ST坐标
      GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT); // S轴的拉伸方式为重复，决定采样值的坐标超出图片范围时的采样方式
      GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT); // T轴的拉伸方式为重复
      // 4. 通过OpenGL对象读取Bitmap数据，并且绑定到纹理对象上，之后就可以回收Bitmap对象
      GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }else{
      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, usedTexId);
      GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, bitmap);
      textureIds[0] = usedTexId;
    }
    //
    if(bRecycle){
      bitmap.recycle();
    }
    // 7. 将纹理从OpenGL对象上解绑
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    return textureIds[0];
  }
}
