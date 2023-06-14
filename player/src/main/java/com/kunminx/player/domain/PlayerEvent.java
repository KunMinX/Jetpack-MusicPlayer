package com.kunminx.player.domain;

import com.kunminx.player.PlayingInfoManager;
import com.kunminx.player.bean.base.BaseAlbumItem;
import com.kunminx.player.bean.base.BaseArtistItem;
import com.kunminx.player.bean.base.BaseMusicItem;
import com.kunminx.player.bean.dto.ChangeMusic;
import com.kunminx.player.bean.dto.PlayingMusic;

/**
 * Create by KunMinX at 2022/7/4
 */
public class PlayerEvent<
        B extends BaseAlbumItem<M, A>,
        M extends BaseMusicItem<A>,
        A extends BaseArtistItem> {
  public final static int EVENT_CHANGE_MUSIC = 1;
  public final static int EVENT_PROGRESS = 2;
  public final static int EVENT_PLAY_STATUS = 3;
  public final static int EVENT_REPEAT_MODE = 4;

  public final int eventId;
  public final ChangeMusic<B, M, A> changeMusic;
  public final PlayingMusic<B, M, A> playingMusic;
  public final boolean toPause;
  public final Enum<PlayingInfoManager.RepeatMode> repeatMode;

  public PlayerEvent(int eventId,
                     ChangeMusic<B, M, A> changeMusic,
                     PlayingMusic<B, M, A> playingMusic,
                     boolean toPause,
                     Enum<PlayingInfoManager.RepeatMode> repeatMode) {
    this.eventId = eventId;
    this.changeMusic = changeMusic;
    this.playingMusic = playingMusic;
    this.toPause = toPause;
    this.repeatMode = repeatMode;
  }

  public PlayerEvent(int eventId, ChangeMusic<B, M, A> changeMusic) {
    this.eventId = eventId;
    this.changeMusic = changeMusic;
    this.playingMusic = null;
    this.toPause = false;
    this.repeatMode = null;
  }

  public PlayerEvent(int eventId, PlayingMusic<B, M, A> playingMusic) {
    this.eventId = eventId;
    this.changeMusic = null;
    this.playingMusic = playingMusic;
    this.toPause = false;
    this.repeatMode = null;
  }

  public PlayerEvent(int eventId, boolean toPause) {
    this.eventId = eventId;
    this.changeMusic = null;
    this.playingMusic = null;
    this.toPause = toPause;
    this.repeatMode = null;
  }

  public PlayerEvent(int eventId, Enum<PlayingInfoManager.RepeatMode> repeatMode) {
    this.eventId = eventId;
    this.changeMusic = null;
    this.playingMusic = null;
    this.toPause = false;
    this.repeatMode = repeatMode;
  }
}
