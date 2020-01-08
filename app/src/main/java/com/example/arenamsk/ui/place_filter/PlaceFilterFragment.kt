package com.example.arenamsk.ui.place_filter

import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_filter.*

class PlaceFilterFragment : DialogFragment(), LifecycleOwner {

    companion object {
        const val FILTER_MODEL_TAG = "filter_tag"

        fun createInstance(): PlaceFilterFragment {
            return PlaceFilterFragment()
        }
    }

    private val placesViewModel by lazy {
        ViewModelProviders.of(this).get(PlacesViewModel::class.java)
    }

    private val placeFilterModel by lazy { placesViewModel.getFilterLiveData().value ?: PlaceFilterModel() }

    private lateinit var filterModel: PlaceFilterModel

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

        placesViewModel.getPlacesLiveData().observe(this, Observer {
            //TODO set кол-во найденных мест
        })

        updateUI()
    }

    //TODO на основе placeFilterModel выставляем UI
    private fun updateUI() {

    }

    //TODO при изменении фильтра, мы меняем значения в placeFilterModel
    // Затем, этот объект сохраняем в liveData, у нас стартует загрузка на основе фильтра
    // Обновляем кол-во найденых мест в этом фрагменте
}