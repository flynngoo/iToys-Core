import com.itoys.android.plugin.PluginIds
import com.itoys.android.plugin.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/1
 */
class AndroidTheRouterPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(PluginIds.kotlin_kapt)
            }

            dependencies {
                "implementation"(libs.findLibrary("therouter").get())
                "kapt"(libs.findLibrary("therouter.apt").get())
            }
        }
    }
}