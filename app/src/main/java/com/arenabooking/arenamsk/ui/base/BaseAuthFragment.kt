package com.arenabooking.arenamsk.ui.base

import com.arenabooking.arenamsk.ui.AuthActivity

/** Базовый класс фрагментов авторизации */
abstract class BaseAuthFragment(layoutId: Int) : BaseFragment(layoutId) {

    protected fun openApp(activity: AuthActivity) {
        activity.openApp()
        activity.finish()
    }
}