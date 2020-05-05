package com.derysudrajat.fastingreport.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.derysudrajat.fastingreport.R
import com.derysudrajat.fastingreport.adapter.FastingHistoryAdapter
import com.derysudrajat.fastingreport.model.Report
import com.derysudrajat.fastingreport.room.ReportDatabase
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.item_daily_report.*
import kotlinx.android.synthetic.main.item_progress_fasting.*
import org.joda.time.Chronology
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.chrono.ISOChronology
import org.joda.time.chrono.IslamicChronology
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private lateinit var fastingReportAdapter: FastingHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fastingData = ReportDatabase.getDatabase(requireActivity()).reporDao().getDataReport()

        initHistoryFasting(fastingData)

        initDailyView(fastingData)

        val format = SimpleDateFormat("dd-MM-yyyy")
        val date = format.format(Date())

        btnDailyPositif.setOnClickListener {
            val report = Report(date, 1)
            ReportDatabase.getDatabase(requireActivity()).reporDao().addReport(report)
            refreshData()
        }

        btnDailyNegatif.setOnClickListener {
            val report = Report(date, 0)
            ReportDatabase.getDatabase(requireActivity()).reporDao().addReport(report)
            refreshData()
        }

        srlMain.setOnRefreshListener {
            fastingReportAdapter.clear()
            refreshData()
            srlMain.isRefreshing = false
        }
    }

    fun refreshData(){
        fastingReportAdapter.clear()
        val fastingData = ReportDatabase.getDatabase(requireActivity()).reporDao().getDataReport()
        initDailyView(fastingData)
        initHistoryFasting(fastingData)
    }

    @SuppressLint("SimpleDateFormat")
    private fun initHistoryFasting(fastingData: MutableList<Report>) {

        fastingReportAdapter =
            FastingHistoryAdapter(
                requireActivity(),
                fastingData
            )

        rvMain.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = fastingReportAdapter
        }

        if (fastingData.isNotEmpty()) {
            tvTitleHistoryFasting.visibility = View.VISIBLE

            val format = SimpleDateFormat(getString(R.string.fotmat_pattern_database))
            val todayDate = format.format(Date())
            var isAlreadyFill = false
            fastingData.forEach {
                Log.d("FragmentFirst", "today: $todayDate, it.Date : ${it.date}")
                if (it.date.equals(todayDate)) {
                    isAlreadyFill = true
                }
            }
            if (isAlreadyFill) layoutDailyReport.visibility = View.GONE
        } else {
            tvTitleHistoryFasting.visibility = View.GONE
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initDailyView(fastingData: List<Report>) {
        val date = Date()
        val format = SimpleDateFormat(getString(R.string.format_pattern_display))
        val todayDate = format.format(date)

        val dateTime = DateTime()
        val iso: Chronology = ISOChronology.getInstanceUTC()
        val hijri: Chronology = IslamicChronology.getInstanceUTC()

        val todayIso = LocalDate(dateTime.year, dateTime.monthOfYear, dateTime.dayOfMonth, iso)
        val todayHijri = LocalDate(todayIso.toDateTimeAtStartOfDay(), hijri).toString()
        val day = todayHijri.split("-")[2]
        val year = todayHijri.split("-")[0]

        val todayIslamic = "$day Ramadhan $year H"

        tvDateDaily.text = "$todayDate \n($todayIslamic)"

        initProgressView(todayIslamic, day.toInt(), fastingData)
    }

    @SuppressLint("SetTextI18n")
    private fun initProgressView(
        todayIslamic: String,
        day: Int,
        fastingData: List<Report>
    ) {
        tvTitleProgress.text = "Hari ini, $todayIslamic"
        val fastingLeft = 30 - day
        var fastringFail = 0
        var fastringFinish = 0

        fastingData.forEach {
            if (it.isFasting == 1) {
                fastringFinish++
            } else {
                fastringFail++
            }
        }

        pbFasting.progress = day

        tvProgressFinish.text = "$fastringFinish"
        tvProgressLeft.text = "$fastingLeft"
        tvProgressFail.text = "$fastringFail"
    }
}
