package com.example.arenamsk.ui.booking_accept

import android.app.Dialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.arenamsk.R
import com.example.arenamsk.network.models.BookingPlaceModel
import com.example.arenamsk.ui.booking.PlaceBookingViewModel
import com.example.arenamsk.utils.ActionEvent
import com.example.arenamsk.utils.EnumUtils
import kotlinx.android.synthetic.main.fragment_booking_accept.*
import org.greenrobot.eventbus.EventBus

class BookingAcceptDialogFragment private constructor() : DialogFragment(), LifecycleOwner {

    companion object {
        const val BOOKING_ACCEPT_TAG = "booking_accept"
        private const val ARG_TAG = "arg_tag"

        fun getInstance(
            bookingModel: BookingPlaceModel? = null
        ): BookingAcceptDialogFragment {
            bookingModel?.let {
                return BookingAcceptDialogFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_TAG, it)
                    }
                }
            }

            return BookingAcceptDialogFragment()
        }
    }

    private var toast: Toast? = null

    private val placeBookingViewModel by lazy {
        ViewModelProviders.of(this).get(PlaceBookingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(activity!!, theme) {
            override fun onBackPressed() {
                dismiss()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_booking_accept, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Подписываемся на статус бронирования
        placeBookingViewModel.getBookingStatusLiveData().observe(viewLifecycleOwner, Observer {
            when (it) {
                EnumUtils.BookingStatus.BOOKED -> {
                    EventBus.getDefault().post(ActionEvent.UpdateBookingList())
                    Toast.makeText(context, "Площадка забронирована", Toast.LENGTH_LONG).show()
                }

                else -> {
                    Toast.makeText(context, "Не удалось забронировать площадку", Toast.LENGTH_LONG).show()
                }
            }
        })

        placeBookingViewModel.getUserLiveData().observe(viewLifecycleOwner, Observer {
            accept_name_edit_text.getEditText().setText(it.firstName)
            accept_email_edit_text.getEditText().setText(if (it.email.isNullOrEmpty()) it.number else it.email)
        })

        btn_pay.setOnClickListener {
            val name = accept_name_edit_text.getEditText().text.toString()
            val emailPhone = accept_email_edit_text.getEditText().text.toString()

            if (name.isEmpty()) {
                showToast("Укажите имя")
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailPhone).matches() && !phoneIsCorrect(emailPhone)) {
                showToast("Укажите email/Телефон")
            } else {
                showToast("${arguments!!.getParcelable<BookingPlaceModel>(ARG_TAG)}")
            }
        }
    }

    private fun phoneIsCorrect(phone: String): Boolean {
        return phone.length == 11 && (phone.toIntOrNull() == null)
    }

    private fun showToast(msg: String) {
        toast?.cancel()
        toast = Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG)
        toast?.show()
    }
}