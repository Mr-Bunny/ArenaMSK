package com.example.arenamsk.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

abstract class BaseFragment: Fragment(), LifecycleOwner {

    private var toast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    override fun onDestroyView() {
        toast?.cancel()

        super.onDestroyView()
    }

    abstract fun getLayout(): Int

    protected fun showToast(msg: String) {
        toast?.cancel()
        toast = Toast.makeText(context, msg, Toast.LENGTH_LONG).apply { show() }
    }
}