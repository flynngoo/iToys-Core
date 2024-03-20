package com.itoys.android.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Process
import androidx.core.content.FileProvider
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.locationManager
import com.itoys.android.utils.expansion.then
import java.io.File

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/21
 */
object SysUtils {

    /**
     * 是否是 Android 4.1 及以上版本
     */
    @JvmStatic
    fun isAndroidJelly(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
    }

    /**
     * 是否是 Android 4.4 及以上版本
     */
    @JvmStatic
    fun isAndroid4(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    }

    /**
     * 是否是 Android 5.0 及以上版本
     */
    @JvmStatic
    fun isAndroid5(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    /**
     * 是否是 Android 5.1 及以上版本
     */
    @JvmStatic
    fun isAndroidLollipopMr1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1
    }

    /**
     * 是否是 Android 6.0 及以上版本
     */
    @JvmStatic
    fun isAndroid6(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    /**
     * 是否是 Android 7.0 及以上版本
     */
    @JvmStatic
    fun isAndroid7(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    /**
     * 是否是 Android 8.0 及以上版本
     */
    @JvmStatic
    fun isAndroid8(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    /**
     * 是否是 Android 9.0 及以上版本
     */
    @JvmStatic
    fun isAndroid9(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    /**
     * 是否是 Android 10.0 及以上版本
     */
    @JvmStatic
    fun isAndroid10(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    /**
     * 是否是 Android 11.0 及以上版本
     */
    @JvmStatic
    fun isAndroid11(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    /**
     * 是否是 Android 12 及以上版本
     */
    @JvmStatic
    fun isAndroid12(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }

    /**
     * 是否是 Android 13 及以上版本
     */
    @JvmStatic
    fun isAndroid13(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    /**
     * 是否是 Android 14 及以上版本
     */
    @JvmStatic
    fun isAndroid14(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
    }

    /**
     * 判断当前进程是否是主进程
     */
    @JvmStatic
    fun isMainProcess(context: Context): Boolean {
        val packageName = context.packageName
        val processName = getProcessName(context, Process.myPid())
        return packageName == processName
    }

    /**
     * 根据进程 ID 获取进程名.
     */
    @JvmStatic
    fun getProcessName(context: Context, pid: Int): String {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processInfoList = am.runningAppProcesses ?: return ""
        for (processInfo in processInfoList) {
            if (processInfo.pid == pid) {
                return processInfo.processName
            }
        }
        return ""
    }

    @JvmStatic
    fun callPhone(context: Context, phone: String) {
        if (phone.isBlank()) return
        val intent = Intent(Intent.ACTION_DIAL)
        val data: Uri = Uri.parse("tel:$phone")
        intent.data = data
        context.startActivity(intent)
    }

    @JvmStatic
    fun installApp(context: Context, file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        val fileUri: Uri
        if (isAndroid7()) {
            fileUri =
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            fileUri = Uri.fromFile(file)
        }

        intent.setDataAndType(fileUri, "application/vnd.android.package-archive")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    @JvmStatic
    fun appVersionName(context: Context): String {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: Exception) {
            "1.0.0"
        }
    }

    @JvmStatic
    fun appVersionCode(context: Context): Long {
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageInfo(context.packageName, 0)

        return try {
            if (isAndroid9()) {
                packageInfo.longVersionCode
            } else {
                packageInfo.versionCode.toLong()
            }
        } catch (e: NoSuchMethodException) {
            1
        }
    }

    @JvmStatic
    fun appName(context: Context): String {
        return try {
            val applicationInfo =
                context.packageManager.getPackageInfo(context.packageName, 0).applicationInfo
            applicationInfo.nonLocalizedLabel.invalid()
        } catch (e: Exception) {
            ""
        }
    }

    @JvmStatic
    fun appIcon(context: Context): Drawable? {
        return try {
            val applicationInfo =
                context.packageManager.getPackageInfo(context.packageName, 0).applicationInfo
            applicationInfo.loadIcon(context.packageManager)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * xiaomi 设备
     */
    @JvmStatic
    fun isXiaoMi(): Boolean {
        return Build.BRAND.lowercase() == "xiaomi"
    }

    /**
     * 定位服务打开状态
     */
    @JvmStatic
    fun isLocationServiceEnable(context: Context): Boolean {
        val locationManager = context.locationManager ?: return false

        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        return gps || network
    }
}