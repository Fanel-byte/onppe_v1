package com.example.onppe_v1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SignalementTransfert::class ] , version = 1)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun getSignalementDao():SignalementDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun buildDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                INSTANCE =
                    Room.databaseBuilder(context,AppDatabase::class.java,
                        "signalements_db").allowMainThreadQueries().build() }
            return INSTANCE
        }
    }
}
