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

package com.kunminx.player;

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
import com.kunminx.player.bean.dto.ChangeMusic;
import com.kunminx.player.bean.dto.PlayingMusic;
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

  private final MutableLiveData<ChangeMusic<B, M, A>> changeMusicLiveData = new MutableLiveData<>();
  private final MutableLiveData<PlayingMusic<B, M, A>> playingMusicLiveData = new MutableLiveData<>();
  private final MutableLiveData<Boolean> pauseLiveData = new MutableLiveData<>();
  private final MutableLiveData<Enum<PlayingInfoManager.RepeatMode>> playModeLiveData = new MutableLiveData<>();

  private IServiceNotifier mIServiceNotifier;

  private final PlayingMusic<B, M, A> mCurrentPlay = new PlayingMusic<>("00:00", "00:00");
  private final ChangeMusic<B, M, A> mChangeMusic = new ChangeMusic<>();

  private ExoPlayer mPlayer;
  private Handler mHandler = new Handler();

  public void init(Context context, IServiceNotifier iServiceNotifier, ICacheProxy iCacheProxy) {
    mIServiceNotifier = iServiceNotifier;
    mICacheProxy = iCacheProxy;
    mPlayer = new ExoPlayer.Builder(context).build();
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
    mCurrentPlay.setPlayerPosition((int) mPlayer.getCurrentPosition());
    playingMusicLiveData.postValue(mCurrentPlay);
    if (mCurrentPlay.getAllTime().equals(mCurrentPlay.getNowTime())) {
      if (getRepeatMode() == PlayingInfoManager.RepeatMode.SINGLE_CYCLE) playAgain();
      else playNext();
    }
    mHandler.postDelayed(mProgressAction, 1000);
  }

  private Runnable mProgressAction = () -> updateProgress();

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
    if (mIsChangingPlayingMusic) {
      getUrlAndPlay();
    } else if (isPaused()) {
      resumeAudio();
    }
  }

  private void getUrlAndPlay() {
    String url;
    M freeMusic;
    freeMusic = mPlayingInfoManager.getCurrentPlayingMusic();
    url = freeMusic.getUrl();

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
    pauseLiveData.postValue(false);
    if (mIServiceNotifier != null) {
      mIServiceNotifier.notifyService(true);
    }
  }

  public void requestLastPlayingInfo() {
    playingMusicLiveData.postValue(mCurrentPlay);
    changeMusicLiveData.postValue(mChangeMusic);
    pauseLiveData.postValue(isPaused());
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
      if (second < 10) {
        return "00:0" + second;
      }
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
    pauseLiveData.postValue(true);
    if (mIServiceNotifier != null) {
      mIServiceNotifier.notifyService(true);
    }
  }

  public void resumeAudio() {
    mPlayer.play();
    mHandler.post(mProgressAction);
    pauseLiveData.postValue(false);
    if (mIServiceNotifier != null) {
      mIServiceNotifier.notifyService(true);
    }
  }

  public void clear() {
    mPlayer.stop();
    mPlayer.clearMediaItems();
    pauseLiveData.postValue(true);
    resetIsChangingPlayingChapter();
    if (mIServiceNotifier != null) {
      mIServiceNotifier.notifyService(false);
    }
  }

  public void resetIsChangingPlayingChapter() {
    mIsChangingPlayingMusic = true;
    setChangingPlayingMusic(true);
  }

  public void changeMode() {
    playModeLiveData.postValue(mPlayingInfoManager.changeMode());
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
      mChangeMusic.setBaseInfo(mPlayingInfoManager.getMusicAlbum(), getCurrentPlayingMusic());
      changeMusicLiveData.postValue(mChangeMusic);
      mCurrentPlay.setBaseInfo(mPlayingInfoManager.getMusicAlbum(), getCurrentPlayingMusic());
      mCurrentPlay.setNowTime("00:00");
      mCurrentPlay.setAllTime("00:00");
      mCurrentPlay.setPlayerPosition(0);
      mCurrentPlay.setDuration(0);
    }
  }

  public int getAlbumIndex() {
    return mPlayingInfoManager.getAlbumIndex();
  }

  public LiveData<ChangeMusic<B, M, A>> getChangeMusicResult() {
    return changeMusicLiveData;
  }

  public LiveData<PlayingMusic<B, M, A>> getPlayingMusicResult() {
    return playingMusicLiveData;
  }

  public LiveData<Boolean> getPauseResult() {
    return pauseLiveData;
  }

  public LiveData<Enum<PlayingInfoManager.RepeatMode>> getPlayModeResult() {
    return playModeLiveData;
  }

  public Enum<PlayingInfoManager.RepeatMode> getRepeatMode() {
    return mPlayingInfoManager.getRepeatMode();
  }

  public void togglePlay() {
    if (isPlaying()) {
      pauseAudio();
    } else {
      playAudio();
    }
  }

  public M getCurrentPlayingMusic() {
    return mPlayingInfoManager.getCurrentPlayingMusic();
  }
}
