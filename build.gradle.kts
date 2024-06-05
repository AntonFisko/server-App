plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
//    id("dagger.hilt.android.plugin") version "2.41"
//    id("dagger.hilt.android.plugin") version "2.41" apply false
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