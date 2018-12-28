package com.binarybricks.coiny.data.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Pragya Agrawal
 */
@Entity(indices = [(Index("exchangeID", unique = true))])
@Parcelize
data class Exchange(
        @ColumnInfo(name = "exchangeID") var id: String,
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "url") var url: String?,
        @ColumnInfo(name = "logoUrl") var logoUrl: String?,
        @ColumnInfo(name = "itemType") var itemType: String?,
        @ColumnInfo(name = "internalName") var internalName: String?,
        @ColumnInfo(name = "affiliateUrl") var affiliateUrl: String?,
        @ColumnInfo(name = "country") var country: String?,
        @ColumnInfo(name = "orderBook") var orderBook: Boolean = false,
        @ColumnInfo(name = "trades") var trades: Boolean = false,
        @ColumnInfo(name = "recommended") var recommended: Boolean = false,
        @ColumnInfo(name = "sponsored") var sponsored: Boolean = false,
        @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var idKey: Long = 0
) : Parcelable
