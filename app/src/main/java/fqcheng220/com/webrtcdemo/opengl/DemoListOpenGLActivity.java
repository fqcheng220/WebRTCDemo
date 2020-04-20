package fqcheng220.com.webrtcdemo.opengl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import fqcheng220.com.webrtcdemo.R;
import fqcheng220.com.webrtcdemo.opengl.camera.CameraPreviewDemoOpenGLActivity;
import fqcheng220.com.webrtcdemo.opengl.filter.ColorFilterGrayDemoOpenGLActivity;
import fqcheng220.com.webrtcdemo.opengl.filter.ColorFilterInverseDemoOpenGLActivity;
import fqcheng220.com.webrtcdemo.opengl.filter.ColorFilterLightUpDemoOpenGLActivity;
import fqcheng220.com.webrtcdemo.opengl.filter.PositionFilterTranslateDemoOpenGLActivity;
import fqcheng220.com.webrtcdemo.opengl.shape.ShapePolygonDemoOpenGLActivity;
import fqcheng220.com.webrtcdemo.opengl.shape.ShapeTriangleDemoOpenGLActivity;
import fqcheng220.com.webrtcdemo.opengl.texture.TextureDemoOpenGLActivity;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 * @date 2019/12/4 16:11
 */
public class DemoListOpenGLActivity extends AppCompatActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_opengl_demo_list);

    findViewById(R.id.btn_shape_triangle).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(DemoListOpenGLActivity.this, ShapeTriangleDemoOpenGLActivity.class);
        DemoListOpenGLActivity.this.startActivity(intent);
      }
    });

    findViewById(R.id.btn_shape_polygon).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(DemoListOpenGLActivity.this, ShapePolygonDemoOpenGLActivity.class);
        DemoListOpenGLActivity.this.startActivity(intent);
      }
    });

    findViewById(R.id.btn_color_filter_gray).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(DemoListOpenGLActivity.this, ColorFilterGrayDemoOpenGLActivity.class);
        DemoListOpenGLActivity.this.startActivity(intent);
      }
    });

    findViewById(R.id.btn_color_filter_inverse).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(DemoListOpenGLActivity.this, ColorFilterInverseDemoOpenGLActivity.class);
        DemoListOpenGLActivity.this.startActivity(intent);
      }
    });

    findViewById(R.id.btn_color_filter_lightup).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(DemoListOpenGLActivity.this, ColorFilterLightUpDemoOpenGLActivity.class);
        DemoListOpenGLActivity.this.startActivity(intent);
      }
    });

    findViewById(R.id.btn_position_filter_translate).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(DemoListOpenGLActivity.this, PositionFilterTranslateDemoOpenGLActivity.class);
        DemoListOpenGLActivity.this.startActivity(intent);
      }
    });

    findViewById(R.id.btn_texture).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(DemoListOpenGLActivity.this, TextureDemoOpenGLActivity.class);
        DemoListOpenGLActivity.this.startActivity(intent);
      }
    });

    findViewById(R.id.btn_camera_preview).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(DemoListOpenGLActivity.this, CameraPreviewDemoOpenGLActivity.class);
        DemoListOpenGLActivity.this.startActivity(intent);
      }
    });
  }
}
