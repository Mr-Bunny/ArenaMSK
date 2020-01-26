package com.example.arenamsk.ui.place_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceModel

class PlaceDetailFragment private constructor() : DialogFragment(), LifecycleOwner {

    companion object {
        const val PLACE_DETAIL_TAG = "place_detail_tag"
        private const val PLACE_DETAIL_ARG_TAG = "place_detail_arg_tag"

        fun getInstance(place: PlaceModel? = null): PlaceDetailFragment {
            place?.let {
                return PlaceDetailFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(PLACE_DETAIL_ARG_TAG, it)
                    }
                }
            }

            return PlaceDetailFragment()
        }
    }

    private val placeFilterModel by lazy {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_place_detail,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI()
    }

    private fun updateUI() {

    }
}