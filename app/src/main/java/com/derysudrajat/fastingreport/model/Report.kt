package com.derysudrajat.fastingreport.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Report(
    @PrimaryKey
    var date: String,
    @ColumnInfo(name = "is_fasting")
    var isFasting : Int = 0
) : Parcelable