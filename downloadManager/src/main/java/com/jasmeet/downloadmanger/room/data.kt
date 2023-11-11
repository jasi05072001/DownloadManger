package com.jasmeet.downloadmanger.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DownloadedVideoData")
data class DownloadedVideoData (



    @ColumnInfo(name = "ImgUrl")
    var thumbnailUrl :String ?= "Not Available",

    @ColumnInfo(name = "VideoTitle")
    var heading:String ?= "Not Available",

    @ColumnInfo(name = "VideoDescription")
    var description:String ?= "Not Available",

    @PrimaryKey
    @ColumnInfo(name = "VideoUrl")
    var url :String
)

