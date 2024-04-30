plugins {
    `kotlin-dsl`
}


repositories {
    mavenCentral()
    google()
}

dependencies {
    compileOnly(lib.android.gradle.plugin)
}

kotlin {
    jvmToolchain(17)
}

gradlePlugin {
    /**
     * Register convention plugins so they are available in the build scripts of the application
     */
    plugins {
        register("conventionAndroidLib") {
            id = "io.monstarlab.mosaic.library"
            implementationClass = "io.monstarlab.mosaic.plugins.AndroidLibConvention"
        }

        register("conventionAndroidApp") {
            id = "io.monstarlab.mosaic.application"
            implementationClass = "io.monstarlab.mosaic.plugins.AndroidAppConvention"
        }
    }
}