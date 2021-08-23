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

package com.kunminx.puremusic.ui.helper;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData;
import com.kunminx.architecture.ui.callback.UnPeekLiveData;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by KunMinX at 19/11/3
 */
public class DrawerCoordinateHelper implements DefaultLifecycleObserver {

  private static final DrawerCoordinateHelper S_HELPER = new DrawerCoordinateHelper();

  private DrawerCoordinateHelper() {
  }

  public static DrawerCoordinateHelper getInstance() {
    return S_HELPER;
  }

  private final List<String> tagOfSecondaryPages = new ArrayList<>();

  private final UnPeekLiveData<Boolean> enableSwipeDrawer = new UnPeekLiveData<>();

  public ProtectedUnPeekLiveData<Boolean> isEnableSwipeDrawer() {
    return enableSwipeDrawer;
  }

  public void requestToUpdateDrawerMode(boolean pageOpened, String pageName) {
    if (pageOpened) {
      tagOfSecondaryPages.add(pageName);
    } else {
      tagOfSecondaryPages.remove(pageName);
    }
    enableSwipeDrawer.setValue(tagOfSecondaryPages.size() == 0);
  }

  @Override
  public void onCreate(@NonNull LifecycleOwner owner) {

    tagOfSecondaryPages.add(owner.getClass().getSimpleName());

    enableSwipeDrawer.setValue(tagOfSecondaryPages.size() == 0);

  }

  @Override
  public void onDestroy(@NonNull LifecycleOwner owner) {

    tagOfSecondaryPages.remove(owner.getClass().getSimpleName());

    enableSwipeDrawer.setValue(tagOfSecondaryPages.size() == 0);

  }

}
