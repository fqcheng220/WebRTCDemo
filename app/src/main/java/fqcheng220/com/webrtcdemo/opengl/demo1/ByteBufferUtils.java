package fqcheng220.com.webrtcdemo.opengl.demo1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 * @date 2019/12/5 11:24
 */
public class ByteBufferUtils {
  public static FloatBuffer getFloatBuffer(float[] arr){
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(arr.length * 4);
    byteBuffer.order(ByteOrder.nativeOrder());//设置字节顺序 注意必须设置 否则不显示
    FloatBuffer ret = byteBuffer.asFloatBuffer();
    ret.put(arr);//向缓冲区中放入顶点坐标数据 注意必须设置 否则不显示
    ret.position(0);//设置缓冲区起始位置 注意必须设置 否则不显示
    return ret;
  }
}
