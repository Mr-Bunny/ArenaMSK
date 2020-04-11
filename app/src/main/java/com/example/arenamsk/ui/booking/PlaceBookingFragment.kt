package com.example.arenamsk.ui.booking

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arenamsk.R
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.booking.adapter.PlaceBookingAdapter
import com.example.arenamsk.utils.ActionEvent.OpenCalendar
import com.example.arenamsk.utils.TimeUtils
import kotlinx.android.synthetic.main.fragment_place_booking.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class PlaceBookingFragment : BaseFragment(R.layout.fragment_place_booking) {

    companion object {
        const val PLACE_BOOKING_TAG = "place_booking_tag"
        const val PLACE_BOOKING_ARG_TAG = "place_booking_arg_tag"
    }

    private val placeBookingAdapter by lazy { PlaceBookingAdapter(::itemClickCallback) }

    private val placeBookingViewModel by lazy {
        ViewModelProviders.of(this).get(PlaceBookingViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placeBookingViewModel.getCurrentDateLiveData().observe(viewLifecycleOwner, Observer {
            updateDates(it)
        })

        initRecycler()

        placeBookingViewModel.getPlaceBookingLiveData().observe(this, Observer {
            placeBookingAdapter.setNewList(it)
        })

        booking_date_next.setOnClickListener {
            placeBookingViewModel.setNextDate()
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

    @Subscribe
    fun openCalendar(event: OpenCalendar) {
        Toast.makeText(requireContext(), TimeUtils.getCurrentDay(), Toast.LENGTH_SHORT).show()
    }

    private fun initRecycler() {
        with(recycler_place_booking) {
            setHasFixedSize(true)
            adapter = placeBookingAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
    }

    private fun itemClickCallback() {
        //TODO сохранять кликнутый item
    }

    /** Сохраняем в нужнгом формате текущую дату, выбранную дату, которая изначально равна текущей
     * @param currentDate - Текущий выбранный день */
    private fun updateDates(currentDate: String) {
        booking_date_string.text = TimeUtils.getCurrentDayToDisplay(currentDate)
        updateNextBtnText(currentDate)
    }

    /** Обновляем дату у кнопки отображения расписания за следующий день
     * @param currentDate - Текущий выбранный день */
    private fun updateNextBtnText(currentDate: String) {
        booking_date_next_text.text = TimeUtils.getNextDayToDisplay(currentDate)
    }

}
