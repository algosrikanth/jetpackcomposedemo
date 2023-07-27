package com.square.employeedirectory.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET


interface EmployeeService {
    @GET("/sq-mobile-interview/employees.json")
    fun fetchEmployees(): Single<EmployeesList>
}

