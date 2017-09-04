package com.pedro.rtplibrary.rtsp;

import android.media.MediaCodec;
import android.view.SurfaceView;
import com.pedro.rtplibrary.base.Camera1Base;
import com.pedro.rtsp.rtsp.Protocol;
import com.pedro.rtsp.rtsp.RtspClient;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;
import java.nio.ByteBuffer;

/**
 * Created by pedro on 10/02/17.
 */

public class RtspCamera1 extends Camera1Base {

  private RtspClient rtspClient;

  public RtspCamera1(SurfaceView surfaceView, Protocol protocol,
      ConnectCheckerRtsp connectCheckerRtsp) {
    super(surfaceView);
    rtspClient = new RtspClient(connectCheckerRtsp, protocol);
  }

  @Override
  public void setAuthorization(String user, String password) {
    rtspClient.setAuthorization(user, password);
  }

  @Override
  protected void prepareAudioRtp(boolean isStereo, int sampleRate) {
    rtspClient.setIsStereo(isStereo);
    rtspClient.setSampleRate(sampleRate);
  }

  @Override
  public boolean prepareAudio() {
    microphoneManager.setSampleRate(16000);
    audioEncoder.setSampleRate(16000);
    microphoneManager.createMicrophone();
    rtspClient.setSampleRate(microphoneManager.getSampleRate());
    return audioEncoder.prepareAudioEncoder();
  }

  @Override
  protected void startStreamRtp(String url) {
    rtspClient.setUrl(url);
  }

  @Override
  protected void stopStreamRtp() {
    rtspClient.disconnect();
  }

  @Override
  protected void getAacDataRtp(ByteBuffer aacBuffer, MediaCodec.BufferInfo info) {
    rtspClient.sendAudio(aacBuffer, info);
  }

  @Override
  protected void onSPSandPPSRtp(ByteBuffer sps, ByteBuffer pps) {
    rtspClient.setSPSandPPS(sps, pps);
    rtspClient.connect();
  }

  @Override
  protected void getH264DataRtp(ByteBuffer h264Buffer, MediaCodec.BufferInfo info) {
    rtspClient.sendVideo(h264Buffer, info);
  }
}