import com.android.build.gradle.LibraryExtension
import com.itoys.android.plugin.AppConfig
import com.itoys.android.plugin.PluginIds
import com.itoys.android.plugin.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/1
 */
class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(PluginIds.library)
                apply(PluginIds.kotlin_android)
                apply(PluginIds.kotlin_kapt)
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(commonExtension = this)

                defaultConfig.targetSdk = AppConfig.targetSdkVersion.apiLevel

                viewBinding { enable = true }
            }
        }
    }
}