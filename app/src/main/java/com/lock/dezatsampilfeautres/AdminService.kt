package com.lock.dezatsampilfeautres

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DeviceAdminService
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.UserHandle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity

class AdminService: DeviceAdminReceiver() {
    //https://isuruka.medium.com/make-a-device-admin-application-android-emm-b08e6a134

    override fun onProfileProvisioningComplete(context: Context, intent: Intent) {
        val manager = context.getSystemService(ComponentActivity.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(context.applicationContext, AdminService::class.java)

        Log.d("islam", "onProfileProvisioningComplete : ")
        super.onProfileProvisioningComplete(context, intent)
    }
    private fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        showToast(context,"onEnabled")
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        showToast(context,"onDisabled")
    }

    override fun onPasswordChanged(context: Context, intent: Intent, user: UserHandle) {
        super.onPasswordChanged(context, intent, user)
        showToast(context,"onPasswordChanged")
    }

}