package fqcheng220.com.webrtcdemo;

import android.util.Log;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 * @date 2019/11/22 19:57
 */
public class SdpAdapter implements SdpObserver {
  private String tag;
  public SdpAdapter(String tag) {
    this.tag = "cfq " + tag;
  }

  private void log(String s) {
    Log.d(tag, s);
  }

  @Override public void onCreateSuccess(SessionDescription sessionDescription) {
    log("onCreateSuccess " + sessionDescription);
  }

  @Override public void onSetSuccess() {
    log("onSetSuccess ");
  }

  @Override public void onCreateFailure(String s) {
    log("onCreateFailure "+s);
  }

  @Override public void onSetFailure(String s) {
    log("onSetFailure "+s);
  }
}
