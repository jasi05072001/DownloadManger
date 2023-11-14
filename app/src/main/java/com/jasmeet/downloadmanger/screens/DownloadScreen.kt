package com.jasmeet.downloadmanger.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.offline.Download
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.jasmeet.downloadmanger.R
import com.jasmeet.downloadmanger.appComponent.ProgressIndicator
import com.jasmeet.downloadmanger.data.CombinedDownloadData
import com.jasmeet.downloadmanger.repository.DownloadedVideoRepository.getAllDownloadedVideos
import com.jasmeet.downloadmanger.room.DownloadedVideoData
import com.jasmeet.downloadmanger.utils.DownloadUtil
import com.jasmeet.downloadmanger.viewModel.OfflineVideoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class
)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun  DownloadScreen(navController: NavHostController) {

    val offlineViewModel : OfflineVideoViewModel = viewModel()
    val downloads = offlineViewModel.downloads.observeAsState(emptyList()).value

    val downloadedVideos = rememberSaveable { mutableStateOf(emptyList<DownloadedVideoData>()) }
    val downloadProgress = rememberSaveable { mutableFloatStateOf(0f) }
    val context = LocalContext.current

    val isPaused = rememberSaveable { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }


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

    val itemsBS = listOf(
        BottomSheetItem(
            title = "Pause Download",
            icon = R.drawable.baseline_pause_circle_outline_24,
            onClick = {
                DownloadUtil.getDownloadTracker(context).pauseDownload(downloads[0].request.uri)
                isPaused.value = true
            }
        ),
        BottomSheetItem(
            title = "Delete from Downloads",
            icon = R.drawable.twotone_delete_24,
            onClick = {
                DownloadUtil.getDownloadTracker(context).removeDownload(downloads[0].request.uri)
            }
        )
    )

    //this is the list of downloads and downloaded videos combined sorted by heading
    val combineList = CombinedDownloadData(downloads,downloadedVideos.value).sortedBy { it.downloadedVideoData.heading }



    Scaffold (
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
    ){padding ->

        if (combineList.isNotEmpty()){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                items(combineList){
                    OutlinedCard(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()
                            .clickable {
                                isSheetOpen = !isSheetOpen

                            }
                            .then(
                                if (it.download.state == Download.STATE_COMPLETED)
                                    Modifier.combinedClickable(
                                        onClick = {
                                            val encodedMimeType =
                                                URLEncoder.encode(it.download.request.mimeType, "UTF-8")
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
                                        onLongClick = {
                                            isSheetOpen = !isSheetOpen
                                        }
                                    )
                                else Modifier
                            ),
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            Box(
                                Modifier
                                    .height(90.dp)
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
                                if (it.download.percentDownloaded <= 99f && !isPaused.value) {
                                    Column(
                                        modifier = Modifier,
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        ProgressIndicator(
                                            progress = it.download.percentDownloaded.div(
                                                100f
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))

                                        val percent =
                                            it.download.percentDownloaded.toInt().toString() + "%"
                                        Text(text = percent, color = Color.White)

                                    }
                                }
                                if (isPaused.value){
                                    Column(
                                        modifier = Modifier.clickable {
                                                                      isPaused.value = !isPaused.value
                                            DownloadUtil.getDownloadTracker(context).resumeDownload(it.download.request.uri)
                                        },
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = "Paused", color = Color.White)
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier,
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                it.downloadedVideoData.heading?.let { it1 ->
                                    Text(
                                        text = it1,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp),
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
                                        fontWeight = FontWeight.W500,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        lineHeight = 20.sp
                                    )
                                }
                            }

                        }
                    }
                }
            }

        }
        else{
            Text(text = "No Downloads", modifier = Modifier.padding(padding))
        }


    }
    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                isSheetOpen = !isSheetOpen
            },
            modifier = Modifier
                .padding(bottom = 10.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(8.dp),
            windowInsets = WindowInsets(bottom = 40.dp, left = 10.dp, right = 10.dp),
            containerColor = Color.Black.copy(alpha = 0.75f),
            scrimColor = Color.Transparent,
            contentColor = Color.White
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            ) {
                items(itemsBS){ item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                item.onClick()
                                isSheetOpen = !isSheetOpen
                            }
                            .padding(7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(25.dp)
                        )
                        Text(
                            text = item.title,
                            style = TextStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }

        }
    }


}


data class BottomSheetItem(
    val title: String,
    val icon: Int,
    val onClick: () -> Unit
)






