package com.jasmeet.downloadmanger.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jasmeet.downloadmanger.repository.DownloadedVideoRepository.getAllDownloadedVideos
import com.jasmeet.downloadmanger.room.DownloadedVideoData
import com.jasmeet.downloadmanger.viewModel.OfflineVideoViewModel

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun  DownloadScreen(navController: NavHostController) {

    val offlineViewModel : OfflineVideoViewModel = viewModel()
    val downloads = offlineViewModel.downloads.observeAsState(emptyList()).value

    val downloadedVideos = rememberSaveable { mutableStateOf(emptyList<DownloadedVideoData>()) }
    val downloadProgress = rememberSaveable { mutableFloatStateOf(0f) }
    val context = LocalContext.current

    LaunchedEffect(
        key1 = true,
        block ={
            offlineViewModel.startFlow(context = context)
            val downloadedVideoLiveData = getAllDownloadedVideos(context = context)
            downloadedVideos.value = downloadedVideoLiveData
        }
    )

    DisposableEffect(
        key1 = Unit,
        effect ={
            onDispose {
                offlineViewModel.stopFlow()
            }
        }
    )

    val progress = offlineViewModel.downloadPercent.observeAsState(0f).value
    val per = progress.div(100)


    if (downloads.isNotEmpty()){
        LazyColumn(modifier = Modifier.fillMaxSize()){
            items(downloads){
                Column(modifier = Modifier
                    .padding(horizontal = 10.dp)
                ) {
                    Text(text = it.request.uri.toString())
                    Spacer(modifier = Modifier.height(10.dp) )
                    Text(text = it.request.id)
                }
                Divider()
                LinearProgressIndicator(
                    progress = it.percentDownloaded.div(100f),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    trackColor = Color.White,
                    color = Color.Red
                )
            }
        }

    }
    else{
        Text(text = "No Downloads")
    }


}