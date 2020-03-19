package com.example.arenamsk.ui.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.arenamsk.R

abstract class BaseFragment(private val layoutId: Int): Fragment(), LifecycleOwner {

    private var toast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onDestroyView() {
        toast?.cancel()

        super.onDestroyView()
    }

    protected fun showToast(msg: String) {
        toast?.cancel()
        toast = Toast.makeText(context, msg, Toast.LENGTH_LONG).apply { show() }
    }

    protected fun showToast(msgId: Int) {
        toast?.cancel()
        toast = Toast.makeText(context, msgId, Toast.LENGTH_LONG).apply { show() }
    }

    protected fun showNetworkOfflineError() {
        showToast(R.string.network_offline_text)
    }

    protected fun openPhone(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phone")
        }
        startActivity(intent)
    }
}