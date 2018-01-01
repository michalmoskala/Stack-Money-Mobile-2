package com.example.zbyszek.stackmoney2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.Fragment
import com.example.zbyszek.stackmoney2.R

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
        return inflater!!.inflate(R.layout.fragment_operations, container, false)
    }

}// Required empty public constructor
