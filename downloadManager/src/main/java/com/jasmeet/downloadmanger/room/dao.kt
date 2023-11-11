package com.jasmeet.downloadmanger.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface DownloadVideoDao{

    @Insert
    suspend fun insert(videoData: DownloadedVideoData)

    @Query("SELECT * FROM DownloadedVideoData WHERE VideoUrl = :videoUrl")
    suspend fun getVideoDataById(videoUrl: String): DownloadedVideoData

    @Query("SELECT * FROM DownloadedVideoData")
    suspend fun getAllVideoData(): List<DownloadedVideoData>

    @Query("DELETE FROM DownloadedVideoData WHERE VideoUrl = :videoUrl")
    suspend fun deleteVideoDataById(videoUrl: String)

    @Query("SELECT * FROM DownloadedVideoData WHERE VideoUrl = :videoUrl")
    suspend fun getVideoUrl(videoUrl: String): List<DownloadedVideoData>


}