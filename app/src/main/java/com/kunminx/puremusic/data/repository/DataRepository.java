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

package com.kunminx.puremusic.data.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.data.response.ResponseStatus;
import com.kunminx.architecture.utils.Utils;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.data.bean.LibraryInfo;
import com.kunminx.puremusic.data.bean.TestAlbum;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;

/**
 * Create by KunMinX at 19/10/29
 */
public class DataRepository {

  private static final DataRepository S_REQUEST_MANAGER = new DataRepository();

  private DataRepository() {
  }

  public static DataRepository getInstance() {
    return S_REQUEST_MANAGER;
  }


  public Observable<DataResult<TestAlbum>> getFreeMusic() {
    return Observable.create(emitter -> {
      Gson gson = new Gson();
      Type type = new TypeToken<TestAlbum>() {
      }.getType();
      TestAlbum testAlbum = gson.fromJson(Utils.getApp().getString(R.string.free_music_json), type);
      emitter.onNext(new DataResult<>(testAlbum, new ResponseStatus()));
    });
  }

  public Observable<DataResult<List<LibraryInfo>>> getLibraryInfo() {
    return Observable.create(emitter -> {
      Gson gson = new Gson();
      Type type = new TypeToken<List<LibraryInfo>>() {
      }.getType();
      List<LibraryInfo> list = gson.fromJson(Utils.getApp().getString(R.string.library_json), type);
      emitter.onNext(new DataResult<>(list, new ResponseStatus()));
    });
  }
}
