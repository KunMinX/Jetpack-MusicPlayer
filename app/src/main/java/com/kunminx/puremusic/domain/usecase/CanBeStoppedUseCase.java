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

package com.kunminx.puremusic.domain.usecase;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.domain.usecase.UseCase;
import com.kunminx.puremusic.data.repository.DataRepository;

import java.io.File;


/**
 * Create by KunMinX at 19/11/25
 */
public class CanBeStoppedUseCase extends UseCase<CanBeStoppedUseCase.RequestValues,
        CanBeStoppedUseCase.ResponseValue> implements DefaultLifecycleObserver {

  private final DownloadState downloadState = new DownloadState();

  @Override
  public void onStop(@NonNull LifecycleOwner owner) {
    if (getRequestValues() != null) {
      downloadState.isForgive = true;
      downloadState.file = null;
      downloadState.progress = 0;
      getUseCaseCallback().onError();
    }
  }

  @Override
  protected void executeUseCase(RequestValues requestValues) {
    DataRepository.getInstance().downloadFile(downloadState, dataResult -> {
      getUseCaseCallback().onSuccess(new ResponseValue(dataResult));
    });
  }

  public static final class RequestValues implements UseCase.RequestValues {

  }

  public static final class ResponseValue implements UseCase.ResponseValue {

    private final DataResult<CanBeStoppedUseCase.DownloadState> mDataResult;

    public ResponseValue(DataResult<CanBeStoppedUseCase.DownloadState> dataResult) {
      mDataResult = dataResult;
    }

    public DataResult<CanBeStoppedUseCase.DownloadState> getDataResult() {
      return mDataResult;
    }
  }

  public static final class DownloadState {

    public boolean isForgive = false;

    public int progress;

    public File file;
  }
}
