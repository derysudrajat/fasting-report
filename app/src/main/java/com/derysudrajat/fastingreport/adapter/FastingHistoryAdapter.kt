package com.derysudrajat.fastingreport.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.derysudrajat.fastingreport.R
import com.derysudrajat.fastingreport.model.Report
import com.derysudrajat.fastingreport.room.ReportDatabase
import com.derysudrajat.fastingreport.ui.MainActivity
import kotlinx.android.synthetic.main.item_add_edit_report.view.*
import kotlinx.android.synthetic.main.item_report.view.*
import java.text.SimpleDateFormat

class FastingHistoryAdapter(
    val context: Context,
    val fastingList: MutableList<Report> = mutableListOf()
) : RecyclerView.Adapter<FastingHistoryAdapter.FastingHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FastingHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false)
        return FastingHistoryViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = fastingList.size

    override fun onBindViewHolder(holder: FastingHistoryViewHolder, position: Int) {
        val isFasting = fastingList[position].isFasting

        val format = SimpleDateFormat("dd-MM-yyyy")
        val format2 = SimpleDateFormat("d MMMM yyyy")
        val date = format.parse(fastingList[position].date)
        holder.itemView.tvDateReport.text = format2.format(date)

        if (isFasting == 1) {
            Glide.with(context)
                .load(R.drawable.ic_check)
                .into(holder.itemView.ivIndicatorFasting)
            holder.itemView.ivIndicatorFasting.backgroundTintList =
                context.resources.getColorStateList(
                    R.color.colorHoloGreen_Alpha
                )
            holder.itemView.ivIndicatorFasting.imageTintList =
                context.resources.getColorStateList(android.R.color.holo_green_dark)
            holder.itemView.tvDescReport.text = "Anda berpuasa pada hari ini"
        } else {
            Glide.with(context)
                .load(R.drawable.ic_clear)
                .into(holder.itemView.ivIndicatorFasting)
            holder.itemView.ivIndicatorFasting.backgroundTintList =
                context.resources.getColorStateList(
                    R.color.colorHoloRed_Alpha
                )
            holder.itemView.ivIndicatorFasting.imageTintList =
                context.resources.getColorStateList(android.R.color.holo_red_dark)
            holder.itemView.tvDescReport.text = "Anda tidak berpuasa pada hari ini"
        }

        holder.itemView.contentReport.setOnClickListener {
            val alertDialog: AlertDialog? = context.let {
                val builder = AlertDialog.Builder(it)
                val view = LayoutInflater.from(context).inflate(R.layout.item_add_edit_report, null)
                builder.setView(view)
                    .setTitle("Edit Data")
                    .setPositiveButton("Ya"
                    ) { dialog, which ->
                        val report = Report(fastingList[position].date, 1)
                        ReportDatabase.getDatabase(context).reporDao().updateReport(report)
                        dialog.dismiss()

                    }
                    .setNegativeButton("Tidak"
                    ) { dialog, which ->
                        val report = Report(fastingList[position].date, 0)
                        ReportDatabase.getDatabase(context).reporDao().updateReport(report)
                        dialog.dismiss()
                    }
                view.tvDateDialog.text = format2.format(date)
                builder.create()
            }
            alertDialog?.show()
        }
    }

    fun clear() {
        val size = fastingList.size
        fastingList.clear()
        notifyItemRangeRemoved(0, size)
        notifyDataSetChanged()
    }

    class FastingHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view)
}