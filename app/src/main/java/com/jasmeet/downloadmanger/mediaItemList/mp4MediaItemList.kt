package com.jasmeet.downloadmanger.mediaItemList

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import com.jasmeet.downloadmanger.utils.MediaItemTag


object Mp4ListMediaItem{

    val firstMediaItem = MediaItem.Builder()
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
                .setArtworkUri(Uri.parse("https://images.unsplash.com/photo-1533174072545-7a4b6ad7a6c3?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"))
                .build()
        )
        .setTag(MediaItemTag(-1, "Big Buck Bunny"))
        .build()

    val secondMediaItem = MediaItem.Builder()
        .setUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
        .setMimeType(MimeTypes.APPLICATION_MP4)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle("Elephant Dream")
                .setDescription(
                    "The film tells the story of Emo and Proog, two people with different visions of the surreal world in which they live. " +
                            "Viewers are taken on a journey through that world, full of strange mechanical birds, stunning technological vistas and machinery that seems to have a life of its own."
                )
                .setArtworkUri(Uri.parse("https://images.unsplash.com/photo-1594623930572-300a3011d9ae?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"))
                .build()
        )
        .setTag(MediaItemTag(-1, "Elephant Dream"))
        .build()

    val thirdMediaItem = MediaItem.Builder()
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
                .setArtworkUri(Uri.parse("https://images.unsplash.com/photo-1567095761054-7a02e69e5c43?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"))
                .build()
        )
        .setTag(MediaItemTag(-1, "For Bigger Blazes"))
        .build()

}