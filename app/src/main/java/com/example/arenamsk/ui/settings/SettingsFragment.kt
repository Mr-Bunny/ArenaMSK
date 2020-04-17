package com.example.arenamsk.ui.settings

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.example.arenamsk.R
import com.example.arenamsk.network.models.ApiError
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.ui.base.BaseFragment

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val dispatcher by lazy { requireActivity().onBackPressedDispatcher }

    private val errorHandler = object : RequestErrorHandler {
        override suspend fun networkUnavailableError() {
            showToast("Нет соединения с интернетом")
        }

        override suspend fun requestFailedError(error: ApiError?) {
        }

        override suspend fun timeoutException() {
        }

        override suspend fun requestSuccessButResponseIsNull() {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}
