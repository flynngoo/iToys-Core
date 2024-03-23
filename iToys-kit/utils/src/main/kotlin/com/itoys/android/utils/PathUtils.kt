package com.itoys.android.utils

import android.os.Environment
import androidx.core.content.ContextCompat
import java.io.File

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/21
 */
object PathUtils {

    private val SEP = File.separatorChar

    /**
     * Return the path of /system.
     */
    fun getRootPath(): String {
        return getAbsolutePath(Environment.getRootDirectory())
    }

    /**
     * Return the path of /data.
     */
    fun getDataPath(): String {
        return getAbsolutePath(Environment.getDataDirectory())
    }

    /**
     * Return the path of /data.
     */
    fun getDownloadCachePath(): String {
        return getAbsolutePath(Environment.getDownloadCacheDirectory())
    }

    /**
     * Return the path of /data/data/package.
     */
    fun getInternalAppDataPath(): String {
        return getAbsolutePath(ContextCompat.getDataDir(UtilsInitialization.requireApp()))
    }

    /**
     * Return the path of /data/data/package/code_cache.
     */
    fun getInternalAppCodeCacheDir(): String {
        return getAbsolutePath(ContextCompat.getCodeCacheDir(UtilsInitialization.requireApp()))
    }

    /**
     * Return the path of /data/data/package/cache.
     */
    fun getInternalAppCachePath(): String {
        return getAbsolutePath(UtilsInitialization.requireApp().cacheDir)
    }

    /**
     * Return the path of /data/data/package/databases.
     */
    fun getInternalAppDbsPath(): String {
        return getAbsolutePath(ContextCompat.getDataDir(UtilsInitialization.requireApp())) + "${SEP}databases"
    }

    /**
     * Return the path of /data/data/package/databases/name.
     *
     * [dbName] The name of database.
     */
    fun getInternalAppDbPath(dbName: String): String {
        return getAbsolutePath(UtilsInitialization.requireApp().getDatabasePath(dbName))
    }

    /**
     * Return the path of /data/data/package/files.
     */
    fun getInternalAppFilesPath(): String {
        return getAbsolutePath(UtilsInitialization.requireApp().filesDir)
    }

    /**
     * Return the path of /data/data/package/no_backup.
     */
    fun getInternalAppNoBackupPath(): String {
        return getAbsolutePath(ContextCompat.getNoBackupFilesDir(UtilsInitialization.requireApp()))
    }

    /**
     * Return pictures path.
     */
    fun picturesPath(): String {
        return if (SysUtils.isAndroid10()){
            Environment.DIRECTORY_PICTURES
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
        }
    }

    fun getAbsolutePath(file: File?): String {
        return file?.absolutePath ?: ""
    }
}