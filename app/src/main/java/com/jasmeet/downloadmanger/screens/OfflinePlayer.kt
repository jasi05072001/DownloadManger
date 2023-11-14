package com.jasmeet.downloadmanger.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.media3.exoplayer.offline.DownloadRequest
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
fun OfflineVideoPlayerScreen(
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

    val isDescriptionVisible = rememberSaveable {
        mutableStateOf(true)
    }

    val downloadRequest : DownloadRequest? = DownloadUtil.getDownloadTracker(context)
        .getDownloadRequest(Uri.parse(videoUrl))

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
                setMediaItem(maybeSetDownloadProperties(mediaItem,downloadRequest),false)

                prepare()
                addListener(
                    object : Player.Listener {
                        override fun onPlayerError(error: PlaybackException) {
                            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
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
                                    .size(190.dp),
                                tint = Color.White
                            )
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(15.dp))

            TextButton(onClick = {
                isDescriptionVisible.value = !isDescriptionVisible.value

            }) {
                Text(text = "Description")
            }

            AnimatedVisibility(
                visible = isDescriptionVisible.value,
                enter = fadeIn()+ expandVertically(),
                exit = fadeOut() + shrinkVertically(),
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = description ?: "No Description", modifier = Modifier.padding(all = 5.dp))

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

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun  maybeSetDownloadProperties(mediaItem: MediaItem, downloadRequest: DownloadRequest?): MediaItem {
    if (downloadRequest == null) {
        return mediaItem
    }
    val builder = mediaItem.buildUpon()
        .setMediaId(downloadRequest.id)
        .setUri(downloadRequest.uri)
        .setCustomCacheKey(downloadRequest.customCacheKey)
        .setMimeType(downloadRequest.mimeType)
        .setStreamKeys(downloadRequest.streamKeys)

    val drmConfiguration = mediaItem.localConfiguration!!.drmConfiguration
    if (drmConfiguration != null) {
        builder.setDrmConfiguration(
            drmConfiguration.buildUpon().setKeySetId(downloadRequest.keySetId).build()
        )
    }
    return builder.build()
}
