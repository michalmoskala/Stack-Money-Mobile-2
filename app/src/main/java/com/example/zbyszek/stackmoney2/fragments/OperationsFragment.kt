package com.example.zbyszek.stackmoney2.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.activities.AddOperation
import com.example.zbyszek.stackmoney2.adapters.OperationListAdapter
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.helpers.SuperFragment
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.ResultCodes
import com.example.zbyszek.stackmoney2.model.operation.BindedOperation
import com.example.zbyszek.stackmoney2.model.operation.Operation
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.fragment_operations.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.joda.time.DateTime
import java.lang.Long.parseLong
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OperationsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OperationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        view.floatingActionButton_addOperation.setOnClickListener { addOperationButtonOnClick() }

        return view
    }

    fun addOperationButtonOnClick(){
        val intent = Intent(this.context, AddOperation::class.java)
        this.startActivityForResult(intent, RequestCodes.ADD)
    }

    private fun shiftActualMonth(offset: Int){
        actualDate = actualDate.minusMonths(-offset)
        onActualMonthChanged(view)
    }

    private fun setActualMonthTitle(view: View){
        val monthString = actualDate.toString("MMMM", Locale.forLanguageTag("pl-pl"))
        val year = actualDate.year
        view.month_name.text =  if (year != DateTime.now().year) monthString + " " + year
                                else monthString
    }

    private fun onActualMonthChanged(view: View?){
        setActualMonthTitle(view!!)
//        val month = actualDate.get(MONTH) + 1
//        val year = actualDate.get(YEAR)

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
                if(bindedOperation.date!!.startsWith(actualDate.toString("YYYY-MM"))){
                    operationsArrayList.add(0, bindedOperation)
                    operationsAdapter.notifyItemInserted(0)
                }
            }
            RequestCodes.EDIT -> {
                // TODO: Edit
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
                val index = operationsArrayList.indexOfFirst { it.id == id }
                if (index != -1) {
                    operationsArrayList.removeAt(index)
                    operationsAdapter.notifyItemRemoved(index)
                }
            }
        }
    }

    private fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(context!!)
    }
}// Required empty public constructor
