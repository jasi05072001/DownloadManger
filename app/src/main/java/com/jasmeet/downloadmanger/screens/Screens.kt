package com.jasmeet.downloadmanger.screens

const val VIDEO_URL = "url"
const val ARTWORK_URL = "artworkUrl"
const val MIME_TYPE = "mimeType"
const val TITLE = "title"
const val DESCRIPTION = "description"
const val DRM_LICENCE_URL = "drmLicenceUrl"

sealed class Screens(val route :String){
    data object HomeScreen : Screens("homeScreen")
    data object DownloadScreen : Screens("downloadScreen")
    data object VideoPlayer : Screens(
        "videoPlayer/{$VIDEO_URL}/{$ARTWORK_URL}/{$MIME_TYPE}/{$TITLE}/{$DESCRIPTION}?{$DRM_LICENCE_URL}"
    ){
        fun passArguments(
            videoUrl : String,
            artworkUrl : String,
            mimeType : String,
            title : String,
            description : String,
            drmLicence: String? = null
        ) : String{
            return this.route
                .replace("{$VIDEO_URL}",videoUrl)
                .replace("{$ARTWORK_URL}",artworkUrl)
                .replace("{$MIME_TYPE}",mimeType)
                .replace("{$TITLE}",title)
                .replace("{$DESCRIPTION}",description)
                .replace("{$DRM_LICENCE_URL}",drmLicence ?: "")

        }

    }

    data object OfflineVideoPlayer : Screens(
        "offlineVideoPlayer/{$VIDEO_URL}/{$ARTWORK_URL}/{$MIME_TYPE}/{$TITLE}/{$DESCRIPTION}?{$DRM_LICENCE_URL}"
    ){
        fun passArguments(
            videoUrl : String,
            artworkUrl : String,
            mimeType : String,
            title : String,
            description : String,
            drmLicence: String? = null
        ) : String{
            return this.route
                .replace("{$VIDEO_URL}",videoUrl)
                .replace("{$ARTWORK_URL}",artworkUrl)
                .replace("{$MIME_TYPE}",mimeType)
                .replace("{$TITLE}",title)
                .replace("{$DESCRIPTION}",description)
                .replace("{$DRM_LICENCE_URL}",drmLicence ?: "")

        }

    }
    data object DrmScreen : Screens("drmScreen")
    data object HlsScreen : Screens("hlsScreen")
    data object Mp4Screen : Screens("mp4Screen")


}
