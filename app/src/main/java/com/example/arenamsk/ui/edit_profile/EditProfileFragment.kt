package com.example.arenamsk.ui.edit_profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arenamsk.R
import com.example.arenamsk.models.DateModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.network.models.BookingDateModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.booked.adapter.BookedViewPagerAdapter
import com.example.arenamsk.ui.booking.adapter.PlaceBookingAdapter
import com.example.arenamsk.utils.ActionEvent.OpenCalendar
import com.example.arenamsk.utils.TimeUtils
import com.example.arenamsk.utils.disable
import com.example.arenamsk.utils.enable
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import kotlinx.android.synthetic.main.fragment_booked.*
import kotlinx.android.synthetic.main.fragment_place_booking.*
import org.angmarch.views.OnSpinnerItemSelectedListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*
import kotlin.collections.HashSet

class EditProfileFragment : BaseFragment(R.layout.fragment_edit_profile) {

    private val dispatcher by lazy { requireActivity().onBackPressedDispatcher }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setupTopPadding(booked_tab_layout)

    }
}
