package com.example.arenamsk.ui.place_filter

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceFilterModel
import com.example.arenamsk.ui.places.PlacesViewModel
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.fragment_filter_content.*

class PlaceFilterFragment private constructor() : DialogFragment(), LifecycleOwner {

    companion object {
        const val FILTER_MODEL_TAG = "filter_tag"
        const val MIN_PRICE = 0
        const val MAX_PRICE = 100000
        const val PRICE_STEP = 1

        fun getInstance(): PlaceFilterFragment {
            return PlaceFilterFragment()
        }
    }

    private var handler: Handler? = null

    private var priceFrom = MIN_PRICE
    private var priceTo = MAX_PRICE

    private val placesViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(PlacesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_filter,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placesViewModel.getPlacesLiveData().observe(viewLifecycleOwner, Observer {
            filter_text_founded_places_count.text =
                resources.getString(R.string.text_filter_founded_places, it.size.toString())
        })

        updateUI()

        setupListeners()
    }

    /** На основе placeFilterModel выставляем UI */
    private fun updateUI() {
        val filter = placesViewModel.getFilterLiveData().value

        filter_has_baths.isChecked = filter?.hasBaths ?: false
        filter_has_inventory.isChecked = filter?.hasInventory ?: false
        filter_has_lockers.isChecked = filter?.hasLockers ?: false
        filter_has_parking.isChecked = filter?.hasParking ?: false
        filter_open_field.isChecked = filter?.openField ?: false
        filter_close_field.isChecked = !filter_open_field.isChecked

        priceFrom = filter?.priceFrom ?: MIN_PRICE
        priceTo = filter?.priceTo ?: MAX_PRICE

        filter_start_price_edit_text.setText(priceFrom.toString())
        filter_end_price_edit_text.setText(priceTo.toString())

        filter_price_range_bar.setProgress(priceFrom.toFloat(), priceTo.toFloat())

        //TODO set up sports and subways
    }

    private fun setupListeners() {
        filter_has_baths.setOnCheckedChangeListener { _, _ -> updatePlaces() }

        filter_has_inventory.setOnCheckedChangeListener { _, _ -> updatePlaces() }

        filter_has_lockers.setOnCheckedChangeListener { _, _ -> updatePlaces() }

        filter_has_parking.setOnCheckedChangeListener { _, _ -> updatePlaces() }

        filter_open_field.setOnCheckedChangeListener { _, isChecked ->
            filter_close_field.isChecked = !isChecked
            updatePlaces()
        }

        filter_close_field.setOnCheckedChangeListener { _, isChecked ->
            filter_open_field.isChecked = !isChecked
            updatePlaces()
        }

        close_filter_btn.setOnClickListener { dismiss() }

        filter_btn_show.setOnClickListener {
            //Закрываем фильтр для возврата на предыдущий экран
            dismiss()
        }

        filter_btn_reset.setOnClickListener {
            //Обнуляем фильтр и view
            placesViewModel.resetFilter()

            resetView()
        }

        filter_start_price_edit_text.addTextChangedListener(object : TextWatcher {
            var editing = false

            override fun afterTextChanged(price: Editable?) {
                priceFrom = if (price.isNullOrEmpty()) {
                    MIN_PRICE
                } else {
                    val newPrice = price.toString().toInt()

                    if (newPrice > MAX_PRICE - PRICE_STEP) priceFrom else newPrice
                }

                if (!editing) {
                    editing = true
                    filter_start_price_edit_text.setText(if (priceFrom != MIN_PRICE) priceFrom.toString() else price.toString())
                    filter_start_price_edit_text.setSelection(if (priceFrom != MIN_PRICE) priceFrom.toString().length else price.toString().length)
                    editing = false
                }

                updateRangeBar()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        filter_end_price_edit_text.addTextChangedListener(object : TextWatcher {
            var editing = false

            override fun afterTextChanged(price: Editable?) {
                priceTo = if (price.isNullOrEmpty()) {
                    MAX_PRICE
                } else {
                    val newPrice = price.toString().toInt()

                    if (newPrice > MAX_PRICE) priceTo else newPrice
                }

                if (!editing) {
                    editing = true
                    filter_end_price_edit_text.setText(if (priceTo != MAX_PRICE) priceTo.toString() else price.toString())
                    filter_end_price_edit_text.setSelection(if (priceTo != MAX_PRICE) priceTo.toString().length else price.toString().length)
                    editing = false
                }

                updateRangeBar()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        filter_price_range_bar.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onRangeChanged(
                view: RangeSeekBar?,
                leftValue: Float,
                rightValue: Float,
                isFromUser: Boolean
            ) {
                //Если число делится на тысячу без остатка, то выставляем его,
                //иначе считаем ближайшее число, которое делится на 1000 без остатка
                if (isFromUser) {
                    filter_start_price_edit_text.setText(
                        if (leftValue.toInt() % 1000f != 0f) getValueFromSeekBar(
                            leftValue.toInt()
                        ).toString() else leftValue.toInt().toString()
                    )
                    filter_end_price_edit_text.setText(
                        if (rightValue.toInt() % 1000f != 0f) getValueFromSeekBar(
                            rightValue.toInt()
                        ).toString() else rightValue.toInt().toString()
                    )
                }

                updatePlaces()
            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
            }
        })
    }

    //Определяем в каком диапозоне число
    private fun getValueFromSeekBar(value: Int): Int {
        val steps = value / 1000
        return if (steps < 1) 0 else steps * 1000
    }

    private fun updateRangeBar() {
        if (priceTo - PRICE_STEP > priceFrom) {
            filter_price_range_bar.setProgress(
                priceFrom.toFloat(),
                priceTo.toFloat()
            )
        }
    }

    private fun updatePlaces() {
        //Обновляем площадки через какое-то время
        if (handler == null) {
            handler = Handler().apply {
                postDelayed(
                    {
                        placesViewModel.updatePlaceWithFilter(
                            hasBaths = filter_has_baths.isChecked,
                            hasParking = filter_has_parking.isChecked,
                            hasLockers = filter_has_lockers.isChecked,
                            hasInventory = filter_has_inventory.isChecked,
                            openField = filter_open_field.isChecked,
                            priceFrom = priceFrom,
                            priceTo = priceTo,
                            sports = ArrayList(),
                            subways = ArrayList()
                        )
                        handler = null
                    },
                    800L
                )
            }
        }
    }

    private fun resetView() {
        filter_has_baths.isChecked = false
        filter_has_parking.isChecked = false
        filter_has_lockers.isChecked = false
        filter_has_inventory.isChecked = false
        filter_open_field.isChecked = false
        filter_close_field.isChecked = true
        priceFrom = MIN_PRICE
        priceTo = MAX_PRICE
        filter_start_price_edit_text.setText(priceFrom.toString())
        filter_end_price_edit_text.setText(priceTo.toString())
        //TODO reset sports and subways
    }
}