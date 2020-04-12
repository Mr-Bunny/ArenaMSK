package com.example.arenamsk.ui.booking

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arenamsk.R
import com.example.arenamsk.models.DateModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.network.models.BookingDateModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.booking.adapter.PlaceBookingAdapter
import com.example.arenamsk.utils.ActionEvent.OpenCalendar
import com.example.arenamsk.utils.TimeUtils
import com.example.arenamsk.utils.disable
import com.example.arenamsk.utils.enable
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import kotlinx.android.synthetic.main.fragment_place_booking.*
import org.angmarch.views.OnSpinnerItemSelectedListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

class PlaceBookingFragment : BaseFragment(R.layout.fragment_place_booking), DatePickerDialog.OnDateSetListener {

    companion object {
        const val PLACE_BOOKING_TAG = "place_booking_tag"
        const val PLACE_BOOKING_ARG_TAG = "place_booking_arg_tag"
    }

    private val placeBookingAdapter by lazy { PlaceBookingAdapter(::itemClickCallback) }

    private val placeBookingViewModel by lazy {
        ViewModelProviders.of(this).get(PlaceBookingViewModel::class.java)
    }

    private val place by lazy { arguments?.getParcelable(PLACE_BOOKING_ARG_TAG) ?: PlaceModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Проверяем количество площадок у выбранного места, если их 2 и больше - показываем выпадающий список для выбора площадки
        if (place.playgroundModels.size > 1) {
            booking_spinner.enable()
            booking_spinner_playground_type.attachDataSource(getPlaygroundList())
            booking_spinner_playground_type.onSpinnerItemSelectedListener =
                OnSpinnerItemSelectedListener { _, _, position, _ ->
                    showProgressBar()
                    placeBookingViewModel.loadBookingData(getPlaygroundIdByPosition(position))
                }
        }

        //Подписываемся на LiveData с выбранной датой, при ее изменении обновляем UI и подгружаем новое расписание
        placeBookingViewModel.getCurrentDateLiveData().observe(viewLifecycleOwner, Observer {
            showProgressBar()
            placeBookingViewModel.loadBookingData(place.playgroundModels[0].id)
            updateDates(it)
        })

        initRecycler()

        placeBookingViewModel.getPlaceBookingLiveData().observe(this, Observer { list ->
            val sorted = list.filter { it.isHalfBooking == booking_half_field_check_box.isChecked }

            if (sorted.isNullOrEmpty()) {
                showError()
            } else {
                showRecycler()
                placeBookingAdapter.setNewList(sorted)
            }
        })

        //Выбрали следующий день
        booking_date_next.setOnClickListener {
            placeBookingViewModel.setNextDate()
        }

        booking_half_field_check_box.setOnCheckedChangeListener { _, isChecked ->
            val list = placeBookingViewModel.getPlaceBookingLiveData().value ?: emptyList<BookingDateModel>()
            val sorted = list.filter { it.isHalfBooking == isChecked }

            if (sorted.isNullOrEmpty()) {
                showError()
            } else {
                showRecycler()
                placeBookingAdapter.setNewList(sorted)
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

    /** Вызывается после выбора даты через DatePicker */
    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        placeBookingViewModel.setCurrentDate(DateModel(
            year = year,
            month = monthOfYear,
            day = dayOfMonth
        ))
    }

    @Subscribe
    fun openCalendar(event: OpenCalendar) {
        val minDateModel = TimeUtils.getCurrentDateModel("")
        val defaultDateModel = TimeUtils.getCurrentDateModel(placeBookingViewModel.getCurrentDateLiveData().value ?: "")

        SpinnerDatePickerDialogBuilder()
            .context(requireContext())
            .callback(this)
            .spinnerTheme(R.style.NumberPickerStyle)
            .showTitle(true)
            .showDaySpinner(true)
            .defaultDate(defaultDateModel.year, defaultDateModel.month, defaultDateModel.day)
            .minDate(minDateModel.year, minDateModel.month, minDateModel.day)
            .build()
            .show()
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

    /** Метод возвращает список площадок (вида спорта) открытой площадки (места) */
    private fun getPlaygroundList(): List<String> {
        return mutableListOf<String>().apply {
            for (place in place.playgroundModels) {
                add(place.sport?.name ?: "")
            }
        }
    }

    /** Возвращаем id площадки на основе позиции в спинере */
    private fun getPlaygroundIdByPosition(position: Int) = place.playgroundModels[position].id

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

    /** Показываем загрузку */
    private fun showProgressBar() {
        booking_error_text.disable()
        btn_book.disable()
        recycler_place_booking.disable()
        booking_progress_bar.enable()
    }

    /** Скрываем загрузку */
    private fun hideProgressBar() {
        booking_progress_bar.disable()
    }

    /** Показываем текст что ничего не нашлось */
    private fun showError() {
        hideProgressBar()
        btn_book.disable()
        recycler_place_booking.disable()
        booking_error_text.enable()
    }

    /** Показываем recycler с кнопкой бронирования */
    private fun showRecycler() {
        hideProgressBar()
        booking_error_text.disable()
        btn_book.enable()
        recycler_place_booking.enable()
    }
}
