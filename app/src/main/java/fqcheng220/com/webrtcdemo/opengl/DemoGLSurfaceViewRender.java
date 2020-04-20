package fqcheng220.com.webrtcdemo.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
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
public class DemoGLSurfaceViewRender implements GLSurfaceView.Renderer {
  private final String TAG = getClass().getSimpleName();

  public void setmDrawDemo(IDrawDemo mDrawDemo) {
    this.mDrawDemo = mDrawDemo;
  }
  private IDrawDemo mDrawDemo;

  @Override public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
    if(mDrawDemo != null){
      mDrawDemo.init();
    }
  }

  @Override public void onSurfaceChanged(GL10 gl10, int width, int height) {
    // 设置绘图的窗口(可以理解成在画布上划出一块区域来画图)
    GLES20.glViewport(0, 0, width, height);
    if(mDrawDemo != null){
      mDrawDemo.setDimension(width,height);
    }
  }

  @Override public void onDrawFrame(GL10 gl10) {
    if(mDrawDemo != null){
      mDrawDemo.draw();
    }
  }
}
