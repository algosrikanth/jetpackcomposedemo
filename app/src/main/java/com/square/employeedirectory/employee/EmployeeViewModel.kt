package com.square.employeedirectory.employee

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.square.employeedirectory.network.Employee
import com.square.employeedirectory.network.EmployeeRepo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class EmployeeViewModel : ViewModel() {
    var _isLoading = MutableStateFlow(false)
    var isLoading = _isLoading.asStateFlow()
    var showError = mutableStateOf(false)
    var employeeList = mutableStateListOf<Employee>()
    val noEmployeesFound = mutableStateOf(false)
    lateinit var disposable: Disposable
    lateinit var repo: EmployeeRepo
    fun init(repo: EmployeeRepo) {
        this.repo = repo
    }

    fun getEmployeeList() {
        try {
            viewModelScope.launch {
                _isLoading.value = true
            }
            disposable =
                repo.getEmployees()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        _isLoading.value = false
                        employeeList.clear()
                        if (result.employees.isEmpty()) {
                            noEmployeesFound.value = true
                        } else {
                            result.employees.sortedBy { it.fullName }
                            employeeList.addAll(result.employees)
                        }
                    },
                        { error ->
                            employeeList.clear()
                            _isLoading.value = false
                            showError.value = true
                        })
        } catch (Ex: Exception) {
            employeeList.clear()
            _isLoading.value = false
            showError.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposable?.isDisposed!!) {
            disposable.dispose()
        }
    }
}