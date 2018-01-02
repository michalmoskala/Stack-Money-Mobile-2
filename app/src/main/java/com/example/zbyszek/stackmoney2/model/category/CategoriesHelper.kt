package com.example.zbyszek.stackmoney2.model.category

class CategoriesHelper {
    companion object {
        fun getCategoriesWithSubCategories(list: List<BindedCategorySQL>): List<CategoryWithSubCategories> {
            val mainCategories = list.filter { it.parentCategoryId == null }
            val subCategoriesGroups = list
                    .filter { it.parentCategoryId != null }
                    .groupBy { it.parentCategoryId }

            return mainCategories
                    .map { CategoryWithSubCategories(
                            it.convertToCategory(),
                            if (subCategoriesGroups[it.id] == null) arrayListOf() else ArrayList(subCategoriesGroups[it.id]!!.map { it.convertToCategory()} ))
                    }
        }

        fun getCategoriesWithSubCategoriesInExpenses(list: List<BindedCategorySQL>): List<CategoryWithSubCategories> {
            val mainCategories = list.filter { it.parentCategoryId == null && it.visibleInExpenses }
            val subCategoriesGroups = list
                    .filter { it.parentCategoryId != null && it.visibleInExpenses }
                    .groupBy { it.parentCategoryId }

            return mainCategories
                    .map { CategoryWithSubCategories(
                            it.convertToCategory(),
                            if (subCategoriesGroups[it.id] == null) arrayListOf() else ArrayList(subCategoriesGroups[it.id]!!.map { it.convertToCategory()} ))
                    }
        }

        fun getCategoriesWithSubCategoriesInIncomes(list: List<BindedCategorySQL>): List<CategoryWithSubCategories> {
            val mainCategories = list.filter { it.parentCategoryId == null && it.visibleInIncomes }
            val subCategoriesGroups = list
                    .filter { it.parentCategoryId != null && it.visibleInIncomes }
                    .groupBy { it.parentCategoryId }

            return mainCategories
                    .map { CategoryWithSubCategories(
                            it.convertToCategory(),
                            if (subCategoriesGroups[it.id] == null) arrayListOf() else ArrayList(subCategoriesGroups[it.id]!!.map { it.convertToCategory()} ))
                    }
        }
    }
}