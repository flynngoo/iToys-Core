
import com.itoys.android.plugin.AppConfig
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.itoys.android.application)
    alias(libs.plugins.itoys.android.hilt)
}

android {
    namespace = AppConfig.appId

    // 打包配置
    android.applicationVariants.all {
        this.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val buildDate = SimpleDateFormat("yyyyMMddHHmm").format(Date())
                val outputFileName = "iToys_simple_${this.versionName}_${buildDate}_${this.buildType.name}_${AppConfig.patchVersion}.apk"
                output.outputFileName = outputFileName
            }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.material)
    implementation(libs.mmkv)
    implementation(libs.retrofit)

    implementation(projects.iToysKit.core)
    implementation(projects.iToysKit.uikit)
    implementation(projects.iToysKit.utils)
}