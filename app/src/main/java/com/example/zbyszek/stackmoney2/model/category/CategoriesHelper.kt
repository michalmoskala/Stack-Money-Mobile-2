package com.example.zbyszek.stackmoney2.model.category

class CategoriesHelper {
    companion object {
        fun getCategoriesWithSubCategoriesInExpenses(list: List<CategorySQL>): List<CategoryWithSubCategories> {
            val mainCategories = list.filter { it.parentCategoryId == null }
            val subCategoriesGroups = list
                    .filter { it.parentCategoryId != null }
                    .groupBy { it.parentCategoryId }
//

//            fun group(id: Long): List<CategorySQL>

            return mainCategories
                    .map { CategoryWithSubCategories(
                            it.convertToCategory(),
                            if (subCategoriesGroups[it.id] == null) arrayListOf() else ArrayList(subCategoriesGroups[it.id]!!.map { it.convertToCategory()} ))
                    }
//            return mainCategories.map { CategoryWithSubCategories(it.convertToCategory(), arrayListOf()) }
        }
    }
}