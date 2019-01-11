package com.binarybricks.coiny.data.database

import android.arch.persistence.room.TypeConverter
import java.math.BigDecimal

/**
 * Created by Pranay Airan
 */
class BigDecimalConverter {
    @TypeConverter
    fun fromString(value: String?): BigDecimal {
        return if (value == null) BigDecimal.ZERO else BigDecimal(value)
    }

    @TypeConverter
    fun amountToString(bigDecimal: BigDecimal): String {
        return bigDecimal.toPlainString()
    }
}
