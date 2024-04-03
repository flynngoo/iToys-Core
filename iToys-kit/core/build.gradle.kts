plugins {
    alias(libs.plugins.itoys.android.library)
    alias(libs.plugins.itoys.android.therouter)
}

apply(from = "${rootDir}/publish-maven.gradle.kts")

android {
    namespace = "com.itoys.android.core"
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.activity.ktx)
    implementation(libs.core.ktx)
    implementation(libs.material)
    implementation(libs.multidex)

    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.brv)
    implementation(libs.eventbus.live)
    implementation(libs.jackson)
    implementation(libs.keyboard.event)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.mmkv)
    implementation(libs.title.bar)
    implementation(libs.recycler.view)
    implementation(libs.refresh.header.classics)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.jackson)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.okhttp.logger)
    implementation(libs.qiniu)

    implementation(libs.itoys.logcat)
    implementation(libs.itoys.uikit)
    implementation(libs.itoys.utils)
}