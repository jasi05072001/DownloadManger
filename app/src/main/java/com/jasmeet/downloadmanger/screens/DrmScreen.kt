package com.jasmeet.downloadmanger.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jasmeet.downloadmanger.R
import com.jasmeet.downloadmanger.mediaItemList.drmListMediaItem
import com.jasmeet.downloadmanger.utils.DownloadUtil
import com.jasmeet.downloadmanger.utils.MediaItemTag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class, ExperimentalStdlibApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
fun DrmScreen(navController: NavHostController) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "DRM Type videos")
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
        LazyColumn(
            Modifier
                .padding(top = it.calculateTopPadding() + 15.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ){
            items(drmListMediaItem){
                OutlinedCard(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            val encodedMimeType = URLEncoder.encode(it.localConfiguration?.mimeType.toString(), "UTF-8")
                            val encodedVideoUrl = URLEncoder.encode(it.localConfiguration?.uri.toString(), "UTF-8")
                            val encodedArtWorkUrl = URLEncoder.encode(it.mediaMetadata.artworkUri.toString(), "UTF-8")

                            navController.navigate(
                                Screens.VideoPlayer.passArguments(
                                    videoUrl = encodedVideoUrl,
                                    mimeType =  encodedMimeType,
                                    title = it.mediaMetadata.title.toString(),
                                    artworkUrl = encodedArtWorkUrl,
                                    description = it.mediaMetadata.description.toString(),

                                    )
                            )
                        }

                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(it.mediaMetadata.artworkUri.toString())
                                .crossfade(true)
                                .build(),
                            onError = {error ->
                                Log.d("error", error.result.throwable.message.toString())
                            },
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .size(120.dp, 90.dp),
                            contentScale = ContentScale.FillBounds
                        )


                        Spacer(modifier = Modifier.width(20.dp))

                        Text(
                            text = it.mediaMetadata.title.toString(),
                            fontSize = 11.sp,
                            softWrap = true,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis

                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                try {
                                    if (DownloadUtil.getDownloadTracker(context).isDownloaded(it)) {
                                        Toast.makeText(context, "Already Downloaded", Toast.LENGTH_SHORT).show()
                                    } else {
                                        val item = it.buildUpon()
                                            .setTag((it.localConfiguration?.tag as MediaItemTag)

                                            )
                                            .build()

                                        if (!DownloadUtil.getDownloadTracker(context)
                                                .hasDownload(item.localConfiguration?.uri)
                                        ) {
                                            DownloadUtil.getDownloadTracker(context)
                                                .toggleDownloadDialogHelper(context, item, videoId = "null")
                                        }
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.offline),
                                contentDescription = null,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                    }

                }
            }
        }
    }

}