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
 * Create by KunMinX at 18/9/24
 */
public class BaseMusicItem<A extends BaseArtistItem> implements Serializable {

  public final String musicId;
  public final String coverImg;
  public final String url;
  public final String title;
  public final A artist;

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
}
