package com.jasmeet.downloadmanger.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.offline.Download
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.jasmeet.downloadmanger.appComponent.ProgressIndicator
import com.jasmeet.downloadmanger.data.CombinedDownloadData
import com.jasmeet.downloadmanger.repository.DownloadedVideoRepository.getAllDownloadedVideos
import com.jasmeet.downloadmanger.room.DownloadedVideoData
import com.jasmeet.downloadmanger.utils.DownloadUtil
import com.jasmeet.downloadmanger.viewModel.OfflineVideoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class
)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun  DownloadScreen(navController: NavHostController) {

    val offlineViewModel: OfflineVideoViewModel = viewModel()
    val downloads = offlineViewModel.downloads.observeAsState(emptyList()).value

    val downloadedVideos = rememberSaveable { mutableStateOf(emptyList<DownloadedVideoData>()) }
    val downloadProgress = rememberSaveable { mutableFloatStateOf(0f) }
    val context = LocalContext.current

    val downloadUtil = remember {
        DownloadUtil.getDownloadTracker(context = context)
    }

    val isPaused = rememberSaveable { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(
        key1 = true,
        block = {
            offlineViewModel.startFlow(context = context)
            val downloadedVideoLiveData = getAllDownloadedVideos(context = context)
            downloadedVideos.value = downloadedVideoLiveData
        }
    )

    DisposableEffect(
        key1 = Unit,
        effect = {
            onDispose {
                offlineViewModel.stopFlow()
            }
        }
    )

    val progress = offlineViewModel.downloadPercent.observeAsState(0f).value
    val per = progress.div(100)


    //this is the list of downloads and downloaded videos combined sorted by heading
    val combineList = CombinedDownloadData(
        downloads,
        downloadedVideos.value
    )



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Downloads")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        if (combineList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(combineList) {
                    OutlinedCard(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()
                            .then(
                                if (it.download.state == Download.STATE_COMPLETED)
                                    Modifier.combinedClickable(
                                        onClick = {
                                            val encodedMimeType =
                                                URLEncoder.encode(
                                                    it.download.request.mimeType,
                                                    "UTF-8"
                                                )
                                            val encodedVideoUrl =
                                                URLEncoder.encode(
                                                    it.download.request.uri.toString(),
                                                    "UTF-8"
                                                )
                                            val encodedArtWorkUrl =
                                                URLEncoder.encode(
                                                    it.downloadedVideoData.thumbnailUrl,
                                                    "UTF-8"
                                                )

                                            navController.navigate(
                                                Screens.OfflineVideoPlayer.passArguments(
                                                    videoUrl = encodedVideoUrl,
                                                    mimeType = encodedMimeType,
                                                    title = it.downloadedVideoData.heading.toString(),
                                                    artworkUrl = encodedArtWorkUrl,
                                                    description = it.downloadedVideoData.description.toString(),
                                                )
                                            )
                                        },
                                    )
                                else Modifier
                            )
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            Box(
                                Modifier
                                    .fillMaxHeight()
                                    .width(120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = it.downloadedVideoData.thumbnailUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .then(
                                            if (it.download.percentDownloaded <= 99f) Modifier.blur(
                                                10.dp
                                            ) else Modifier
                                        ),
                                    contentScale = ContentScale.FillBounds
                                )
                                if (it.download.percentDownloaded <= 99f) {
                                    Column(
                                        modifier = Modifier,
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {

                                        ProgressIndicator(
                                            progress = it.download.percentDownloaded.div(
                                                100f
                                            ),
                                            onClick = {
                                                isPaused.value = true
                                                DownloadUtil.getDownloadTracker(context)
                                                    .pauseDownload(it.download.request.uri,context)
                                                Toast.makeText(
                                                    context,
                                                    "${it.downloadedVideoData.heading.toString()} paused",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                            }
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))

                                        val percent =
                                            it.download.percentDownloaded.toInt()
                                                .toString() + "%"
                                        Text(text = percent, color = Color.White)
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 10.dp),
                            ) {

                                it.downloadedVideoData.heading?.let { it1 ->
                                    Text(
                                        text = it1,
                                        modifier = Modifier.padding(
                                            horizontal = 5.dp,
                                            vertical = 5.dp
                                        ),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.W800,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                it.downloadedVideoData.description?.let { it1 ->
                                    Text(
                                        text = it1,
                                        modifier = Modifier.padding(horizontal = 5.dp),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W300,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        lineHeight = 18.sp
                                    )
                                }
                            }

                            IconButton(
                                onClick = {

                                    downloadUtil.removeDownload(it.download.request.uri)
                                    Toast.makeText(context,
                                        "${it.downloadedVideoData.heading.toString()} deleted",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    coroutineScope.launch {
                                        downloadedVideos.value =
                                            getAllDownloadedVideos(context = context)
                                    }

                                }
                            ) {
                                Icon(
                                    imageVector = Icons.TwoTone.Delete,
                                    contentDescription = null,

                                    )
                            }

                        }
                    }
                }
            }

        } else {
            Text(text = "No Downloads", modifier = Modifier.padding(top = padding.calculateTopPadding(), start = 25.dp))
        }


    }
}





