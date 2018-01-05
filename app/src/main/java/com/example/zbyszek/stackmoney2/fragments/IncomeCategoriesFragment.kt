package com.example.zbyszek.stackmoney2.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zbyszek.stackmoney2.R
import com.example.zbyszek.stackmoney2.adapters.CategoryListAdapter
import com.example.zbyszek.stackmoney2.model.category.CategoryWithSubCategories
import kotlinx.android.synthetic.main.fragment_income_categories.*
import kotlinx.android.synthetic.main.fragment_income_categories.view.*

class IncomeCategoriesFragment : Fragment() {

    private var arrayList: ArrayList<CategoryWithSubCategories> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CategoryListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_income_categories, container, false)

//        linearLayoutManager = LinearLayoutManager(context)
        view.recyclerview_income_categories.layoutManager = linearLayoutManager
        view.recyclerview_income_categories.adapter = adapter

        return view
    }

    companion object {
        fun newInstance(adapter: CategoryListAdapter, linearLayoutManager: LinearLayoutManager): IncomeCategoriesFragment {
            val fragment = IncomeCategoriesFragment()
            fragment.adapter = adapter
            fragment.linearLayoutManager = linearLayoutManager
            return fragment
        }
    }
}// Required empty public constructor