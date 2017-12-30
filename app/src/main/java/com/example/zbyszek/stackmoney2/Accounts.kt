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
 * [Accounts.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Accounts.newInstance] factory method to
 * create an instance of this fragment.
 */
class Accounts : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_accounts, container, false)
    }

}// Required empty public constructor
