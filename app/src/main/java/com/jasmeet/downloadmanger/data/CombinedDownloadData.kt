package com.jasmeet.downloadmanger.data

import androidx.media3.exoplayer.offline.Download
import com.jasmeet.downloadmanger.room.DownloadedVideoData

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun CombinedDownloadData(
    list1: List<Download>,
    list2: List<DownloadedVideoData>
): List<CombineData> {
    return list1.mapNotNull { data1 ->
        val data2 = list2.find { it.url == data1.request.uri.toString() }
        data2?.let { CombineData(data1, it) }
    }
}

data class CombineData(
    val download: Download,
    val downloadedVideoData: DownloadedVideoData
)
