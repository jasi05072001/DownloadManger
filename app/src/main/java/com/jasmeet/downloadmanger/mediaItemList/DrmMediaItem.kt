package com.jasmeet.downloadmanger.mediaItemList

import android.net.Uri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import com.jasmeet.downloadmanger.utils.MediaItemTag

val drmListMediaItem: List<MediaItem> = listOf(

    MediaItem.Builder()
        .setUri("https://storage.googleapis.com/wvmedia/clear/vp9/tears/tears.mpd")
        .setMimeType(MimeTypes.APPLICATION_MPD)
        .setMediaMetadata(MediaMetadata.Builder()
            .setTitle("Clear(WebM, VP9)")
            .setDescription(
                "Tears of Steel is a short film by producer Ton Roosendaal and director Ian Hubert. " +
                        "It was made using new enhancements to the visual effects capabilities of Blender, a free and open source software application for animation. " +
                        "The film was funded by the Blender Foundation, donations from the Blender community, pre-sales of the film's DVD and commercial sponsorship.")
            .setArtworkUri(Uri.parse("https://cdn.pixabay.com/photo/2017/07/03/20/17/colorful-2468874_640.jpg"))
            .build()
        ).setTag(MediaItemTag(-1, "Clear(WebM, VP9)"))
        .build(),


    MediaItem.Builder()
        .setUri("https://storage.googleapis.com/exoplayer-test-media-0/dash-multiple-base-urls/manifest.mpd")
        .setMimeType(MimeTypes.APPLICATION_MPD)
//        .setDrmConfiguration(
//            MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID)
//                .setLicenseUri("https://proxy.uat.widevine.com/proxy?video_id=2015_tears&provider=widevine_test")
//                .build()
//        )
        .setMediaMetadata(MediaMetadata.Builder()
            .setTitle("Licensed-H264(cenc)")
            .setDescription(
                "Tears of Steel is a short film by producer Ton Roosendaal and director Ian Hubert. " +
                        "It was made using new enhancements to the visual effects capabilities of Blender, a free and open source software application for animation. " +
                        "The film was funded by the Blender Foundation, donations from the Blender community, pre-sales of the film's DVD and commercial sponsorship.")
            .setArtworkUri(Uri.parse("https://cdn.pixabay.com/photo/2017/07/03/20/17/colorful-2468874_640.jpg"))
            .build()
        )
        .setTag(MediaItemTag(-1, "Licensed-H264(cenc)"))
        .build(),

    MediaItem.Builder()
        .setUri("https://storage.googleapis.com/wvmedia/cenc/hevc/tears/tears.mpd")
        .setMimeType(MimeTypes.APPLICATION_MPD)
        .setDrmConfiguration(
            MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID)
                .setLicenseUri("https://proxy.uat.widevine.com/proxy?video_id=2015_tears&provider=widevine_test")
                .build()
        )
        .setMediaMetadata(MediaMetadata.Builder()
            .setTitle("Licensed-H265(cenc)")
            .setDescription(
                "Tears of Steel is a short film by producer Ton Roosendaal and director Ian Hubert. " +
                        "It was made using new enhancements to the visual effects capabilities of Blender, a free and open source software application for animation. " +
                        "The film was funded by the Blender Foundation, donations from the Blender community, pre-sales of the film's DVD and commercial sponsorship.")
            .setArtworkUri(Uri.parse("https://cdn.pixabay.com/photo/2017/07/03/20/17/colorful-2468874_640.jpg"))
            .build()
        )
        .setTag(MediaItemTag(-1, "Licensed-H265(cenc)"))
        .build(),
)