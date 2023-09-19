/*
 * Copyright 2018-2019 KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunminx.player.domain;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.kunminx.player.bean.base.BaseAlbumItem;
import com.kunminx.player.bean.base.BaseArtistItem;
import com.kunminx.player.bean.base.BaseMusicItem;
import com.kunminx.player.contract.ICacheProxy;
import com.kunminx.player.contract.IServiceNotifier;

import java.util.List;

/**
 * Create by KunMinX at 18/9/25
 */
public class PlayerController<
        B extends BaseAlbumItem<M, A>,
        M extends BaseMusicItem<A>,
        A extends BaseArtistItem> {

  private final PlayingInfoManager<B, M, A> mPlayingInfoManager = new PlayingInfoManager<>();
  private boolean mIsChangingPlayingMusic;

  private ICacheProxy mICacheProxy;
  private IServiceNotifier mIServiceNotifier;
  private final MusicDTO<B, M, A> mCurrentPlay = new MusicDTO<>();
  private final MutableLiveData<MusicDTO<B, M, A>> mUiStates = new MutableLiveData<>();

  private ExoPlayer mPlayer;
  private final static Handler mHandler = new Handler();
  private final Runnable mProgressAction = this::updateProgress;

  public void init(Context context, IServiceNotifier iServiceNotifier, ICacheProxy iCacheProxy) {
    mIServiceNotifier = iServiceNotifier;
    mICacheProxy = iCacheProxy;
    mPlayer = new ExoPlayer.Builder(context).build();
    mCurrentPlay.setRepeatMode(mPlayingInfoManager.getRepeatMode());
  }

  public boolean isInit() {
    return mPlayingInfoManager.isInit();
  }

  public void loadAlbum(B musicAlbum) {
    setAlbum(musicAlbum, 0);
  }

  private void updateProgress() {
    mCurrentPlay.setNowTime(calculateTime(mPlayer.getCurrentPosition() / 1000));
    mCurrentPlay.setAllTime(calculateTime(mPlayer.getDuration() / 1000));
    mCurrentPlay.setDuration((int) mPlayer.getDuration());
    mCurrentPlay.setProgress((int) mPlayer.getCurrentPosition());
    mUiStates.setValue(mCurrentPlay);
    if (mCurrentPlay.getAllTime().equals(mCurrentPlay.getNowTime())) {
      if (getRepeatMode() == PlayingInfoManager.RepeatMode.SINGLE_CYCLE) playAgain();
      else playNext();
    }
    mHandler.postDelayed(mProgressAction, 1000);
  }

  private void setAlbum(B musicAlbum, int albumIndex) {
    mPlayingInfoManager.setMusicAlbum(musicAlbum);
    mPlayingInfoManager.setAlbumIndex(albumIndex);
    setChangingPlayingMusic(true);
  }

  public void loadAlbum(B musicAlbum, int albumIndex) {
    setAlbum(musicAlbum, albumIndex);
    playAudio();
  }

  public boolean isPlaying() {
    return mPlayer.isPlaying();
  }

  public boolean isPaused() {
    return !mPlayer.getPlayWhenReady();
  }

  public void playAudio(int albumIndex) {
    if (isPlaying() && albumIndex == mPlayingInfoManager.getAlbumIndex()) {
      return;
    }
    mPlayingInfoManager.setAlbumIndex(albumIndex);
    setChangingPlayingMusic(true);
    playAudio();
  }

  public void playAudio() {
    if (mIsChangingPlayingMusic) getUrlAndPlay();
    else if (isPaused()) resumeAudio();
  }

  private void getUrlAndPlay() {
    String url;
    M freeMusic;
    freeMusic = mPlayingInfoManager.getCurrentPlayingMusic();
    url = freeMusic.url;

    if (TextUtils.isEmpty(url)) {
      pauseAudio();
    } else {
      MediaItem item;
      if ((url.contains("http:") || url.contains("ftp:") || url.contains("https:"))) {
        item = MediaItem.fromUri(mICacheProxy.getCacheUrl(url));
      } else if (url.contains("storage")) {
        item = MediaItem.fromUri(url);
      } else {
        item = MediaItem.fromUri(Uri.parse("file:///android_asset/" + url));
      }
      mPlayer.setMediaItem(item, true);
      mPlayer.prepare();
      mPlayer.play();
      afterPlay();
    }
  }

  private void afterPlay() {
    setChangingPlayingMusic(false);
    mHandler.post(mProgressAction);
    mCurrentPlay.setPaused(false);
    mUiStates.setValue(mCurrentPlay);
    if (mIServiceNotifier != null) mIServiceNotifier.notifyService(true);
  }

  public void setSeek(int progress) {
    mPlayer.seekTo(progress);
  }

  public String getTrackTime(int progress) {
    return calculateTime(progress / 1000);
  }

  private String calculateTime(long _time) {
    int time = (int) _time;
    int minute;
    int second;
    if (time >= 60) {
      minute = time / 60;
      second = time % 60;
      return (minute < 10 ? "0" + minute : "" + minute) + (second < 10 ? ":0" + second : ":" + second);
    } else {
      second = time;
      if (second < 10) return "00:0" + second;
      return "00:" + second;
    }
  }

  public void playNext() {
    mPlayingInfoManager.countNextIndex();
    setChangingPlayingMusic(true);
    playAudio();
  }

  public void playPrevious() {
    mPlayingInfoManager.countPreviousIndex();
    setChangingPlayingMusic(true);
    playAudio();
  }

  public void playAgain() {
    setChangingPlayingMusic(true);
    playAudio();
  }

  public void pauseAudio() {
    mPlayer.pause();
    mHandler.removeCallbacks(mProgressAction);
    mCurrentPlay.setPaused(true);
    mUiStates.setValue(mCurrentPlay);
    if (mIServiceNotifier != null) mIServiceNotifier.notifyService(true);
  }

  public void resumeAudio() {
    mPlayer.play();
    mHandler.post(mProgressAction);
    mCurrentPlay.setPaused(false);
    mUiStates.setValue(mCurrentPlay);
    if (mIServiceNotifier != null) mIServiceNotifier.notifyService(true);
  }

  public void clear() {
    mPlayer.stop();
    mPlayer.clearMediaItems();
    mCurrentPlay.setPaused(true);
    mUiStates.setValue(mCurrentPlay);
    resetIsChangingPlayingChapter();
    if (mIServiceNotifier != null) mIServiceNotifier.notifyService(false);
  }

  public void resetIsChangingPlayingChapter() {
    mIsChangingPlayingMusic = true;
    setChangingPlayingMusic(true);
  }

  public void changeMode() {
    mCurrentPlay.setRepeatMode(mPlayingInfoManager.changeMode());
    mUiStates.setValue(mCurrentPlay);
  }

  public B getAlbum() {
    return mPlayingInfoManager.getMusicAlbum();
  }

  public List<M> getAlbumMusics() {
    return mPlayingInfoManager.getOriginPlayingList();
  }

  public void setChangingPlayingMusic(boolean changingPlayingMusic) {
    mIsChangingPlayingMusic = changingPlayingMusic;
    if (mIsChangingPlayingMusic) {
      mCurrentPlay.setBaseInfo(mPlayingInfoManager.getMusicAlbum(), getCurrentPlayingMusic());
      mCurrentPlay.setNowTime("00:00");
      mCurrentPlay.setAllTime("00:00");
      mCurrentPlay.setProgress(0);
      mCurrentPlay.setDuration(0);
      mUiStates.setValue(mCurrentPlay);
    }
  }

  public int getAlbumIndex() {
    return mPlayingInfoManager.getAlbumIndex();
  }

  public Enum<PlayingInfoManager.RepeatMode> getRepeatMode() {
    return mPlayingInfoManager.getRepeatMode();
  }

  public void togglePlay() {
    if (isPlaying()) pauseAudio();
    else playAudio();
  }

  public M getCurrentPlayingMusic() {
    return mPlayingInfoManager.getCurrentPlayingMusic();
  }

  public LiveData<MusicDTO<B, M, A>> getUiStates() {
    return mUiStates;
  }
}
