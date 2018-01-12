package com.example.zbyszek.stackmoney2.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.model.statistics.SumByCategoryItem
import com.example.zbyszek.stackmoney2.model.statistics.SumByOperationTypeItem
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlinx.android.synthetic.main.fragment_statistics.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.joda.time.DateTime
import java.util.*

class StatisticsFragment : Fragment() {
    lateinit var database : AppDatabase

    lateinit var startDatePicker: DatePickerDialog
    lateinit var endDatePicker: DatePickerDialog

    var sumsByOperationType: List<SumByOperationTypeItem> = arrayListOf()
    var sumsByCategoryType: List<SumByCategoryItem> = arrayListOf()

    private fun DatePickerToDateTime(datePicker: DatePicker): DateTime{
        val day = datePicker.dayOfMonth
        val month = datePicker.month + 1
        val year = datePicker.year
        return DateTime(year, month, day, 0, 0)
    }

    private fun onStartDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val cal = DatePickerToDateTime(view)
        stats_start_date_input.setText(cal.toString("d MMMM YYYY", Locale.forLanguageTag("pl-PL")))
    }

    private fun onEndDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val cal = DatePickerToDateTime(view)
        stats_end_date_input.setText(cal.toString("d MMMM YYYY", Locale.forLanguageTag("pl-PL")))
    }

    private fun refreshData(){
        val startDate = DatePickerToDateTime(startDatePicker.datePicker).toString("YYYY-MM-dd", Locale.forLanguageTag("pl-PL"))
        val endDate = DatePickerToDateTime(endDatePicker.datePicker).toString("YYYY-MM-dd", Locale.forLanguageTag("pl-PL"))

        doAsync {
            sumsByOperationType = database
                    .statisticsDAO()
                    .getSumsByOperationType( Preferences.getUserId(context!!), startDate, endDate)

            sumsByCategoryType = database
                    .statisticsDAO()
                    .getSumsByCategory( Preferences.getUserId(context!!), startDate, endDate)

            uiThread {
                Toast.makeText(context, sumsByOperationType.toString(), Toast.LENGTH_SHORT).show()
                textView2.text = sumsByOperationType.map {"${if (it.isExpense) "Wydatki:" else "Wpływy:"} ${it.valueToString()}\n" }.toString()
                textView3.text = sumsByCategoryType.map { "${if (it.isExpense) "Wydatki:" else "Wpływy:"} ${it.category} ${it.subcategory?:""} ${it.valueToString()}\n" }.toString()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)
        databaseConnection()

        val dateEnd = DateTime.now()
        endDatePicker = DatePickerDialog(context, DatePickerDialog.OnDateSetListener(this::onEndDateSet), dateEnd.year, dateEnd.monthOfYear - 1, dateEnd.dayOfMonth)
        val dateStart = dateEnd.minusMonths(1)
        startDatePicker = DatePickerDialog(context, DatePickerDialog.OnDateSetListener(this::onStartDateSet), dateStart.year, dateStart.monthOfYear - 1, dateStart.dayOfMonth)

        view.stats_start_date_input.setText(dateStart.toString("d MMMM YYYY", Locale.forLanguageTag("pl-PL")))
        view.stats_end_date_input.setText(dateEnd.toString("d MMMM YYYY", Locale.forLanguageTag("pl-PL")))

        view.stats_start_date_input.setOnClickListener { startDatePicker.show() }
        view.stats_end_date_input.setOnClickListener { endDatePicker.show() }
        view.stats_button_refresh.setOnClickListener { refreshData() }
        refreshData()

        return view
    }

    private fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(context!!)
    }
}// Required empty public constructor
