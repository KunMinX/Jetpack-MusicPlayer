/*
 *
 *  * Copyright 2018-present KunMinX
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.kunminx.puremusic.domain.message;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.kunminx.architecture.domain.result.MutableResult;
import com.kunminx.architecture.domain.result.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by KunMinX at 19/11/3
 */
public class DrawerCoordinateManager implements DefaultLifecycleObserver {

  private static final DrawerCoordinateManager S_HELPER = new DrawerCoordinateManager();

  private DrawerCoordinateManager() {
  }

  public static DrawerCoordinateManager getInstance() {
    return S_HELPER;
  }

  private final List<String> tagOfSecondaryPages = new ArrayList<>();

  private boolean isNoneSecondaryPage() {
    return tagOfSecondaryPages.size() == 0;
  }

  private final MutableResult<Boolean> enableSwipeDrawer = new MutableResult<>();

  public Result<Boolean> isEnableSwipeDrawer() {
    return enableSwipeDrawer;
  }

  public void requestToUpdateDrawerMode(boolean pageOpened, String pageName) {
    if (pageOpened) {
      tagOfSecondaryPages.add(pageName);
    } else {
      tagOfSecondaryPages.remove(pageName);
    }
    enableSwipeDrawer.setValue(isNoneSecondaryPage());
  }

  //TODO tip 3：让 NetworkStateManager 可观察页面生命周期，
  // 从而在进入或离开目标页面时，自动在此登记和处理抽屉的禁用和解禁，避免一系列不可预期问题。

  // 关于 Lifecycle 组件的存在意义，可详见《为你还原一个真实的 Jetpack Lifecycle》解析
  // https://xiaozhuanlan.com/topic/3684721950

  @Override
  public void onCreate(@NonNull LifecycleOwner owner) {

    tagOfSecondaryPages.add(owner.getClass().getSimpleName());

    enableSwipeDrawer.setValue(isNoneSecondaryPage());

  }

  @Override
  public void onDestroy(@NonNull LifecycleOwner owner) {

    tagOfSecondaryPages.remove(owner.getClass().getSimpleName());

    enableSwipeDrawer.setValue(isNoneSecondaryPage());

  }

}
