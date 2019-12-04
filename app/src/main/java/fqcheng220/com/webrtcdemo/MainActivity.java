package fqcheng220.com.webrtcdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import fqcheng220.com.webrtcdemo.opengl.demo1.Demo1OpenGLActivity;
import fqcheng220.com.webrtcdemo.step01.Step01Activity;
import fqcheng220.com.webrtcdemo.step02.Step02Activity;
import fqcheng220.com.webrtcdemo.step03.Step03Activity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_step01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Step01Activity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.btn_step02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Step02Activity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.btn_step03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Step03Activity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.btn_opengl_demo1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Demo1OpenGLActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
