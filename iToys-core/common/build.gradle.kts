plugins {
    alias(libs.plugins.itoys.android.library)
    alias(libs.plugins.itoys.android.therouter)
}

apply(from = "${rootDir}/publish-maven.gradle.kts")

android {
    namespace = "com.itoys.android.common"
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.material)
    implementation(libs.multidex)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.brv)
    implementation(libs.eventbus.live)
    implementation(libs.keyboard.event)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.title.bar)
    implementation(libs.recycler.view)
    implementation(libs.refresh.header.classics)

    implementation(libs.itoys.logcat)
    implementation(libs.itoys.network)
    implementation(libs.itoys.uikit)
    implementation(libs.itoys.utils)
}