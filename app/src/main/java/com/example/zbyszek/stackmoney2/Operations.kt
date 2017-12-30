package com.example.zbyszek.stackmoney2

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.Fragment

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Operations.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Operations.newInstance] factory method to
 * create an instance of this fragment.
 */
class Operations : Fragment() {



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_operations, container, false)
    }

}// Required empty public constructor
