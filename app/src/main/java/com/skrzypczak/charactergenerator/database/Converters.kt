package com.skrzypczak.charactergenerator.database

import android.net.Uri
import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun timestampToDate(timestamp: Long?) = timestamp?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?) = date?.time

    @TypeConverter
    fun uriToString(uri: Uri?) = uri?.toString()

    @TypeConverter
    fun stringToUri(string: String?) = Uri.parse(string)
}