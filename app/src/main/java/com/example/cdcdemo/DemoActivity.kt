package com.example.cdcdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.cdcdemo.databinding.ActivityMainBinding

class DemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
                        }
                    }
                }
            }

            btn2.setOnClickListener {
                // Sort data
            }
        }
    }
}