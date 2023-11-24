package com.jasmeet.downloadmanger.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.jasmeet.downloadmanger.appComponent.DownloadItemComponent
import com.jasmeet.downloadmanger.appComponent.VerticalSpace
import com.jasmeet.downloadmanger.mediaItemList.DrmMediaItem
import com.jasmeet.downloadmanger.mediaItemList.Mp4ListMediaItem
import com.jasmeet.downloadmanger.utils.DownloadUtil
import com.jasmeet.downloadmanger.utils.MediaItemTag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class, ExperimentalStdlibApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
fun Mp4Screen( modifier: Modifier=Modifier,navController: NavHostController) {


    val context = LocalContext.current

    val firstMediaItem = Mp4ListMediaItem.firstMediaItem
    val secondMediaItem = Mp4ListMediaItem.secondMediaItem
    val thirdMediaItem = Mp4ListMediaItem.thirdMediaItem


    val downloadUtil = remember{
        DownloadUtil.getDownloadTracker(context)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Mp4 Type videos")
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
            .padding(it)
        ) {

            DownloadItemComponent(
                onClick = {
                    val encodedMimeType = URLEncoder.encode(
                        firstMediaItem.localConfiguration?.mimeType.toString(),
                        "UTF-8"
                    )
                    val encodedVideoUrl = URLEncoder.encode(
                        firstMediaItem.localConfiguration?.uri.toString(),
                        "UTF-8"
                    )
                    val encodedArtWorkUrl = URLEncoder.encode(
                        firstMediaItem.mediaMetadata.artworkUri.toString(),
                        "UTF-8"
                    )
                    val drmLicenceUrl = URLEncoder.encode(
                        firstMediaItem.localConfiguration?.drmConfiguration?.licenseUri.toString(),
                        "UTF-8"
                    )

                    navController.navigate(
                        Screens.VideoPlayer.passArguments(
                            videoUrl = encodedVideoUrl,
                            mimeType =  encodedMimeType,
                            title = DrmMediaItem.firstMediaItem.mediaMetadata.title.toString(),
                            artworkUrl = encodedArtWorkUrl,
                            description = DrmMediaItem.firstMediaItem.mediaMetadata.description.toString(),
                            drmLicence = drmLicenceUrl
                        )
                    )

                },
                artWorkUri =firstMediaItem.mediaMetadata.artworkUri.toString(),
                title = firstMediaItem.mediaMetadata.title.toString(),
                onDownloadClick = {
                    try {
                        if (
                            downloadUtil.isDownloaded(firstMediaItem)) {
                            Toast.makeText(context, "Already Downloaded", Toast.LENGTH_SHORT).show()
                        } else {
                            val item = DrmMediaItem.firstMediaItem.buildUpon()
                                .setTag((firstMediaItem.localConfiguration?.tag as MediaItemTag)

                                )
                                .build()

                            if (!downloadUtil
                                    .hasDownload(item.localConfiguration?.uri)
                            ) {
                                downloadUtil
                                    .toggleDownloadDialogHelper(context, item, videoId = "null")
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                },
                cardClickTestTag = "FirstMediaItem",
                downloadClickTextTag ="FirstMediaItemDownload",
                context = context
            )
            VerticalSpace(height = 20)

            DownloadItemComponent(
                onClick = {
                    val encodedMimeType = URLEncoder.encode(
                        secondMediaItem.localConfiguration?.mimeType.toString(),
                        "UTF-8"
                    )
                    val encodedVideoUrl = URLEncoder.encode(
                        secondMediaItem.localConfiguration?.uri.toString(),
                        "UTF-8"
                    )
                    val encodedArtWorkUrl = URLEncoder.encode(
                        secondMediaItem.mediaMetadata.artworkUri.toString(),
                        "UTF-8"
                    )
                    val drmLicenceUrl = URLEncoder.encode(
                        secondMediaItem.localConfiguration?.drmConfiguration?.licenseUri.toString(),
                        "UTF-8"
                    )

                    navController.navigate(
                        Screens.VideoPlayer.passArguments(
                            videoUrl = encodedVideoUrl,
                            mimeType =  encodedMimeType,
                            title = secondMediaItem.mediaMetadata.title.toString(),
                            artworkUrl = encodedArtWorkUrl,
                            description = secondMediaItem.mediaMetadata.description.toString(),
                            drmLicence = drmLicenceUrl
                        )
                    )

                },
                artWorkUri =secondMediaItem.mediaMetadata.artworkUri.toString(),
                title = secondMediaItem.mediaMetadata.title.toString(),
                onDownloadClick = {
                    try {
                        if (
                            downloadUtil.isDownloaded(secondMediaItem)) {
                            Toast.makeText(context, "Already Downloaded", Toast.LENGTH_SHORT).show()
                        } else {
                            val item = secondMediaItem.buildUpon()
                                .setTag((secondMediaItem.localConfiguration?.tag as MediaItemTag)

                                )
                                .build()

                            if (!downloadUtil
                                    .hasDownload(item.localConfiguration?.uri)
                            ) {
                                downloadUtil
                                    .toggleDownloadDialogHelper(context, item, videoId = "null")
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                },
                cardClickTestTag = "SecondMediaItem",
                downloadClickTextTag ="SecondMediaItemDownload",
                context = context
            )
            VerticalSpace(height = 20)

            DownloadItemComponent(
                onClick = {
                    val encodedMimeType = URLEncoder.encode(
                        thirdMediaItem.localConfiguration?.mimeType.toString(),
                        "UTF-8"
                    )
                    val encodedVideoUrl = URLEncoder.encode(
                        thirdMediaItem.localConfiguration?.uri.toString(),
                        "UTF-8"
                    )
                    val encodedArtWorkUrl = URLEncoder.encode(
                        thirdMediaItem.mediaMetadata.artworkUri.toString(),
                        "UTF-8"
                    )
                    val drmLicenceUrl = URLEncoder.encode(
                        thirdMediaItem.localConfiguration?.drmConfiguration?.licenseUri.toString(),
                        "UTF-8"
                    )

                    navController.navigate(
                        Screens.VideoPlayer.passArguments(
                            videoUrl = encodedVideoUrl,
                            mimeType =  encodedMimeType,
                            title = thirdMediaItem.mediaMetadata.title.toString(),
                            artworkUrl = encodedArtWorkUrl,
                            description = thirdMediaItem.mediaMetadata.description.toString(),
                            drmLicence = drmLicenceUrl
                        )
                    )

                },
                artWorkUri = thirdMediaItem.mediaMetadata.artworkUri.toString(),
                title = thirdMediaItem.mediaMetadata.title.toString(),
                onDownloadClick = {
                    try {
                        if (
                            downloadUtil.isDownloaded(thirdMediaItem)) {
                            Toast.makeText(context, "Already Downloaded", Toast.LENGTH_SHORT).show()
                        } else {
                            val item = thirdMediaItem.buildUpon()
                                .setTag((thirdMediaItem.localConfiguration?.tag as MediaItemTag)

                                )
                                .build()

                            if (!downloadUtil
                                    .hasDownload(item.localConfiguration?.uri)
                            ) {
                                downloadUtil
                                    .toggleDownloadDialogHelper(context, item, videoId = "null")
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                },
                cardClickTestTag = "ThirdMediaItem",
                downloadClickTextTag ="ThirdMediaItemDownload",
                context = context
            )


        }
    }

}