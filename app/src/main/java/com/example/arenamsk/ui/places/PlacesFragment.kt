package com.example.arenamsk.ui.places

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arenamsk.R
import com.example.arenamsk.custom_view.TagView
import com.example.arenamsk.models.PlaceFilterModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.base.PlaceDialogFragment
import com.example.arenamsk.ui.base.PlaceDialogFragment.Companion.BOOKING_FRAGMENT_TAG
import com.example.arenamsk.ui.place_filter.PlaceFilterFragment
import com.example.arenamsk.ui.places.adapter.PlacesAdapter
import com.example.arenamsk.utils.ActionEvent
import com.example.arenamsk.utils.EnumUtils.GetPlacesStatus
import com.example.arenamsk.utils.disable
import com.example.arenamsk.utils.enable
import com.example.arenamsk.utils.EnumUtils.Sports.*
import kotlinx.android.synthetic.main.fragment_places.*
import kotlinx.android.synthetic.main.places_errors_form.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class PlacesFragment : BaseFragment(R.layout.fragment_places), TagSelectedCallback {

    private val placeAdapter by lazy { PlacesAdapter(::itemClickCallback, ::itemBookingClickCallback, ::addPlaceToFavourite) }

    private val placesViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(PlacesViewModel::class.java)
    }

    private var placeFilterFragment: PlaceFilterFragment? = null
    private var placeDetailFragment: PlaceDialogFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placesViewModel.updatePlaceWithFilter()

        //Добавляем теги
        initTags()

        initRecycler()

        placesViewModel.getPlacesStatusLiveData().observe(this, Observer {
            handlePlacesLoadingStatus(it)
        })

        placesViewModel.getPlacesLiveData().observe(this, Observer {
            //Скрываем progress bar и показываем данные, если они есть, иначе показываем ошибку
            if (it.isNullOrEmpty()) {
                showPlacesNotFoundForm()
            } else {
                showRecycler()
                placeAdapter.setNewList(it)
            }
        })

        //LiveData с найденными площадками
        placesViewModel.getFoundedPlacesLiveData().observe(this, Observer {
            //Показываем площадки только, если мы что-то вводили в поиск
            if (edit_text_search.text.toString().isNotEmpty()) {
                if (it.isNullOrEmpty()) {
                    showPlacesNotFoundForm()
                } else {
                    showRecycler()
                    placeAdapter.setNewList(it)
                }
            }
        })

        place_filter_button.setOnClickListener { openFilterFragment() }

        //Вешаем слушатель на поле поиска площадок по названию и адресу
        edit_text_search.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(textToSearch: Editable?) {
                placesViewModel.showFilteredPlaces(textToSearch.toString().trim())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(textToSearch: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    override fun onStart() {
        super.onStart()

        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)

        super.onStop()
    }

    /** Обрабатываем нажатие на тег. Если было нажатие на ALL_TYPE, то мы выделение не снимаем,
     * оно снимается автоматически по нажатию на остальные теги.
     * Если тег был выбран - добавляем его в массив sports объекта фильтра и снимаем выделение с ALL_TYPE
     * Если мы сняли выделение, то удаляем его из этого списка и делаем проверку на размер списка -
     * Если он пустой, то выделяем тег "Все виды"
     * @param isSelected true если тег был выбран, false если сняли выделение
     * @param sportName одна из констант спорта класса Constants */
    override fun tagWasSelected(isSelected: Boolean, sportName: String) {
        val newList = ArrayList<String>().apply { if (isSelected && sportName != SPORT_ALL.type) add(sportName) }
        val filter = placesViewModel.getFilterLiveData().value ?: PlaceFilterModel(sportList = newList)

        //Меняем liveDat-у с фильтром, что приведет к обновлению данных
        placesViewModel.updatePlaceWithFilter(filter.apply { sportList = newList })

        initTags()
    }

    @Subscribe
    fun updateSportTags(event: ActionEvent.UpdateSportList) {
        initTags()
    }

    private fun initRecycler() {
        with(recycler_places) {
            setHasFixedSize(true)
            adapter = placeAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
    }

    /** Добавляем теги на экран */
    private fun initTags() {
        val sports: ArrayList<String>? = placesViewModel.getFilterLiveData().value?.sportList

        with(place_tag_container) {
            removeAllViews()
            addView(TagView(requireContext(), this@PlacesFragment, 1, SPORT_ALL.type))
            addView(TagView(requireContext(), this@PlacesFragment, 2, SPORT_BASKETBALL.type))
            addView(TagView(requireContext(), this@PlacesFragment, 3, SPORT_MINI_FOOTBALL.type))
            addView(TagView(requireContext(), this@PlacesFragment, 4, SPORT_FOOTBALL.type))
            addView(TagView(requireContext(), this@PlacesFragment, 5, SPORT_TENNIS.type))
            addView(TagView(requireContext(), this@PlacesFragment, 6, SPORT_VOLLEYBALL.type))
        }

        //Если были ранее выбранные - выделяем их
        if (sports.isNullOrEmpty()) {
            setFirstTagCheck()
        } else {
            place_tag_container.children.forEach { tagView ->
                (tagView as TagView).setTagCheck(sports.contains(tagView.getTagName()))
            }
        }
    }

    /** Сбрасываем выделения со всех тегов и отмечаем только первый */
    private fun resetTags() {
        place_tag_container.forEach { tag ->
            (tag as TagView ).setTagCheck(false)
        }

        setFirstTagCheck()
    }

    private fun setFirstTagCheck(isChecked: Boolean = true) {
        (place_tag_container.getChildAt(0) as? TagView)?.setTagCheck(isChecked)
    }

    private fun itemBookingClickCallback(place: PlaceModel) {
        openPlaceDetail(place, true)
    }

    private fun itemClickCallback(place: PlaceModel) {
        openPlaceDetail(place)
    }

    private fun openPlaceDetail(place: PlaceModel, openBooking: Boolean = false) {
        placeDetailFragment?.dismiss()
        placeDetailFragment = PlaceDialogFragment.getInstance(place, openBooking)
        placeDetailFragment?.show(activity!!.supportFragmentManager, PlaceDialogFragment.PLACE_DIALOG_FRAGMENT_TAG)
    }

    private fun openFilterFragment() {
        placeFilterFragment?.dismiss()
        placeFilterFragment = PlaceFilterFragment.getInstance()
        placeFilterFragment?.show(activity!!.supportFragmentManager, PlaceFilterFragment.FILTER_MODEL_TAG)
    }

    /** Обрабатываем ошибки запроса площадок */
    private fun handlePlacesLoadingStatus(status: GetPlacesStatus) {
        hideProgressBar()

        when (status) {
            GetPlacesStatus.NOT_FOUND -> {
                showPlacesNotFoundForm()
            }

            GetPlacesStatus.REQUEST_ERROR -> {
                showRequestErrorForm()
            }

            GetPlacesStatus.NETWORK_OFFLINE -> {
                showRequestErrorForm()
                showNetworkOfflineError()
            }

            GetPlacesStatus.LOAD_PLACES -> {
                showProgressBar()
            }
        }
    }

    /** Отображаем текст, что площадки не найдены */
    private fun showPlacesNotFoundForm() {
        hideProgressBar()
        recycler_places.disable()
        places_not_found_form.enable()
    }

    /** Отображаем текст, что не удалось загрузить площадки и кнопку для повтора запроса */
    private fun showRequestErrorForm() {
        hideProgressBar()
        recycler_places.disable()
        places_request_error_form.enable()
        try_again_button.setOnClickListener {
            showProgressBar()
            placesViewModel.updatePlaceWithFilter()
        }
    }

    /** Скрываем progress bar и view с ошибками и показываем recycler с площадками */
    private fun showRecycler() {
        hideProgressBar()
        places_not_found_form.disable()
        places_request_error_form.disable()
        recycler_places.enable()
    }

    /** Скрываем progress bar */
    private fun hideProgressBar() {
        places_loading_progress_bar.disable()
    }

    /** Показываем progress bar */
    private fun showProgressBar() {
        places_not_found_form.disable()
        places_request_error_form.disable()
        recycler_places.disable()
        places_loading_progress_bar.enable()
    }

    private fun addPlaceToFavourite(toFavourite: Boolean,
                                    place: PlaceModel,
        requestAddToFavouriteFailed: (toFavourite: Boolean, place: PlaceModel) -> Unit) {
        placesViewModel.addPlaceToFavourite(toFavourite, place, requestAddToFavouriteFailed)
    }
}