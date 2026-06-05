package be.howest.annaudenaert.sweetdroiddelights.ui.permissions

import android.Manifest

enum class AppPermission {
    NOTIFICATIONS,
    LOCATION
}

fun AppPermission.toAndroidPermission(): String {
    return when (this) {
        AppPermission.NOTIFICATIONS -> Manifest.permission.POST_NOTIFICATIONS
        AppPermission.LOCATION -> Manifest.permission.ACCESS_FINE_LOCATION
    }
}