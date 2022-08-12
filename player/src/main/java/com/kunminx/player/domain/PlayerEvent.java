package com.kunminx.player.domain;

import com.kunminx.architecture.domain.event.Event;
import com.kunminx.player.PlayingInfoManager;
import com.kunminx.player.bean.dto.ChangeMusic;
import com.kunminx.player.bean.dto.PlayingMusic;

/**
 * Create by KunMinX at 2022/7/4
 */
public class PlayerEvent extends Event<PlayerEvent.Param, PlayerEvent.Result> {
  public final static int EVENT_CHANGE_MUSIC = 1;
  public final static int EVENT_PROGRESS = 2;
  public final static int EVENT_PLAY_STATUS = 3;
  public final static int EVENT_REPEAT_MODE = 4;

  public PlayerEvent(int eventId) {
    this.eventId = eventId;
    this.param = new Param();
    this.result = new Result();
  }

  public static class Param {
    public ChangeMusic changeMusic;
    public PlayingMusic playingMusic;
    public boolean toPause;
    public Enum<PlayingInfoManager.RepeatMode> repeatMode;
  }

  public PlayerEvent setPlayingMusic(PlayingMusic music) {
    param.playingMusic = music;
    return this;
  }

  public PlayerEvent setChangeMusic(ChangeMusic music) {
    param.changeMusic = music;
    return this;
  }

  public PlayerEvent setStatus(boolean toPause) {
    param.toPause = toPause;
    return this;
  }

  public PlayerEvent setRepeatMode(Enum<PlayingInfoManager.RepeatMode> repeatMode) {
    param.repeatMode = repeatMode;
    return this;
  }

  public static class Result {
  }
}
