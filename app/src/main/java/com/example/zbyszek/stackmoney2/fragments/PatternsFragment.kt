package com.example.zbyszek.stackmoney2.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zbyszek.stackmoney2.R


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PatternsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PatternsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PatternsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patterns, container, false)
    }

}// Required empty public constructor
