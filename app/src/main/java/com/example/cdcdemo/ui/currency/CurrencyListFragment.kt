package com.example.cdcdemo.ui.currency

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cdcdemo.R
import com.example.cdcdemo.databinding.FragmentCurrencyBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrencyListFragment: Fragment(R.layout.fragment_currency) {
    private val currencyViewModel: CurrencyViewModel by activityViewModels()
    private val currencyAdapter = CurrencyAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCurrencyBinding.bind(view)
        binding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = currencyAdapter
                setHasFixedSize(true)
            }

        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                currencyViewModel.currencyList
                    .collect {
                        currencyAdapter.submitList(it)
                    }
            }
        }
    }

}