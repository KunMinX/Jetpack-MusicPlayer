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
import com.kunminx.puremusic.ui.page.adapter.DrawerAdapter;
import com.kunminx.puremusic.ui.state.DrawerViewModel;

/**
 * Create by KunMinX at 19/10/29
 */
public class DrawerFragment extends BaseFragment {

    private DrawerViewModel mState;

    @Override
    protected void initViewModel() {
        mState = getFragmentScopeViewModel(DrawerViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.fragment_drawer, BR.vm, mState)
                .addBindingParam(BR.click, new ClickProxy())
                .addBindingParam(BR.adapter, new DrawerAdapter(getContext()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mState.infoRequest.getLibraryLiveData().observe(getViewLifecycleOwner(), dataResult -> {
            if (!dataResult.getResponseStatus().isSuccess()) return;

            if (mAnimationLoaded && dataResult.getResult() != null) {
                mState.list.setValue(dataResult.getResult());
            }
        });

        if (mState.infoRequest.getLibraryLiveData().getValue() == null) {
            mState.infoRequest.requestLibraryInfo();
        }
    }

    @Override
    public void loadInitData() {
        super.loadInitData();
        if (mState.infoRequest.getLibraryLiveData().getValue() != null
                && mState.infoRequest.getLibraryLiveData().getValue().getResult() != null) {
            mState.list.setValue(mState.infoRequest.getLibraryLiveData().getValue().getResult());
        }
    }

    public class ClickProxy {

        public void logoClick() {
            openUrlInBrowser(getString(R.string.github_project));
        }
    }

}
