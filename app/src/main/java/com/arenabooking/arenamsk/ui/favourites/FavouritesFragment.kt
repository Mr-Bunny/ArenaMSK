package com.arenabooking.arenamsk.ui.favourites

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.models.PlaceModel
import com.arenabooking.arenamsk.network.utils.AuthUtils
import com.arenabooking.arenamsk.ui.base.BaseFragment
import com.arenabooking.arenamsk.ui.base.PlaceDialogFragment
import com.arenabooking.arenamsk.ui.places.adapter.PlacesAdapter
import com.arenabooking.arenamsk.utils.ActionEvent
import com.arenabooking.arenamsk.utils.EnumUtils
import com.arenabooking.arenamsk.utils.disable
import com.arenabooking.arenamsk.utils.enable
import kotlinx.android.synthetic.main.fragment_favourites.*
import kotlinx.android.synthetic.main.places_errors_form.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/** Экран избранных площадок */
class FavouritesFragment : BaseFragment(R.layout.fragment_favourites) {

    private val placeAdapter by lazy {
        PlacesAdapter(
            ::itemClickCallback,
            ::openMap,
            ::itemBookingClickCallback,
            ::openPhone,
            ::addPlaceToFavourite
        )
    }

    private var placeDetailFragment: PlaceDialogFragment? = null

    private val favouritesViewModel by lazy {
        ViewModelProviders.of(this).get(FavouritesViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (AuthUtils.isUserDefault()) {
            showUnauthorizedDialog()
            return
        }

        initRecycler()

        //LiveData с найденными площадками
        favouritesViewModel.getFoundedFavouritesPlacesLiveData().observe(this, Observer {
            //Показываем площадки только, если мы что-то вводили в поиск
            if (edit_text_search_favourites.text.toString().isNotEmpty()) {
                setupList(it)
            }
        })

        favouritesViewModel.getFavouritesPlacesLiveData().observe(viewLifecycleOwner, Observer {
            setupList(it)
        })

        favouritesViewModel.getFavouritesStatusPlacesLiveData()
            .observe(viewLifecycleOwner, Observer {
                handlePlacesLoadingStatus(it)
            })

        //Вешаем слушатель на поле поиска площадок по названию и адресу
        edit_text_search_favourites.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(textToSearch: Editable?) {
                favouritesViewModel.showFilteredPlacesInFavourites(textToSearch.toString().trim())
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

    private fun setupList(list: MutableList<PlaceModel>?) {
        if (list.isNullOrEmpty()) {
            showPlacesNotFoundForm()
        } else {
            hidePlacesNotFoundForm()
            placeAdapter.setNewList(list)
        }
    }

    private fun initRecycler() {
        recycler_favourites.setHasFixedSize(true)
        recycler_favourites.adapter = placeAdapter
        recycler_favourites.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun itemBookingClickCallback(place: PlaceModel) {
        openPlaceDetail(place, true)
    }

    private fun itemClickCallback(place: PlaceModel, position: Int) {
        openPlaceDetail(place, false, position)
    }

    private fun openPlaceDetail(place: PlaceModel, openBooking: Boolean = false, position: Int = -1) {
        placeDetailFragment?.dismiss()
        placeDetailFragment = PlaceDialogFragment.getInstance(place, openBooking, position)
        placeDetailFragment?.show(
            activity!!.supportFragmentManager,
            PlaceDialogFragment.PLACE_DIALOG_FRAGMENT_TAG
        )
    }

    private fun addPlaceToFavourite(
        toFavourite: Boolean,
        place: PlaceModel,
        requestAddToFavouriteFailed: (toFavourite: Boolean, place: PlaceModel) -> Unit
    ) {
        favouritesViewModel.addPlaceToFavourite(toFavourite, place, requestAddToFavouriteFailed)
    }

    /** Обрабатываем ошибки запроса площадок */
    private fun handlePlacesLoadingStatus(status: EnumUtils.GetPlacesStatus) {
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

    /** Если пользователь не авторизирован - показываем диалоговое окно с кнопкой для перехода к регистрации */
    private fun showUnauthorizedDialog() {
        AlertDialog.Builder(requireContext(), R.style.FavouriteAlertDialog)
            .setTitle("Вы не авторизированы")
            .setMessage("Для добавления площадок в избранное пройдите регистрацию")
            .setPositiveButton("Зарегистрироваться") { _, _ ->
                exitFromProfile()
            }
            .setNegativeButton("Закрыть") { _, _ ->
                findNavController().navigate(R.id.navigation_places)
            }
            .setCancelable(false)
            .show()
    }

    //TODO вынести методы ниже в один и через when это все обрабатывать
    /** Отображаем текст, что площадки не найдены */
    private fun showPlacesNotFoundForm() {
        hideProgressBar()
        recycler_favourites.disable()
        places_request_error_form.disable()
        places_not_found_form.enable()
    }

    /** Отображаем текст, что не удалось загрузить площадки и кнопку для повтора запроса */
    private fun showRequestErrorForm() {
        hideProgressBar()
        recycler_favourites.disable()
        places_request_error_form.enable()
        try_again_button.setOnClickListener {
            showProgressBar()
            favouritesViewModel.getFavourites()
        }
    }

    /** Скрываем progress bar и view с ошибками и показываем recycler с площадками */
    private fun showRecycler() {
        hideProgressBar()
        places_not_found_form.disable()
        places_request_error_form.disable()
        recycler_favourites.enable()
    }

    /** Скрываем progress bar */
    private fun hideProgressBar() {
        favourites_loading_progress_bar.disable()
    }

    /** Показываем progress bar */
    private fun showProgressBar() {
        places_request_error_form.disable()
        places_not_found_form.disable()
        recycler_favourites.disable()
        favourites_loading_progress_bar.enable()
    }

    /** Скрываем текст, что площадки не найдены и показываем список площадок */
    private fun hidePlacesNotFoundForm() {
        recycler_favourites.enable()
        hideProgressBar()
        places_not_found_form.disable()
        places_request_error_form.disable()
    }
}