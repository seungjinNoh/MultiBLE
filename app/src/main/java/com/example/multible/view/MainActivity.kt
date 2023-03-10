package com.example.multible.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.multible.R
import com.example.multible.base.BaseActivity
import com.example.multible.databinding.ActivityMainBinding
import com.example.multible.utils.Utils.Companion.repeatOnStarted
import com.example.multible.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    override val viewModel: MainViewModel by viewModels()

    private val scanAdapter: ScanAdapter by lazy {
        ScanAdapter { scanResult ->
            Timber.d("아이템 클릭 scanResult.bleDevice: ${scanResult.bleDevice.macAddress}")
            viewModel.connectDevice(scanResult.bleDevice)
        }
    }

    companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val PERMISSIONS_S_ABOVE = arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val LOCATION_PERMISSION = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val REQUEST_ALL_PERMISSION = 1
        val REQUEST_LOCATION_PERMISION = 2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("*")
        super.onCreate(savedInstanceState)
    }

    override fun initVariable() {
        binding.viewmodel = viewModel
        binding.adapter = scanAdapter
    }

    override fun initPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            if (!hasPermissions(this, PERMISSIONS_S_ABOVE)) {
                requestPermissions(PERMISSIONS_S_ABOVE, REQUEST_ALL_PERMISSION)
            }
        } else {
            if (!hasPermissions(this, PERMISSIONS)) {
                requestPermissions(PERMISSIONS, REQUEST_ALL_PERMISSION)
            }
        }
    }

    override fun onResume() {
        Timber.d("*")
        super.onResume()
    }

    override fun onPause() {
        Timber.d("*")
        super.onPause()
    }

    override fun initObserver() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                handleEvent(event)
            }
        }

        repeatOnStarted {
            viewModel.deviceConnectionEvent.collect { deviceEvent ->
                Toast.makeText(this@MainActivity, "다바이스 이벤트: ${deviceEvent.deviceAddress}\t 상태: ${deviceEvent.data}", Toast.LENGTH_SHORT).show()
                Timber.d("다바이스 이벤트: ${deviceEvent.deviceAddress}\t 상태: ${deviceEvent.data}")
            }
        }

    }

    private fun handleEvent(event: MainViewModel.Event) = when (event) {
        else->{}
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show()
        } else {
            //requestPermissions(permissions, LOCATION_PERMISSION)
            Toast.makeText(this, "Permissions must be granted!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }


}