package com.example.zbyszek.stackmoney2.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.activities.AddOperation
import com.example.zbyszek.stackmoney2.adapters.OperationListAdapter
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.helpers.SuperFragment
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.operation.BindedOperation
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.fragment_operations.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

class OperationsFragment : SuperFragment() {

    lateinit var database : AppDatabase

    private var operationsArrayList: ArrayList<BindedOperation> = ArrayList()
    private lateinit var operationsAdapter: OperationListAdapter

    private var actualDate = DateTime.now()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_operations, container, false)

        view.recyclerview_operations.layoutManager = LinearLayoutManager(activity)
        operationsAdapter = OperationListAdapter(operationsArrayList, this)
        view.recyclerview_operations.adapter = operationsAdapter

        databaseConnection()
        onActualMonthChanged(view)

        view.prev_month_button.setOnClickListener { shiftActualMonth(-1) }
        view.next_month_button.setOnClickListener { shiftActualMonth(1) }
        view.action_add_operation.setOnClickListener { addOperationButtonOnClick() }
        view.action_add_transaction.setOnClickListener { addTransactionButtonOnClick() }

        return view
    }

    private fun addOperationButtonOnClick(){
        val intent = Intent(this.context, AddOperation::class.java)
        intent.action = RequestCodes.ADD.toString()
        this.startActivityForResult(intent, RequestCodes.ADD)
    }

    private fun addTransactionButtonOnClick(){
        // TODO: Transaction activity
    }

    private fun shiftActualMonth(offset: Int){
        actualDate = actualDate.minusMonths(-offset)
        onActualMonthChanged(view)
    }

    private fun setActualMonthTitle(view: View){
        val monthString = SimpleDateFormat(
                "LLLL",
                Locale.forLanguageTag("pl-PL"))
                .format(actualDate.toCalendar(Locale.forLanguageTag("pl-pl")).time)
//        var datetimeformat = DateTimeFormat.patternForStyle("L", Locale.forLanguageTag("pl-pl"))
//        val monthString = actualDate.toString("MMMM", Locale.forLanguageTag("pl-PL"))
        val year = actualDate.year
        view.month_name.text =  if (year != DateTime.now().year) monthString + " " + year
                                else monthString
    }

    private fun onActualMonthChanged(view: View?){
        setActualMonthTitle(view!!)

        doAsync {
            val userId = Preferences.getUserId(context!!)
            val operations = database.operationDAO().getAllUserBindedOperationsOfCertainMonth(userId, "%02d".format(actualDate.monthOfYear), "%04d".format(actualDate.year))

            uiThread {
                operationsArrayList.clear()
                operationsArrayList.addAll(operations)
                operationsAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != Activity.RESULT_OK)
            return
        when(requestCode){
            RequestCodes.ADD -> {
                val bindedOperation = data.getSerializableExtra("new_operation") as BindedOperation
                addOperation(bindedOperation)
            }
            RequestCodes.EDIT -> {
                val bindedOperation = data.getSerializableExtra("edited_operation") as BindedOperation
                deleteOperation(bindedOperation.id)
                addOperation(bindedOperation)
            }
        }
    }

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: String) {
        if (resultCode != Activity.RESULT_OK )
            return
        when (requestCode) {
            RequestCodes.DELETE -> {
                val id = data.trim().toLong()
                doAsync {
                    database.operationDAO().deleteOperation(Preferences.getUserId(context!!), id)
                }
                deleteOperation(id)
            }
        }
    }

    private fun addOperation(operation: BindedOperation){
        // TODO: same month but future
        if(operation.date!!.startsWith(actualDate.toString("YYYY-MM"))){
            operationsArrayList.add(0, operation)
            operationsAdapter.notifyItemInserted(0)
        }
    }

    private fun deleteOperation(id: Long): Boolean {
        val index = operationsArrayList.indexOfFirst { it.id == id }
        if (index != -1) {
            operationsArrayList.removeAt(index)
            operationsAdapter.notifyItemRemoved(index)
        }
        return index != -1
    }

    private fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(context!!)
    }
}// Required empty public constructor
