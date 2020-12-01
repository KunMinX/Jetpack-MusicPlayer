package com.kunminx.puremusic.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.domain.request.BaseRequest;
import com.kunminx.architecture.domain.usecase.UseCaseHandler;
import com.kunminx.puremusic.data.bean.DownloadFile;
import com.kunminx.puremusic.data.repository.DataRepository;
import com.kunminx.puremusic.domain.usecase.CanBeStoppedUseCase;

/**
 * Create by KunMinX at 20/03/18
 */
public class DownloadRequest extends BaseRequest {

    private final MutableLiveData<DataResult<DownloadFile>> mDownloadFileLiveData = new MutableLiveData<>();

    private final MutableLiveData<DataResult<DownloadFile>> mDownloadFileCanBeStoppedLiveData = new MutableLiveData<>();

    private final CanBeStoppedUseCase mCanBeStoppedUseCase = new CanBeStoppedUseCase();

    public LiveData<DataResult<DownloadFile>> getDownloadFileLiveData() {
        return mDownloadFileLiveData;
    }

    public LiveData<DataResult<DownloadFile>> getDownloadFileCanBeStoppedLiveData() {
        return mDownloadFileCanBeStoppedLiveData;
    }

    public CanBeStoppedUseCase getCanBeStoppedUseCase() {
        return mCanBeStoppedUseCase;
    }

    public void requestDownloadFile() {

        DownloadFile downloadFile = new DownloadFile();

        DataRepository.getInstance().downloadFile(downloadFile, mDownloadFileLiveData::postValue);
    }

    public void requestCanBeStoppedDownloadFile() {
        UseCaseHandler.getInstance().execute(getCanBeStoppedUseCase(),
                new CanBeStoppedUseCase.RequestValues(), response -> {
                    mDownloadFileCanBeStoppedLiveData.setValue(response.getDataResult());
                });
    }
}
