package com.jasmeet.downloadmanger.mediaItemList

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import com.jasmeet.downloadmanger.utils.MediaItemTag


val mp4ListMediaItem: List<MediaItem> = listOf(

    MediaItem.Builder()
        .setUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        .setMimeType(MimeTypes.APPLICATION_MP4)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle("Big Buck Bunny")
                .setDescription(
                    "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. " +
                            "When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! " +
                            "In the typical cartoon tradition he prepares the nasty rodents a comical revenge."
                )
                .setArtworkUri(Uri.parse("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTjflj-1oZ6LzJWiTh71LZYfiHDi5PolDX1_4ns4MbwUiqqgSb9qtN7eBnstnyJou4Lf38&usqp=CAU"))
                .build()
        )
        .setTag(MediaItemTag(-1, "Big Buck Bunny"))
        .build(),

    MediaItem.Builder()
        .setUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
        .setMimeType(MimeTypes.APPLICATION_MP4)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle("Elephant Dream")
                .setDescription(
                    "The film tells the story of Emo and Proog, two people with different visions of the surreal world in which they live. " +
                            "Viewers are taken on a journey through that world, full of strange mechanical birds, stunning technological vistas and machinery that seems to have a life of its own."
                )
                .setArtworkUri(Uri.parse("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTjflj-1oZ6LzJWiTh71LZYfiHDi5PolDX1_4ns4MbwUiqqgSb9qtN7eBnstnyJou4Lf38&usqp=CAU"))
                .build()
        )
        .setTag(MediaItemTag(-1, "Elephant Dream"))
        .build(),

    MediaItem.Builder()
        .setUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4")
        .setMimeType(MimeTypes.APPLICATION_MP4)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle("For Bigger Blazes")
                .setDescription(
                    "For Bigger Blazes is a short film by producer Ton Roosendaal and director Ian Hubert. " +
                            "It was made using new enhancements to the visual effects capabilities of Blender, a free and open source software application for animation. " +
                            "The film was funded by the Blender Foundation, donations from the Blender community, pre-sales of the film's DVD and commercial sponsorship."
                )
                .setArtworkUri(Uri.parse("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTjflj-1oZ6LzJWiTh71LZYfiHDi5PolDX1_4ns4MbwUiqqgSb9qtN7eBnstnyJou4Lf38&usqp=CAU"))
                .build()
        )
        .setTag(MediaItemTag(-1, "For Bigger Blazes"))
        .build(),

    )