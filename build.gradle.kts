// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.13.1" apply false
    id("com.android.library") version "8.13.1" apply false

    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
    id ("org.jetbrains.kotlin.plugin.serialization") version "2.1.0" apply false

    id("androidx.room") version "2.7.1" apply false
    id ("com.google.dagger.hilt.android") version "2.57.1" apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.9.7")
    }
}