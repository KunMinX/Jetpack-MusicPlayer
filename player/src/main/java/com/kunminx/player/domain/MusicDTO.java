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

import com.kunminx.player.bean.base.BaseAlbumItem;
import com.kunminx.player.bean.base.BaseArtistItem;
import com.kunminx.player.bean.base.BaseMusicItem;

import java.io.Serializable;

/**
 * Create by KunMinX at 18/9/24
 */
public class MusicDTO<
        B extends BaseAlbumItem<M, A>,
        M extends BaseMusicItem<A>,
        A extends BaseArtistItem>
        implements Serializable {

  private String title;
  private String summary;
  private A artist;
  private String albumId;
  private String musicId;
  private String img;
  private String nowTime = "00:00";
  private String allTime = "00:00";
  private int duration;
  private int progress;
  private boolean isPaused;
  private Enum<PlayingInfoManager.RepeatMode> repeatMode = PlayingInfoManager.RepeatMode.LIST_CYCLE;

  public MusicDTO() {
  }

  public String getTitle() {
    return title;
  }
  public String getSummary() {
    return summary;
  }
  public A getArtist() {
    return artist;
  }
  public String getAlbumId() {
    return albumId;
  }
  public String getMusicId() {
    return musicId;
  }
  public String getImg() {
    return img;
  }
  public String getNowTime() {
    return nowTime;
  }
  public String getAllTime() {
    return allTime;
  }
  public int getDuration() {
    return duration;
  }
  public int getProgress() {
    return progress;
  }
  public boolean isPaused() {
    return isPaused;
  }
  public Enum<PlayingInfoManager.RepeatMode> getRepeatMode() {
    return repeatMode;
  }

  void setBaseInfo(B musicAlbum, M music) {
    this.title = music.title;
    this.summary = musicAlbum.summary;
    this.albumId = musicAlbum.albumId;
    this.musicId = music.musicId;
    this.img = music.coverImg;
    this.artist = (A) music.artist;
  }
  void setTitle(String title) {
    this.title = title;
  }
  void setSummary(String summary) {
    this.summary = summary;
  }
  void setArtist(A artist) {
    this.artist = artist;
  }
  void setAlbumId(String albumId) {
    this.albumId = albumId;
  }
  void setMusicId(String musicId) {
    this.musicId = musicId;
  }
  void setImg(String img) {
    this.img = img;
  }
  void setNowTime(String nowTime) {
    this.nowTime = nowTime;
  }
  void setAllTime(String allTime) {
    this.allTime = allTime;
  }
  void setDuration(int duration) {
    this.duration = duration;
  }
  void setProgress(int progress) {
    this.progress = progress;
  }
  void setPaused(boolean paused) {
    isPaused = paused;
  }
  void setRepeatMode(Enum<PlayingInfoManager.RepeatMode> repeatMode) {
    this.repeatMode = repeatMode;
  }
}
