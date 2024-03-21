import java.util.Properties

// 读取本地配置
val properties = Properties()
val propertiesFile = File(rootDir, "/local.properties")
properties.load(propertiesFile.inputStream())

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    // ---------------------------------------------------------------------------------------------
    // iToys plugin
    // ---------------------------------------------------------------------------------------------
    includeBuild("iToys-plugin")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        maven {
            credentials {
                username = properties.getProperty("aliyun.maven.username")
                password = properties.getProperty("aliyun.maven.password")
            }

            setUrl(properties.getProperty("aliyun.maven.repo"))
        }
        maven {
            credentials {
                username = properties.getProperty("aliyun.maven.username")
                password = properties.getProperty("aliyun.maven.password")
            }

            setUrl(properties.getProperty("aliyun.maven.repo.snapshot"))
        }
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "iToysCore"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
includeModules(":simple", ":iToys-core")

// -------------------------------------------------------------------------------------------------
// Tools (Modules)
// -------------------------------------------------------------------------------------------------

fun includeModules(vararg paths: String, with: (Module.() -> Unit)? = null) {
    paths.forEach {
        includeModule(it, with)
    }
}

fun includeModule(path: String, with: (Module.() -> Unit)? = null) {
    val moduleSpec = Module()
    with?.invoke(moduleSpec)
    val moduleDir = File(rootDir, path.replace(':', '/'))
    if (moduleDir.exists()) {
        include(path)
        val module = project(path)
        module.projectDir = moduleDir
        val settingsFile = File(moduleDir, "settings.gradle")
        if (settingsFile.exists()) {
            apply {
                from(settingsFile)
                to(SettingsProxy(settings, module, moduleSpec))
            }
        }
        moduleDir.walk().maxDepth(1).filter { it.isDirectory && it != moduleDir }.forEach {
            val settingsFileInternal = File(it, "settings.gradle")
            if (settingsFileInternal.exists()) {
                val moduleInternal = settings.findProject("$path:${it.name}")
                if (moduleInternal != null) {
                    moduleInternal.projectDir = it
                    apply {
                        from(settingsFileInternal)
                        to(SettingsProxy(settings, moduleInternal, moduleSpec))
                    }
                }
            }
        }

    }
}

class Module {
    private val excludes = mutableListOf<String>()

    private var filter: ((String) -> Boolean)? = null

    fun exclude(vararg paths: String) {
        excludes.addAll(paths)
    }

    fun accept(path: String): Boolean {
        if (excludes.contains(path)) {
            return false
        }

        if (filter?.invoke(path) == true) {
            return false
        }

        return true
    }
}

class SettingsProxy(
    private var mSettings: Settings,
    private var mModule: ProjectDescriptor,
    private var mModuleSpec: Module,
) {

    fun getRootProject(): ProjectDescriptor {
        return mModule
    }

    fun include(vararg paths: String) {
        paths.forEach {
            if (mModuleSpec.accept(it.replace(":", ""))) {
                val descendantPath = generateDescendantPath(it)
                mSettings.include(descendantPath)
                val descendantProjectDir = File(mModule.projectDir, it.replace(':', '/'))
                mSettings.project(descendantPath).projectDir = descendantProjectDir
            }

        }
    }

    fun project(path: String): ProjectDescriptor {
        return mSettings.project("${mModule.path}$path")
    }

    private fun generateDescendantPath(path: String): String {
        return "${mModule.path}$path"
    }
}