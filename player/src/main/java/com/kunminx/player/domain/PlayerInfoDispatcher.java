package com.kunminx.player.domain;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;

/**
 * Create by KunMinX at 2022/8/12
 */
public class PlayerInfoDispatcher extends MviDispatcher<PlayerEvent> {
  @Override
  protected void onHandle(PlayerEvent event) {
    sendResult(event);
  }
}
