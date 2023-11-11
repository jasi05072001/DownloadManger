package com.jasmeet.downloadmanger.mediaItemList

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import com.jasmeet.downloadmanger.utils.MediaItemTag

val hlsListMediaItem: List<MediaItem> = listOf(

    MediaItem.Builder()
        .setUri("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
        .setMimeType(MimeTypes.APPLICATION_M3U8)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle("Google Example")
                .setDescription(
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                )
                .setArtworkUri(Uri.parse("https://cdn.pixabay.com/photo/2016/12/15/20/21/texture-1909992_1280.jpg"))
                .build()
        )
        .setTag(MediaItemTag(-1, "Google Example"))
        .build(),


    MediaItem.Builder()
        .setUri("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8")
        .setMimeType(MimeTypes.APPLICATION_M3U8)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle("Animation Movie")
                .setDescription(
                    "Sintel is an independently produced short film, initiated by the Blender Foundation as a means to further improve and validate the free/open source 3D creation suite Blender. " +
                            "With initial funding provided by 1000s of donations via the internet community, it has again proven to be a viable development model for both open 3D technology as for independent animation film."
                )
                .setArtworkUri(Uri.parse("https://cdn.pixabay.com/photo/2016/12/15/20/21/texture-1909992_1280.jpg"))
                .build()
        )
        .setTag(MediaItemTag(-1, "Animation Movie"))
        .build(),

    MediaItem.Builder()
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
                .setArtworkUri(Uri.parse("https://cdn.pixabay.com/photo/2016/12/15/20/21/texture-1909992_1280.jpg"))
                .build()
        )
        .setTag(MediaItemTag(-1, "Big Bunny"))
        .build(),

    MediaItem.Builder()
        .setUri("https://d1gnaphp93fop2.cloudfront.net/videos/multiresolution/rendition_new10.m3u8")
        .setMimeType(MimeTypes.APPLICATION_M3U8)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle("Pipe Dream Haiti")
                .setDescription(
                    "Pipe Dream Haiti is a short film about bringing clean water to Haiti. " +
                            "The film follows former pro surfer Jon Rose and his mission to bring clean water access to the people of Haiti's forgotten surf village, " +
                            "Cote de Fer. The film documents the journey of Jon and his organization, Waves For Water, " +
                            "as they distribute water filters throughout the small Caribbean country. "
                )
                .setArtworkUri(Uri.parse("https://cdn.pixabay.com/photo/2016/12/15/20/21/texture-1909992_1280.jpg"))
                .build()
        )
        .setTag(MediaItemTag(-1, "Pipe Dream Haiti"))
        .build(),

    MediaItem.Builder()
        .setUri("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
        .setMimeType(MimeTypes.APPLICATION_M3U8)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle("Caminandes 4k")
                .setDescription(
                    "Caminandes is a series of short 3D animated films about the adventures of Koro the Llama and his friends. " +
                            "In this episode called Llamigos (2016) Koro discovers that friendship is above any difference."
                )
                .setArtworkUri(Uri.parse("https://cdn.pixabay.com/photo/2016/12/15/20/21/texture-1909992_1280.jpg"))
                .build()
        )
        .setTag(MediaItemTag(-1, "Caminandes 4k"))
        .build(),


    )