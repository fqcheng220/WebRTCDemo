package fqcheng220.com.webrtcdemo.opengl.demo1;

import android.content.Context;

public class ColorFilterInverse extends TextureDraw {
    //main函数里不能直接使用1.0f，会报错
    private static final String FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            "varying vec2 v_TexCoord;\n" +
            "uniform sampler2D u_TextureUnit;\n" +
            "void main()\n" +
            "{\n" +
            "    vec4 tempColor = texture2D(u_TextureUnit, v_TexCoord);\n" +
            "    gl_FragColor = vec4(1.0-tempColor.r,1.0-tempColor.g,1.0-tempColor.b,tempColor.a);\n" +
            "}";

    public ColorFilterInverse(Context context) {
        super(context,VERTEX_SHADER,FRAGMENT_SHADER);
    }
}
