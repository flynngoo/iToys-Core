plugins {
    alias(libs.plugins.itoys.android.library)
}

android {
    namespace = "com.itoys.android.scan"
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraint.layout)
    implementation(libs.material)

    implementation(libs.title.bar)
    implementation(libs.zxing)

    implementation(projects.iToysCore.uikit)
    implementation(projects.iToysCore.utils)
}