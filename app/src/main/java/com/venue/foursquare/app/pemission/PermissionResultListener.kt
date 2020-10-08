package com.venue.foursquare.app.pemission

interface PermissionResultListener {
    fun onPermissionsGranted()
    fun onPermissionsDenied()
    fun onPermissionDenied(permission: String)
}