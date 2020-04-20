package fqcheng220.com.webrtcdemo.opengl.filter;

import android.content.Context;
import android.opengl.GLES20;
import fqcheng220.com.webrtcdemo.opengl.texture.TextureDraw;

public class PositionFilterTranslate extends TextureDraw {
    //main函数里不能直接使用1.0f，会报错
    private static final String FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            "varying vec2 v_TexCoord;\n" +
            "uniform sampler2D u_TextureUnit;\n" +
            "uniform float xOffset;\n" +
            "uniform float yOffset;\n" +
            "void main()\n" +
            "{\n" +
            "    vec2 dest_v_TexCoord = vec2(v_TexCoord.x+xOffset, v_TexCoord.y+yOffset);\n" +
//            "    if(dest_v_TexCoord.x>=0.0 && dest_v_TexCoord.x<=1.0" +
//            "&& dest_v_TexCoord.y>=0.0&& dest_v_TexCoord.y<=1.0)\n" +
//            "    {\n" +
            "    gl_FragColor = texture2D(u_TextureUnit, dest_v_TexCoord);\n" +
//            "    }\n" +
            "}";
    private long mCreateTime;
    private int mIndex_XOffset;
    private int mIndex_YOffset;

    @Override
    public void init() {
        super.init();
        mCreateTime = System.currentTimeMillis();
        mIndex_XOffset = GLES20.glGetUniformLocation(mProgramId,"xOffset");
        mIndex_YOffset = GLES20.glGetUniformLocation(mProgramId,"yOffset");
    }

    public PositionFilterTranslate(Context context) {
        super(context,VERTEX_SHADER,FRAGMENT_SHADER);
    }

    @Override
    public void draw() {
        GLES20.glUniform1f(mIndex_XOffset,(float)(Math.sin((System.currentTimeMillis() - mCreateTime)/1000.0)/2.0));
        GLES20.glUniform1f(mIndex_YOffset,0.0f);
        super.draw();
    }
}
