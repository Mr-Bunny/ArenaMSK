package com.example.arenamsk.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    /** Конвертируем время из workStart = "12:11:10" и workEnd = "15:14:13" в "12.11-15.14" */
    fun convertWorkTime(workStart: String, workEnd: String): String {
        return try {
            val dateFormatInput = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val dateFormatOutput = SimpleDateFormat("HH.mm", Locale.getDefault())

            val dateStart: Date = dateFormatInput.parse(workStart) ?: Date("")
            val dateEnd: Date = dateFormatInput.parse(workEnd) ?: Date("")

            val workStartOutput = dateFormatOutput.format(dateStart)
            val workEndOutput = dateFormatOutput.format(dateEnd)

            "$workStartOutput-$workEndOutput"
        } catch (e: Exception) {
            ""
        }
    }
}