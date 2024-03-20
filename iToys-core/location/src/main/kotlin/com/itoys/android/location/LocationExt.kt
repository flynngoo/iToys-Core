package com.itoys.android.location

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.itoys.android.location.dialog.OpenPermissionDialog

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/3/4
 */
fun AppCompatActivity.doLocation(
    callback: (LocationModel) -> Unit,
    ex: ((String) -> Unit)? = null
) {
    LocationHelper.doLocation(this, object : OnLocationCallback {
        override fun onLocation(location: LocationModel) {
            callback.invoke(location)
        }

        override fun onLocationServiceNotEnabled() {
            super.onLocationServiceNotEnabled()
            OpenPermissionDialog.show {
                fm = supportFragmentManager
                contentRes = R.string.location_service_not_enable
            }
        }

        override fun onLocationFailed(msg: String) {
            super.onLocationFailed(msg)
            ex?.invoke(msg)
        }

        override fun onPermissionDenied() {
            super.onPermissionDenied()
            OpenPermissionDialog.show {
                fm = supportFragmentManager
                contentRes = R.string.location_permission_denied
            }
        }
    })
}

fun Fragment.doLocation(
    callback: (LocationModel) -> Unit,
    ex: ((String) -> Unit)? = null
) {
    LocationHelper.doLocation(requireContext(), object : OnLocationCallback {
        override fun onLocation(location: LocationModel) {
            callback.invoke(location)
        }

        override fun onLocationServiceNotEnabled() {
            super.onLocationServiceNotEnabled()
            OpenPermissionDialog.show {
                fm = childFragmentManager
                contentRes = R.string.location_service_not_enable
            }
        }

        override fun onLocationFailed(msg: String) {
            super.onLocationFailed(msg)
            ex?.invoke(msg)
        }

        override fun onPermissionDenied() {
            super.onPermissionDenied()
            OpenPermissionDialog.show {
                fm = childFragmentManager
                contentRes = R.string.location_permission_denied
            }
        }
    })
}

fun doLocation(
    context: Context,
    fragmentManager: FragmentManager,
    callback: (LocationModel) -> Unit,
    ex: ((String) -> Unit)? = null
) {
    LocationHelper.doLocation(context, object : OnLocationCallback {
        override fun onLocation(location: LocationModel) {
            callback.invoke(location)
        }

        override fun onLocationServiceNotEnabled() {
            super.onLocationServiceNotEnabled()
            OpenPermissionDialog.show {
                fm = fragmentManager
                contentRes = R.string.location_service_not_enable
            }
        }

        override fun onLocationFailed(msg: String) {
            super.onLocationFailed(msg)
            ex?.invoke(msg)
        }

        override fun onPermissionDenied() {
            super.onPermissionDenied()
            OpenPermissionDialog.show {
                fm = fragmentManager
                contentRes = R.string.location_permission_denied
            }
        }
    })
}