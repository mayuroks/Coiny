package com.binarybricks.coiny.utils

import com.binarybricks.coiny.data.database.entities.Exchange
import com.binarybricks.coiny.network.DATA
import com.binarybricks.coiny.network.RAW
import com.binarybricks.coiny.network.models.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal

/**
Created by Pranay Airan 1/15/18.
 */

fun getCoinPriceFromJson(jsonObject: JsonObject): BigDecimal {
    var coinPrice = BigDecimal.ZERO

    val prices = jsonObject.keySet() // this will give us list of all the currency like USD, EUR
    prices.forEach { currency ->
        coinPrice = jsonObject.getAsJsonPrimitive(currency).asBigDecimal
    }

    return coinPrice
}

fun getCoinPriceFromJsonHistorical(jsonObject: JsonObject): MutableMap<String, BigDecimal> {

    val coinPrices: MutableMap<String, BigDecimal> = hashMapOf()

    val prices = jsonObject.keySet() // this will give us list of all the currency like USD, EUR
    prices.forEach { currency ->
        val currencyJson = jsonObject.getAsJsonObject(currency)
        val currencyValue = currencyJson.keySet()
        currencyValue.forEach { currencyValue ->
            coinPrices[currencyValue] = currencyJson.getAsJsonPrimitive(currencyValue).asBigDecimal
        }
    }

    return coinPrices
}

fun getCoinPricesFromJson(jsonObject: JsonObject): ArrayList<CoinPrice> {
    val coinPriceList: ArrayList<CoinPrice> = ArrayList()

    if (jsonObject.has(RAW)) {
        val rawCoinObject = jsonObject.getAsJsonObject(RAW)
        val coins = rawCoinObject.keySet() // this will give us list of all the coins in raw like BTC, ETH
        coins.forEach { coinName ->
            val toCurrencies = rawCoinObject.getAsJsonObject(coinName) // this will give us list of prices for this coinSymbol in currencies we asked for
            toCurrencies.keySet().forEach {
                val coinJsonObject = toCurrencies.getAsJsonObject(it) // this will give us the price object we need
                val coin = Gson().fromJson(coinJsonObject, CoinPrice::class.java)
                coinPriceList.add(coin)
            }
        }
    }

    return coinPriceList
}

fun getCoinPriceListFromJson(jsonObject: JsonObject): ArrayList<CoinPrice> {
    val coinPriceList: ArrayList<CoinPrice> = ArrayList()

    if (jsonObject.has(DATA)) {
        val dataCoinObject = jsonObject.getAsJsonArray(DATA)

        dataCoinObject.forEach {
            val jsonObject = it as JsonObject
            if (jsonObject.has(RAW)) {
                val rawCoinObject = jsonObject.getAsJsonObject(RAW)
                val coins = rawCoinObject.keySet() // this will give us list of all the coins in raw like BTC, ETH
                coins.forEach { coinName ->
                    val coinJsonObject = rawCoinObject.getAsJsonObject(coinName) // this will give us list of prices for this coinSymbol in currencies we asked for
                    val coin = Gson().fromJson(coinJsonObject, CoinPrice::class.java)
                    coinPriceList.add(coin)
                }
            }
        }
    }
    return coinPriceList
}

fun getTopPairsFromJson(jsonObject: JsonObject): ArrayList<CoinPair> {
    val coinPairList: ArrayList<CoinPair> = ArrayList()

    if (jsonObject.has(DATA)) {
        val dataPairObject = jsonObject.getAsJsonArray(DATA)

        dataPairObject.forEach {
            val jsonObject = it as JsonObject
            val coinPair = Gson().fromJson(jsonObject, CoinPair::class.java)
            coinPairList.add(coinPair)
        }
    }
    return coinPairList
}

fun getCoinsFromJson(jsonObject: JsonObject): ArrayList<CCCoin> {
    val CCCoinList: ArrayList<CCCoin> = ArrayList()

    if (jsonObject.has(DATA)) {
        val rawCoinObject = jsonObject.getAsJsonObject(DATA)
        val coins = rawCoinObject.keySet() // this will give us list of all the coins in DATA like BTC, ETH
        coins.forEach { coinName ->
            val toCurrencies = rawCoinObject.getAsJsonObject(coinName)
            val coin = Gson().fromJson(toCurrencies, CCCoin::class.java)
            CCCoinList.add(coin)
        }
    }

    return CCCoinList
}

fun getExchangeListFromJson(jsonObject: JsonObject): HashMap<String, MutableList<ExchangePair>> {
    val coinExchangeSet = HashMap<String, MutableList<ExchangePair>>()
    val gson = Gson()
    val listType = object : TypeToken<List<String>>() {
    }.type

    val exchangeSet = jsonObject.entrySet().iterator()
    var exchangePairList: MutableList<ExchangePair>
    exchangeSet.forEach { exchange ->
        val coinSet = exchange.value.asJsonObject.entrySet().iterator()
        coinSet.forEach { coin ->
            exchangePairList = mutableListOf()
            if (coinExchangeSet.containsKey(coin.key)) {
                exchangePairList = coinExchangeSet[coin.key] ?: mutableListOf()
            }

            exchangePairList.add(ExchangePair(exchange.key, gson.fromJson(coin.value.asJsonArray, listType)))

            coinExchangeSet[coin.key] = exchangePairList
        }
    }

    return coinExchangeSet
}

fun getExchangeInfo(jsonObject: JsonObject): List<Exchange> {

    val exchangeList: MutableList<Exchange> = mutableListOf()

    if (jsonObject.has(DATA)) {
        val rawExchangeObject = jsonObject.getAsJsonObject(DATA)
        val exchanges = rawExchangeObject.keySet() // this will give us list of all the exchange in DATA like BTCChina
        exchanges.forEach { exchangeName ->
            val exchange = rawExchangeObject.getAsJsonObject(exchangeName)
            exchangeList.add(Exchange(id = exchange.getAsJsonPrimitive("Id").asString,
                    name = exchange.getAsJsonPrimitive("Name").asString,
                    url = exchange.getAsJsonPrimitive("Url").asString,
                    logoUrl = exchange.getAsJsonPrimitive("LogoUrl").asString,
                    itemType = exchange.getAsJsonPrimitive("ItemType").asString,
                    internalName = exchange.getAsJsonPrimitive("InternalName").asString,
                    affiliateUrl = exchange.getAsJsonPrimitive("AffiliateUrl").asString,
                    country = exchange.getAsJsonPrimitive("Country").asString,
                    orderBook = exchange.getAsJsonPrimitive("OrderBook").asBoolean,
                    trades = exchange.getAsJsonPrimitive("Trades").asBoolean,
                    recommended = exchange.getAsJsonPrimitive("Recommended").asBoolean,
                    sponsored = exchange.getAsJsonPrimitive("Sponsored").asBoolean))
        }
    }

    return exchangeList
}

fun getCryptoNewsJson(jsonObject: JsonObject): MutableList<CryptoCompareNews> {
    val coinNewsList: MutableList<CryptoCompareNews> = mutableListOf()

    if (jsonObject.has(DATA)) {
        val dataCoinObject = jsonObject.getAsJsonArray(DATA)

        dataCoinObject.forEach {
            val coinNews = Gson().fromJson(it as JsonObject, CryptoCompareNews::class.java)
            coinNewsList.add(coinNews)
        }
    }
    return coinNewsList
}