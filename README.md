# DownloadManager

This is a media 3 download manager that can download Videos from the mediaItems.
To use this download manager you need to add the following dependencies to your app level build.gradle file,and don't forget to add
the jitpack.io to your project level settings.gradle file.

`groovy:`
maven { url 'https://jitpack.io' }

`kotlin:`
maven { url = uri("https://jitpack.io") }

add the following to your project's `build.gradle.kts` file,

'groovy:'
implementation 'com.github.jasi05072001:DownloadManger:1.X.X'

'kotlin:'
implementation("com.github.jasi05072001:DownloadManger:1.X.X")

# How to use this library
To use this library you need to pass the mediaItem to the download manager and call the download
function.

The `DownloadManager` will handle downloading media Items on its own , if the mime Type of the mediaItem is
`MimeTypes.APPLICATION_MPD`, `MimeTypes.APPLICATION_M3U8`, `MimeTypes.APPLICATION_SS`, `MimeTypes.APPLICATION_DASH_XML`, `MimeTypes.APPLICATION_HLS`
then the download manager will show you a dialog for the qualities present in the mediaItem and you can select the quality you want to download.
The dialog will show the `qualities mapped with estimated download size` or the `quality mapped with the bitrate of the mediaItem`.

`The dialog will be shown only once after that it will download the previous selected quality or the quality nearest to the previous selected quality.`
`If you want to show the dialog again then you need to clear the shared preferences of the app.`


`You can also pass a specific quality to the download manager and it will download the mediaItem with that quality.`


If the mime type of the mediaItem is `MimeTypes.VIDEO_MP4` or `MimeTypes.AUDIO_MP4` then the download manager will download the mediaItem with the default quality.


# Code Snippet(For Jetpack Compose)

`Step 1:`
Create a object of the `DownloadManager` class.

```
val downloadUtil = remember{ DownloadUtil.getDownloadTracker(context)
```

`Assuming you have created a mediaItem like this`

```
val mediaItem = MediaItem.Builder()
    .setUri(`your uri`)
    .setMimeType(`mime type of the mediaItem`)
    .setMediaMetadata(
        MediaMetadata.Builder()
             .setTitle(`title of the mediaItem`)
             .setDescription(`description of the mediaItem`)
             .setArtworkUri(Uri.parse(`Thumbnail uri of the mediaItem`)
             .build()
    ).setTag(MediaItemTag(-1, `title of the mediaItem`))
    .build()
```

`Step 2:`
Call the download function of the `DownloadManager` class and pass the mediaItem to it.
```
downloadUtil.isDownloaded(mediaItem)
```
this function will return true if the mediaItem is already downloaded.

if you want to display user the estimated download size then build the mediaItem again with editing the pass the duration of the video as tag.
You can do this by using the following code snippet.

```
val item = mediaItem
    .buildUpon()
    .setTag(
        (mediaItem.localConfiguration?.tag as MediaItemTag).copy(
            duration = exoPlayer.duration
        )
    )
    .build()
```

`Step 3:`
If the mediaItem is not downloaded then call the download function of the `DownloadManager` class and pass the mediaItem to it.
```
 if (!downloadUtil.hasDownload(item.localConfiguration?.uri)
 ) {
     downloadUtil
        .toggleDownloadDialogHelper(
        context,
        item,
        videoId = "null"
        )
    }
```

`Step 4:`
If you want to download the mediaItem with a specific quality then you can pass the quality to the download function of the `DownloadManager` class.
```
 if (!downloadUtil.hasDownload(item.localConfiguration?.uri)
 ) {
     downloadUtil
        .toggleDownloadDialogHelper(
        context,
        item,
        videoId = "null",
        quality = `quality you want to download`
        )
    }
```

`Step 5:`
The  `Download Manager` will also take care if there is any network error or the download is paused or the download is cancelled. and it will only starts downloading the media Item when there is enough storage space available.



# Summary
Combining from Step 2 to Step 4 the code will look like this.

```
TextButton(
                        onClick = {
                            try {
                                if (downloadUtil
                                        .isDownloaded(mediaItem)
                                ) {
                                    Toast
                                        .makeText(
                                            context,
                                            "Already Downloaded",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                } else {
                                    val item = mediaItem
                                        .buildUpon()
                                        .setTag(
                                            (mediaItem.localConfiguration?.tag as MediaItemTag).copy(
                                                duration = exoPlayer.duration
                                            )
                                        )
                                        .build()

                                    if (!downloadUtil.hasDownload(item.localConfiguration?.uri)
                                    ) {
                                        downloadUtil
                                            .toggleDownloadDialogHelper(
                                                context,
                                                item,
                                                videoId = "null"
                                            )
                                    }
                                }
                            } catch (e: Exception) {
                                Toast
                                    .makeText(
                                        context,
                                        e.message.toString(),
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                Log.d("error", e.message.toString())
                            }
                        }) {
                        Text(text = "Download")

                    }
```






