package com.example.arenamsk.ui.place_detail_info

import android.os.Bundle
import android.view.View
import com.example.arenamsk.R
import com.example.arenamsk.custom_view.AdditionalInfoView
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.place_detail.PlaceDetailFragment.Companion.PLACE_DETAIL_ARG_TAG
import com.example.arenamsk.utils.ActionEvent
import com.example.arenamsk.utils.TimeUtils
import kotlinx.android.synthetic.main.fragment_filter_content.*
import kotlinx.android.synthetic.main.fragment_place_info.*
import org.greenrobot.eventbus.EventBus

/** Фрагмент с подробной информацией о площадке */
class PlaceDetailInfoFragment private constructor() : BaseFragment(R.layout.fragment_place_info) {

    companion object {
        fun getInstance(place: PlaceModel? = null): PlaceDetailInfoFragment {
            place?.let {
                return PlaceDetailInfoFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(PLACE_DETAIL_ARG_TAG, it)
                    }
                }
            }


            return PlaceDetailInfoFragment()
        }
    }

    private val place: PlaceModel by lazy { arguments?.getParcelable(PLACE_DETAIL_ARG_TAG) ?: PlaceModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Подразумевается, что на pre-lolipop девайсах маленький экран и соответственно мы отображаем только одну колонку
        additional_info_container.columnCount = if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) 1 else 2

        //Открываем экран бронирования
        place_detail_info_btn_book.setOnClickListener { openBookingScreen(place) }

        setupPlaceInfo()
    }

    /** Отображаем информацию о площадке в ui */
    private fun setupPlaceInfo() {
        work_time_text_view.text = TimeUtils.convertWorkTime(place.workDayStartAt, place.workDayEndAt)
        total_area_text_view.text = requireContext().resources.getString(R.string.place_total_area, place.totalArea.toString())
        number_of_places_text_view.text = place.playgroundModels.size.toString()

        additional_info_container.removeAllViews()

        //Добавляем дополнительную информацию
        if (place.hasParking) addNewAdditionalInfoView(R.string.text_filter_parking, R.drawable.ic_parking_info_icon)
        if (place.hasLockers) addNewAdditionalInfoView(R.string.text_filter_locker, R.drawable.ic_lockers_info_icon)
        if (place.hasInventory) addNewAdditionalInfoView(R.string.text_filter_inventory, R.drawable.ic_inventory_info_icon)
        if (place.hasBaths) addNewAdditionalInfoView(R.string.text_filter_bath, R.drawable.ic_baths_info_icon)
        if (place.openField) {
            addNewAdditionalInfoView(R.string.text_place_type_open, R.drawable.ic_open_place_info_icon)
        } else {
            addNewAdditionalInfoView(R.string.text_place_type_closed, R.drawable.ic_close_place_info_icon)
        }
    }

    /** Добавляем view с дополнительной информацией
     * @param stringTypeId - Ссылка на строковый ресурс с названием
     * @param iconId - Ссылка на строковый ресурс с картинкой*/
    private fun addNewAdditionalInfoView(stringTypeId: Int, iconId: Int) {
        additional_info_container.addView(AdditionalInfoView(requireContext(), stringTypeId, iconId))
    }

    /** Открываем экран просмотра расписания и бронирования */
    private fun openBookingScreen(place: PlaceModel?) {
        EventBus.getDefault().post(ActionEvent.OpenBookingEvent(place))
    }
}
