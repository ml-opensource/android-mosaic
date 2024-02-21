package io.monstarlab.mosaic.plugins

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidAppConvention : Plugin<Project> {

    override fun apply(target: Project) {
        target.configure<ApplicationExtension> {
            compileSdk = AndroidSdkConfiguration.COMPILE_SDK
            defaultConfig {
                targetSdk = AndroidSdkConfiguration.TARGET_SDK
                minSdk = AndroidSdkConfiguration.MIN_SDK
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
        }
    }
}
