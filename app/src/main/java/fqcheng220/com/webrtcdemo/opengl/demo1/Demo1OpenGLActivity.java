package fqcheng220.com.webrtcdemo.opengl.demo1;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import fqcheng220.com.webrtcdemo.R;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 * @date 2019/12/4 16:11
 */
public class Demo1OpenGLActivity extends AppCompatActivity {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_opengl_demo1);
    GLSurfaceView glSurfaceView = findViewById(R.id.glSurfaceView1);

    // 设置OpenGL版本(一定要设置) 注意必须设置 否则不显示
    glSurfaceView.setEGLContextClientVersion(2);
    // 设置渲染器(后面会着重讲这个渲染器的类)
    Demo1GLSurfaceViewRender render = new Demo1GLSurfaceViewRender();
    //render.setmDrawDemo(new ShapeTriangle());
    //render.setmDrawDemo(new ShapePolygon());
    render.setmDrawDemo(new TextureDraw(this));
    glSurfaceView.setRenderer(render);
    // 设置渲染模式为连续模式(会以60fps的速度刷新)
    glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
  }
}
