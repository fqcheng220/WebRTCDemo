package fqcheng220.com.webrtcdemo.opengl.camera;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 * @date 2019/12/11 17:56
 */
public class RotationUtils {
  /**
   * 对顶点坐标或者纹理坐标做旋转
   * @param degreeCouterclockwise 逆时针旋转角度
   * @param sourceArra
   * @param sourceArrClockwise sourceArra是否为顺时针摆放
   * @return
   */
  public static float[] rotate(final int degreeCouterclockwise,float[] sourceArra,boolean sourceArrClockwise){
    float[] resultArr = sourceArra;
    switch (degreeCouterclockwise){
      case 90://逆时针90度
        if(sourceArrClockwise){
          resultArr = new float[]{
              sourceArra[2],sourceArra[3],
              sourceArra[4],sourceArra[5],
              sourceArra[6],sourceArra[7],
              sourceArra[0],sourceArra[1],
          };
        }else{
          resultArr = new float[]{
              sourceArra[6],sourceArra[7],
              sourceArra[0],sourceArra[1],
              sourceArra[2],sourceArra[3],
              sourceArra[4],sourceArra[5],
          };
        }
        break;
      case 180:
        if(sourceArrClockwise){
          resultArr = new float[]{
              sourceArra[4],sourceArra[5],
              sourceArra[6],sourceArra[7],
              sourceArra[0],sourceArra[1],
              sourceArra[2],sourceArra[3],
          };
        }else{
          resultArr = new float[]{
              sourceArra[4],sourceArra[5],
              sourceArra[6],sourceArra[7],
              sourceArra[0],sourceArra[1],
              sourceArra[2],sourceArra[3],
          };
        }
        break;
      case 270:
        if(sourceArrClockwise){
          resultArr = new float[]{
              sourceArra[6],sourceArra[7],
              sourceArra[0],sourceArra[1],
              sourceArra[2],sourceArra[3],
              sourceArra[4],sourceArra[5],
          };
        }else{
          resultArr = new float[]{
              sourceArra[2],sourceArra[3],
              sourceArra[4],sourceArra[5],
              sourceArra[6],sourceArra[7],
              sourceArra[0],sourceArra[1],
          };
        }
        break;
      case 0:
        default:
          break;

    }
    return resultArr;
  }
}
