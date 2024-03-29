package com.jasmeet.downloadmanger.mediaItemList

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import com.jasmeet.downloadmanger.utils.MediaItemTag

object HlsMediaItemList {

    val firstMediaItem = MediaItem.Builder()
        .setUri("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
        .setMimeType(MimeTypes.APPLICATION_M3U8)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle("Google Example")
                .setDescription(
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                )
                .setArtworkUri(Uri.parse("https://plus.unsplash.com/premium_photo-1676418546431-815b9c137484?q=80&w=1160&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"))
                .build()
        )
        .setTag(MediaItemTag(-1, "Google Example"))
        .build()


    val secondMediaItem = MediaItem.Builder()
        .setUri("https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8")
        .setMimeType(MimeTypes.APPLICATION_M3U8)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle("Animation Movie")
                .setDescription(
                    "Sintel is an independently produced short film, initiated by the Blender Foundation as a means to further improve and validate the free,open source 3D creation suite Blender. " +
                            "With initial funding provided by 1000s of donations via the internet community, it has again proven to be a viable development model for both open 3D technology as for independent animation film."
                )
                .setArtworkUri(Uri.parse("https://images.unsplash.com/photo-1563089145-599997674d42?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"))
                .build()
        )
        .setTag(MediaItemTag(-1, "Animation Movie"))
        .build()

    val thirdMediaItem = MediaItem.Builder()
        .setUri("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
        .setMimeType(MimeTypes.APPLICATION_M3U8)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle("Big Bunny")
                .setDescription(
                    "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. " +
                            "When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! " +
                            "In the typical cartoon tradition he prepares the nasty rodents a comical revenge."
                )
                .setArtworkUri(Uri.parse("https://images.unsplash.com/photo-1614680376573-df3480f0c6ff?q=80&w=1074&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"))
                .build()
        )
        .setTag(MediaItemTag(-1, "Big Bunny"))
        .build()

}