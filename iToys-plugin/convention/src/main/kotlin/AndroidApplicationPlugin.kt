import com.android.build.api.dsl.ApplicationExtension
import com.itoys.android.plugin.AppConfig
import com.itoys.android.plugin.PluginIds
import com.itoys.android.plugin.configureKotlinAndroid
import com.itoys.android.plugin.env.DevEnv
import com.itoys.android.plugin.env.ProdEnv
import com.itoys.android.plugin.env.TestEnv
import com.itoys.android.plugin.env.UatEnv
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.File

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/1
 */
class AndroidApplicationPlugin : Plugin<Project> {

    private companion object {
        private const val FIELD_TYPE_STRING = "String"
        private const val FIELD_TYPE_BOOLEAN = "boolean"
        private const val FIELD_TYPE_INT = "int"
    }

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(PluginIds.application)
                apply(PluginIds.kotlin_android)
                apply(PluginIds.kotlin_kapt)
                apply(PluginIds.therouter)
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(commonExtension = this)

                defaultConfig {
                    applicationId = AppConfig.appId
                    minSdk = AppConfig.minSdkVersion.apiLevel
                    targetSdk = AppConfig.targetSdkVersion.apiLevel

                    testInstrumentationRunner = AppConfig.testRunner
                    multiDexEnabled = true
                    versionName = AppConfig.versionName
                    versionCode = AppConfig.versionCode

                    ndk {
                        // 64位app支持
                        abiFilters.add("arm64-v8a")
                        abiFilters.add("x86_64")

                        // 32位app支持
                        abiFilters.add("arm64-v7a")
                        abiFilters.add("x86")
                    }
                }

                viewBinding { enable = true }

                buildFeatures { buildConfig = true }

                signingConfigs {
                    getByName("debug") {
                        storeFile = File("${project.rootDir}/${AppConfig.appModuleName}/keystore/itoys-simple.keystore")
                        storePassword = "itoys#2024"
                        keyAlias = "itoys-simple"
                        keyPassword = "itoys#2024"
                    }

                    create("release") {
                        storeFile = File("${project.rootDir}/${AppConfig.appModuleName}/keystore/sign.keystore")
                        storePassword = "ZiUgNMjrt8fC"
                        keyAlias = "sign"
                        keyPassword = "ZiUgNMjrt8fC"
                    }
                }

                buildTypes {
                    getByName("debug") {
                        isMinifyEnabled = false
                        isDebuggable = true
                        proguardFiles(
                            getDefaultProguardFile(AppConfig.defaultProguardFile),
                            AppConfig.proguardRulesFile
                        )

                        signingConfig = signingConfigs.getByName("debug")
                    }

                    getByName("release") {
                        isMinifyEnabled = true
                        isDebuggable = false
                        proguardFiles(
                            getDefaultProguardFile(AppConfig.defaultProguardFile),
                            AppConfig.proguardRulesFile
                        )

                        signingConfig = signingConfigs.getByName("release")
                    }
                }

                flavorDimensions += AppConfig.appModuleName

                productFlavors {
                    // 开发
                    create("Ver-Dev") {
                        applicationIdSuffix = ".debug"
                        versionNameSuffix = "-debug"
                        testNamespace = "${AppConfig.appId}$applicationIdSuffix"

                        signingConfig = signingConfigs.getByName("debug")
                        manifestPlaceholders.putAll(AppConfig.debugAppManifestPlaceholders)

                        // 接口 url
                        buildConfigField(FIELD_TYPE_STRING, "API_URL", DevEnv.INSTANCE.apiUrl())
                        // 登录认证盐
                        buildConfigField(FIELD_TYPE_STRING, "LOGIN_SALT", DevEnv.INSTANCE.loginSalt())
                    }

                    // 测试
                    create("Ver-Test") {
                        applicationIdSuffix = ".test"
                        versionNameSuffix = "-test"
                        testNamespace = "${AppConfig.appId}$applicationIdSuffix"

                        manifestPlaceholders.putAll(AppConfig.previewAppManifestPlaceholders)

                        // 接口 url
                        buildConfigField(FIELD_TYPE_STRING, "API_URL", TestEnv.INSTANCE.apiUrl())
                        // 登录认证盐
                        buildConfigField(FIELD_TYPE_STRING, "LOGIN_SALT", TestEnv.INSTANCE.loginSalt())
                    }

                    // 预发布
                    create("Ver-Uat") {
                        applicationIdSuffix = ".uat"
                        versionNameSuffix = "-uat"
                        testNamespace = "${AppConfig.appId}$applicationIdSuffix"
                        manifestPlaceholders.putAll(AppConfig.uatAppManifestPlaceholders)

                        // 接口 url
                        buildConfigField(FIELD_TYPE_STRING, "API_URL", UatEnv.INSTANCE.apiUrl())
                        // 登录认证盐
                        buildConfigField(FIELD_TYPE_STRING, "LOGIN_SALT", UatEnv.INSTANCE.loginSalt())
                    }

                    // 线上生产
                    create("Ver-Prod") {
                        manifestPlaceholders.putAll(AppConfig.appManifestPlaceholders)

                        // 接口 url
                        buildConfigField(FIELD_TYPE_STRING, "API_URL", ProdEnv.INSTANCE.apiUrl())
                        // 登录认证盐
                        buildConfigField(FIELD_TYPE_STRING, "LOGIN_SALT", ProdEnv.INSTANCE.loginSalt())
                    }
                }
            }
        }
    }
}