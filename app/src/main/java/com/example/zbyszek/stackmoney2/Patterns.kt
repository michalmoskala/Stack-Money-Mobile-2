package com.example.zbyszek.stackmoney2

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Patterns.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Patterns.newInstance] factory method to
 * create an instance of this fragment.
 */
class Patterns : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_patterns, container, false)
    }

}// Required empty public constructor
