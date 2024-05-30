pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "Architectury"
            url = uri("https://maven.architectury.dev/")
        }
        maven {
            name = "Minecraft Forge"
            url = uri("https://maven.minecraftforge.net/")
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

include("Animation")
include("Math")
include("Visual")
