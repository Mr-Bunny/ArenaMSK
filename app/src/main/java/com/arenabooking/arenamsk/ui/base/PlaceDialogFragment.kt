package com.arenabooking.arenamsk.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.models.PlaceModel
import com.arenabooking.arenamsk.network.utils.AuthUtils
import com.arenabooking.arenamsk.ui.booking.PlaceBookingFragment
import com.arenabooking.arenamsk.ui.booking.PlaceBookingFragment.Companion.PLACE_BOOKING_ARG_TAG
import com.arenabooking.arenamsk.ui.place_detail.PlaceDetailFragment
import com.arenabooking.arenamsk.ui.place_detail.PlaceDetailFragment.Companion.PLACE_DETAIL_ARG_TAG
import com.arenabooking.arenamsk.ui.places.PlacesViewModel
import com.arenabooking.arenamsk.utils.ActionEvent
import com.arenabooking.arenamsk.utils.ActionEvent.OpenBookingEvent
import com.arenabooking.arenamsk.utils.ActionEvent.OpenCalendar
import com.arenabooking.arenamsk.utils.disable
import com.arenabooking.arenamsk.utils.enable
import com.arenabooking.arenamsk.utils.getSportIconDrawableId
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_place_dialog.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/** Базовый фрагмент экрана подробной информации о площадке
 * В нем открываются фрагмент с информацией и фрагмент бронирвоания */
class PlaceDialogFragment private constructor() : DialogFragment(), LifecycleOwner {

    companion object {
        const val DETAIL_FRAGMENT_TAG = "detail_fragment_tag"
        const val BOOKING_FRAGMENT_TAG = "booking_fragment_tag"
        const val PLACE_DIALOG_FRAGMENT_TAG = "base_dialog_fragment_tag"
        const val PLACE_DIALOG_FRAGMENT_POSITION_TAG = "position_tag"
        const val PLACE_DIALOG_FRAGMENT_ARG_TAG = "place_dialog_fragment_arg_tag"
        const val PLACE_DIALOG_OPEN_BOOKING_ARG_TAG = "place_dialog_open_booking_arg_tag"

        fun getInstance(
            place: PlaceModel? = null,
            openBooking: Boolean = false,
            position: Int = -1
        ): PlaceDialogFragment {
            place?.let {
                return PlaceDialogFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(PLACE_DIALOG_FRAGMENT_ARG_TAG, it)
                        putBoolean(PLACE_DIALOG_OPEN_BOOKING_ARG_TAG, openBooking)
                        putInt(PLACE_DIALOG_FRAGMENT_POSITION_TAG, position)
                    }
                }
            }

            return PlaceDialogFragment()
        }
    }

    private val placesViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(PlacesViewModel::class.java)
    }

    private val place: PlaceModel by lazy {
        arguments?.getParcelable(PLACE_DIALOG_FRAGMENT_ARG_TAG) ?: PlaceModel()
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

        if (place.playgroundModels.size > 1) {
            place_detail_sport_type_image.setImageResource("sk".getSportIconDrawableId())
        } else if (place.playgroundModels.isNotEmpty()) {
            place_detail_sport_type_image.setImageResource(place.playgroundModels[0].sport?.name.getSportIconDrawableId())
        }

        //Get Place from args if null dismiss fragment
        with(place) {
            setupToolbar(this)

            setUpFavouriteIcon(isFavourite)
            //Добавляем или убираем площадку из избранного
            place_detail_favourite_image_btn.setOnClickListener {
                val toFavourite = !place.isFavourite

                place.isFavourite = toFavourite
                setUpFavouriteIcon(toFavourite)
                //Делаем запрос на добавление или удаление в избранное
                placesViewModel.addPlaceToFavourite(
                    toFavourite,
                    place,
                    ::requestAddToFavouriteFailed
                )

                EventBus.getDefault().post(
                    ActionEvent.UpdatePlaceInPosition(
                        position = arguments?.getInt(
                            PLACE_DIALOG_FRAGMENT_POSITION_TAG, -1
                        ) ?: -1,
                        inFav = toFavourite
                    )
                )
            }

            //Открываем dialog с выбором даты
            place_detail_calendar_image_btn.setOnClickListener {
                EventBus.getDefault().post(OpenCalendar())
            }

            try {
                Picasso.get()
                    .load(this.images[0].fullImage)
                    .placeholder(R.drawable.image_placeholder)
                    .into(place_dialog_image)
            } catch (e: Exception) {
            }

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
            setFavBtnVisibility(true)
        } else {
            place_detail_calendar_image_btn.enable()
            setFavBtnVisibility(false)
        }
    }

    //Нужно проверять - авторизирован ли человек или нет
    private fun setFavBtnVisibility(needToShow: Boolean) {
        if (AuthUtils.isUserDefault()) {
            place_detail_favourite_image_btn.disable()
            return
        }

        if (needToShow) {
            place_detail_favourite_image_btn.enable()
        } else {
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
                    place_detail_toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white)
                    isShow = true
                } else if (isShow) {
                    place_detail_toolbar.title = " "
                    place_detail_toolbar.setNavigationIcon(R.drawable.arrow_back_background)
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

    private fun setUpFavouriteIcon(isFavourite: Boolean) {
        with(place_detail_favourite_image_btn) {
            if (isFavourite) {
                setImageDrawable(context.resources.getDrawable(R.drawable.ic_in_favoutires_btn))
            } else {
                setImageDrawable(context.resources.getDrawable(R.drawable.ic_add_to_favourites_btn))
            }
        }
    }

    /** Если запрос на добавление/удаление в избранное не прошел, если пришел, то view уже поменялась,
     *  а данные потом обновятся при повторном открытии.
     * @param isFavourite Принимаем противоположное значение того, что отправляли для запроса */
    private fun requestAddToFavouriteFailed(toFavourite: Boolean, place: PlaceModel) {
        place.isFavourite = !toFavourite
        setUpFavouriteIcon(!toFavourite)

        EventBus.getDefault().post(
            ActionEvent.UpdatePlaceInPosition(
                position = arguments?.getInt(
                    PLACE_DIALOG_FRAGMENT_POSITION_TAG, -1
                ) ?: -1,
                inFav = !toFavourite
            )
        )
    }
}