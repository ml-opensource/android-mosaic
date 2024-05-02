import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.changelog.date

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kotlin.dokka)
    alias(libs.plugins.gradle.maven.publish) apply false
    alias(libs.plugins.changelog) // Gradle Changelog Plugin

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

changelog {
    path.set(file("CHANGELOG.md").canonicalPath)
    header.set(provider { "[${version.get()}] - ${date()}" })
    headerParserRegex.set("""(\d+\.\d+)""".toRegex())
    keepUnreleasedSection.set(true)
    unreleasedTerm.set("[Unreleased]")
    groups.set(listOf("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security"))
    lineSeparator.set("\n")
}