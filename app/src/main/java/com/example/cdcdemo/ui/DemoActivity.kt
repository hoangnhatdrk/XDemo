package com.example.cdcdemo.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cdcdemo.R
import com.example.cdcdemo.data.CurrencyInfo
import com.example.cdcdemo.databinding.ActivityMainBinding
import com.example.cdcdemo.ui.currency.CurrencyListFragment
import com.example.cdcdemo.ui.currency.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val currencyViewModel: CurrencyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupView()
    }

    private fun setupView() {
        binding.apply {
            btn1.setOnClickListener {
                // Show demo
                supportFragmentManager.apply {
                    if (findFragmentById(R.id.fragment_container) == null) {
                        commit {
                            setReorderingAllowed(true)
                            add<CurrencyListFragment>(R.id.fragment_container)
                            runOnCommit {
                                btn1.isEnabled = false
                                btn2.isEnabled = true
                            }
                        }
                    }
                }
            }

            btn2.setOnClickListener {
                // Sort data
                currencyViewModel.toggleSort()
            }
            btn2.isEnabled = false
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                currencyViewModel.itemClick
                    .collect {
                        handleItemClick(it)
                    }
            }
        }
    }

    private fun handleItemClick(it: CurrencyInfo) {
        Toast.makeText(this@DemoActivity, "Pumping ${it.symbol} ...", Toast.LENGTH_SHORT)
            .show()
    }
}