package com.arenabooking.arenamsk.ui.booking_accept

import android.app.Dialog
import android.content.Intent
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
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.network.models.BookingPlaceModel
import com.arenabooking.arenamsk.ui.booking.PlaceBookingViewModel
import com.arenabooking.arenamsk.ui.webview.WebActivity
import com.arenabooking.arenamsk.utils.ActionEvent
import kotlinx.android.synthetic.main.fragment_booking_accept.*
import org.greenrobot.eventbus.EventBus

/** Экран с вводом имени и email/телефона и кнопкой оплаты, которая открывает сбер
 * после успешного запроса на бронирование возвращается ссылка на оплату
 * которую мы открываем в WebActivity */
class BookingAcceptDialogFragment private constructor() : DialogFragment(), LifecycleOwner {

    companion object {
        const val BOOKING_ACCEPT_TAG = "booking_accept"
        private const val ARG_TAG = "arg_tag"

        fun getInstance(
            bookingModel: BookingPlaceModel
        ): BookingAcceptDialogFragment {
            return BookingAcceptDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_TAG, bookingModel)
                }
            }
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
        return object : Dialog(requireActivity(), theme) {
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
            if (it.isNullOrEmpty()) {
                Toast.makeText(context, "Не удалось забронировать площадку", Toast.LENGTH_LONG).show()
            } else {
                requireActivity().startActivity(Intent(requireContext(), WebActivity::class.java).apply { putExtra("url", it) })

                //Если площадка забронирована для проведения оплаты - посылаем сигнал что нужно обновить экран с временм для бронирвоания
                //а так же открываем webActivity для проведения оплаты
                EventBus.getDefault().post(ActionEvent.UpdateBookingList())
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
                showToast("Укажите корректный email/Телефон")
            } else {
                arguments!!.getParcelable<BookingPlaceModel>(ARG_TAG)?.let {
                    it.name = name
                    //Если введен email сохраняем его, иначе сохраняем телефон
                    if (Patterns.EMAIL_ADDRESS.matcher(emailPhone).matches()) {
                        it.email = emailPhone
                    } else {
                        it.phone = emailPhone
                    }
                    placeBookingViewModel.bookPlace(it)
                } ?: showToast("Не удалось получить время для бронирования, пожалуйста, выберите его еще раз")
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