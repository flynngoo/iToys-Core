plugins {
    alias(libs.plugins.itoys.android.library)
}

apply(from = "${rootDir}/publish-maven.gradle.kts")

android {
    namespace = "com.itoys.android.wechat"
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.kotlin.stdlib)

    implementation(libs.wechat)
}