package com.example.zbyszek.stackmoney2.model.operation

import java.io.Serializable

/**
 * Created by Zbyszek on 1/2/2018.
 */
class Operation(
        override var id : Long,
        override var userId : Long,
        override var accountId : Long,
        override var categoryId : Long,
        override var title : String,
        override var cost : Int,
        override var isExpense : Boolean,
        override var visibleInStatistics : Boolean,
        override var description : String,
        override var date : String

):Serializable, IOperation {


}


