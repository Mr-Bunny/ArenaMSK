package com.example.arenamsk.ui.places

import androidx.lifecycle.MutableLiveData
import com.example.arenamsk.models.FeedbackModel
import com.example.arenamsk.models.PlaceFilterModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.network.models.ApiError
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.repositories.PlaceRepository
import com.example.arenamsk.ui.base.BaseViewModel

class PlacesViewModel : BaseViewModel() {

    private var placesLiveData = MutableLiveData<MutableList<PlaceModel>>()

    private var filterLiveData = MutableLiveData<PlaceFilterModel>()

    private val repository = PlaceRepository.getInstance()

    /** Обработчик ошибок запроса */
    private val errorHandler = object : RequestErrorHandler {
        override suspend fun networkUnavailableError() {

        }

        override suspend fun requestFailedError(error: ApiError?) {

        }

        override suspend fun requestSuccessButResponseIsNull() {

        }

        override suspend fun timeoutException() {

        }
    }

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
//        repository.getPlaces(
//            success = ::getPlacesSuccess,
//            errorHandler = errorHandler
//        )

        getPlacesSuccess(getTestPlaces())

        //TODO при изменении фильтра, делать новый запрос, отменяя старый с учетом фильтров
    }

    fun getPlacesLiveData() = placesLiveData

    fun getFilterLiveData() = filterLiveData

    //Отображаем данные во view
    private fun getPlacesSuccess(places: List<PlaceModel>) {
        placesLiveData.value = places as MutableList<PlaceModel>
    }

    private fun getTestPlaces(): MutableList<PlaceModel> = mutableListOf(
        PlaceModel(
            title = "Теннисный корт «Клуб тенниса»",
            isFavourite = false,
            inHistory = true,
            description = "2 крытых корта премиум класса \nс профессиональным покрытием хард, комфортным освещением, оборудованные системой вентиляции и кондиционирования",
            address = "ул. Комсомольская, д. 2, корп. 1",
            distance = 8.5f,
            id = 1,
            rating = 3.0f,
            feedbackNumber = 52,
            feedbackList = listOf(
                FeedbackModel(
                    "Валентина Валентиновна",
                    "12 июня 2019",
                    true,
                    4.5f,
                    "Расположенный в живописном месте Минска, на берегу Цнянского водохранилища,\n" +
                            "клуб тенниса благодаря тёплой атмосфере собирает любителей этого вида спорта не только из Минска.\n" +
                            "Отличные корты в зале и на улице, корты для пляжного тенниса, тренажёрный зал,солярий,\n" +
                            "сауна, занятия йогой, пилатесом, приветливый персонал понравятся любому"
                ),
                FeedbackModel("Иван", "12 июня 2019", false, 5f, "Нормасное место, рекомендую!"),
                FeedbackModel("Игорь", "12 июня 2019", false, 3f, "Нормасное место, рекомендую!"),
                FeedbackModel("Андроид", "12 июня 2019", true, 2f, "Нормасное место, рекомендую!"),
                FeedbackModel("Олег", "12 июня 2019", false, 1f, "Нормасное место, рекомендую!")
            ),
            imagesUrl = listOf(
                "https://ns328286.ip-37-187-113.eu/ew/wallpapers/800x480/02167_800x480.jpg",
                "https://ns328286.ip-37-187-113.eu/ew/wallpapers/800x480/02167_800x480.jpg",
                "https://ns328286.ip-37-187-113.eu/ew/wallpapers/800x480/02167_800x480.jpg",
                "https://ns328286.ip-37-187-113.eu/ew/wallpapers/800x480/02167_800x480.jpg"
            )
        ),
        PlaceModel(
            title = "Клуб любителей",
            isFavourite = true,
            description = "2 крытых корта премиум класса",
            address = "ул. Пушкина, д. Колотушкина",
            distance = 0.5f,
            id = 1,
            rating = 5.0f,
            feedbackNumber = 188,
            feedbackList = listOf(
                FeedbackModel(
                    "Валентина Валентиновна",
                    "12 июня 2019",
                    true,
                    4.5f,
                    "Расположенный в живописном месте Минска, на берегу Цнянского водохранилища,\n" +
                            "клуб тенниса благодаря тёплой атмосфере собирает любителей этого вида спорта не только из Минска.\n" +
                            "Отличные корты в зале и на улице, корты для пляжного тенниса, тренажёрный зал,солярий,\n" +
                            "cауна, занятия йогой, пилатесом, приветливый персонал понравятся любому"
                ),
                FeedbackModel("Иван", "12 июня 2019", false, 5f, "Нормасное место, рекомендую!"),
                FeedbackModel("Игорь", "12 июня 2019", false, 3f, "Нормасное место, рекомендую!"),
                FeedbackModel("Андроид", "12 июня 2019", true, 2f, "Нормасное место, рекомендую!"),
                FeedbackModel("Олег", "12 июня 2019", false, 1f, "Нормасное место, рекомендую!")
            ),
            imagesUrl = listOf("https://ns328286.ip-37-187-113.eu/ew/wallpapers/800x480/02167_800x480.jpg")
        ),
        PlaceModel(
            title = "«Клуб тенниса»",
            isFavourite = false,
            inHistory = true,
            description = "2 крытых корта премиум класса \nс профессиональным покрытием хард, комфортным освещением, оборудованные системой вентиляции и кондиционирования",
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
            address = "ул. Комсомольская, д. 2, корп. 1",
            distance = 8.5f,
            id = 1,
            rating = 5.0f,
            feedbackNumber = 52,
            imagesUrl = emptyList()
        )
    )
}