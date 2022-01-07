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

package com.kunminx.player.bean.base;

import java.io.Serializable;

/**
 * 音乐实体，music bean
 * <p>
 * Create by KunMinX at 18/9/24
 */
public class BaseMusicItem<A extends BaseArtistItem> implements Serializable {

  private String musicId;
  private String coverImg;
  private String url;
  private String title;
  private A artist;

  public BaseMusicItem() {
  }

  public BaseMusicItem(
          String musicId,
          String coverImg,
          String url,
          String title,
          A artist
  ) {
    this.musicId = musicId;
    this.coverImg = coverImg;
    this.url = url;
    this.title = title;
    this.artist = artist;
  }

  public String getMusicId() {
    return musicId;
  }

  public void setMusicId(String musicId) {
    this.musicId = musicId;
  }

  public String getCoverImg() {
    return coverImg;
  }

  public void setCoverImg(String coverImg) {
    this.coverImg = coverImg;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public A getArtist() {
    return artist;
  }

  public void setArtist(A artist) {
    this.artist = artist;
  }
}
