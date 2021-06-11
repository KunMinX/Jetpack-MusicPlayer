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

package com.kunminx.puremusic.ui.event;

import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData;
import com.kunminx.architecture.ui.callback.UnPeekLiveData;

/**
 * Create by KunMinX at 19/10/16
 */
public class SharedViewModel extends ViewModel {

    private final UnPeekLiveData<Boolean> toCloseSlidePanelIfExpanded = new UnPeekLiveData<>();

    private final UnPeekLiveData<Boolean> toCloseActivityIfAllowed = new UnPeekLiveData<>();

    private final UnPeekLiveData<Boolean> toOpenOrCloseDrawer = new UnPeekLiveData<>();

    private final UnPeekLiveData<Boolean> toAddSlideListener =
            new UnPeekLiveData.Builder<Boolean>().setAllowNullValue(false).create();

    public ProtectedUnPeekLiveData<Boolean> isToAddSlideListener() {
        return toAddSlideListener;
    }

    public ProtectedUnPeekLiveData<Boolean> isToCloseSlidePanelIfExpanded() {
        return toCloseSlidePanelIfExpanded;
    }

    public ProtectedUnPeekLiveData<Boolean> isToCloseActivityIfAllowed() {
        return toCloseActivityIfAllowed;
    }

    public ProtectedUnPeekLiveData<Boolean> isToOpenOrCloseDrawer() {
        return toOpenOrCloseDrawer;
    }

    public void requestToCloseActivityIfAllowed(boolean allow) {
        toCloseActivityIfAllowed.setValue(allow);
    }

    public void requestToOpenOrCloseDrawer(boolean open) {
        toOpenOrCloseDrawer.setValue(open);
    }

    public void requestToCloseSlidePanelIfExpanded(boolean close) {
        toCloseSlidePanelIfExpanded.setValue(close);
    }

    public void requestToAddSlideListener(boolean add) {
        toAddSlideListener.setValue(add);
    }
}
