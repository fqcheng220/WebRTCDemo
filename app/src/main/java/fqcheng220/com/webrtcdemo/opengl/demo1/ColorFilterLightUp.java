package fqcheng220.com.webrtcdemo.opengl.demo1;

import android.content.Context;
import android.opengl.GLES20;

public class ColorFilterLightUp extends TextureDraw {
    //main函数里不能直接使用1.0f，会报错
    private static final String FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            "varying vec2 v_TexCoord;\n" +
            "uniform sampler2D u_TextureUnit;\n" +
            "uniform float uTime;\n" +
            "void main()\n" +
            "{\n" +
            "    vec4 tempColor = texture2D(u_TextureUnit, v_TexCoord);\n" +
            "    float lightUpValue = abs(sin(uTime/1000.0))/4.0;\n" +
//            "    float lightUpValue = 0.0;\n" +
            "    vec4 addColor = vec4(lightUpValue, lightUpValue, lightUpValue, 1.0);\n" +
//            "    gl_FragColor = tempColor + addColor;\n" +
            "    gl_FragColor = vec4(lightUpValue+tempColor.r,lightUpValue+tempColor.g,lightUpValue+tempColor.b,1.0+tempColor.a);\n" +
//            "    gl_FragColor = vec4(tempColor.r,tempColor.g,tempColor.b,tempColor.a);\n" +
            "}";

//    private static final String FRAGMENT_SHADER = "" +
//            "precision mediump float;\n" +
//            "varying vec2 v_TexCoord;\n" +
//            "uniform sampler2D u_TextureUnit;\n" +
//            "void main()\n" +
//            "{\n" +
//            "    vec4 tempColor = texture2D(u_TextureUnit, v_TexCoord);\n" +
//            "    gl_FragColor = vec4(1.0-tempColor.r,1.0-tempColor.g,1.0-tempColor.b,tempColor.a);\n" +
//            "}";

    private long mCreateTime;
    private int mUTime;
    public ColorFilterLightUp(Context context) {
        super(context,VERTEX_SHADER,FRAGMENT_SHADER);
    }

    @Override
    public void init() {
        super.init();
        mCreateTime = System.currentTimeMillis();
        mUTime = GLES20.glGetUniformLocation(mProgramId, "uTime");
    }

    @Override
    public void draw() {
        /**
         * 注意 此处不需要{@link GLES20#glEnableVertexAttribArray(int)}
         */
        GLES20.glUniform1f(mUTime,(System.currentTimeMillis() - mCreateTime));
        super.draw();
    }
}
