package com.example.arenamsk.ui.places

import androidx.lifecycle.MutableLiveData
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.ui.base.BaseViewModel

class PlacesViewModel : BaseViewModel() {

    private var placesLiveData = MutableLiveData<MutableList<PlaceModel>>()

    fun loadPlaces() {
        placesLiveData.value = getTestPlaces()
    }

    fun getPlacesLiveData() = placesLiveData

    //TODO test - remove after get real data from server
    private fun getTestPlaces(): MutableList<PlaceModel> = mutableListOf(
        PlaceModel(
            title = "Теннисный корт «Клуб тенниса»",
            isFavourite = false,
            description = "2 крытых корта премиум класса \nс профессиональным покрытием хард, комфортным освещением, оборудованные системой вентиляции и кондиционирования",
            workTime = "8.00–22.00",
            address = "ул. Комсомольская, д. 2, корп. 1",
            distance = 8.5f,
            id = 1,
            rating = 3.0f,
            feedbackNumber = 52,
            imagesUrl = listOf("https://ns328286.ip-37-187-113.eu/ew/wallpapers/800x480/02167_800x480.jpg",
                "https://ns328286.ip-37-187-113.eu/ew/wallpapers/800x480/02167_800x480.jpg",
                "https://ns328286.ip-37-187-113.eu/ew/wallpapers/800x480/02167_800x480.jpg",
                "https://ns328286.ip-37-187-113.eu/ew/wallpapers/800x480/02167_800x480.jpg")
        ),
        PlaceModel(
            title = "Клуб любителей пощикотать очко",
            isFavourite = true,
            description = "2 крытых корта премиум класса",
            workTime = "6.00–22.00",
            address = "ул. Пушкина, д. Колотушкина",
            distance = 0.5f,
            id = 1,
            rating = 5.0f,
            feedbackNumber = 1488,
            imagesUrl = listOf("https://ns328286.ip-37-187-113.eu/ew/wallpapers/800x480/02167_800x480.jpg")
        ),
        PlaceModel(
            title = "«Клуб тенниса»",
            isFavourite = false,
            description = "2 крытых корта премиум класса \nс профессиональным покрытием хард, комфортным освещением, оборудованные системой вентиляции и кондиционирования",
            workTime = "8.00–22.00",
            address = "ул. Комсомольская, д. 2, корп. 1",
            distance = 8.5f,
            id = 1,
            rating = 3.0f,
            feedbackNumber = 52,
            imagesUrl = emptyList()
        ),
        PlaceModel(
            title = "Теннисный корт",
            isFavourite = false,
            description = "2 крытых корта премиум класса \nс профессиональным покрытием хард, комфортным освещением, оборудованные системой вентиляции и кондиционирования",
            workTime = "8.00–22.00",
            address = "ул. Комсомольская, д. 2, корп. 1",
            distance = 8.5f,
            id = 1,
            rating = 3.0f,
            feedbackNumber = 52,
            imagesUrl = emptyList()
        ),
        PlaceModel(
            title = "Бассейн",
            isFavourite = true,
            description = "профессиональным покрытием хард, комфортным освещением, оборудованные системой вентиляции и кондиционирования",
            workTime = "8.00–22.00",
            address = "ул. Комсомольская, д. 2, корп. 1",
            distance = 8.5f,
            id = 1,
            rating = 3.0f,
            feedbackNumber = 52,
            imagesUrl = emptyList()
        ),
        PlaceModel(
            title = "Клуб любителей кожевенного мастерства",
            isFavourite = true,
            description = "2 блока вниз",
            workTime = "8.00–22.00",
            address = "ул. Комсомольская, д. 2, корп. 1",
            distance = 8.5f,
            id = 1,
            rating = 5.0f,
            feedbackNumber = 52,
            imagesUrl = emptyList()
        )
    )
}