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

package com.kunminx.player.contract;

import com.kunminx.player.bean.base.BaseAlbumItem;
import com.kunminx.player.bean.base.BaseMusicItem;

import java.util.List;

/**
 * Create by KunMinX at 19/11/1
 */
public interface IPlayInfoManager<B extends BaseAlbumItem, M extends BaseMusicItem> {

  B getAlbum();

  List<M> getAlbumMusics();

  void setChangingPlayingMusic(boolean changingPlayingMusic);

  int getAlbumIndex();

  Enum getRepeatMode();

  M getCurrentPlayingMusic();

  void requestLastPlayingInfo();
}
