package com.jasmeet.downloadmanger.dbProvider

import android.content.Context
import androidx.room.Room
import com.jasmeet.downloadmanger.room.DownloadVideoDatabase

object DbProvider {
    private var database: DownloadVideoDatabase? = null

    fun getDatabase(context: Context): DownloadVideoDatabase {
        if (database == null) {
            database = Room.databaseBuilder(
                context.applicationContext,
                DownloadVideoDatabase::class.java,
                "DownloadedVideoDatabase"
            ).build()
        }
        return database!!
    }
}