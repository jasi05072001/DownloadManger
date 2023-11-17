package com.jasmeet.downloadmanger.downloadTracker

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.StatFs
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.media3.common.C
import androidx.media3.common.Format
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.TrackGroup
import androidx.media3.common.util.Assertions
import androidx.media3.common.util.Util
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadHelper
import androidx.media3.exoplayer.offline.DownloadIndex
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.source.TrackGroupArray
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.MappingTrackSelector
import com.jasmeet.downloadmanger.R
import com.jasmeet.downloadmanger.dbProvider.DbProvider
import com.jasmeet.downloadmanger.exceptions.FailedToDownloadException
import com.jasmeet.downloadmanger.exceptions.NotEnoughSpaceException
import com.jasmeet.downloadmanger.room.DownloadedVideoData
import com.jasmeet.downloadmanger.service.MyDownloadService
import com.jasmeet.downloadmanger.utils.DownloadUtil
import com.jasmeet.downloadmanger.utils.MediaItemTag
import com.jasmeet.downloadmanger.utils.formatFileSize
import com.jasmeet.downloadmanger.utils.returnClosestElement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.concurrent.CopyOnWriteArraySet


private const val TAG = "DownloadTracker"
private const val DEFAULT_BITRATE = 500_000

@ExperimentalCoroutinesApi
@SuppressLint("UnsafeOptInUsageError")
class DownloadTracker(
    context: Context,
    private val httpDataSourceFactory: HttpDataSource.Factory,
    private val downloadManager: DownloadManager
) {

    private val database = DbProvider.getDatabase(context)
    private val dao = database.getDownloadVideoDao()

    val coroutineScope = CoroutineScope(Dispatchers.IO)

    /**
     * Listens for changes in the tracked downloads.
     */
    interface Listener {
        /**
         * Called when the tracked downloads changed.
         */
        fun onDownloadsChanged(download: Download)

    }

    companion object{
        private var downloadCache: Cache? = null
        fun getDownloadCache(context: Context): Cache {
            return downloadCache ?: SimpleCache(getDownloadContentDirectory(context), NoOpCacheEvictor(), getDataBase(context))
                .also { downloadCache = it }
        }
        private fun getDownloadContentDirectory(context: Context): File {
            return File(context.getExternalFilesDir(null), "Downloads")
        }
        private fun getDataBase(context: Context): DatabaseProvider {
            return StandaloneDatabaseProvider(context)
        }
    }

    private val applicationContext: Context = context.applicationContext
    private val listeners: CopyOnWriteArraySet<Listener> = CopyOnWriteArraySet()
    private val downloadIndex: DownloadIndex = downloadManager.downloadIndex
    private var startDownloadHlsDialogHelper: StartDownloadHlsDialogHelper? = null
    private var availableBytesLeft: Long =
        StatFs(DownloadUtil.getDownloadDirectory(context).path).availableBytes

    val downloads: HashMap<Uri, Download> = HashMap()

    init {
        downloadManager.addListener(DownloadManagerListener())
        loadDownloads()

    }

    fun addListener(listener: Listener) {
        Assertions.checkNotNull(listener)
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }
    @ExperimentalStdlibApi
    fun isDownloaded(mediaItem: MediaItem): Boolean {
        val download = downloads[mediaItem.localConfiguration?.uri]
        return download != null && download.state == Download.STATE_COMPLETED
    }
    @ExperimentalStdlibApi
    fun hasDownload(uri: Uri?): Boolean = downloads.keys.contains(uri)
    @ExperimentalStdlibApi
    fun getDownloadRequest(uri: Uri?): DownloadRequest? {
        uri ?: return null
        val download = downloads[uri]
        return if(download != null && download.state != Download.STATE_FAILED) download.request else null
    }
    @ExperimentalStdlibApi
    fun toggleDownloadDialogHelper(
        context: Context, mediaItem: MediaItem,
        positiveCallback: (() -> Unit)? = null, dismissCallback: (() -> Unit)? = null,
        quality: Int? = null,
        videoId: String

    ) {
        startDownloadHlsDialogHelper?.release()
        startDownloadHlsDialogHelper =
            StartDownloadHlsDialogHelper(
                context,
                getDownloadHelper(mediaItem),
                mediaItem,
                positiveCallback,
                dismissCallback,
                quality,
                videoId
            )
    }
    @ExperimentalStdlibApi
    fun removeDownload(uri: Uri?) {
        val download = downloads[uri]
        download?.let {
            DownloadService.sendRemoveDownload(
                applicationContext,
                MyDownloadService::class.java,
                download.request.id,
                true
            )
        }
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            dao.deleteVideoDataById(uri
                .toString())
        }
    }

    @ExperimentalStdlibApi
    fun pauseDownload(uri: Uri?,context: Context) {
        val download = downloads[uri]
        download?.let {
            DownloadService.sendSetStopReason(
                context,
                MyDownloadService::class.java,
                download.request.id,
                Download.STATE_STOPPED,
                false
            )
        }
    }
    @ExperimentalStdlibApi
    fun resumeDownload(uri: Uri?,context: Context) {
        val download = downloads[uri]
        download?.let {
            DownloadService.sendSetStopReason(
                context,
                MyDownloadService::class.java,
                download.request.id,
                Download.STOP_REASON_NONE,
                true
            )
        }
    }

    private fun loadDownloads() {
        try {
            downloadIndex.getDownloads().use { loadedDownloads ->
                while (loadedDownloads.moveToNext()) {
                    val download = loadedDownloads.download
                    downloads[download.request.uri] = download
                }
            }
        } catch (e: IOException) {
            Log.w(TAG, "Failed to query downloads", e)
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun getAllDownloadProgressFlow(): Flow<List<Download>> = callbackFlow {
        while (coroutineContext.isActive) {
            trySend(downloads.values.toList()).isSuccess
            delay(1000)
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun getCurrentProgressDownload(uri: Uri?): Flow<Float?> {
        var percent: Float? =
            downloadManager.currentDownloads.find { it.request.uri == uri }?.percentDownloaded
        return callbackFlow {
            while (percent != null) {
                percent =
                    downloadManager.currentDownloads.find { it.request.uri == uri }?.percentDownloaded
                trySend(percent).isSuccess
                withContext(Dispatchers.IO) {
                    delay(100)
                }
            }
        }
    }


    private fun getDownloadHelper(mediaItem: MediaItem): DownloadHelper {
        return when (mediaItem.localConfiguration?.mimeType) {
            MimeTypes.APPLICATION_MPD, MimeTypes.APPLICATION_M3U8, MimeTypes.APPLICATION_SS -> {
                DownloadHelper.forMediaItem(
                    applicationContext,
                    mediaItem,
                    DefaultRenderersFactory(applicationContext),
                    httpDataSourceFactory
                )
            }
            else -> {
                otherDownloads(mediaItem)
            }
        }
    }

    private fun otherDownloads(mediaItem: MediaItem):DownloadHelper{

        val entity = DownloadedVideoData(
            heading = mediaItem.mediaMetadata.title.toString(),
            thumbnailUrl = mediaItem.mediaMetadata.artworkUri.toString(),
            url = mediaItem.localConfiguration?.uri.toString(),
            description = mediaItem.mediaMetadata.description.toString(),
        )

        coroutineScope.launch {
            dao.insert(entity)
        }

        return  DownloadHelper.forMediaItem(applicationContext, mediaItem)
    }


    private inner class DownloadManagerListener : DownloadManager.Listener {
        override fun onDownloadChanged(
            downloadManager: DownloadManager,
            download: Download,
            finalException: Exception?
        ) {
            downloads[download.request.uri] = download
            for (listener in listeners) {
                listener.onDownloadsChanged(download)
            }
            if(download.state == Download.STATE_COMPLETED) {
                // Add delta between estimation and reality to have a better availableBytesLeft
                availableBytesLeft +=
                    Util.fromUtf8Bytes(download.request.data).toLong() - download.bytesDownloaded
            }

        }

        override fun onDownloadRemoved(downloadManager: DownloadManager, download: Download) {
            downloads.remove(download.request.uri)
            for (listener in listeners) {
                listener.onDownloadsChanged(download)
            }

            // Add the estimated or downloaded bytes to the availableBytes
            availableBytesLeft += if(download.percentDownloaded == 100f) {
                download.bytesDownloaded
            } else {
                Util.fromUtf8Bytes(download.request.data).toLong()
            }
        }

    }

    /**
    Can't use applicationContext because it'll result in a crash, instead
    Use context of the activity calling for the AlertDialog
     */
    private inner class StartDownloadHlsDialogHelper(
        private val context: Context,
        private val downloadHelper: DownloadHelper,
        private val mediaItem: MediaItem,
        private val positiveCallback: (() -> Unit)? = null,
        private val dismissCallback: (() -> Unit)? = null,
        quality: Int?,
        videoId: String
    ) :

        DownloadHelper.Callback {

        private var trackSelectionDialog: AlertDialog? = null
        var userQuality : Int? = quality
        var videoId : String? = videoId



        init {
            downloadHelper.prepare(this)
        }

        fun release() {
            downloadHelper.release()
            trackSelectionDialog?.dismiss()
        }

        // DownloadHelper.Callback implementation.
        override fun onPrepared(helper: DownloadHelper) {
            if(helper.periodCount == 0) {
                Log.d(TAG, "No periods found. Downloading entire stream.")
                val mediaItemTag: MediaItemTag = mediaItem.localConfiguration?.tag as MediaItemTag
                val estimatedContentLength: Long = (DEFAULT_BITRATE * mediaItemTag.duration)
                    .div(C.MILLIS_PER_SECOND).div(C.BITS_PER_BYTE)
                val downloadRequest: DownloadRequest = downloadHelper.getDownloadRequest(
                    mediaItemTag.title,
                    Util.getUtf8Bytes(estimatedContentLength.toString())
                )
                startDownload(downloadRequest)
                release()
                return
            }

            val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
            val formatDownloadable: MutableList<Format> = mutableListOf()
            var qualitySelected: DefaultTrackSelector.Parameters
            val mappedTrackInfo = downloadHelper.getMappedTrackInfo(0)

            getTracks(mappedTrackInfo, formatDownloadable)

            if(formatDownloadable.isEmpty()) {
                dialogBuilder.setTitle("An error occurred")
                    .setPositiveButton("OK", null)
                    .show()
                return
            }

            // We sort here because later we use formatDownloadable to select track
            formatDownloadable.sortBy { it.height }
            val mediaItemTag: MediaItemTag = mediaItem.localConfiguration?.tag as MediaItemTag
            Log.d("zD", "MediaItemTag: ${mediaItemTag.duration}")
            val optionsDownload: List<String> = formatDownloadable.map {
                if (mediaItemTag.duration <= 0L){
                    context.getString(
                        R.string.dialog_option, it.height,
                        (it.bitrate / 1024).toString()+" kbps",
                    )
                }else {
                    context.getString(
                        R.string.dialog_option, it.height,
                        (it.bitrate * mediaItemTag.duration).div(8000).formatFileSize()
                    )
                }
            }

            val qualityList = formatDownloadable.map { it.height }

            //Default quality download
            qualitySelected = DefaultTrackSelector(context).buildUponParameters()
                .setMinVideoSize(formatDownloadable[0].width, formatDownloadable[0].height)
                .setMinVideoBitrate(formatDownloadable[0].bitrate)
                .setMaxVideoSize(formatDownloadable[0].width, formatDownloadable[0].height)
                .setMaxVideoBitrate(formatDownloadable[0].bitrate)
                .build()

            dialogBuilder.setTitle("Select Download Quality")
                .setSingleChoiceItems(optionsDownload.toTypedArray(), 0) { _, which ->
                    val format = formatDownloadable[which]
                    qualitySelected = DefaultTrackSelector(context).buildUponParameters()
                        .setMinVideoSize(format.width, format.height)
                        .setMinVideoBitrate(format.bitrate)
                        .setMaxVideoSize(format.width, format.height)
                        .setMaxVideoBitrate(format.bitrate)
                        .build()
                    Log.e(TAG, "format Selected= width: ${format.width}, height: ${format.height}, qualitySelected:${qualitySelected}")


                    /**
                     * This function will save the quality selected by the user
                     * in the shared preferences
                     */
                    DownloadUtil.saveQualitySelected(context, format.height)
                }.setPositiveButton("Download") { _, _ ->
                    val height = DownloadUtil.getQualitySelected(context)

                    //here we will save the media item title, artwork uri and videoId to room db

                    val entity = DownloadedVideoData(
                        heading = mediaItem.mediaMetadata.title.toString(),
                        thumbnailUrl = mediaItem.mediaMetadata.artworkUri.toString(),
                        url = mediaItem.localConfiguration?.uri.toString(),
                        description = mediaItem.mediaMetadata.description.toString(),
                        drmLicenceUrl = mediaItem.localConfiguration?.drmConfiguration?.licenseUri.toString()

                        )


                    coroutineScope.launch {
                        dao.insert(entity)
                    }

                    helper.clearTrackSelections(0)
                    helper.addTrackSelection(0, qualitySelected)
                    val estimatedContentLength: Long =
                        (qualitySelected.maxVideoBitrate * mediaItemTag.duration)
                            .div(C.MILLIS_PER_SECOND).div(C.BITS_PER_BYTE)
                    if(availableBytesLeft > estimatedContentLength) {
                        val downloadRequest: DownloadRequest = downloadHelper.getDownloadRequest(
                            (mediaItem.localConfiguration?.tag as MediaItemTag).title,
                            Util.getUtf8Bytes(estimatedContentLength.toString())
                        )
                        startDownload(downloadRequest)
                        availableBytesLeft -= estimatedContentLength
                        Log.e(TAG, "availableBytesLeft after calculation: $availableBytesLeft")
                    } else {
                        throw NotEnoughSpaceException("Not enough space to download this file")
                    }
                    positiveCallback?.invoke()
                }.setOnDismissListener {
                    trackSelectionDialog = null
                    downloadHelper.release()
                    dismissCallback?.invoke()
                }
            trackSelectionDialog = dialogBuilder.create().apply {


                /**
                 * this if statement will check if the user has already selected a quality and will download the
                 * selected quality using the value saved in shared preferences
                 */

                if(DownloadUtil.getQualitySelected(context)>0){
                    dismiss()
                    val entity = DownloadedVideoData(
                        heading = mediaItem.mediaMetadata.title.toString(),
                        thumbnailUrl = mediaItem.mediaMetadata.artworkUri.toString(),
                        url =  mediaItem.localConfiguration?.uri.toString(),
                        description = mediaItem.mediaMetadata.description.toString(),
                        drmLicenceUrl = mediaItem.localConfiguration?.drmConfiguration?.licenseUri.toString()

                        )


                    coroutineScope.launch {
                        dao.insert(entity)
                    }

                    val savedQuality = DownloadUtil.getQualitySelected(context)
                    val selectedFormat = findSelectedFormat(formatDownloadable, savedQuality)
                    if (selectedFormat != null) {
                        qualitySelected = DefaultTrackSelector(context).buildUponParameters()
                            .setMinVideoSize(selectedFormat.width, selectedFormat.height)
                            .setMinVideoBitrate(selectedFormat.bitrate)
                            .setMaxVideoSize(selectedFormat.width, selectedFormat.height)
                            .setMaxVideoBitrate(selectedFormat.bitrate)
                            .build()


                        val estimatedContentLength: Long =
                            (qualitySelected.maxVideoBitrate * mediaItemTag.duration)
                                .div(C.MILLIS_PER_SECOND).div(C.BITS_PER_BYTE)

                        if (availableBytesLeft >estimatedContentLength){
                            val downloadRequest = downloadHelper.getDownloadRequest(
                                (mediaItem.localConfiguration?.tag as MediaItemTag).title,
                                Util.getUtf8Bytes(estimatedContentLength.toString())
                            )
                            startDownload(downloadRequest)
                            availableBytesLeft -= estimatedContentLength
                        }else{
                            throw NotEnoughSpaceException("Not enough space to download this file")
                        }
                    }

                }

                /**
                this else if statement will check if the user has selected a quality and will download the
                developer entered quality at client level
                 */

                else if (userQuality != null) {
                    val entity = DownloadedVideoData(
                        heading = mediaItem.mediaMetadata.title.toString(),
                        thumbnailUrl = mediaItem.mediaMetadata.artworkUri.toString(),
                        url =  mediaItem.localConfiguration?.uri.toString(),
                        description = mediaItem.mediaMetadata.description.toString(),
                        drmLicenceUrl = mediaItem.localConfiguration?.drmConfiguration?.licenseUri.toString()

                    )


                    coroutineScope.launch {
                        dao.insert(entity)
                    }

                    dismiss()
                    val closestUserQuality = returnClosestElement(qualityList, userQuality!!)
                    val userSelectedFormat = findSelectedFormat(formatDownloadable, closestUserQuality)

                    if (userSelectedFormat != null){
                        qualitySelected = DefaultTrackSelector(context).buildUponParameters()
                            .setMinVideoSize(userSelectedFormat.width, userSelectedFormat.height)
                            .setMinVideoBitrate(userSelectedFormat.bitrate)
                            .setMaxVideoSize(userSelectedFormat.width, userSelectedFormat.height)
                            .setMaxVideoBitrate(userSelectedFormat.bitrate)
                            .build()


                        val estimatedContentLength: Long =
                            (qualitySelected.maxVideoBitrate * mediaItemTag.duration)
                                .div(C.MILLIS_PER_SECOND).div(C.BITS_PER_BYTE)

                        if (availableBytesLeft >estimatedContentLength){
                            val downloadRequest = downloadHelper.getDownloadRequest(
                                (mediaItem.localConfiguration?.tag as MediaItemTag).title,
                                Util.getUtf8Bytes(estimatedContentLength.toString())
                            )
                            startDownload(downloadRequest)
                            availableBytesLeft -= estimatedContentLength
                        }else{
                            throw NotEnoughSpaceException("Not enough space to download this file")
                        }
                    }
                }

                /**
                 * this else statement will show the quality selection dialog to the user
                 * this dialog will only appear if the user has not selected a quality
                 * and the developer has not entered a quality at client level
                 */
                else {
                    show()
                }
            }
        }

        private fun getTracks(
            mappedTrackInfo: MappingTrackSelector.MappedTrackInfo,
            formatDownloadable: MutableList<Format>
        ) {
            for (i in 0 until mappedTrackInfo.rendererCount) {
                if (C.TRACK_TYPE_VIDEO == mappedTrackInfo.getRendererType(i)) {
                    val trackGroups: TrackGroupArray = mappedTrackInfo.getTrackGroups(i)
                    for (j in 0 until trackGroups.length) {
                        val trackGroup: TrackGroup = trackGroups[j]
                        for (k in 0 until trackGroup.length) {
                            formatDownloadable.add(trackGroup.getFormat(k))
                        }
                    }
                }
            }
        }

        override fun onPrepareError(helper: DownloadHelper, e: IOException) {
            throw FailedToDownloadException("Failed to download: ${e.message}")
        }

        private fun startDownload(downloadRequest: DownloadRequest = buildDownloadRequest()) {
            Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show()

            DownloadService.sendAddDownload(
                applicationContext,
                MyDownloadService::class.java,
                downloadRequest,
                true
            )
        }


        private fun buildDownloadRequest(): DownloadRequest {
            return downloadHelper.getDownloadRequest(
                (mediaItem.localConfiguration?.tag as MediaItemTag).title,
                Util.getUtf8Bytes(mediaItem.localConfiguration?.uri.toString())
            )
        }
    }

    private fun findSelectedFormat(formats: MutableList<Format>, savedQuality: Int): Format? {
        return formats.find {
            it.height == savedQuality
        }

    }


}