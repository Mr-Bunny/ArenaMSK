package com.example.arenamsk.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    /** Конвертируем время TimeStamp в формат dd MMMM yyyy */
    fun convertTimeStampToDate(time: Long): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        return dateFormat.format(Date(time))
    }

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

    /** Метод возвращает текущую дату в формате yyyy-MM-dd */
    fun getCurrentDay(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Date(System.currentTimeMillis())

        return dateFormat.format(currentDate)
    }

    /** Возвращает текущую дату в формате для отображения пользователю на экране
     * @param currentDate - Дата в формате yyyy-MM-dd, которую надо привести к нужному формату */
    fun getCurrentDayToDisplay(currentDate: String = ""): String {
        val dateFormatInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateFormatOutput = SimpleDateFormat("dd MMMM, cccc", Locale.getDefault())

        return dateFormatOutput.format(dateFormatInput.parse(if (currentDate.isEmpty()) getCurrentDay() else currentDate) ?: Date(""))
    }

    /** Возвращает текущую дату в формате для отображения пользователю на экране
     * @param currentDate - Текущая выбранная дата */
    fun getNextDayToDisplay(currentDate: String): String {
        val dateFormatInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateFormatOutput = SimpleDateFormat("dd MMM", Locale.getDefault())

        val date: Date = dateFormatInput.parse(currentDate) ?: Date("")
        val calendar = Calendar.getInstance().apply {
            time = date
            add(Calendar.DAY_OF_MONTH, 1)
        }

        return dateFormatOutput.format(calendar.time)
    }

    /** Возвращает следующий день
     * @param currentDate - Текущая выбранная дата */
    fun getNextDay(currentDate: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val date: Date = dateFormat.parse(currentDate) ?: Date("")
        val calendar = Calendar.getInstance().apply {
            time = date
            add(Calendar.DAY_OF_MONTH, 1)
        }

        return dateFormat.format(calendar.time)
    }
}