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

import androidx.lifecycle.LiveData;

import com.kunminx.player.PlayingInfoManager;
import com.kunminx.player.bean.base.BaseAlbumItem;
import com.kunminx.player.bean.base.BaseArtistItem;
import com.kunminx.player.bean.base.BaseMusicItem;
import com.kunminx.player.bean.dto.ChangeMusic;
import com.kunminx.player.bean.dto.PlayingMusic;

/**
 * Create by KunMinX at 19/11/1
 */
public interface ILiveDataNotifier<
        B extends BaseAlbumItem<M, A>,
        M extends BaseMusicItem<A>,
        A extends BaseArtistItem> {

  LiveData<ChangeMusic<B, M, A>> getChangeMusicEvent();

  LiveData<PlayingMusic<B, M, A>> getPlayingMusicEvent();

  LiveData<Boolean> getPauseEvent();

  LiveData<Enum<PlayingInfoManager.RepeatMode>> getPlayModeEvent();

}
