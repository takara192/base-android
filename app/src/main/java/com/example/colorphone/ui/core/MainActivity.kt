package com.example.colorphone.ui.core

import android.os.Bundle
import com.example.colorphone.databinding.ActivityMainBinding
import com.example.colorphone.ui.core.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}