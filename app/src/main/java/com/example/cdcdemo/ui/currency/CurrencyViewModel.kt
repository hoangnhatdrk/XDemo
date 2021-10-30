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
    private var sortingMode: SortingMode? = null
    private var isSorting = false

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

    fun toggleSort() {
        if (isSorting) return
        isSorting = true
        sortingMode = when (sortingMode) {
            null -> SortingMode.NAME_ASC
            SortingMode.NAME_ASC -> SortingMode.NAME_DESC
            SortingMode.NAME_DESC -> SortingMode.NAME_ASC
        }

        val tempList = currencyList.value.toMutableList()
        val newList = when (sortingMode) {
            SortingMode.NAME_ASC -> tempList.sortedBy { it.name }
            SortingMode.NAME_DESC -> tempList.sortedByDescending { it.name }
            null -> tempList
        }

        _currencyList.value = newList
        isSorting = false
    }

    enum class SortingMode {
        NAME_ASC,
        NAME_DESC
    }
}