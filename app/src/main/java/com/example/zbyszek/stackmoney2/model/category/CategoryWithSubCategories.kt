package com.example.zbyszek.stackmoney2.model.category

import android.support.v7.util.SortedList

class CategoryWithSubCategories(var category: ICategory, var subCategories: ArrayList<ICategory>): Comparable<CategoryWithSubCategories> {
    override fun compareTo(other: CategoryWithSubCategories) = category.name.compareTo(other.category.name)
}