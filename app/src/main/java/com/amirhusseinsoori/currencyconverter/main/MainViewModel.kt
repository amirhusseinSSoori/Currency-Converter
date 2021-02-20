package com.amirhusseinsoori.currencyconverter.main

import android.view.KeyEvent
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirhusseinsoori.currencyconverter.data.models.Rates
import com.amirhusseinsoori.currencyconverter.util.DispatcherProvider
import com.amirhusseinsoori.currencyconverter.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import kotlin.math.round

class MainViewModel @ViewModelInject constructor(
        val repository: MainRepository,
        val dispatcher: DispatcherProvider
) : ViewModel() {

    sealed class CurrencyEvent {
        class Success(val resultText: String) : CurrencyEvent()
        class Failure(val errorText: String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()

    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(
            amountStr: String,
            fromCurrency: String,
            toCurrency: String
    ) {
        val fromAmount = amountStr.toFloatOrNull()

        //if amount is empty
        if (fromAmount == null) {
            _conversion.value = CurrencyEvent.Failure("Not a Valid Amount")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _conversion.value = CurrencyEvent.Loading
            when (val rateResponse = repository.getRates(fromCurrency)) {

                //Error Handel like network Connection
                is Resource.Error -> _conversion.value = CurrencyEvent.Failure(rateResponse.message!!)
                //Result Success
                is Resource.Success -> {
                    val rates = rateResponse.data!!.rates
                    val rate = getRateForCurrency(toCurrency, rates)
                    if (rate == null) {
                        _conversion.value = CurrencyEvent.Failure("Unexpected error")

                    } else {
                        val convertedCurrency = round(fromAmount * rate * 100)/100
                        _conversion.value=CurrencyEvent.Success(
                                "$fromAmount  $fromCurrency =  $convertedCurrency  $toCurrency"
                        )

                    }

                }
            }

        }

    }


    private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
        "CAD" -> rates.CAD
        "HKD" -> rates.HKD
        "ISK" -> rates.ISK
        "EUR" -> rates.EUR
        "PHP" -> rates.PHP
        "DKK" -> rates.DKK
        "HUF" -> rates.HUF
        "CZK" -> rates.CZK
        "AUD" -> rates.AUD
        "RON" -> rates.RON
        "SEK" -> rates.SEK
        "IDR" -> rates.IDR
        "INR" -> rates.INR
        "BRL" -> rates.BRL
        "RUB" -> rates.RUB
        "HRK" -> rates.HRK
        "JPY" -> rates.JPY
        "THB" -> rates.THB
        "CHF" -> rates.CHF
        "SGD" -> rates.SGD
        "PLN" -> rates.PLN
        "BGN" -> rates.BGN
        "TRY" -> rates.TRY
        "CNY" -> rates.CNY
        "NOK" -> rates.NOK
        "NZD" -> rates.NZD
        "ZAR" -> rates.ZAR
        "USD" -> rates.USD
        "MXN" -> rates.MXN
        "ILS" -> rates.ILS
        "GBP" -> rates.GBP
        "KRW" -> rates.KRW
        "MYR" -> rates.MYR
        else -> rates.EUR
    }

}