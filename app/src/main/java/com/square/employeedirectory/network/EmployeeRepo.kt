package com.square.employeedirectory.network

import io.reactivex.rxjava3.core.Single

open class EmployeeRepo {
    var service: EmployeeService =
        RetrofitClient.getClient().create(EmployeeService::class.java)

    fun getEmployees(): Single<EmployeesList> {
        return service.fetchEmployees()
    }
}

