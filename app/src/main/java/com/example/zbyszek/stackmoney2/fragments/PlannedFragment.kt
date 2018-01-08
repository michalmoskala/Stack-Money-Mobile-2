package com.example.zbyszek.stackmoney2.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.adapters.PlannedOperationListAdapter
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.helpers.SuperFragment
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.operation.BindedOperation
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.fragment_planned.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PlannedFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PlannedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlannedFragment : SuperFragment() {

    lateinit var database : AppDatabase

    private var plannedArrayList: ArrayList<BindedOperation> = ArrayList()
    private lateinit var plannedAdapter: PlannedOperationListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_planned, container, false)
        databaseConnection()

        val fragment = this
        doAsync {
            val userId = Preferences.getUserId(context!!)
            val plannedList = database.operationDAO().getAllUserBindedOperationsOfPlanned(userId)
            plannedArrayList = ArrayList(plannedList)

            uiThread {
                view.recyclerview_planned_operations.layoutManager = LinearLayoutManager(activity)
                plannedAdapter = PlannedOperationListAdapter(plannedArrayList, fragment)
                view.recyclerview_planned_operations.adapter = plannedAdapter
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != Activity.RESULT_OK)
            return
        when(requestCode){
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
                val index = plannedArrayList.indexOfFirst { it.id == id }
                if (index != -1) {
                    plannedArrayList.removeAt(index)
                    plannedAdapter.notifyItemRemoved(index)
                }
            }
        }
    }

    private fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(context!!)
    }

}// Required empty public constructor
