package com.anima.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anima.data.payment.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal

class PaymentViewModel(application: Application) : AndroidViewModel(application) {
    private val paymentRepository = PaymentRepository()

    val текущаяЦена = paymentRepository.текущаяЦена
    val активныеБонусы = paymentRepository.активныеБонусы
    val статусПокупки = paymentRepository.статусПокупки

    private val _покупкаВПроцессе = MutableStateFlow(false)
    val покупкаВПроцессе: StateFlow<Boolean> = _покупкаВПроцессе.asStateFlow()

    fun рассчитатьЦену(страна: String, среднийДоход: BigDecimal) {
        val информация = ПлатежнаяИнформация(
            страна = страна,
            среднийДоход = среднийДоход,
            валютаСтраны = when (страна) {
                "RU" -> Валюта.RUB
                "US" -> Валюта.USD
                "EU" -> Валюта.EUR
                else -> Валюта.USD
            }
        )
        paymentRepository.рассчитатьЦену(информация)
    }

    fun выполнитьПокупку(способОплаты: СпособОплаты) {
        viewModelScope.launch {
            _покупкаВПроцессе.value = true
            try {
                paymentRepository.выполнитьПокупку(способОплаты)
            } finally {
                _покупкаВПроцессе.value = false
            }
        }
    }

    fun сброситьСтатусПокупки() {
        paymentRepository.сброситьСтатусПокупки()
    }
} 