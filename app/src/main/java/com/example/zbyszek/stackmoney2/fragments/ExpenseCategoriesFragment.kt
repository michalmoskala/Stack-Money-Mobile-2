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
import kotlinx.android.synthetic.main.fragment_expense_categories.view.*

class ExpenseCategoriesFragment : Fragment() {

    private var arrayList: ArrayList<CategoryWithSubCategories> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CategoryListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_expense_categories, container, false)

        view.recyclerview_expense_categories.layoutManager = linearLayoutManager
        view.recyclerview_expense_categories.adapter = adapter

        return view
    }

    companion object {
        fun newInstance(adapter: CategoryListAdapter, linearLayoutManager: LinearLayoutManager): ExpenseCategoriesFragment {
            val fragment = ExpenseCategoriesFragment()
            fragment.adapter = adapter
            fragment.linearLayoutManager = linearLayoutManager
            return fragment
        }
    }
}// Required empty public constructor