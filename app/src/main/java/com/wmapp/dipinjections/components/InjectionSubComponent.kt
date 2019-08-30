package com.wmapp.dipinjections.components

import com.wmapp.ui.cardetail.views.CarDetailsActivity
import com.wmapp.ui.home.views.HomeActivity
import com.wmapp.ui.splash.views.MainActivity
import dagger.Subcomponent

/**
 * Dagger sub component with multiple dependencies.
 */
@Subcomponent
interface InjectionSubComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(homeActivity: HomeActivity)

    fun inject(carDetailsActivity: CarDetailsActivity)


}