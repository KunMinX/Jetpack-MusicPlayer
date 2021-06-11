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

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.player.PlayingInfoManager;
import com.kunminx.puremusic.BR;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.databinding.FragmentPlayerBinding;
import com.kunminx.puremusic.player.PlayerManager;
import com.kunminx.puremusic.ui.event.SharedViewModel;
import com.kunminx.puremusic.ui.helper.DefaultInterface;
import com.kunminx.puremusic.ui.helper.DrawerCoordinateHelper;
import com.kunminx.puremusic.ui.state.PlayerViewModel;
import com.kunminx.puremusic.ui.view.PlayerSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

/**
 * Create by KunMinX at 19/10/29
 */
public class PlayerFragment extends BaseFragment {

    private PlayerViewModel mState;
    private SharedViewModel mEvent;

    @Override
    protected void initViewModel() {
        mState = getFragmentScopeViewModel(PlayerViewModel.class);
        mEvent = getApplicationScopeViewModel(SharedViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.fragment_player, BR.vm, mState)
                .addBindingParam(BR.click, new ClickProxy())
                .addBindingParam(BR.listener, new ListenerHandler());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEvent.isToAddSlideListener().observe(this, aBoolean -> {
            if (view.getParent().getParent() instanceof SlidingUpPanelLayout) {
                SlidingUpPanelLayout sliding = (SlidingUpPanelLayout) view.getParent().getParent();
                sliding.addPanelSlideListener(new PlayerSlideListener((FragmentPlayerBinding) getBinding(), sliding));
                sliding.addPanelSlideListener(new DefaultInterface.PanelSlideListener() {
                    @Override
                    public void onPanelStateChanged(
                            View view, SlidingUpPanelLayout.PanelState panelState,
                            SlidingUpPanelLayout.PanelState panelState1) {
                        DrawerCoordinateHelper.getInstance().requestToUpdateDrawerMode(
                                panelState1 == SlidingUpPanelLayout.PanelState.EXPANDED,
                                this.getClass().getSimpleName()
                        );
                    }
                });
            }
        });

        PlayerManager.getInstance().getChangeMusicEvent().observe(getViewLifecycleOwner(), changeMusic -> {
            mState.title.set(changeMusic.getTitle());
            mState.artist.set(changeMusic.getSummary());
            mState.coverImg.set(changeMusic.getImg());
        });

        PlayerManager.getInstance().getPlayingMusicEvent().observe(getViewLifecycleOwner(), playingMusic -> {
            mState.maxSeekDuration.set(playingMusic.getDuration());
            mState.currentSeekPosition.set(playingMusic.getPlayerPosition());
        });

        PlayerManager.getInstance().getPauseEvent().observe(getViewLifecycleOwner(), aBoolean -> {
            mState.isPlaying.set(!aBoolean);
        });

        PlayerManager.getInstance().getPlayModeEvent().observe(getViewLifecycleOwner(), anEnum -> {
            int tip;
            if (anEnum == PlayingInfoManager.RepeatMode.LIST_CYCLE) {
                mState.playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT);
                tip = R.string.play_repeat;
            } else if (anEnum == PlayingInfoManager.RepeatMode.SINGLE_CYCLE) {
                mState.playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT_ONCE);
                tip = R.string.play_repeat_once;
            } else {
                mState.playModeIcon.set(MaterialDrawableBuilder.IconValue.SHUFFLE);
                tip = R.string.play_shuffle;
            }
            if (view.getParent().getParent() instanceof SlidingUpPanelLayout) {
                SlidingUpPanelLayout sliding = (SlidingUpPanelLayout) view.getParent().getParent();
                if (sliding.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    showShortToast(tip);
                }
            }
        });

        mEvent.isToCloseSlidePanelIfExpanded().observe(this, aBoolean -> {

            if (view.getParent().getParent() instanceof SlidingUpPanelLayout) {

                SlidingUpPanelLayout sliding = (SlidingUpPanelLayout) view.getParent().getParent();

                if (sliding.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    sliding.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                } else {
                    mEvent.requestToCloseActivityIfAllowed(true);
                }
            } else {
                mEvent.requestToCloseActivityIfAllowed(true);
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
            showShortToast(R.string.unfinished);
        }

        public void slideDown() {
            mEvent.requestToCloseSlidePanelIfExpanded(true);
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

}
