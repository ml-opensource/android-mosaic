import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.mosaic.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.gradle.maven.publish)

}

android {
    namespace = "io.monstarlab.mosaic.lib"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
mavenPublishing {
    configure(AndroidSingleVariantLibrary("release"))
    publishToMavenCentral(SonatypeHost.S01)
    signAllPublications()
}


kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}