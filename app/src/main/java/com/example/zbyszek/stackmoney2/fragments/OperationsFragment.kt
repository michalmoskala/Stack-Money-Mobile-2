package com.example.zbyszek.stackmoney2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.Fragment
import android.content.Intent
import android.widget.Toast
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.activities.AddOperation
import com.example.zbyszek.stackmoney2.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_operations.view.*
import org.jetbrains.anko.startActivity
import android.app.Activity.RESULT_OK



/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OperationsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OperationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OperationsFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment




        val view = inflater!!.inflate(R.layout.fragment_operations, container, false)
        view.floatingActionButton_addOperation.setOnClickListener {
            val intent: Intent = Intent(context, AddOperation::class.java)
            startActivity(intent)

        }
        return view

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        val data = data.getStringExtra("data")


    }

}// Required empty public constructor
