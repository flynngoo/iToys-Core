import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.itoys.android.build"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "itoys.android.application"
            implementationClass = "AndroidApplicationPlugin"
        }

        register("androidLibrary") {
            id = "itoys.android.library"
            implementationClass = "AndroidLibraryPlugin"
        }

        register("androidHilt") {
            id = "itoys.android.hilt"
            implementationClass = "AndroidHiltPlugin"
        }

        register("androidTheRoute") {
            id = "itoys.android.therouter"
            implementationClass = "AndroidTheRouterPlugin"
        }
    }
}