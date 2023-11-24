package com.jasmeet.downloadmanger.mediaItemList

import android.net.Uri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import com.jasmeet.downloadmanger.utils.MediaItemTag

object DrmMediaItem {

    val firstMediaItem = MediaItem.Builder()
        .setUri("https://storage.googleapis.com/wvmedia/cenc/h264/tears/tears.mpd")
        .setMimeType(MimeTypes.APPLICATION_MPD)
        .setDrmConfiguration(
            MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID)
                .setLicenseUri("https://proxy.uat.widevine.com/proxy?video_id=2015_tears&provider=widevine_test")
                .build()
        )
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle("Clear(WebM, VP9)")
                .setDescription(
                    "Tears of Steel is a short film by producer Ton Roosendaal and director Ian Hubert. " +
                            "It was made using new enhancements to the visual effects capabilities of Blender, a free and open source software application for animation. " +
                            "The film was funded by the Blender Foundation, donations from the Blender community, pre-sales of the film's DVD and commercial sponsorship.")
                .setArtworkUri(Uri.parse("https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"))
                .build()
        ).setTag(MediaItemTag(-1, "Clear(WebM, VP9)"))
        .build()

    val secondMediaItem =   MediaItem.Builder()
        .setUri("https://storage.googleapis.com/exoplayer-test-media-1/widevine/tears_enc_clear_enc.mpd")
        .setMimeType(MimeTypes.APPLICATION_MPD)
        .setDrmConfiguration(
            MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID)
                .setLicenseUri("https://proxy.uat.widevine.com/proxy?video_id=2015_tears&provider=widevine_test")
                .build()
        )
        .setMediaMetadata(
            MediaMetadata.Builder()
            .setTitle("Licensed-H264(cenc)")
            .setDescription(
                "Tears of Steel is a short film by producer Ton Roosendaal and director Ian Hubert. " +
                        "It was made using new enhancements to the visual effects capabilities of Blender, a free and open source software application for animation. " +
                        "The film was funded by the Blender Foundation, donations from the Blender community, pre-sales of the film's DVD and commercial sponsorship.")
            .setArtworkUri(Uri.parse("https://images.unsplash.com/photo-1514525253161-7a46d19cd819?q=80&w=1074&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"))
            .build()
        )
        .setTag(MediaItemTag(-1, "Licensed-H264(cenc)"))
        .build()

    val thirdMediaItem =  MediaItem.Builder()
        .setUri("https://storage.googleapis.com/wvmedia/cenc/hevc/tears/tears.mpd")
        .setMimeType(MimeTypes.APPLICATION_MPD)
        .setDrmConfiguration(
            MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID)
                .setLicenseUri("https://proxy.uat.widevine.com/proxy?video_id=2015_tears&provider=widevine_test")
                .build()
        )
        .setMediaMetadata(
            MediaMetadata.Builder()
            .setTitle("Licensed-H265(cenc)")
            .setDescription(
                "Tears of Steel is a short film by producer Ton Roosendaal and director Ian Hubert. " +
                        "It was made using new enhancements to the visual effects capabilities of Blender, a free and open source software application for animation. " +
                        "The film was funded by the Blender Foundation, donations from the Blender community, pre-sales of the film's DVD and commercial sponsorship.")
            .setArtworkUri(Uri.parse("https://images.unsplash.com/photo-1614149162883-504ce4d13909?q=80&w=1074&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"))
            .build()
        )
        .setTag(MediaItemTag(-1, "Licensed-H265(cenc)"))
        .build()

}