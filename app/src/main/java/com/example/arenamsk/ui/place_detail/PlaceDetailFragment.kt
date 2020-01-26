package com.example.arenamsk.ui.place_detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceModel
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_place_detail.*

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
        //Get Place from args if null dismiss fragment
        val place: PlaceModel = arguments?.getParcelable(PLACE_DETAIL_ARG_TAG) ?: PlaceModel().also { dismiss() }

        place_detail_toolbar.setNavigationOnClickListener { this@PlaceDetailFragment.dismiss() }

        place_detail_app_bar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout!!.totalScrollRange
                }

                if (scrollRange + verticalOffset == 0) {
                    place_detail_toolbar.title = "Теннисный корт"
                    isShow = true
                } else if(isShow) {
                    place_detail_toolbar.title = " "
                    isShow = false
                }
            }
        })

        place_detail_title.text = place.title
    }
}