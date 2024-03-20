plugins {
    alias(libs.plugins.itoys.android.library)
}

android {
    namespace = "com.itoys.android.image"
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraint.layout)
    implementation(libs.fragment.ktx)
    implementation(libs.material)

    implementation(libs.brv)
    implementation(libs.glide)
    kapt(libs.glide.compiler)
    implementation(libs.glide.transformers)
    implementation(libs.glide.transformers.gpu)
    implementation(libs.okhttp)

    implementation(libs.picture.compress)
    implementation(libs.picture.selector)

    implementation(projects.iToysCore.network)
    implementation(projects.iToysCore.utils)
}