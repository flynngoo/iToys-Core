plugins {
    alias(libs.plugins.itoys.android.library)
}

apply(from = "${rootDir}/publish-maven.gradle.kts")

android {
    namespace = "com.itoys.android.utils"
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.kotlin.stdlib)

    implementation(libs.cryptography)
    implementation(libs.lifecycle.common)
    implementation(libs.mmkv)
    implementation(libs.okio)

    // Test
    testImplementation(libs.junit)
}