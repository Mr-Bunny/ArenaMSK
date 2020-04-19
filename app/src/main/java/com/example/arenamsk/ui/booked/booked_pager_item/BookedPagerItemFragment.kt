package com.example.arenamsk.ui.booked.booked_pager_item

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arenamsk.R
import com.example.arenamsk.models.OrderModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.base.PlaceDialogFragment
import com.example.arenamsk.ui.booked.adapter.CurrentOrdersAdapter
import com.example.arenamsk.ui.places.PlacesViewModel
import com.example.arenamsk.ui.places.adapter.PlacesAdapter
import com.example.arenamsk.utils.ActionEvent
import com.example.arenamsk.utils.EnumUtils
import com.example.arenamsk.utils.disable
import com.example.arenamsk.utils.enable
import kotlinx.android.synthetic.main.fragment_booked_pager_item.*
import kotlinx.android.synthetic.main.fragment_places.*
import kotlinx.android.synthetic.main.places_errors_form.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class BookedPagerItemFragment: BaseFragment(R.layout.fragment_booked_pager_item) {

    companion object {
        const val CURRENT_SCREEN_TYPE = "current"
        const val HISTORY_SCREEN_TYPE = "history"

        private const val SCREEN_TYPE_ARG = "arg"

        fun getInstance(arg: String): BookedPagerItemFragment {
            return BookedPagerItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_TYPE_ARG, arg)
                }
            }
        }
    }

    private var placeDetailFragment: PlaceDialogFragment? = null

    private val placeAdapter by lazy {
        PlacesAdapter(
            ::itemClickCallback, ::openMap,
            ::itemBookingClickCallback, ::openPhone, ::addPlaceToFavourite
        )
    }

    private val currentOrdersAdapter by lazy { CurrentOrdersAdapter(::itemClickCallback) }

    private val bookedPlacesViewModel by lazy {
        ViewModelProviders.of(this).get(BookedPagerItemViewModel::class.java)
    }

    private val placesViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(PlacesViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(booked_pager_recycler) {
            setHasFixedSize(true)
            adapter =  if (arguments?.getString(SCREEN_TYPE_ARG, CURRENT_SCREEN_TYPE) == CURRENT_SCREEN_TYPE) currentOrdersAdapter else placeAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        }

        bookedPlacesViewModel.getPlacesStatusLiveData().observe(this, Observer {
            handlePlacesLoadingStatus(it)
        })

        bookedPlacesViewModel.getCurrentBookedPlacesLiveData().observe(this, Observer {
            showCurrentOrdersData(it)
        })

        bookedPlacesViewModel.getInBookedHistoryPlacesLiveData().observe(this, Observer {
            showData(it)
        })

        getData()
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
    fun updatePlaceFavourite(event: ActionEvent.UpdatePlaceInPosition) {
        with(event) {
            if (position >= 0 && position < placeAdapter.places.size) {
                val place = placeAdapter.places[position]
                place.isFavourite = inFav
                placeAdapter.notifyItemChanged(position)
            }
        }
    }

    private fun getData() {
        if (arguments?.getString(SCREEN_TYPE_ARG, CURRENT_SCREEN_TYPE) == CURRENT_SCREEN_TYPE) {
            //загружаем текущие брони
            bookedPlacesViewModel.loadCurrentBookedPlaces()
        } else {
            //загружаем историю бронирования
            bookedPlacesViewModel.loadInBookedHistoryPlaces()
        }
    }

    private fun showData(list: MutableList<PlaceModel>) {
        if (list.isNullOrEmpty()) {
            showPlacesNotFoundForm()
        } else {
            showRecycler()
            placeAdapter.setNewList(list)
        }
    }

    private fun showCurrentOrdersData(list: MutableList<OrderModel>) {
        if (list.isNullOrEmpty()) {
            showPlacesNotFoundForm()
        } else {
            showRecycler()
            currentOrdersAdapter.setNewList(list)
        }
    }


    /** Обрабатываем ошибки запроса площадок */
    private fun handlePlacesLoadingStatus(status: EnumUtils.GetPlacesStatus) {
        hideProgressBar()

        when (status) {
            EnumUtils.GetPlacesStatus.NOT_FOUND -> {
                showPlacesNotFoundForm()
            }

            EnumUtils.GetPlacesStatus.REQUEST_ERROR -> {
                showRequestErrorForm()
            }

            EnumUtils.GetPlacesStatus.NETWORK_OFFLINE -> {
                showRequestErrorForm()
                showNetworkOfflineError()
            }

            EnumUtils.GetPlacesStatus.LOAD_PLACES -> {
                showProgressBar()
            }
        }
    }

    /** Отображаем текст, что площадки не найдены */
    private fun showPlacesNotFoundForm() {
        hideProgressBar()
        booked_pager_recycler.disable()
        places_not_found_form.enable()
    }

    /** Отображаем текст, что не удалось загрузить площадки и кнопку для повтора запроса */
    private fun showRequestErrorForm() {
        hideProgressBar()
        booked_pager_recycler.disable()
        places_request_error_form.enable()
        try_again_button.setOnClickListener {
            showProgressBar()
            getData()
        }
    }

    /** Скрываем progress bar и view с ошибками и показываем recycler с площадками */
    private fun showRecycler() {
        hideProgressBar()
        places_not_found_form.disable()
        places_request_error_form.disable()
        booked_pager_recycler.enable()
    }

    /** Скрываем progress bar */
    private fun hideProgressBar() {
        booked_pager_loading_progress_bar.disable()
    }

    /** Показываем progress bar */
    private fun showProgressBar() {
        places_not_found_form.disable()
        places_request_error_form.disable()
        booked_pager_recycler.disable()
        booked_pager_loading_progress_bar.enable()
    }

    private fun itemBookingClickCallback(place: PlaceModel) {
        openPlaceDetail(place, true)
    }

    private fun itemClickCallback(place: PlaceModel, position: Int) {
        openPlaceDetail(place, false, position)
    }

    private fun addPlaceToFavourite(
        toFavourite: Boolean,
        place: PlaceModel,
        requestAddToFavouriteFailed: (toFavourite: Boolean, place: PlaceModel) -> Unit
    ) {
        placesViewModel.addPlaceToFavourite(toFavourite, place, requestAddToFavouriteFailed)
    }

    private fun openPlaceDetail(place: PlaceModel, openBooking: Boolean = false, position: Int = -1) {
        placeDetailFragment?.dismiss()
        placeDetailFragment = PlaceDialogFragment.getInstance(place, openBooking, position)
        placeDetailFragment?.show(
            activity!!.supportFragmentManager,
            PlaceDialogFragment.PLACE_DIALOG_FRAGMENT_TAG
        )
    }

}