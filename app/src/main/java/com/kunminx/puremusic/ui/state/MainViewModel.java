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

package com.kunminx.puremusic.ui.state;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kunminx.puremusic.data.bean.TestAlbum;
import com.kunminx.puremusic.domain.request.MusicRequest;

import java.util.List;

/**
 * Create by KunMinX at 19/10/29
 */
public class MainViewModel extends ViewModel {

  public final ObservableBoolean initTabAndPage = new ObservableBoolean(true);

  public final ObservableField<String> pageAssetPath = new ObservableField<>("summary.html");

  public final MutableLiveData<List<TestAlbum.TestMusic>> list = new MutableLiveData<>();

  public final MutableLiveData<Boolean> notifyCurrentListChanged = new MutableLiveData<>();

  public final MusicRequest musicRequest = new MusicRequest();

}
