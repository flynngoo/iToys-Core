plugins {
    alias(libs.plugins.itoys.android.library)
}

apply(from = "${rootDir}/publish-maven.gradle.kts")

android {
    namespace = "com.itoys.android.scan"
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraint.layout)
    implementation(libs.immersionbar)
    implementation(libs.material)

    implementation(libs.title.bar)
    implementation(libs.zxing)

    implementation(libs.itoys.logcat)
    implementation(libs.itoys.utils)
}