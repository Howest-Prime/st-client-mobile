package be.howest.annaudenaert.sweetdroiddelights.ui.permissions

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val _permissionRequest = MutableStateFlow<AppPermission?>(null)
    val permissionRequest: StateFlow<AppPermission?> = _permissionRequest

    private val _deniedPermission = MutableStateFlow<AppPermission?>(null)
    val deniedPermission: StateFlow<AppPermission?> = _deniedPermission

    fun requestPermission(permission: AppPermission) {
        _permissionRequest.value = permission
        Log.d("Permission", "$permission asked")
    }

    fun onPermissionResult(permission: AppPermission, granted: Boolean) {
        Log.d("Permission", "$permission granted: $granted")
        _permissionRequest.value = null
        if (!granted) _deniedPermission.value = permission
    }

    fun onDeniedMessageShown() {
        _deniedPermission.value = null
    }
}