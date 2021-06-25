package com.example.vahahelloapp

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

private val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        or View.SYSTEM_UI_FLAG_FULLSCREEN
        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

private var mDevicePolicyManager: DevicePolicyManager? = null
private var deviceAdminReceiver: ComponentName? = null

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  full screen mode
        window.decorView.systemUiVisibility = flags

        mDevicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        deviceAdminReceiver = MyDeviceAdminReceiver.getComponentName(this)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        if (mDevicePolicyManager!!.isAdminActive(deviceAdminReceiver!!)) {
            val intentFilter = IntentFilter(Intent.ACTION_MAIN)
            intentFilter.addCategory(Intent.CATEGORY_HOME)
            intentFilter.addCategory(Intent.CATEGORY_DEFAULT)

            if (mDevicePolicyManager!!.isDeviceOwnerApp(packageName)) {
                mDevicePolicyManager!!.setLockTaskPackages(deviceAdminReceiver!!, arrayOf(packageName))
                startLockTask()

                mDevicePolicyManager!!.addPersistentPreferredActivity(deviceAdminReceiver!!,
                        intentFilter, ComponentName(packageName, MainActivity::class.java.name))
                mDevicePolicyManager!!.setKeyguardDisabled(deviceAdminReceiver!!, true)
//                keep the screen on with full power
            }
        } else {
//            add app in Device Admin Apps
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdminReceiver)
            startActivity(intent)

//            go to Device Admin Apps
            startActivity(Intent().setComponent(ComponentName("com.android.settings", "com.android.settings.DeviceAdminSettings")))
        }
    }
}