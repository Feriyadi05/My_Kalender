buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Harus sama dengan "kotlin" di libs.versions.toml
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.10")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}