package fqcheng220.com.webrtcdemo.opengl.shape;

import android.content.Context;
import fqcheng220.com.webrtcdemo.opengl.DrawDemoOpenGLActivity;
import fqcheng220.com.webrtcdemo.opengl.IDrawDemo;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/4/20 9:53
 */
public class ShapeTriangleDemoOpenGLActivity extends DrawDemoOpenGLActivity {
  @Override protected IDrawDemo getDrawDemo(Context context) {
    return new ShapeTriangle();
  }
}
