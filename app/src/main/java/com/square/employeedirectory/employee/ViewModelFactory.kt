package com.square.employeedirectory.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == EmployeeViewModel::class.java) {
            return EmployeeViewModel() as T
        }
        throw IllegalArgumentException("unknown model class $modelClass")
    }

    companion object {
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory {
            if (INSTANCE == null) {
                INSTANCE = ViewModelFactory()
            }
            return INSTANCE!!
        }
    }
}