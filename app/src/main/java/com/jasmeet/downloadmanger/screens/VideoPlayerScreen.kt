package com.jasmeet.downloadmanger.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.jasmeet.downloadmanger.R
import com.jasmeet.downloadmanger.utils.DownloadUtil
import com.jasmeet.downloadmanger.utils.MediaItemTag
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class,
    ExperimentalMaterial3Api::class
)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoPlayerScreen(
    navController: NavHostController,
    videoUrl: String?,
    mimeType: String?,
    title: String?,
    description: String?,
    artworkUrl: String?,
    drmLicence: String?,
) {

    val context = LocalContext.current
    val isArtworkVisible = rememberSaveable {
        mutableStateOf(true)
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
//                            isDownloadBtnVisible.value = false
                        }

                        override fun onPlaybackStateChanged(playbackState: Int) {
                            super.onPlaybackStateChanged(playbackState)
                            when (playbackState) {
                                Player.STATE_READY -> {
//                                    isDownloadBtnVisible.value = true
                                }

                                else ->{}

                            }
                        }
                    }
                )
            }
    }

    Scaffold(
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
                                    .size(50.dp),
                                tint = Color.Black
                            )
                        }
                    }
                }

            }

            Button(onClick = {
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

                        if (!DownloadUtil
                                .getDownloadTracker(context)
                                .hasDownload(item.localConfiguration?.uri)
                        ) {
                            DownloadUtil
                                .getDownloadTracker(context)
                                .toggleDownloadDialogHelper(
                                    context,
                                    item,
                                    videoId = "null"
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
        }
    }


    DisposableEffect(
        key1 = Unit,
        effect = {
            onDispose {
                exoPlayer.release()
            }
        }
    )


}