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

package com.kunminx.puremusic;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.kunminx.puremusic.bridge.status.MainActivityViewModel;
import com.kunminx.puremusic.databinding.ActivityMainBinding;
import com.kunminx.puremusic.player.PlayerManager;
import com.kunminx.puremusic.player.notification.PlayerService;
import com.kunminx.puremusic.ui.base.BaseActivity;

/**
 * Create by KunMinX at 19/10/16
 */

public class MainActivity extends BaseActivity {

    private ActivityMainBinding mBinding;
    private MainActivityViewModel mMainActivityViewModel;
    private boolean isListened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PlayerManager.getInstance().getStartService().observe(this, aBoolean -> {
            Intent intent = new Intent(getApplicationContext(), PlayerService.class);
            if (aBoolean) {
                getApplicationContext().startService(intent);
            } else {
                getApplicationContext().stopService(intent);
            }
        });

        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setVm(mMainActivityViewModel);

        mSharedViewModel.activityCanBeClosedDirectly.observe(this, aBoolean -> {
            NavController nav = Navigation.findNavController(this, R.id.main_fragment_host);
            if (nav.getCurrentDestination() != null && nav.getCurrentDestination().getId() != R.id.mainFragment) {
                nav.navigateUp();

                //TODO tip 6: ͬ tip 5.

            } else if (mBinding.dl != null && mBinding.dl.isDrawerOpen(GravityCompat.START)) {
                mBinding.dl.closeDrawer(GravityCompat.START);

            } else {
                super.onBackPressed();
            }
        });

        mSharedViewModel.openOrCloseDrawer.observe(this, aBoolean -> {
            if (mBinding.dl != null) {
                if (aBoolean && !mBinding.dl.isDrawerOpen(GravityCompat.START)) {
                    mBinding.dl.openDrawer(GravityCompat.START);
                } else {
                    mBinding.dl.closeDrawer(GravityCompat.START);
                }
            }
        });


        if (mBinding.dl != null) {
            mBinding.dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!isListened) {
            mSharedViewModel.timeToAddSlideListener.setValue(true);
            isListened = true;
        }
    }

    @Override
    public void onBackPressed() {
        mSharedViewModel.closeSlidePanelIfExpanded.setValue(true);
    }
}
