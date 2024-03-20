plugins {
    alias(libs.plugins.itoys.android.library)
}

android {
    namespace = "com.itoys.android.network"

    buildFeatures { buildConfig = true }

    defaultConfig {
        buildConfigField("String", "PUBLIC_RES_DOMAIN", "\"https://openres-housing.tianhuo.vip/\"")
    }
}

dependencies {
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.gson)
    implementation(libs.jackson)
    implementation(libs.mmkv)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logger)
    implementation(libs.qiniu)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.jackson)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.retrofit.converter.scalars)

    implementation(projects.iToysCore.logcat)
    implementation(projects.iToysCore.utils)

    // Test
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}