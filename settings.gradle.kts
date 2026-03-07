pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/mecoFarid/Yarus")
            credentials {
                username = "mecoFarid"
                password = System.getenv("GTHB_PERSONAL_TOKEN")
            }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/mecoFarid/Yarus")
            credentials {
                username = "mecoFarid"
                password = System.getenv("GTHB_PERSONAL_TOKEN")
            }
        }
    }
}

rootProject.name = "Summy"
include(":androidApp")
include(":shared")