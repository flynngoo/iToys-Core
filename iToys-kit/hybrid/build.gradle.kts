plugins {
    alias(libs.plugins.itoys.android.library)
    alias(libs.plugins.itoys.android.therouter)
}

apply(from = "${rootDir}/publish-maven.gradle.kts")

android {
    namespace = "com.itoys.android.hybrid"
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraint.layout)
    implementation(libs.fragment.ktx)
    implementation(libs.material)

    implementation(libs.eventbus.live)
    implementation(libs.permissions)

    implementation(libs.itoys.image)
    implementation(libs.itoys.logcat)
    implementation(libs.itoys.utils)

    implementation(libs.itoys.library.hybrid) {
        exclude(group = "io.github.jeremyliao", module = "live-event-bus-x")
    }
}