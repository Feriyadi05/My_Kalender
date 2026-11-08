package com.example.mykalender.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Agenda::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun agendaDao(): AgendaDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        // Database dibuat sekali saja, lalu dipakai bersama
        fun get(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mykalender.db"
                ).build().also { instance = it }
            }
    }
}
