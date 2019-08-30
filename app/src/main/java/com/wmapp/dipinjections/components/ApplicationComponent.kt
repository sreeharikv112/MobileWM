package com.wmapp.dipinjections.components

import com.wmapp.dipinjections.customscope.ApplicationScope
import com.wmapp.dipinjections.modules.ApplicationModule
import com.wmapp.dipinjections.modules.NetworkModule
import dagger.Component

/**
 * Dagger main app component
 */
@ApplicationScope
@Component(
    modules = arrayOf(ApplicationModule::class, NetworkModule::class)
)
interface ApplicationComponent {

    fun newInjectionComponent():InjectionSubComponent
}