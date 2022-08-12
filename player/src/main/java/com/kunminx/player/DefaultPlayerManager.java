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

import android.annotation.SuppressLint;
import android.content.Context;

import com.kunminx.player.bean.DefaultAlbum;
import com.kunminx.player.contract.ICacheProxy;
import com.kunminx.player.contract.IPlayController;
import com.kunminx.player.contract.IServiceNotifier;
import com.kunminx.player.domain.PlayerInfoDispatcher;

import java.util.List;

/**
 * Create by KunMinX at 19/10/31
 */
public class DefaultPlayerManager implements IPlayController<DefaultAlbum, DefaultAlbum.DefaultMusic, DefaultAlbum.DefaultArtist> {

  @SuppressLint("StaticFieldLeak")
  private static final DefaultPlayerManager sManager = new DefaultPlayerManager();

  private final PlayerController<DefaultAlbum, DefaultAlbum.DefaultMusic, DefaultAlbum.DefaultArtist> mController;

  private DefaultPlayerManager() {
    mController = new PlayerController<>();
  }

  public static DefaultPlayerManager getInstance() {
    return sManager;
  }

  @Override
  public void init(Context context, IServiceNotifier iServiceNotifier, ICacheProxy iCacheProxy) {
    mController.init(context.getApplicationContext(), iServiceNotifier, iCacheProxy);
  }

  @Override
  public void loadAlbum(DefaultAlbum musicAlbum) {
    mController.loadAlbum(musicAlbum);
  }

  @Override
  public void loadAlbum(DefaultAlbum musicAlbum, int playIndex) {
    mController.loadAlbum(musicAlbum, playIndex);
  }

  @Override
  public void playAudio() {
    mController.playAudio();
  }

  @Override
  public void playAudio(int albumIndex) {
    mController.playAudio(albumIndex);
  }

  @Override
  public void playNext() {
    mController.playNext();
  }

  @Override
  public void playPrevious() {
    mController.playPrevious();
  }

  @Override
  public void playAgain() {
    mController.playAgain();
  }

  @Override
  public void pauseAudio() {
    mController.pauseAudio();
  }

  @Override
  public void resumeAudio() {
    mController.resumeAudio();
  }

  @Override
  public void clear() {
    mController.clear();
  }

  @Override
  public void changeMode() {
    mController.changeMode();
  }

  @Override
  public boolean isPlaying() {
    return mController.isPlaying();
  }

  @Override
  public boolean isPaused() {
    return mController.isPaused();
  }

  @Override
  public boolean isInit() {
    return mController.isInit();
  }

  @Override
  public void requestLastPlayingInfo() {
    mController.requestLastPlayingInfo();
  }

  @Override
  public void setSeek(int progress) {
    mController.setSeek(progress);
  }

  @Override
  public String getTrackTime(int progress) {
    return mController.getTrackTime(progress);
  }

  @Override
  public PlayerInfoDispatcher getDispatcher() {
    return mController.getDispatcher();
  }

  @Override
  public DefaultAlbum getAlbum() {
    return mController.getAlbum();
  }

  @Override
  public List<DefaultAlbum.DefaultMusic> getAlbumMusics() {
    return mController.getAlbumMusics();
  }

  @Override
  public void setChangingPlayingMusic(boolean changingPlayingMusic) {
    mController.setChangingPlayingMusic(changingPlayingMusic);
  }

  @Override
  public int getAlbumIndex() {
    return mController.getAlbumIndex();
  }

  @Override
  public Enum<PlayingInfoManager.RepeatMode> getRepeatMode() {
    return mController.getRepeatMode();
  }

  @Override
  public void togglePlay() {
    mController.togglePlay();
  }

  @Override
  public DefaultAlbum.DefaultMusic getCurrentPlayingMusic() {
    return mController.getCurrentPlayingMusic();
  }
}
