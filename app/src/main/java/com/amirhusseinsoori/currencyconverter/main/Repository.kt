package com.amirhusseinsoori.currencyconverter.main

import com.amirhusseinsoori.currencyconverter.data.models.CurrencyApi
import com.amirhusseinsoori.currencyconverter.data.models.CurrencyResponse
import com.amirhusseinsoori.currencyconverter.util.Resource
import javax.inject.Inject

class Repository @Inject constructor(
        private val api: CurrencyApi
) : MainRepository {


    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(base)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)

            } else {
                Resource.Error(response.message())
            }

        } catch (ex: Exception) {
            Resource.Error(ex.message ?: "An error occured")
        }
    }
}