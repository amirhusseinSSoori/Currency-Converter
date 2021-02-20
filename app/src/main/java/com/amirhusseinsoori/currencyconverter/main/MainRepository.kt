package com.amirhusseinsoori.currencyconverter.main

import com.amirhusseinsoori.currencyconverter.data.models.CurrencyResponse
import com.amirhusseinsoori.currencyconverter.util.Resource

interface MainRepository {

    suspend fun getRates(base:String):Resource<CurrencyResponse>
}