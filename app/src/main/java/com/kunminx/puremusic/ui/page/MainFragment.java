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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.puremusic.BR;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.data.bean.TestAlbum;
import com.kunminx.puremusic.player.PlayerManager;
import com.kunminx.puremusic.ui.callback.SharedViewModel;
import com.kunminx.puremusic.ui.page.adapter.PlaylistAdapter;
import com.kunminx.puremusic.ui.state.MainViewModel;

/**
 * Create by KunMinX at 19/10/29
 */
public class MainFragment extends BaseFragment {

    private MainViewModel mState;
    private SharedViewModel mPageCallback;

    @Override
    protected void initViewModel() {
        mState = getFragmentScopeViewModel(MainViewModel.class);
        mPageCallback = getApplicationScopeViewModel(SharedViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.fragment_main, BR.vm, mState)
                .addBindingParam(BR.click, new ClickProxy())
                .addBindingParam(BR.adapter, new PlaylistAdapter(getContext()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PlayerManager.getInstance().getChangeMusicLiveData().observe(getViewLifecycleOwner(), changeMusic -> {
            mState.notifyCurrentListChanged.setValue(true);
        });

        mState.musicRequest.getFreeMusicsLiveData().observe(getViewLifecycleOwner(), dataResult -> {
            if (!dataResult.getResponseStatus().isSuccess()) return;

            TestAlbum musicAlbum = dataResult.getResult();

            if (musicAlbum != null && musicAlbum.getMusics() != null) {
                mState.list.setValue(musicAlbum.getMusics());

                if (PlayerManager.getInstance().getAlbum() == null ||
                        !PlayerManager.getInstance().getAlbum().getAlbumId().equals(musicAlbum.getAlbumId())) {
                    PlayerManager.getInstance().loadAlbum(musicAlbum);
                }
            }
        });

        if (PlayerManager.getInstance().getAlbum() == null) {
            mState.musicRequest.requestFreeMusics();
        } else {
            mState.list.setValue(PlayerManager.getInstance().getAlbum().getMusics());
        }
    }

    public class ClickProxy {

        public void openMenu() {
            mPageCallback.requestToOpenOrCloseDrawer(true);
        }

        public void login() {
        }

        public void search() {
        }

    }

}
