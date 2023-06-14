package com.kunminx.player.domain;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;
import com.kunminx.player.bean.base.BaseAlbumItem;
import com.kunminx.player.bean.base.BaseArtistItem;
import com.kunminx.player.bean.base.BaseMusicItem;

/**
 * Create by KunMinX at 2022/8/12
 */
public class PlayerInfoDispatcher<
        B extends BaseAlbumItem<M, A>,
        M extends BaseMusicItem<A>,
        A extends BaseArtistItem> extends MviDispatcher<PlayerEvent<B, M, A>> {
  @Override
  protected void onHandle(PlayerEvent<B, M, A> event) {
    sendResult(event);
  }
}
