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

package com.kunminx.player.bean;

import com.kunminx.player.bean.base.BaseAlbumItem;
import com.kunminx.player.bean.base.BaseArtistItem;
import com.kunminx.player.bean.base.BaseMusicItem;

/**
 * Create by KunMinX at 19/11/1
 */
public class DefaultAlbum extends BaseAlbumItem<
        DefaultAlbum.DefaultMusic,
        DefaultAlbum.DefaultArtist> {

  public static class DefaultMusic extends BaseMusicItem<DefaultArtist> {

  }

  public static class DefaultArtist extends BaseArtistItem {

  }
}
