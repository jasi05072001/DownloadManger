package com.jasmeet.downloadmanger.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jasmeet.downloadmanger.utils.DownloadUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {

    private val _downloadPercent = MutableLiveData<Float>()
    val downloadPercent: LiveData<Float>
        get() = _downloadPercent

    private var coroutineScope: CoroutineScope? = null


    fun startFlow(context: Context, uri: Uri){
        coroutineScope?.cancel()
        val job = SupervisorJob()
        coroutineScope = CoroutineScope(Dispatchers.Main + job).apply {
            launch {
                DownloadUtil.getDownloadTracker(context).getCurrentProgressDownload(uri).collect {
                    _downloadPercent.postValue(it)
                }
            }
        }
    }
    fun stopFlow() {
        coroutineScope?.cancel()
    }


}