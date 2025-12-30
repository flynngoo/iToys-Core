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
    implementation(libs.imageviewer)
    implementation(libs.itoys.library.compress)
    implementation(libs.picture.selector) {
        exclude("io.github.lucksiege", "compress")
    }
    implementation(libs.okhttp)

    implementation(libs.itoys.utils)
}