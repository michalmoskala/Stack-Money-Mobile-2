package com.example.zbyszek.stackmoney2.helpers

import android.content.Intent

interface ISuperFragment {
    fun onDialogResult(requestCode: Int, resultCode: Int, data: String){
    }
}