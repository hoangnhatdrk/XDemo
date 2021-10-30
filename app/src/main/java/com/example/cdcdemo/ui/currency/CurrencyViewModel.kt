package com.example.cdcdemo.ui.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdcdemo.data.CurrencyDao
import com.example.cdcdemo.data.CurrencyInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(private val currencyDao: CurrencyDao) : ViewModel() {
    private val _currencyList = MutableStateFlow<List<CurrencyInfo>>(emptyList())
    val currencyList: StateFlow<List<CurrencyInfo>> = _currencyList

    init {
        viewModelScope.launch {
            currencyDao.getAll()
                .flowOn(Dispatchers.IO)
                .collect {
                    _currencyList.value = it
                }
        }
    }



}