package com.square.employeedirectory.employee

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.square.employeedirectory.network.Employee
import com.square.employeedirectory.network.EmployeeRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class EmployeeModule {
    // This method will return the dependent object
    @Singleton
    @Provides
    fun provideEmployeeRepo(): EmployeeRepo {
        return EmployeeRepo()
    }
}