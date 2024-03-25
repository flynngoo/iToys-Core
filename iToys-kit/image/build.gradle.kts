plugins {
    alias(libs.plugins.itoys.android.library)
}

apply(from = "${rootDir}/publish-maven.gradle.kts")

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
    implementation(libs.picture.compress)
    implementation(libs.picture.selector)
    implementation(libs.okhttp)

    implementation(libs.itoys.utils)
}