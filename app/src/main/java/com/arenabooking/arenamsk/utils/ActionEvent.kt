package com.arenabooking.arenamsk.utils

import com.arenabooking.arenamsk.models.PlaceModel

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
    /** Применяется когда нужно обновить список времени для бронирования */
    class UpdateBookingList: ActionEvent()
    /** Применяется когда нужно обновить список после закрытия окна оплаты */
    class PaymentFinished: ActionEvent()
}