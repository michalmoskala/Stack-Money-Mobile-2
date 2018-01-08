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
import com.example.zbyszek.stackmoney2.adapters.OperationPatternListAdapter
import com.example.zbyszek.stackmoney2.helpers.Preferences
import com.example.zbyszek.stackmoney2.helpers.SuperFragment
import com.example.zbyszek.stackmoney2.model.RequestCodes
import com.example.zbyszek.stackmoney2.model.operationPattern.BindedOperationPattern
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import kotlinx.android.synthetic.main.fragment_patterns.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.onUiThread
import org.jetbrains.anko.support.v4.uiThread
import java.lang.Long


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PatternsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PatternsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PatternsFragment : SuperFragment() {

    lateinit var database : AppDatabase

    private var patternsArrayList: ArrayList<BindedOperationPattern> = ArrayList()
    private lateinit var patternsAdapter: OperationPatternListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_patterns, container, false)
        databaseConnection()

        val fragment = this
        doAsync {
            val userId = Preferences.getUserId(context!!)
            val patternsList = database.operationPatternDAO().getAllUserBindedOperationPatterns(userId)
            patternsArrayList = ArrayList(patternsList)

            onUiThread {
                view.recyclerview_operation_patterns.layoutManager = LinearLayoutManager(activity)
                patternsAdapter = OperationPatternListAdapter(patternsArrayList, fragment)
                view.recyclerview_operation_patterns.adapter = patternsAdapter
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != Activity.RESULT_OK)
            return
        when(requestCode){
            RequestCodes.ADD -> {
                val bindedOperationPattern = data.getSerializableExtra("new_operation_pattern") as BindedOperationPattern
                patternsArrayList.add(0, bindedOperationPattern)
                patternsAdapter.notifyItemInserted(0)
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
                    database.operationPatternDAO().deleteOperationPattern(Preferences.getUserId(context!!), id)
                }
                val index = patternsArrayList.indexOfFirst { it.id == id }
                if (index != -1) {
                    patternsArrayList.removeAt(index)
                    patternsAdapter.notifyItemRemoved(index)
                }
            }
        }
    }

    private fun databaseConnection(){
        database = AppDatabase.getInMemoryDatabase(context!!)
    }
}// Required empty public constructor
