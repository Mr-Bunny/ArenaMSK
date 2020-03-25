package com.example.arenamsk.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.booking.PlaceBookingFragment
import com.example.arenamsk.ui.booking.PlaceBookingFragment.Companion.PLACE_BOOKING_ARG_TAG
import com.example.arenamsk.ui.place_detail.PlaceDetailFragment
import com.example.arenamsk.ui.place_detail.PlaceDetailFragment.Companion.PLACE_DETAIL_ARG_TAG
import com.example.arenamsk.utils.ActionEvent.OpenBookingEvent
import com.example.arenamsk.utils.ActionEvent.OpenCalendar
import com.example.arenamsk.utils.disable
import com.example.arenamsk.utils.enable
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_place_dialog.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class PlaceDialogFragment private constructor() : DialogFragment(), LifecycleOwner {

    companion object {
        const val DETAIL_FRAGMENT_TAG = "detail_fragment_tag"
        const val BOOKING_FRAGMENT_TAG = "booking_fragment_tag"
        const val PLACE_DIALOG_FRAGMENT_TAG = "base_dialog_fragment_tag"
        const val PLACE_DIALOG_FRAGMENT_ARG_TAG = "place_dialog_fragment_arg_tag"
        const val PLACE_DIALOG_OPEN_BOOKING_ARG_TAG = "place_dialog_open_booking_arg_tag"

        fun getInstance(
            place: PlaceModel? = null,
            openBooking: Boolean = false
        ): PlaceDialogFragment {
            place?.let {
                return PlaceDialogFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(PLACE_DIALOG_FRAGMENT_ARG_TAG, it)
                        putBoolean(PLACE_DIALOG_OPEN_BOOKING_ARG_TAG, openBooking)
                    }
                }
            }

            return PlaceDialogFragment()
        }
    }

    private var placeDetailFragment: PlaceDetailFragment? = null
    private var placeBookingFragment: PlaceBookingFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    //Переопределяем onBackPressed, он должен вызывать тот же метод, что и слушатель кнопки назад в тулбаре
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(activity!!, theme) {
            override fun onBackPressed() {
                backBtnPressed()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        place_detail_favourite_image_btn.setOnClickListener {
            //TODO add to favourites
        }

        place_detail_calendar_image_btn.setOnClickListener {
            EventBus.getDefault().post(OpenCalendar())
        }

        //Get Place from args if null dismiss fragment
        with(arguments?.getParcelable(PLACE_DIALOG_FRAGMENT_ARG_TAG)
            ?: PlaceModel().also { dismiss() }) {
            setupToolbar(this)

            createPlaceDetailFragment(this)
            createPlaceBookingFragment(this)

            if (arguments?.getBoolean(PLACE_DIALOG_OPEN_BOOKING_ARG_TAG, false) == true) {
                openPlaceBookingFragment(OpenBookingEvent(place = this))
            } else {
                openPlaceDetailFragment()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)

        super.onStop()
    }

    /** Открываем экран подробной информации */
    private fun openPlaceDetailFragment() {
        changeIcons(true)

        childFragmentManager.beginTransaction()
            .replace(R.id.place_dialog_container, placeDetailFragment!!, DETAIL_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
    }

    /** Открываем экран бронирования */
    @Subscribe
    fun openPlaceBookingFragment(event: OpenBookingEvent) {
        changeIcons(false)

        childFragmentManager.beginTransaction()
            .add(R.id.place_dialog_container, placeBookingFragment!!, BOOKING_FRAGMENT_TAG)
            .hide(placeDetailFragment!!)
            .addToBackStack(null)
            .commit()
    }

    private fun createPlaceDetailFragment(place: PlaceModel) {
        placeDetailFragment = PlaceDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(PLACE_DETAIL_ARG_TAG, place)
            }
        }
    }

    private fun createPlaceBookingFragment(place: PlaceModel) {
        placeBookingFragment = PlaceBookingFragment().apply {
            arguments = Bundle().apply {
                putParcelable(PLACE_BOOKING_ARG_TAG, place)
            }
        }
    }

    private fun changeIcons(isPlaceDetail: Boolean) {
        if (isPlaceDetail) {
            place_detail_calendar_image_btn.disable()
            place_detail_favourite_image_btn.enable()
        } else {
            place_detail_calendar_image_btn.enable()
            place_detail_favourite_image_btn.disable()
        }
    }

    private fun setupToolbar(place: PlaceModel) {
        place_detail_toolbar.setNavigationOnClickListener {
            backBtnPressed()
        }

        place_detail_app_bar.addOnOffsetChangedListener(object :
            AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout!!.totalScrollRange
                }

                if (scrollRange + verticalOffset == 0) {
                    place_detail_toolbar.title = place.placeTitle
                    isShow = true
                } else if (isShow) {
                    place_detail_toolbar.title = " "
                    isShow = false
                }
            }
        })

        place_detail_title.text = place.placeTitle
    }

    private fun backBtnPressed() {
        with(childFragmentManager) {
            if (fragments.size == 1) {
                super.dismiss()
            } else {
                fragments.forEach {
                    if (it.isVisible) {
                        when (it.tag) {
                            BOOKING_FRAGMENT_TAG -> {
                                changeIcons(true)
                                popBackStack()
                            }

                            DETAIL_FRAGMENT_TAG -> {
                                super.dismiss()
                            }
                        }
                    }
                }
            }
        }
    }
}