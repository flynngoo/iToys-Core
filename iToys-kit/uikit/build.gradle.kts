plugins {
    alias(libs.plugins.itoys.android.library)
}

apply(from = "${rootDir}/publish-maven.gradle.kts")

android {
    namespace = "com.itoys.android.uikit"
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.material)
    implementation(libs.autosize)
    implementation(libs.brv)
    implementation(libs.constraint.layout)
    implementation(libs.calendar.angcyo)
    implementation(libs.immersionbar)
    implementation(libs.imageviewer)
    implementation(libs.jackson)
    implementation(libs.keyboard.event)
    implementation(libs.lottie)
    implementation(libs.magic.indicator)
    implementation(libs.okdownload)
    implementation(libs.okdownload.ktx)
    implementation(libs.picker.wheel)
    implementation(libs.progressbar.bga)
    implementation(libs.refresh.header.classics)
    implementation(libs.title.bar)
    implementation(libs.viewpager2)
    implementation(libs.wheel)

    implementation(libs.itoys.image)
    implementation(libs.itoys.logcat)
    implementation(libs.itoys.utils)
}