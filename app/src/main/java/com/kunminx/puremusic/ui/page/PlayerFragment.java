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

package com.kunminx.puremusic.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.kunminx.player.PlayingInfoManager;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.bridge.status.PlayerViewModel;
import com.kunminx.puremusic.databinding.FragmentPlayerBinding;
import com.kunminx.puremusic.player.PlayerManager;
import com.kunminx.puremusic.ui.base.BaseFragment;
import com.kunminx.puremusic.ui.view.PlayerSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

/**
 * Create by KunMinX at 19/10/29
 */
public class PlayerFragment extends BaseFragment {

    private FragmentPlayerBinding mBinding;
    private PlayerViewModel mPlayerViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        mBinding = FragmentPlayerBinding.bind(view);
        mBinding.setClick(new ClickProxy());
        mBinding.setVm(mPlayerViewModel);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSharedViewModel.timeToAddSlideListener.observe(this, aBoolean -> {
            if (view.getParent().getParent() instanceof SlidingUpPanelLayout) {
                SlidingUpPanelLayout sliding = (SlidingUpPanelLayout) view.getParent().getParent();
                sliding.addPanelSlideListener(new PlayerSlideListener(mBinding, sliding));
            }
        });

        PlayerManager.getInstance().getChangeMusicLiveData().observe(this, changeMusic -> {
            mPlayerViewModel.title.set(changeMusic.getTitle());
            mPlayerViewModel.artist.set(changeMusic.getSummary());
            mPlayerViewModel.coverImg.set(changeMusic.getImg());
        });

        PlayerManager.getInstance().getPlayingMusicLiveData().observe(this, playingMusic -> {
            mPlayerViewModel.maxSeekDuration.set(playingMusic.getDuration());
            mPlayerViewModel.currentSeekPosition.set(playingMusic.getPlayerPosition());
        });

        PlayerManager.getInstance().getPauseLiveData().observe(this, aBoolean -> {
            mPlayerViewModel.isPlaying.set(!aBoolean);
        });

        PlayerManager.getInstance().getPlayModeLiveData().observe(this, anEnum -> {
            if (anEnum == PlayingInfoManager.RepeatMode.LIST_LOOP) {
                mPlayerViewModel.playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT);
                showShortToast(R.string.play_repeat);
            } else if (anEnum == PlayingInfoManager.RepeatMode.ONE_LOOP) {
                mPlayerViewModel.playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT_ONCE);
                showShortToast(R.string.play_repeat_once);
            } else {
                mPlayerViewModel.playModeIcon.set(MaterialDrawableBuilder.IconValue.SHUFFLE);
                showShortToast(R.string.play_shuffle);
            }
        });

        mSharedViewModel.closeSlidePanelIfExpanded.observe(this, aBoolean -> {
            if (view.getParent().getParent() instanceof SlidingUpPanelLayout) {
                SlidingUpPanelLayout sliding = (SlidingUpPanelLayout) view.getParent().getParent();
                if (sliding.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    sliding.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                } else {
                    mSharedViewModel.activityCanBeClosedDirectly.setValue(true);
                }
            }
        });


    }

    public class ClickProxy implements SeekBar.OnSeekBarChangeListener {

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
            mSharedViewModel.closeSlidePanelIfExpanded.setValue(true);
        }

        public void more() {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            PlayerManager.getInstance().setSeek(seekBar.getProgress());
        }
    }

}
