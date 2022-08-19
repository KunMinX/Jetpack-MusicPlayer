/*
 * Copyright 2018-present KunMinX
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

package com.kunminx.puremusic.ui.page;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.ui.state.State;
import com.kunminx.architecture.utils.ToastUtils;
import com.kunminx.architecture.utils.Utils;
import com.kunminx.player.PlayingInfoManager;
import com.kunminx.player.domain.PlayerEvent;
import com.kunminx.puremusic.BR;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.databinding.FragmentPlayerBinding;
import com.kunminx.puremusic.domain.message.DrawerCoordinateManager;
import com.kunminx.puremusic.domain.message.PageMessenger;
import com.kunminx.puremusic.player.PlayerManager;
import com.kunminx.puremusic.ui.page.helper.DefaultInterface;
import com.kunminx.puremusic.ui.view.PlayerSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

/**
 * Create by KunMinX at 19/10/29
 */
public class PlayerFragment extends BaseFragment {

  private PlayerViewModel mStates;
  private PageMessenger mMessenger;
  private PlayerSlideListener mListener;

  @Override
  protected void initViewModel() {
    mStates = getFragmentScopeViewModel(PlayerViewModel.class);
    mMessenger = getApplicationScopeViewModel(PageMessenger.class);
  }

  @Override
  protected DataBindingConfig getDataBindingConfig() {
    return new DataBindingConfig(R.layout.fragment_player, BR.vm, mStates)
            .addBindingParam(BR.click, new ClickProxy())
            .addBindingParam(BR.listener, new ListenerHandler());
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mMessenger.isToAddSlideListener().observe(getViewLifecycleOwner(), aBoolean -> {
      if (view.getParent().getParent() instanceof SlidingUpPanelLayout) {
        SlidingUpPanelLayout sliding = (SlidingUpPanelLayout) view.getParent().getParent();
        sliding.addPanelSlideListener(mListener = new PlayerSlideListener((FragmentPlayerBinding) getBinding(), sliding));
        sliding.addPanelSlideListener(new DefaultInterface.PanelSlideListener() {
          @Override
          public void onPanelStateChanged(
                  View view, SlidingUpPanelLayout.PanelState panelState,
                  SlidingUpPanelLayout.PanelState panelState1) {
            DrawerCoordinateManager.getInstance().requestToUpdateDrawerMode(
                    panelState1 == SlidingUpPanelLayout.PanelState.EXPANDED,
                    this.getClass().getSimpleName()
            );
          }
        });
      }
    });

    PlayerManager.getInstance().getDispatcher().output(this, playerEvent -> {
      switch (playerEvent.eventId) {
        case PlayerEvent.EVENT_CHANGE_MUSIC:
          mStates.title.set(playerEvent.param.changeMusic.getTitle());
          mStates.artist.set(playerEvent.param.changeMusic.getSummary());
          mStates.coverImg.set(playerEvent.param.changeMusic.getImg());
          if (mListener != null) view.post(mListener::calculateTitleAndArtist);
          break;
        case PlayerEvent.EVENT_PROGRESS:
          mStates.maxSeekDuration.set(playerEvent.param.playingMusic.getDuration());
          mStates.currentSeekPosition.set(playerEvent.param.playingMusic.getPlayerPosition());
          break;
        case PlayerEvent.EVENT_PLAY_STATUS:
          mStates.isPlaying.set(!playerEvent.param.toPause);
          break;
        case PlayerEvent.EVENT_REPEAT_MODE:
          Enum<PlayingInfoManager.RepeatMode> anEnum = playerEvent.param.repeatMode;
          if (anEnum == PlayingInfoManager.RepeatMode.LIST_CYCLE) {
            mStates.playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT);
          } else if (anEnum == PlayingInfoManager.RepeatMode.SINGLE_CYCLE) {
            mStates.playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT_ONCE);
          } else {
            mStates.playModeIcon.set(MaterialDrawableBuilder.IconValue.SHUFFLE);
          }
          break;
      }
    });

    mMessenger.isToCloseSlidePanelIfExpanded().observe(getViewLifecycleOwner(), aBoolean -> {
      if (view.getParent().getParent() instanceof SlidingUpPanelLayout) {
        SlidingUpPanelLayout sliding = (SlidingUpPanelLayout) view.getParent().getParent();
        if (sliding.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
          sliding.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
          mMessenger.requestToCloseActivityIfAllowed(true);
        }
      } else {
        mMessenger.requestToCloseActivityIfAllowed(true);
      }
    });

  }

  public class ClickProxy {
    public void playMode() {
      PlayerManager.getInstance().changeMode();
    }

    public void previous() {
      PlayerManager.getInstance().playPrevious();
    }

    public void togglePlay() {
      PlayerManager.getInstance().togglePlay();
    }

    public void next() {
      PlayerManager.getInstance().playNext();
    }

    public void showPlayList() {
      ToastUtils.showShortToast(getApplicationContext(), getString(R.string.unfinished));
    }

    public void slideDown() {
      mMessenger.requestToCloseSlidePanelIfExpanded(true);
    }

    public void more() {
    }
  }

  public static class ListenerHandler implements DefaultInterface.OnSeekBarChangeListener {

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
      PlayerManager.getInstance().setSeek(seekBar.getProgress());
    }
  }

  public static class PlayerViewModel extends ViewModel {
    public final State<String> title = new State<>(Utils.getApp().getString(R.string.app_name));
    public final State<String> artist = new State<>(Utils.getApp().getString(R.string.app_name));
    public final State<String> coverImg = new State<>("");
    public final State<Drawable> placeHolder = new State<>(ContextCompat.getDrawable(Utils.getApp(), R.drawable.bg_album_default));
    public final State<Integer> maxSeekDuration = new State<>(0);
    public final State<Integer> currentSeekPosition = new State<>(0);
    public final State<Boolean> isPlaying = new State<>(false, false);
    public final State<MaterialDrawableBuilder.IconValue> playModeIcon = new State<>(MaterialDrawableBuilder.IconValue.REPEAT);

    {
      if (PlayerManager.getInstance().getRepeatMode() == PlayingInfoManager.RepeatMode.LIST_CYCLE) {
        playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT);
      } else if (PlayerManager.getInstance().getRepeatMode() == PlayingInfoManager.RepeatMode.SINGLE_CYCLE) {
        playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT_ONCE);
      } else {
        playModeIcon.set(MaterialDrawableBuilder.IconValue.SHUFFLE);
      }
    }
  }

}
