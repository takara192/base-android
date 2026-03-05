package com.example.colorphone.ui.core.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

typealias ActivityInflater <T> = (LayoutInflater) -> T

abstract class BaseActivity<VB: ViewBinding>(
    private val inflate: ActivityInflater<VB>
) : AppCompatActivity(){
    private lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = inflate.invoke(layoutInflater)

        setContentView(binding.root)

        setupView()
        setupListeners()
        setupObservers()
    }

    open fun setupView() {
    }

    open fun setupListeners() {
    }

    open fun setupObservers() {
    }
}