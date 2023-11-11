package com.jasmeet.downloadmanger.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DownloadedVideoData::class], version = 1, exportSchema = false)
abstract class DownloadVideoDatabase : RoomDatabase(){

    abstract fun getDownloadVideoDao(): DownloadVideoDao

//    companion object{
//        @Volatile
//        private var INSTANCE : DownloadVideoDatabase? = null
//
//        fun getDatabase(context: Context): DownloadVideoDatabase{
//            synchronized(this){
//
//                var instance = INSTANCE
//
//
//                if(instance == null){
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        DownloadVideoDatabase::class.java,
//                        "DownloadedVideoDatabase"
//                    ).build()
//
//                    INSTANCE = instance
//                }
//                return instance
//            }
//        }
//    }

}

