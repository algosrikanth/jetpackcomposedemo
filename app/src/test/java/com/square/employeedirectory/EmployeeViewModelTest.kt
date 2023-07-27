package com.square.employeedirectory

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.square.employeedirectory.employee.EmployeeViewModel
import com.square.employeedirectory.network.Employee
import com.square.employeedirectory.network.EmployeeRepo
import com.square.employeedirectory.network.EmployeeService
import com.square.employeedirectory.network.EmployeesList
import io.reactivex.rxjava3.core.Single

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class EmployeeViewModelTest {

    val mockViewModel =  mock(EmployeeViewModel::class.java)
    val mockRepo = mock(EmployeeRepo::class.java)
    val service = mock(EmployeeService::class.java)

    @Test
    fun testGetEmployeeListError(){
        mockRepo.service = service
        `when`(mockRepo.getEmployees()).thenReturn(Single.just(EmployeesList(mutableListOf())))
        mockViewModel.employeeList = mutableStateListOf<Employee>()
        mockViewModel._isLoading =  MutableStateFlow(false)
        mockViewModel.showError =  mutableStateOf(true)
        mockViewModel.isLoading = mockViewModel._isLoading.asStateFlow()
        mockViewModel.getEmployeeList()
        Assert.assertEquals(true, mockViewModel.showError.value)
    }
}