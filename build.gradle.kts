import io.gitlab.arturbosch.detekt.extensions.DetektExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kotlin.dokka)
    alias(libs.plugins.gradle.maven.publish) apply false
}


subprojects {
    apply(plugin = rootProject.libs.plugins.kotlin.dokka.get().pluginId)
    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)
    configure<DetektExtension> {
        buildUponDefaultConfig = true
        allRules = false
        config.setFrom("$rootDir/detekt.yml")
    }
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint()
            .editorConfigOverride(
                mapOf(
                    "indent_size" to 4,
                    "indent_style" to "space",
                    "ij_kotlin_imports_layout" to "*,java.**,javax.**,kotlin.**,kotlinx.**,^",
                    "ij_kotlin_allow_trailing_comma_on_call_site" to "true",
                    "ij_kotlin_allow_trailing_comma" to "true",
                    "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
                    "ktlint_code_style" to "android_studio",
                )
            )
    }
}