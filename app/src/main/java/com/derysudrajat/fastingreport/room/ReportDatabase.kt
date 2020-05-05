package com.derysudrajat.fastingreport.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.derysudrajat.fastingreport.model.Report

@Database(entities = [Report::class], version = 1, exportSchema = false)
abstract class ReportDatabase: RoomDatabase() {
    abstract fun reporDao(): ReportDao

    companion object{

        @Volatile
        private var INSTANCE : ReportDatabase? = null

        fun getDatabase(context: Context): ReportDatabase{
            val tmpInstance = INSTANCE
            if (tmpInstance!=null){
                return tmpInstance
            }else{
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReportDatabase::class.java,
                    "fasting_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }

    }
}