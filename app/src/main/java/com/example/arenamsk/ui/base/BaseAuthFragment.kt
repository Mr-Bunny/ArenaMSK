package com.example.arenamsk.ui.base

import com.example.arenamsk.ui.AuthActivity

abstract class BaseAuthFragment(layoutId: Int) : BaseFragment(layoutId) {

    protected fun openApp(activity: AuthActivity) {
        activity.openApp()
        activity.finish()
    }
}