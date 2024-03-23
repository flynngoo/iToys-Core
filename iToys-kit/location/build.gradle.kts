plugins {
    alias(libs.plugins.itoys.android.library)
}

apply(from = "${rootDir}/publish-maven.gradle.kts")

android {
    namespace = "com.itoys.android.location"
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraint.layout)
    implementation(libs.fragment.ktx)
    implementation(libs.material)

    implementation(libs.location.amap)
    implementation(libs.location.amap)
    implementation(libs.permissions)
}