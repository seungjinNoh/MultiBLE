package com.example.multible.view

import androidx.activity.viewModels
import com.example.multible.R
import com.example.multible.base.BaseActivity
import com.example.multible.base.BaseViewModel
import com.example.multible.databinding.ActivityMainBinding
import com.example.multible.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, BaseViewModel>(R.layout.activity_main) {

    override val viewModel: MainViewModel by viewModels()


}