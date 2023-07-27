package com.square.employeedirectory.employee

import com.square.employeedirectory.EmployeeScreen
import dagger.Component
import javax.inject.Singleton


// Definition of a Dagger subcomponent
@Singleton
@Component(modules = [EmployeeModule::class])
interface EmployeeComponent {
    // Classes that can be injected by this Component
    fun inject(activity: EmployeeScreen)
}


