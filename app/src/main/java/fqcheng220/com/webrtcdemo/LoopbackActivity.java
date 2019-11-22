package fqcheng220.com.webrtcdemo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import org.webrtc.Camera1Enumerator;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 * @date 2019/11/22 14:42
 */
public class LoopbackActivity extends AppCompatActivity {

  private PeerConnection mPeerConnectionLocal;
  private PeerConnection mPeerConnectionRemote;
  private SurfaceViewRenderer mLocalView;
  private SurfaceViewRenderer mRemoteView;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_loopback);
    mLocalView = findViewById(R.id.localView);
    mRemoteView = findViewById(R.id.remoteView);

    EglBase.Context eglBaseContext = EglBase.create().getEglBaseContext();
    mLocalView.init(eglBaseContext,null);
    mRemoteView.init(eglBaseContext,null);

    initWebRTCEnv();
  }

  private void initWebRTCEnv(){
    // create PeerConnectionFactory
    PeerConnectionFactory.InitializationOptions initializationOptions = PeerConnectionFactory.InitializationOptions.builder(this).createInitializationOptions();
    PeerConnectionFactory.initialize(initializationOptions);
    PeerConnectionFactory peerConnectionFactory = PeerConnectionFactory.builder().createPeerConnectionFactory();

    VideoCapturer localVideoCapturer = createCameraCapturer(true);
    VideoSource localVideoSource = peerConnectionFactory.createVideoSource(localVideoCapturer.isScreencast());
    VideoTrack localVideoTrack = peerConnectionFactory.createVideoTrack("localVideoTrack",localVideoSource);

    VideoCapturer remoteVideoCapturer = createCameraCapturer(false);
    VideoSource remoteVideoSource = peerConnectionFactory.createVideoSource(remoteVideoCapturer.isScreencast());
    VideoTrack remoteVideoTrack = peerConnectionFactory.createVideoTrack("remoteVideoTrack",remoteVideoSource);

    MediaStream localMediaStream = peerConnectionFactory.createLocalMediaStream("local");
    localMediaStream.addTrack(localVideoTrack);
    MediaStream remoteMediaStream = peerConnectionFactory.createLocalMediaStream("remote");
    remoteMediaStream.addTrack(remoteVideoTrack);
    call(peerConnectionFactory,localMediaStream,remoteMediaStream);
  }

  private void call(PeerConnectionFactory peerConnectionFactory, MediaStream localMediaStream,MediaStream remoteMediaStream){
    List<PeerConnection.IceServer> iceServerList = new ArrayList<>();
    mPeerConnectionLocal = peerConnectionFactory.createPeerConnection(iceServerList, new PeerConnectionAdapter("local connection"){
      @Override public void onAddStream(MediaStream mediaStream) {
        super.onAddStream(mediaStream);
        VideoTrack videoTrack = mediaStream.videoTracks.get(0);
        runOnUiThread(new Runnable() {
          @Override public void run() {
            if(videoTrack != null){
              videoTrack.addSink(mLocalView);
            }
          }
        });
      }

      @Override public void onIceCandidate(IceCandidate iceCandidate) {
        super.onIceCandidate(iceCandidate);
        mPeerConnectionRemote.addIceCandidate(iceCandidate);
      }
    });
    mPeerConnectionRemote = peerConnectionFactory.createPeerConnection(iceServerList, new PeerConnectionAdapter("remote connection"){
      @Override public void onAddStream(MediaStream mediaStream) {
        super.onAddStream(mediaStream);
        VideoTrack videoTrack = mediaStream.videoTracks.get(0);
        runOnUiThread(new Runnable() {
          @Override public void run() {
            if(videoTrack != null){
              videoTrack.addSink(mLocalView);
            }
          }
        });
      }

      @Override public void onIceCandidate(IceCandidate iceCandidate) {
        super.onIceCandidate(iceCandidate);
        mPeerConnectionLocal.addIceCandidate(iceCandidate);
      }
    });


    mPeerConnectionLocal.addStream(localMediaStream);
    mPeerConnectionLocal.createOffer(new SdpAdapter("local offer sdp"){
      @Override public void onCreateSuccess(SessionDescription sessionDescription) {
        super.onCreateSuccess(sessionDescription);

        // todo crashed here
        mPeerConnectionLocal.setLocalDescription(new SdpAdapter("local set local"), sessionDescription);
        mPeerConnectionRemote.addStream(remoteMediaStream);
        mPeerConnectionRemote.setRemoteDescription(new SdpAdapter("remote set remote"), sessionDescription);
        mPeerConnectionRemote.createAnswer(new SdpAdapter("remote answer sdp") {
          @Override
          public void onCreateSuccess(SessionDescription sdp) {
            super.onCreateSuccess(sdp);
            mPeerConnectionRemote.setLocalDescription(new SdpAdapter("remote set local"), sdp);
            mPeerConnectionRemote.setRemoteDescription(new SdpAdapter("local set remote"), sdp);
          }
        }, new MediaConstraints());
      }
    },new MediaConstraints());
  }

  private VideoCapturer createCameraCapturer(boolean isFront) {
    Camera1Enumerator enumerator = new Camera1Enumerator(false);
    final String[] deviceNames = enumerator.getDeviceNames();

    // First, try to find front facing camera
    for (String deviceName : deviceNames) {
      if (isFront ? enumerator.isFrontFacing(deviceName) : enumerator.isBackFacing(deviceName)) {
        VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

        if (videoCapturer != null) {
          return videoCapturer;
        }
      }
    }

    // Front facing camera not found, try something else
    for (String deviceName : deviceNames) {
      if (!enumerator.isFrontFacing(deviceName)) {
        VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

        if (videoCapturer != null) {
          return videoCapturer;
        }
      }
    }

    return null;
  }
}
