package com.example.cdcdemo.ui.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdcdemo.data.CurrencyDao
import com.example.cdcdemo.data.CurrencyInfo
import com.example.cdcdemo.hilt.IODispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyDao: CurrencyDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
    ) : ViewModel() {
    private var sortingMode: SortingMode? = null
    private var isSorting = false

    private val _currencyList = MutableStateFlow<List<CurrencyInfo>>(emptyList())
    val currencyList: StateFlow<List<CurrencyInfo>> = _currencyList

    private val _itemClick = MutableSharedFlow<CurrencyInfo>()
    val itemClick: SharedFlow<CurrencyInfo> = _itemClick

    init {
        viewModelScope.launch {
            currencyDao.getAll()
                .map {
                    it.sortWithMode(sortingMode)
                }
                .flowOn(ioDispatcher)
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

        val newList = currencyList.value.sortWithMode(sortingMode)

        _currencyList.value = newList
        isSorting = false
    }

    fun itemClick(currencyInfo: CurrencyInfo) {
        viewModelScope.launch {
            _itemClick.emit(currencyInfo)
        }
    }

    private fun List<CurrencyInfo>.sortWithMode(sortingMode: SortingMode?): List<CurrencyInfo> {
        return if (sortingMode == null) {
            this
        } else {
            toMutableList()
                .let { list ->
                    when (sortingMode) {
                        SortingMode.NAME_ASC -> list.sortedBy { it.name }
                        SortingMode.NAME_DESC -> list.sortedByDescending { it.name }
                    }
                }
        }
    }

    enum class SortingMode {
        NAME_ASC,
        NAME_DESC
    }
}
