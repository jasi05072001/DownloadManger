package com.jasmeet.downloadmanger.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.offline.Download
import com.jasmeet.downloadmanger.utils.DownloadUtil
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
class OfflineVideoViewModel() : ViewModel() {

    private val _downloads: MutableLiveData<List<Download>> = MutableLiveData()
    val downloads: LiveData<List<Download>>
        get() = _downloads

    private val _downloadPercent = MutableLiveData<Float>()

    val downloadPercent: LiveData<Float>
        get() = _downloadPercent

    private var job: CompletableJob = SupervisorJob()
    private var coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + job)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun  startFlow(context: Context) {
        coroutineScope.launch {
            DownloadUtil.getDownloadTracker(context).getAllDownloadProgressFlow().collect {
                _downloads.postValue(it)

                for (element in it){
                    Log.d("progress", "startFlow: ${element.percentDownloaded}")
                    _downloadPercent.postValue(element.percentDownloaded)
                }
            }
        }
    }
    fun stopFlow() {
        coroutineScope.cancel()
    }
}