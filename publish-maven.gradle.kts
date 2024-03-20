import org.gradle.kotlin.dsl.*

apply(plugin = "maven-publish")

configure<PublishingExtension> {
    repositories {
        mavenLocal()
    }
}

afterEvaluate {
    extensions.configure<PublishingExtension>("publishing") {
        publications {
            create<MavenPublication>("plugin") {
                from(components["debug"])
                groupId = "com.itoys.android"
                artifactId = project.name
                version = "0.0.1-SNAPSHOT"
            }
        }

        repositories {
            maven {
                credentials {
                    username = "615c20e489e1d5005aff93a3"
                    password = "TDVE)dmX34=D"
                }

                setUrl("https://packages.aliyun.com/maven/repository/2110207-release-jCk4L0/")
            }
            maven {
                credentials {
                    username = "615c20e489e1d5005aff93a3"
                    password = "TDVE)dmX34=D"
                }

                setUrl("https://packages.aliyun.com/maven/repository/2110207-snapshot-M5JxFx/")
            }
        }
    }
}
