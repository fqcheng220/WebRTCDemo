package fqcheng220.com.webrtcdemo.opengl.demo2;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.view.Surface;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 * @date 2019/12/11 19:46
 */
public class CameraUtils {
  public static Camera cameraOpen(final int cameraId,final Activity activity,
                                  final int width,
                                  final int height,
                                  final int frameRate){
    Camera camera = Camera.open(cameraId);
    setCameraDisplayOrientation(activity,cameraId,camera);

    Camera.Parameters parameters = camera.getParameters();
    Size previewSize = getClosestSupportedSize(parameters.getSupportedPreviewSizes(),width,height);
    parameters.setPreviewSize(previewSize.width,previewSize.height);
    Size pictureSize = getClosestSupportedSize(parameters.getSupportedPictureSizes(),width,height);
    parameters.setPictureSize(pictureSize.width,pictureSize.height);

    List<String> focusModes = parameters.getSupportedFocusModes();
    if (parameters.isVideoStabilizationSupported()) {
      parameters.setVideoStabilization(true);
    }

    if (focusModes.contains("continuous-video")) {
      parameters.setFocusMode("continuous-video");
    }
    camera.setParameters(parameters);

    return camera;
  }

  public static Size getClosestSupportedSize(List<Size> supportedSizes, final int requestedWidth, final int requestedHeight) {
    return (Size) Collections.min(supportedSizes, new ClosestComparator<Size>() {
      int diff(Size size) {
        return Math.abs(requestedWidth - size.width) + Math.abs(requestedHeight - size.height);
      }
    });
  }

  private abstract static class ClosestComparator<T> implements Comparator<T> {
    private ClosestComparator() {
    }

    abstract int diff(T var1);

    public int compare(T t1, T t2) {
      return this.diff(t1) - this.diff(t2);
    }
  }

  public static void setCameraDisplayOrientation(Activity activity,
      int cameraId, android.hardware.Camera camera) {
    android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
    android.hardware.Camera.getCameraInfo(cameraId, info);
    int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
    int degrees = 0;
    switch (rotation) {
      case Surface.ROTATION_0:
        degrees = 0;
        break;
      case Surface.ROTATION_90:
        degrees = 90;
        break;
      case Surface.ROTATION_180:
        degrees = 180;
        break;
      case Surface.ROTATION_270:
        degrees = 270;
        break;
    }

    int result;
    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      result = (info.orientation + degrees) % 360;
      result = (360 - result) % 360;  // compensate the mirror
    } else {  // back-facing
      result = (info.orientation - degrees + 360) % 360;
    }
    camera.setDisplayOrientation(result);
  }
}
