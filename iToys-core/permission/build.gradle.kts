plugins {
    alias(libs.plugins.itoys.android.library)
}

android {
    namespace = "com.itoys.android.permission"
}

dependencies {
    implementation(libs.permissions)
}