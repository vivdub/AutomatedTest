import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(project(":shared"))

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)
    implementation("ai.koog:koog-agents:1.2.0")
    implementation("ai.koog:koog-agents-additions:1.2.0-beta")
}

compose.desktop {
    application {
        mainClass = "com.ai.automated.tests.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.ai.automated.tests"
            packageVersion = "1.0.0"
        }
    }
}