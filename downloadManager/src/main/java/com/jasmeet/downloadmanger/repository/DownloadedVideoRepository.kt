package com.jasmeet.downloadmanger.repository

import android.content.Context
import com.jasmeet.downloadmanger.dbProvider.DbProvider
import com.jasmeet.downloadmanger.room.DownloadedVideoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DownloadedVideoRepository {
    suspend fun getVideoDetailsById(id: String, context: Context): DownloadedVideoData {
        val database = DbProvider.getDatabase(context)
        val dao = database.getDownloadVideoDao()

        return withContext(Dispatchers.IO) {
            dao.getVideoDataById(id)
        }
    }

    suspend fun getAllDownloadedVideos(context: Context): List<DownloadedVideoData> {
        val database = DbProvider.getDatabase(context)
        val dao = database.getDownloadVideoDao()

        return withContext(Dispatchers.IO) {
            dao.getAllVideoData()
        }
    }

    suspend fun removeDownloadedVideo(context: Context, videoId: String) {
        val database = DbProvider.getDatabase(context)
        val dao = database.getDownloadVideoDao()

        return withContext(Dispatchers.IO) {
            dao.deleteVideoDataById(videoId)
        }
    }
}