plugins {
    alias(libs.plugins.itoys.android.library)
}

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