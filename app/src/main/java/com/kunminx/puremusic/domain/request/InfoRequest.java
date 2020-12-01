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

package com.kunminx.puremusic.domain.request;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.domain.request.BaseRequest;
import com.kunminx.puremusic.data.bean.LibraryInfo;
import com.kunminx.puremusic.data.repository.DataRepository;

import java.util.List;

/**
 * Create by KunMinX at 19/11/2
 */
public class InfoRequest extends BaseRequest {

    private final MutableLiveData<DataResult<List<LibraryInfo>>> mLibraryLiveData = new MutableLiveData<>();

    public LiveData<DataResult<List<LibraryInfo>>> getLibraryLiveData() {
        return mLibraryLiveData;
    }

    public void requestLibraryInfo() {
        DataRepository.getInstance().getLibraryInfo(mLibraryLiveData::setValue);
    }
}
