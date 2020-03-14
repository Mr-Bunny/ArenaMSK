package com.example.arenamsk.utils

import com.example.arenamsk.models.PlaceModel

sealed class ActionEvent {
    class OpenBookingEvent(val place: PlaceModel?): ActionEvent()
    class OpenCalendar: ActionEvent()
    class UpdateSportList: ActionEvent()
}