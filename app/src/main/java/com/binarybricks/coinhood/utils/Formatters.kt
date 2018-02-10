package com.binarybricks.coinhood.utils

import android.text.format.DateFormat
import timber.log.Timber
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by pranay airan on 1/13/18.
 */

/**
 * Use to format amount that we get it from api
 */

class Formatters {

    private val million = BigDecimal(1000000)
    private val thousand = BigDecimal(1000)
    private val mathContext = MathContext(0, RoundingMode.HALF_UP)

    private val formatter: NumberFormat by lazy {
        NumberFormat.getCurrencyInstance(Locale.getDefault())
    }

    private val formatterNumber: NumberFormat by lazy {
        NumberFormat.getNumberInstance(Locale.getDefault())
    }

    private val calendar: Calendar by lazy {
        Calendar.getInstance(Locale.getDefault())
    }

    // this formatter is use to show just time like 10:32 PM PST
    private val simpleTimeFormat: SimpleDateFormat by lazy {
        SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "h:mm a z"), Locale.getDefault())
    }


    // use to show date like January 10 2017
    private val simpleDateFormatMonthDayYear: SimpleDateFormat by lazy {
        SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMM d yyyy"), Locale.getDefault())
    }

    // this formatter is use to show full date with time
    private val simpleDateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd/MM/YYYY hh:mm aaa"), Locale.getDefault())
    }

    // this is ISO 8601 format for api
    private val simpleDateFormatIso: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
    }

    // formats the amount based on the currency code
    fun formatAmount(amount: String, currency: Currency = Currency.getInstance("USD"), rounding: Boolean = false): String {
        formatter.currency = currency

        val amountNumber = BigDecimal(amount)

        if (rounding && amountNumber > BigDecimal.ONE) {
            formatter.maximumFractionDigits = 0
        }

        return if (amountNumber < million) {
            formatter.format(amountNumber)
        } else {
            val remainder = amountNumber.divide(million, mathContext) // divide this number by million
            if (remainder <= thousand) {
                "${formatter.format(remainder.toInt())} Million"
            } else {
                "${formatter.format(remainder.divide(thousand, mathContext).toInt())} Billion"
            }
        }
    }

    fun formatNumber(number: Int): String? {
        return formatterNumber.format(number)
    }

    fun formatDate(timestamp: String, multiplier: Int): String {
        calendar.timeInMillis = timestamp.toLong() * multiplier // time we get from some api call is in seconds
        return simpleDateFormat.format(calendar.time)
    }

    // for ISO dates we need to parse it and then format it.
    fun parseAndFormatIsoDate(timestamp: String, shouldShortenToday: Boolean = false): String {
        try {
            val date = simpleDateFormatIso.parse(timestamp)

            return if (shouldShortenToday) {
                val todayTime = System.currentTimeMillis()
                val daysApart: Double = ((todayTime - date.time).toDouble() / (1000 * 60 * 60 * 24L))
                if (daysApart > 0 && daysApart <= 1) {
                    simpleTimeFormat.format(date)
                } else {
                    simpleDateFormatMonthDayYear.format(date)
                }
            } else {
                simpleDateFormatMonthDayYear.format(date)
            }
        } catch (ex: ParseException) {
            Timber.e(ex)
        }

        return timestamp
    }

}