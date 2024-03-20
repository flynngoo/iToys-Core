package com.itoys.android.utils.compat

import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import com.itoys.android.utils.SysUtils
import com.itoys.android.utils.expansion.then

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/21
 */
object VibratorCompat {

    fun vibrator(context: Context): Vibrator {
        return vibratorApi.getVibrator(context)
    }

    private val vibratorApi: Api = (SysUtils.isAndroid12()).then(
        { ApiLiveS() },
        { Api() }
    )

    private open class Api {

        open fun getVibrator(context: Context): Vibrator {
            return context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    private class ApiLiveS : Api() {

        @RequiresApi(Build.VERSION_CODES.S)
        override fun getVibrator(context: Context): Vibrator {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            return vibratorManager.defaultVibrator
        }
    }
}