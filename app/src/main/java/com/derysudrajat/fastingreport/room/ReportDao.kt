package com.derysudrajat.fastingreport.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.derysudrajat.fastingreport.model.Report

@Dao
public interface ReportDao{

    @Query("SELECT * From Report")
    fun getDataReport() : MutableList<Report>

    @Query("SELECT * From Report LIMIT 5")
    fun getDataReportLimit() : MutableList<Report>

    @Insert
    fun addReport(report: Report)

    @Update
    fun updateReport(report: Report)
}