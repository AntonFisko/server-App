plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle) // Use the appropriate version
//        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.jetbrains.kotlin.gradle.plugin)
    }
}