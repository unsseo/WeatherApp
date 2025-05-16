pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // 중앙에서만 저장소 관리
    repositories {
        google()
        mavenCentral()
        // ⬇️ 카카오 SDK 및 카카오맵 SDK 저장소 추가
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

rootProject.name = "map_project"
include(":app")
 