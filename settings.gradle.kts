pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        //maven { url = uri("https://jitpack.io") }
        maven {
            url = java.net.URI("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")
        }

    }
}

rootProject.name = "BasicWeatherApp"
include(":app")
