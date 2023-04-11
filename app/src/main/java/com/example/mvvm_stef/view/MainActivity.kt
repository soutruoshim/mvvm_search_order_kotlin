package com.example.mvvm_stef.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm_stef.databinding.ActivityMainBinding
import com.example.mvvm_stef.model.LoadingState
import com.example.mvvm_stef.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val adapter = OrderAdapter(arrayListOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        initializeUI()
        initializeObservers()

        viewModel.onViewReady()
    }
    private fun initializeUI() {
        binding.ordersRV.adapter = adapter
        binding.ordersRV.layoutManager = LinearLayoutManager(this)

        binding.searchET.doOnTextChanged { text, start, before, count ->
            viewModel.onSearchQuery(text.toString())
        }
    }

    private fun initializeObservers() {
        viewModel.loadingStateLiveData.observe(this, Observer {
            onLoadingStateChanged(it)
        })

        viewModel.ordersLiveData.observe(this, Observer {
            adapter.updateOrders(it)
        })
    }

    private fun onLoadingStateChanged(state: LoadingState) {
        binding.searchET.visibility = if (state == LoadingState.LOADED) View.VISIBLE else View.GONE
        binding.ordersRV.visibility = if (state == LoadingState.LOADED) View.VISIBLE else View.GONE
        binding.errorTV.visibility = if (state == LoadingState.ERROR) View.VISIBLE else View.GONE
        binding.loadingPB.visibility = if (state == LoadingState.LOADING) View.VISIBLE else View.GONE
    }

}