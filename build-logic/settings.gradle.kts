dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("lib") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

include(":convention")
