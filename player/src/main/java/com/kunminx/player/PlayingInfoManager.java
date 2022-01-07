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

import com.kunminx.player.bean.base.BaseAlbumItem;
import com.kunminx.player.bean.base.BaseMusicItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Create by KunMinX at 18/9/24
 */
public class PlayingInfoManager<B extends BaseAlbumItem, M extends BaseMusicItem> {

  //index of current playing which maybe Shuffled
  private int mPlayIndex = 0;

  //index of current playing which user see in the list
  private int mAlbumIndex = 0;

  //循环模式
  private Enum mRepeatMode;

  public enum RepeatMode {
    SINGLE_CYCLE,
    LIST_CYCLE,
    RANDOM
  }

  //原始列表
  private List<M> mOriginPlayingList = new ArrayList<>();

  //随机播放列表
  private List<M> mShufflePlayingList = new ArrayList<>();

  //专辑详情
  private B mMusicAlbum;

  boolean isInit() {
    return mMusicAlbum != null;
  }

  private void fitShuffle() {
    mShufflePlayingList.clear();
    mShufflePlayingList.addAll(mOriginPlayingList);
    Collections.shuffle(mShufflePlayingList);
  }

  Enum changeMode() {
    if (mRepeatMode == RepeatMode.LIST_CYCLE) {
      mRepeatMode = RepeatMode.SINGLE_CYCLE;
    } else if (mRepeatMode == RepeatMode.SINGLE_CYCLE) {
      mRepeatMode = RepeatMode.RANDOM;
    } else {
      mRepeatMode = RepeatMode.LIST_CYCLE;
    }
    return mRepeatMode;
  }

  B getMusicAlbum() {
    return mMusicAlbum;
  }

  void setMusicAlbum(B musicAlbum) {
    this.mMusicAlbum = musicAlbum;
    mOriginPlayingList.clear();
    mOriginPlayingList.addAll(mMusicAlbum.getMusics());
    fitShuffle();
  }

  List<M> getPlayingList() {
    if (mRepeatMode == RepeatMode.RANDOM) {
      return mShufflePlayingList;
    } else {
      return mOriginPlayingList;
    }
  }

  List<M> getOriginPlayingList() {
    return mOriginPlayingList;
  }

  M getCurrentPlayingMusic() {
    if (getPlayingList().isEmpty()) {
      return null;
    }
    return getPlayingList().get(mPlayIndex);
  }

  Enum getRepeatMode() {
    return mRepeatMode;
  }

  void countPreviousIndex() {
    if (mPlayIndex == 0) {
      mPlayIndex = (getPlayingList().size() - 1);
    } else {
      --mPlayIndex;
    }
    mAlbumIndex = mOriginPlayingList.indexOf(getCurrentPlayingMusic());
  }

  void countNextIndex() {
    if (mPlayIndex == (getPlayingList().size() - 1)) {
      mPlayIndex = 0;
    } else {
      ++mPlayIndex;
    }
    mAlbumIndex = mOriginPlayingList.indexOf(getCurrentPlayingMusic());
  }

  int getAlbumIndex() {
    return mAlbumIndex;
  }

  void setAlbumIndex(int albumIndex) {
    mAlbumIndex = albumIndex;
    mPlayIndex = getPlayingList().indexOf(mOriginPlayingList.get(mAlbumIndex));
  }
}
