package com.jasmeet.downloadmanger.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.jasmeet.downloadmanger.R
import com.jasmeet.downloadmanger.appComponent.ProgressIndicator
import com.jasmeet.downloadmanger.downloadTracker.DownloadTracker
import com.jasmeet.downloadmanger.utils.DownloadUtil
import com.jasmeet.downloadmanger.utils.MediaItemTag
import com.jasmeet.downloadmanger.viewModel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class,
    ExperimentalMaterial3Api::class
)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoPlayerScreen(
    modifier: Modifier=Modifier,
    navController: NavHostController,
    videoUrl: String?,
    mimeType: String?,
    title: String?,
    description: String?,
    artworkUrl: String?,
    drmLicence: String?,
) {
    var downloadProgress by rememberSaveable { mutableFloatStateOf(0f) }
    val context = LocalContext.current
    val isArtworkVisible = rememberSaveable {
        mutableStateOf(true)
    }

    val mainViewModel : MainViewModel = viewModel()


    val downloadUtil = remember {
        DownloadUtil.getDownloadTracker(context = context)
    }

    val isDownloadBtnVisible = rememberSaveable {
        mutableStateOf(false)
    }

    val mediaItem =
        if (drmLicence != null) {
            MediaItem.Builder()
                .setUri(videoUrl)
                .setMimeType(mimeType)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(title)
                        .setDescription(description)
                        .setArtworkUri(Uri.parse(artworkUrl))
                        .build()
                )
                .setDrmConfiguration(
                    MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID)
                        .setLicenseUri(drmLicence)
                        .build()
                )
                .setTag(MediaItemTag(-1, title ?:"Video Player"))
                .build()

        } else {
            MediaItem.Builder()
                .setUri(videoUrl)
                .setMimeType(mimeType)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(title)
                        .setDescription(description)
                        .setArtworkUri(Uri.parse(artworkUrl))
                        .build()
                )
                .setTag(MediaItemTag(-1, title ?: "Video Player"))
                .build()
        }


    val downloadTrackerListener = object: DownloadTracker.Listener{
        override fun onDownloadsChanged(download: Download) {
            when(download.state){
                Download.STATE_DOWNLOADING ->{
                    mainViewModel.startFlow(context,download.request.uri)
                }
                Download.STATE_COMPLETED ->{
                    downloadProgress = 100f
                    mainViewModel.stopFlow()

                }
                Download.STATE_FAILED ->{
                    downloadProgress = 0f
                    Toast.makeText(context, "Something Went wrong!", Toast.LENGTH_SHORT).show()
                    mainViewModel.stopFlow()
                }
                Download.STATE_STOPPED ->{
                    mainViewModel.stopFlow()
                }
                Download.STATE_REMOVING ->{
                    mainViewModel.stopFlow()
                    downloadProgress = 0f

                }
                else ->{
                    mainViewModel.stopFlow()
                }
            }

        }
    }
    downloadUtil.addListener(downloadTrackerListener)


    mainViewModel.downloadPercent.observeAsState().value.let {
        if (it != null) {
            downloadProgress = it / 100f
        }
    }


    val exoPlayer = remember{
        ExoPlayer.Builder(context)
            .setMediaSourceFactory(
                DefaultMediaSourceFactory(DownloadUtil.getReadOnlyDataSourceFactory(context))
            ).build().apply {
                playWhenReady = false
                setMediaItem(mediaItem)

                prepare()
                addListener(
                    object : Player.Listener {
                        override fun onPlayerError(error: PlaybackException) {
                            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                            isDownloadBtnVisible.value = false
                        }

                        override fun onPlaybackStateChanged(playbackState: Int) {
                            super.onPlaybackStateChanged(playbackState)
                            when (playbackState) {
                                Player.STATE_READY -> {
                                    isDownloadBtnVisible.value = true
                                }

                                else ->{}

                            }
                        }
                    }
                )
            }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = title ?: "Video Player")
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
    ) {

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = it.calculateTopPadding() + 15.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .height(LocalConfiguration.current.screenHeightDp.dp * 0.3f),
                contentAlignment = Alignment.Center
            ) {


                AndroidView(
                    modifier = Modifier
                        .height(LocalConfiguration.current.screenHeightDp.dp * 0.3f),
                    factory = { context ->
                        PlayerView(context).apply {
                            player = exoPlayer
                            setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
                            setShowSubtitleButton(true)
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                            controllerAutoShow = false

                        }
                    }
                )
                if (isArtworkVisible.value) {

                    Box(
                        modifier = Modifier
                            .height(LocalConfiguration.current.screenHeightDp.dp * 0.3f),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = artworkUrl,
                            contentDescription = "Artwork",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .blur(5.dp)
                                .height(LocalConfiguration.current.screenHeightDp.dp * 0.3f)
                        )
                        IconButton(
                            onClick = {
                                exoPlayer.play()
                                isArtworkVisible.value = false
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.baseline_play_circle_outline_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(190.dp),
                                tint = Color.White
                            )
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(10.dp))

            Column (modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally){
                Row(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    TextButton(
                        onClick = {
                            try {
                                if (DownloadUtil
                                        .getDownloadTracker(context)
                                        .isDownloaded(mediaItem)
                                ) {
                                    Toast
                                        .makeText(
                                            context,
                                            "Already Downloaded",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                } else {
                                    val item = mediaItem
                                        .buildUpon()
                                        .setTag(
                                            (mediaItem.localConfiguration?.tag as MediaItemTag).copy(
                                                duration = exoPlayer.duration
                                            )
                                        )
                                        .build()

                                    if (!downloadUtil.hasDownload(item.localConfiguration?.uri)
                                    ) {
                                        DownloadUtil
                                            .getDownloadTracker(context)
                                            .toggleDownloadDialogHelper(
                                                context,
                                                item,
                                                videoId = "null",
                                                quality = 720
                                            )
                                    }
                                }
                            } catch (e: Exception) {
                                Toast
                                    .makeText(
                                        context,
                                        e.message.toString(),
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                Log.d("error", e.message.toString())
                            }
                        }) {
                        Text(text = "Download")

                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column {
                        ProgressIndicator(
                            isAnimating = downloadProgress > 0f && downloadProgress < 1f,
                            progress = downloadProgress,
                        )
                        Spacer(modifier = Modifier.height(5.dp))

                        val percent = if (downloadProgress < 1f) {
                            "${(downloadProgress * 100).toInt()}%"
                        } else {
                            "100%"
                        }

                        if (downloadProgress>0f)
                            Text(
                                text = percent,
                                fontSize = 14.sp
                            )
                    }
                    Spacer(modifier =Modifier.width(10.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly

                ) {
                    TextButton(
                        onClick = {
                            downloadUtil.pauseDownload(Uri.parse(videoUrl), context = context)
                            Toast.makeText(context, "Download Paused", Toast.LENGTH_SHORT).show()


                        }
                    ) {
                        Text(text = "Pause Download")

                    }
                    TextButton(
                        onClick = {
                            downloadUtil.resumeDownload(Uri.parse(videoUrl),context)
                            Toast.makeText(context, "Download Resumed", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(text = "Resume Download")

                    }
                }

                TextButton(onClick = {
                    downloadUtil.removeDownload(Uri.parse(videoUrl))
                    Toast.makeText(context, "Download Removed", Toast.LENGTH_SHORT).show()
                }
                ) {
                    Text(text = "Delete Download")
                }
            }

        }
    }


    DisposableEffect(
        key1 = Unit,
        effect = {
            onDispose {
                exoPlayer.release()
                downloadUtil.removeListener(downloadTrackerListener)
                mainViewModel.stopFlow()
            }
        }
    )


}