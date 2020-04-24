package com.example.arenamsk.utils

import com.example.arenamsk.models.PlaceModel

/** Классы для передачи через EvenBus */
sealed class ActionEvent {
    /** Применяется для открытия экрана бронирования */
    class OpenBookingEvent(val place: PlaceModel?): ActionEvent()
    /** Применяется для открытия календаря на жкране площадки */
    class OpenCalendar: ActionEvent()
    /** Применяется когда нужно обновить площадки */
    class UpdateSportList: ActionEvent()
    /** Применяется когда нужно обновить UI конкретной площадки в recyclerView */
    class UpdatePlaceInPosition(val position: Int, val inFav: Boolean): ActionEvent()
    /** Применяется когда нужно обновить список рвемени для бронирования */
    class UpdateBookingList: ActionEvent()
}