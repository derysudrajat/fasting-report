package com.derysudrajat.fastingreport

import org.joda.time.Chronology
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.chrono.ISOChronology
import org.joda.time.chrono.IslamicChronology
import org.joda.time.format.DateTimeFormat
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun getIslamicDate(){
        val dateTime = DateTime()
        val iso: Chronology = ISOChronology.getInstanceUTC()
        val hijri: Chronology = IslamicChronology.getInstanceUTC()

        val todayIso = LocalDate(dateTime.year, dateTime.monthOfYear, dateTime.dayOfMonth, iso)
        val todayHijri = LocalDate(todayIso.toDateTimeAtStartOfDay(), hijri).toString()
        val day = todayHijri.split("-")[2]
        val year = todayHijri.split("-")[0]
        System.out.println("$day Ramadhan $year AH")
    }
}
